plugins {
    id("java")
}

repositories {
    mavenCentral()
    maven(url = "https://repo.spring.io/plugins-release/")
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:2.4.5")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.4.5")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.0.0-M6")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.9.8")
}