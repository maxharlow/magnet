package com.gu.magnet

import dispatch._

object Magnetiser {

  def triplestore = url("http://localhost:8000/")

  def query() {
//    val query = "SELECT DISTINCT ?article WHERE {\n { ?article <http://purl.org/dc/elements/1.1/subject> ?country }\n UNION\n { ?country <http://dbpedia.org/ontology/language> <http://dbpedia.org/resource/Arabic_language> }\n}"
    val query = "SELECT * WHERE { ?o ?a ?v }"
    runQuery(query)
  }

  private def runQuery(sparql: String) { //: Promise[Either[Throwable, String]] = {
    println("runquery: " + sparql)
    val parameters = Map("query" -> sparql)
    val request = triplestore / "sparql/" <<? parameters
    val x = Http(request OK as.String)()
//    x.right.map(parseQuery)
    println(x)
    x
  }

  private def parseQuery(queryResponse: String): String = {
    println("parsequery: " + queryResponse)
//    val uris = (queryResponse \\ "uri").map(_ text).toSet
//    println(uris)
//    uris
    "hi"
  }

}