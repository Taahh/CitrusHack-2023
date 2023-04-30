plugins {
    id("java")
    id("org.springframework.boot") version "3.0.6"
    id("io.spring.dependency-management") version "1.1.0"
}


group = "dev.taah"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-core")
    implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("com.github.docker-java:docker-java:3.3.0")
    implementation("com.github.docker-java:docker-java-transport-httpclient5:3.3.0")

    implementation("com.corundumstudio.socketio:netty-socketio:1.7.23")
    implementation("com.google.firebase:firebase-admin:9.1.1")
    implementation("org.json:json:20230227")


    implementation("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
}
