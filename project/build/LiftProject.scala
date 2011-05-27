import sbt._

class LiftProject(info: ProjectInfo) extends DefaultWebProject(info) {

  val liftVersion = "2.3"

  val squerylrecord = "net.liftweb" % "lift-squeryl-record_2.8.1" % "2.3"

  val sonatype_snapshot = "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"

  val reactive_web = "cc.co.scala-reactive" %% "reactive-web" % "0.0.1-SNAPSHOT"

  override def libraryDependencies = Set( 
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-mapper" % liftVersion % "compile->default",
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default",
    "junit" % "junit" % "4.5" % "test->default",
    "ch.qos.logback" % "logback-classic" % "0.9.26",
    "org.scala-tools.testing" %% "specs" % "1.6.6" % "test->default",
    "com.h2database" % "h2" % "1.2.138"
  ) ++ super.libraryDependencies
}