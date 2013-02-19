package com.gu.magnet

import dispatch._
import net.liftweb.json._
import net.liftweb.json.Serialization.write

object Searcher {

  case class MagnetResponse(content: List[Content])
  case class Content(uri: String, headline: String, thumbnailUri: Option[String], body: String, thingUris: List[Thing])
  case class Thing(name: String, image: Option[String] = None)

  def guardianContent = host("content.guardianapis.com")
  def triplestore = url("http://localhost:8000/")

  val guardianContentApiKey = ""

  def search(query: String) = {
    implicit val formats = Serialization.formats(NoTypeHints)
    val computedQuery = computeQuery(query)
    val contentUris = runQuery(computedQuery + " LIMIT 25")
    val response = contentUris map retrieveContent
    write(response)
  }

  private var useCountries = false

  private def computeQuery(query: String) = {
    val computedQuery = if (query.contains("Arabic")) {
      "SELECT DISTINCT ?article WHERE {" +
      " { ?article <http://purl.org/dc/elements/1.1/subject> ?country }" +
      " UNION" +
      " { ?country <http://dbpedia.org/ontology/officialLanguage> <http://dbpedia.org/resource/Arabic_language> }" +
      "}"
    }
    else {
     "SELECT DISTINCT ?article WHERE {" +
       "{ ?article <http://purl.org/dc/elements/1.1/subject> ?x }" +
       " UNION" +
       " { ?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/Actor> }" +
       " UNION" +
       "{ ?x <http://dbpedia.org/property/starring> ?film }" +
       "  UNION" +
       "{ ?film <http://dbpedia.org/ontology/director> <http://dbpedia.org/resource/Quentin_Tarantino> } }"
    }
    if (query.contains("Arabic")) {
      useCountries = true
    } else {
      useCountries = false
    }
    computedQuery
  }

  private def runQuery(sparql: String): Set[String] = {
    val parameters = Map("query" -> sparql)
    val request = triplestore / "sparql/" <<? parameters
    Http(request OK as.xml.Elem).map(parseQuery)()
  }

  private def parseQuery(queryResponse: xml.Elem): Set[String] = {
    (queryResponse \\ "uri").map(_ text).toSet
  }

  private def retrieveContent(contentUri: String): Content = {
    val parameters = Map("api-key" -> guardianContentApiKey, "show-fields" -> "headline,thumbnail,body")
    val contentId = contentUri.replace("http://www.guardian.co.uk/", "")
    val request = guardianContent / contentId <<? parameters
    Http(request OK as.String).map(parseContent)()
  }

  private def parseContent(contentResponse: String): Content = {
    implicit val formats = DefaultFormats
    val json = parse(contentResponse)
    val uri = (json \\ "webUrl").extract[String]
    val headline = (json \\ "headline").extract[String]
    val thumbnailUri = (json \\ "thumbnail") match {
      case JString(thumbnail) => Some(thumbnail)
      case _ => None
    }
    val body = (json \\ "body").extract[String]

    val things = if (useCountries) getThingsFor(body) else List(Thing(""))
    Content(uri, headline, thumbnailUri, body, things)
  }

  lazy val countries = List("Mauritania", "Somaliland", "Palestinian  territories", "Northland  State", "Morocco", "Algeria", "Saudi  Arabia", "Bahrain", "Djibouti", "United  Arab  Emirates", "Eritrea", "Tunisia", "Somalia", "Puntland", "Jubaland", "Moh%C3%A9li", "Anjouan", "Yemen  Arab  Republic", "Maakhir", "Nineveh  plains", "E-Government  in  the  United  Arab  Emirates", "Egypt", "Yemen", "Iraq", "Qatar", "Libya", "Palestinian  National  Authority", "Syria", "Chad", "Sudan", "Oman", "Southwestern  Somalia", "Grande  Comore", "Islamic  Courts  Union", "Abyei", "Sahrawi  Arab  Democratic  Republic", "State  of  Palestine", "Italian  Cyrenaica", "Israel", "Jordan", "Kuwait", "Darfur", "Iraq  under  U.S.  Military  Occupation", "Darfur  Regional  Authority", "Awdalland")

  private def getThingsFor(body:String): List[Thing] = {
   val countriesForBody = countries map { country =>
      if (body.contains(country)) Some(Thing(country))
      else None
    }
    countriesForBody.flatten
  }

}