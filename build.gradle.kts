plugins {
    java
}

group = "GradleWork"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val seleniumVersion = "4.45.0"
val junitVersion = "5.14.4"
val cucumberVersion = "7.34.3"
val allureVersion = "2.33.0"
val extentVersion = "5.1.2"
val extentAdapterVersion = "1.14.0"
val slf4jVersion = "2.0.17"

val testcontainersVersion = "2.0.5"
val flywayVersion = "10.22.0"
val mysqlVersion = "8.4.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(22))
    }
}

dependencies {

    testImplementation("org.seleniumhq.selenium:selenium-java:$seleniumVersion")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation(platform("io.cucumber:cucumber-bom:$cucumberVersion"))
    testImplementation("io.cucumber:cucumber-java")
    testImplementation("io.cucumber:cucumber-junit-platform-engine")
    testImplementation("io.cucumber:cucumber-picocontainer")

    testImplementation("org.junit.platform:junit-platform-suite")

    testImplementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
    testImplementation("io.qameta.allure:allure-cucumber7-jvm")
    testImplementation("io.qameta.allure:allure-junit5")

    testImplementation("com.aventstack:extentreports:$extentVersion")

    testImplementation("tech.grasshopper:extentreports-cucumber7-adapter:$extentAdapterVersion")

    testImplementation("org.slf4j:slf4j-simple:$slf4jVersion")

    testImplementation("org.testcontainers:testcontainers-junit-jupiter:$testcontainersVersion")
    testImplementation("org.testcontainers:testcontainers-mysql:$testcontainersVersion")
    testImplementation("org.flywaydb:flyway-core:$flywayVersion")
    testImplementation("org.flywaydb:flyway-mysql:$flywayVersion")
    testImplementation("org.mysql:mysql-connector-j:$mysqlVersion")


}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    systemProperty(
        "baseUrl",
        providers.gradleProperty("baseUrl")
            .orElse("http://localhost:5173")
            .get()
    )

    systemProperty(
        "headless",
        providers.gradleProperty("headless")
            .orElse("false")
            .get()
    )

    systemProperty(
        "browser",
        providers.gradleProperty("browser")
            .orElse("chrome")
            .get()
    )

    systemProperty("cucumber.publish.quiet", "true")

    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.test {
    useJUnitPlatform()
}