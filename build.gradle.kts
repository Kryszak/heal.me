plugins {
    id("org.springframework.boot") version "2.7.8"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.jetbrains.kotlin.jvm") version "1.7.22"
    id("org.jetbrains.kotlin.plugin.spring") version "1.7.22"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.7.22"
    jacoco
}

group = "net.kryszak"
version = "0.0.1-SNAPSHOT"


val kotestVersion = "5.3.2"
val kotestArrowExtensionVersion = "1.2.5"
val kotestSpringExtensionVersion = "1.1.1"
val mockkVersion = "1.12.4"
val h2Version = "1.3.148"
val loggingVersion = "3.0.5"
val arrowVersion = "1.1.5"
val jacksonKotlinVersion = "2.14.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonKotlinVersion")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.microutils:kotlin-logging-jvm:$loggingVersion")

    implementation("io.arrow-kt:arrow-core:$arrowVersion")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:$kotestSpringExtensionVersion")
    testImplementation("io.kotest.extensions:kotest-assertions-arrow:$kotestArrowExtensionVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("com.h2database:h2:$h2Version")

}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.test {
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.8"
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(false)
    }
    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it).apply {
                exclude(
                    "net/kryszak/healme/**/configuration/*",
                    "net/kryszak/healme/**/*Entity.*"
                )
            }
        }))
    }
}

