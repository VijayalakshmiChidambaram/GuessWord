plugins {
  java
  jacoco
  pmd
  application
}       

repositories {
	mavenCentral()
    jcenter()
}

dependencies {
  implementation("junit:junit:4.12")
  testCompile("org.junit.jupiter:junit-jupiter-api:5.7.0")
	testRuntime("org.junit.jupiter:junit-jupiter-engine:5.7.0")
	testRuntime("org.junit.platform:junit-platform-console:1.7.1")
  testCompile("org.mockito:mockito-core:3.8.+")
}

tasks {
  val flags = listOf("-Xlint:unchecked", "-Xlint:deprecation", "-Werror")

  getByName<JavaCompile>("compileJava") {
    options.compilerArgs = flags
  }

  getByName<JavaCompile>("compileTestJava") {
    options.compilerArgs = flags
  }
}
 
sourceSets {
  main {
    java.srcDirs("src")
  }
  test {
    java.srcDirs("test")
  }
}

val test by tasks.getting(Test::class) {
	useJUnitPlatform {}
//	jvmArgs("--enable-preview")
}

pmd {
    ruleSets = listOf()
    ruleSetFiles = files("../conf/pmd/ruleset.xml")
    toolVersion = "6.27.0"    
}

tasks.withType<JacocoReport> {
  afterEvaluate {
    classDirectories.setFrom(files(classDirectories.files.map {
      fileTree(it).apply {
        exclude("*/preview/*")
        exclude("**/ui/*")
      }
    }))
  }
}

application{
  mainClassName = "ui.GuesserConsole"
}

val run by tasks.getting(JavaExec::class) {
  standardInput = System.`in`
}

defaultTasks("clean", "test", "jacocoTestReport", "pmdMain")
