import com.bmuschko.gradle.docker.tasks.image.*
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  application
  java
  alias(libs.plugins.shadowJar)
  alias(libs.plugins.docker)
  jacoco
  checkstyle
  pmd
  alias(libs.plugins.spotbugs)
  alias(libs.plugins.sonarqube)
}

checkstyle {
  config = resources.text.fromFile("checkstyle.xml")
  toolVersion = libs.versions.checkstyle.get()
}

pmd {
  toolVersion = libs.versions.pmd.get()
  ruleSetConfig = resources.text.fromFile("pmd.xml")
}

spotbugs {
  toolVersion = libs.versions.spotbugs
}

jacoco {
  toolVersion = libs.versions.jacoco.get()
}

sonar {
  properties {
    property("sonar.projectKey", "cake-lier_$name")
    property("sonar.organization", "cake-lier")
    property("sonar.host.url", "https://sonarcloud.io")
  }
}

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

repositories {
  mavenCentral()
}

group = "io.cake-lier.github"
version = "0.0.0"

dependencies {
  compileOnly(libs.spotbugs.annotations)
  pmd(libs.pmd.java)
  pmd(libs.pmd.ant)

  testImplementation(platform(libs.junit.platform))
  testImplementation(libs.junit.jupiter)

  testCompileOnly(libs.spotbugs.annotations)
}

application {
  mainClass = "io.github.cakelier.AppMain"
}

tasks {
  val createJarFile = named<ShadowJar>("shadowJar") {
    archiveBaseName = "main"
    archiveClassifier = ""
    archiveVersion = ""
  }

  val buildImage = create("buildImage", DockerBuildImage::class) {
    dependsOn(createJarFile)
    inputDir.set(file("."))
    images.add("matteocastellucci3/$name:$version")
    images.add("matteocastellucci3/$name:latest")
  }

  create("pushImage", DockerPushImage::class) {
    dependsOn(buildImage)
    images.add("matteocastellucci3/$name:$version")
    images.add("matteocastellucci3/$name:latest")
  }

  check {
    dependsOn(jacocoTestReport)
  }

  test {
    useJUnitPlatform()
  }

  jacocoTestReport {
    reports {
      xml.required = true
      csv.required = false
    }
  }

  spotbugsMain {
    reports.create("html") {
      required = true
    }
  }
  spotbugsTest {
    reports.create("html") {
      required = true
    }
  }
}
