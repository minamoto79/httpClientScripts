plugins {
    kotlin("jvm") version "1.5.20"
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks

compileKotlin.kotlinOptions.jvmTarget = "1.8"

repositories {
    mavenCentral()
    maven(url = "https://repo.spring.io/plugins-release/")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.5.20")
    implementation("org.springframework.boot:spring-boot-starter-web:2.4.5")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.4.5")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.0.0-M6")
    implementation("org.springframework.boot:spring-boot-starter-webflux:2.4.5")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.4.5")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation(kotlin("test"))
}

kotlin {
    sourceSets["main"].apply {
        kotlin.srcDir("src/main/kotlin")
    }
}

val test by tasks.existing(Test::class) {
    testLogging.showStackTraces = true
}
