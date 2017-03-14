/*
This file is part of Intake24.

Copyright 2015, 2016, 2017 Newcastle University.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

name := "survey-frontend-server"

organization := "uk.ac.ncl.openlab.intake24"

version := "3.0.0-SNAPSHOT"

scalaVersion := "2.11.8"

resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
  ws,
  "org.webjars" %% "webjars-play" % "2.5.0-4",
  "org.webjars" % "bootstrap" % "3.1.1-2",
  "uk.ac.ncl.openlab.intake24" % "survey-client" % "3.0.0-SNAPSHOT"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala, SystemdPlugin)