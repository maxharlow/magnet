package com.gu.magnet

import org.scalatra.ScalatraServlet

class Dispatcher extends ScalatraServlet {

  get("/search") {
    val query = "SELECT DISTINCT ?article WHERE {" +
      " { ?article <http://purl.org/dc/elements/1.1/subject> ?country }" +
      " UNION" +
      " { ?country <http://dbpedia.org/ontology/officialLanguage> <http://dbpedia.org/resource/Arabic_language> }" +
      "}"
//    val query2 = "SELECT DISTINCT ?article WHERE {" +
//      " { ?article <http://purl.org/dc/elements/1.1/subject> ?person }" +
//      " UNION" +
//      " { ?person <http://purl.org/dc/terms/subject> <http://dbpedia.org/resource/Category:People_educated_at_Eton_College> }" +
//      "}"
//    val query = params.get("query").headOption getOrElse halt(400)
    Searcher.search(query)
  }

}