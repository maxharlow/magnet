package com.gu.magnet

import org.scalatra.ScalatraServlet

class Dispatcher extends ScalatraServlet {

  get("/search") {
    val query = "SELECT DISTINCT ?article WHERE {\n { ?article <http://purl.org/dc/elements/1.1/subject> ?country }\n UNION\n { ?country <http://dbpedia.org/ontology/officialLanguage> <http://dbpedia.org/resource/Arabic_language> }\n}"
//    val query = params("query")
    Magnetiser.query(query)
  }

}