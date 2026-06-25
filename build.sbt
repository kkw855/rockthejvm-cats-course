scalaVersion := "3.8.4"

val catsVersion = "2.13.0"

lazy val root = rootProject
  .settings(
    name := "rockthejvm-cats-course",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % catsVersion
    )
  )
