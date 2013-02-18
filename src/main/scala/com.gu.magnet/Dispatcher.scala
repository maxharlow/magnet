package com.gu.magnet

import org.scalatra.ScalatraServlet

class Dispatcher extends ScalatraServlet {

  get("/search") {
    val query = params.get("query").headOption getOrElse halt(400)
    Searcher.search(query)
  }

}