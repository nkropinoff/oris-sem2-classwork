import java.util.*

buildscript {
    repositories { mavenCentral() }
    dependencies {
        classpath("org.liquibase:liquibase-core:4.33.0")
    }
}

plugins {
    id("java")
//    id("application")
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.liquibase.gradle") version "3.1.0"
    id("jacoco")
}

group = "ru.kpfu.itis.kropinov"
version = "1.0-SNAPSHOT"

val postgresVersion: String by project
val springSecurityVersion: String by project

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.3")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("javax.mail:javax.mail-api:1.6.2")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-taglibs:${springSecurityVersion}")
    implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    implementation("org.liquibase:liquibase-core")
    liquibaseRuntime("org.liquibase:liquibase-core:4.33.0")
    liquibaseRuntime("org.postgresql:postgresql:$postgresVersion")
    liquibaseRuntime("info.picocli:picocli:4.7.5")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    // testImplementation("org.junit.jupiter:junit-jupiter-engine:5.14.0")
    // testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.14.0")
}

extra["byte-buddy.version"] = "1.17.5"

//application {
//    mainClass = "ru.kpfu.itis.kropinov.Main"
//}

val props = Properties()
file("src/main/resources/db/liquibase.properties").inputStream().use { props.load(it) }

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "changelogFile" to props.getProperty("output-changelog-file"),
            "url"           to props.getProperty("url"),
            "username"      to props.getProperty("username"),
            "password"      to props.getProperty("password"),
            "driver"        to props.getProperty("driver-class-name")
        )
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport{
    dependsOn(tasks.test)
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it).matching {
            exclude(jacocoExcludes)
        }
    }))
}

jacoco {
    toolVersion = "0.8.14"
    reportsDirectory.set(layout.buildDirectory.dir("jacoco"))
}

val jacocoExcludes = listOf(
    "**/ru/kpfu/itis/kropinov/dto/**",
    "**/ru/kpfu/itis/kropinov/model/**",
    "**/ru/kpfu/itis/kropinov/config/**",
    "**/ru/kpfu/itis/kropinov/exception/**"
)

tasks.jacocoTestCoverageVerification {

    violationRules {
        rule {
            limit {
                minimum = BigDecimal.valueOf(0.1)
            }
        }
    }

    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it).matching {
            exclude(jacocoExcludes)
        }
    }))

}