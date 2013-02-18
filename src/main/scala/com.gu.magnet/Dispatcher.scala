package com.gu.magnet

import org.scalatra.{ScalatraServlet, Ok}

class Dispatcher extends ScalatraServlet {

  get("/") {
    Magnetiser.query()
    Ok()
  }

}