import sbt._

import Keys._
import AndroidKeys._

object ProjectSettings {
  val name = "smashballs"
  val version = "0.1"
}

object General {
  val settings = Defaults.defaultSettings ++ Seq (
    name := ProjectSettings.name,
    version := ProjectSettings.version
    versionCode := 0,
    platformName in Android := "android-16",
    libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test",
    useProguard := false,
    autoScalaLibrary in GlobalScope := false,
    proguardOptimizations := Seq.empty
  )

  lazy val testSettings =
    Defaults.defaultSettings ++
    General.settings

  lazy val mainSettings =
    General.settings ++
    AndroidProject.androidSettings ++
    AndroidManifestGenerator.settings ++
    AndroidMarketPublish.settings ++ Seq (
      resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      keyalias in Android := ProjectSettings.name
    )
}

object AndroidBuild extends Build {
  lazy val main = Project (
    ProjectSettings.name,
    file("."),
    settings = General.mainSettings
  )
}
