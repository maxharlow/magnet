package com.gu.magnet

import dispatch._
import net.liftweb.json._
import net.liftweb.json.Serialization.write

object Magnetiser {

  case class MagnetResponse(content: List[Content])
  case class Content(uri: String, headline: String, thumbnailUri: Option[String], body: String)

  def guardianContent = host("content.guardianapis.com")
  def triplestore = url("http://localhost:8000/")

  val guardianContentApiKey = ""

  def query(query: String) = {
    implicit val formats = Serialization.formats(NoTypeHints)
    val contentUris = runQuery(query + " LIMIT 10")
    val response = contentUris map retrieveContent
    write(response)
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
    Content(uri, headline, thumbnailUri, body)
  }

}