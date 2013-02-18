name := "Magnet"

scalaVersion := "2.9.1"

seq(webSettings :_*)

port in container.Configuration := 8085

libraryDependencies ++= Seq(
    "net.liftweb" %% "lift-json" % "2.5-M4",
    "net.databinder.dispatch" %% "dispatch-core" % "0.9.5",
    "org.scalatra" % "scalatra" % "2.1.1",
    "org.eclipse.jetty" % "jetty-webapp" % "8.1.8.v20121106" % "container",
    "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container;provided" artifacts (Artifact("javax.servlet", "jar", "jar")))
