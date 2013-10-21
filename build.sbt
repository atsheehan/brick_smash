import android.Keys._

android.Plugin.androidBuild

name := "brick_smash"

scalaVersion := "2.10.3"

scalacOptions in Compile += "-feature"

platformTarget in Android := "android-16"

run <<= run in Android

install <<= install in Android
