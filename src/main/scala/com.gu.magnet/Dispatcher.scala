package com.gu.magnet

import org.scalatra.{Ok, ScalatraServlet}

class Dispatcher extends ScalatraServlet {

  get("/") {
    Magnetiser.query()
    Ok()
  }

}