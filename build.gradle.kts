import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	kotlin("plugin.jpa") version "1.9.23"
}

group = "aryumka"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

val axonVersion = "4.9.3"
val kafkaVersion = "3.7.0"
val jdslVersion = "3.3.1"
val kotestVersion = "5.8.0"

dependencies {
  // Spring Boot
  implementation("org.springframework.boot:spring-boot-configuration-processor")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-validation")

  // AMQP
	implementation("org.springframework.boot:spring-boot-starter-amqp")
  testImplementation("org.springframework.amqp:spring-rabbit-test")

  // DB
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("com.linecorp.kotlin-jdsl:spring-data-jpa-support:$jdslVersion")
  implementation("com.linecorp.kotlin-jdsl:jpql-dsl:$jdslVersion")
  implementation("com.linecorp.kotlin-jdsl:jpql-render:$jdslVersion")
  runtimeOnly("com.h2database:h2")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")

  // Web
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

  //Axon Framework
  implementation("io.axoniq.console:console-framework-client-spring-boot-starter:1.4.1")
  implementation(platform("org.axonframework:axon-bom:$axonVersion")) // BOM 의존성 추가
  implementation("org.axonframework:axon-spring-boot-starter")
  testImplementation("org.axonframework:axon-test")
  implementation("org.axonframework:axon-spring-boot-autoconfigure")

  //Kafka
  implementation("org.springframework.kafka:spring-kafka")
  implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
  implementation("org.axonframework.extensions.kafka:axon-kafka-spring-boot-autoconfigure")
  implementation("org.axonframework.extensions.kafka:axon-kafka")
  implementation("org.axonframework.extensions.kafka:axon-kafka-spring-boot-starter")

  //Kotlin
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.github.microutils:kotlin-logging:3.0.5")
  // Test
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(module = "mockito-core")
  }
  testImplementation ("io.kotest:kotest-runner-junit5:$kotestVersion")
  testImplementation ("io.kotest.extensions:kotest-extensions-spring:1.1.3")
  testImplementation("io.kotest:kotest-framework-datatest:$kotestVersion")
  testImplementation("io.mockk:mockk:1.13.4")
  testImplementation("com.ninja-squad:springmockk:4.0.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "21"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
