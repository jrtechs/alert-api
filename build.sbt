name := """alert-api"""
organization := "net.jrtechs"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.2"

resolvers += Resolver.JCenterRepository

libraryDependencies += guice

libraryDependencies += "net.katsstuff" %% "ackcord"                 % "0.16.1" //For high level API, includes all the other modules

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.9.0"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "net.jrtechs.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "net.jrtechs.binders._"
