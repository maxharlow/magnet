package com.gu.magnet

import dispatch._

object Magnetiser {

  def triplestore = url("http://localhost:8000/")

  def query() {
    val query = "SELECT DISTINCT ?article WHERE {\n { ?article <http://purl.org/dc/elements/1.1/subject> ?country }\n UNION\n { ?country <http://dbpedia.org/ontology/language> <http://dbpedia.org/resource/Arabic_language> }\n}"
    runQuery(query + " LIMIT 10")
  }

  private def runQuery(sparql: String): Set[String] = {
    val parameters = Map("query" -> sparql)
    val request = triplestore / "sparql/" <<? parameters
    Http(request OK as.xml.Elem).map(parseQuery)()
  }

  private def parseQuery(queryResponse: xml.Elem): Set[String] = {
    val uris = (queryResponse \\ "uri").map(_ text).toSet
    uris
  }

}