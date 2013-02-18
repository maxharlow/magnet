package com.gu.magnet

import org.scalatra.ScalatraServlet

class Dispatcher extends ScalatraServlet {

  get("/") {
    "ok!"
  }

}