plugins {
    id("java")
//    id("application")
    id("war")
}

group = "ru.kpfu.itis.kropinov"
version = "1.0-SNAPSHOT"

val springVersion: String by project
val jakartaVersion: String by project
val hibernateVersion: String by project
val springDataVersion: String by project
val postgresVersion: String by project
val freemarkerVersion: String by project
val hikariVersion: String by project

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-webmvc:$springVersion")
    implementation("org.springframework:spring-jdbc:$springVersion")
    implementation("org.springframework:spring-orm:$springVersion")
    implementation("org.springframework:spring-context-support:$springVersion")
    implementation("jakarta.servlet:jakarta.servlet-api:$jakartaVersion")
    implementation("org.hibernate.orm:hibernate-core:$hibernateVersion")
    implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("org.freemarker:freemarker:$freemarkerVersion")
    implementation("org.springframework.data:spring-data-jpa:$springDataVersion")
    implementation("com.zaxxer:HikariCP:$hikariVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
}

//application {
//    mainClass = "ru.kpfu.itis.kropinov.Main"
//}

tasks.test {
    useJUnitPlatform()
}