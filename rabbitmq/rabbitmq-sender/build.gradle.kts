import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    java

    id("com.diffplug.spotless") version "5.7.0"
}

group = "org.ivangrod"
version = "1.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

val mainClassName = "org.ivangrod.rabbitmq_sender.infrastructure.RabbitMQSenderApplication"

repositories {
    mavenCentral()
}

apply {
    plugin("org.jetbrains.kotlin.jvm")
    plugin("org.jetbrains.kotlin.plugin.spring")
    plugin("org.springframework.boot")
    plugin("io.spring.dependency-management")
    plugin("org.jetbrains.kotlin.plugin.serialization")
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.7.2")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

    implementation("org.apache.commons:commons-text:1.9")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.amqp:spring-rabbit-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.junit.platform:junit-platform-runner")
    testImplementation("org.junit.platform:junit-platform-launcher")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.check {
    dependsOn(tasks.spotlessCheck)
}

tasks.jar {
    enabled = true
}

