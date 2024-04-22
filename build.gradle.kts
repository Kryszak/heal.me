plugins {
    val kotlinVersion = "1.9.23"
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    jacoco
}

group = "io.github.kryszak"
version = "0.0.1-SNAPSHOT"

val kotestVersion = "5.8.1"
val kotestArrowExtensionVersion = "1.4.0"
val kotestSpringExtensionVersion = "1.1.3"
val mockkVersion = "1.13.10"
val h2Version = "2.2.224"
val loggingVersion = "3.0.5"
val arrowVersion = "1.2.4"
val jacksonKotlinVersion = "2.17.0"

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
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }
    withType<Jar>() {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.required.set(true)
            html.required.set(false)
        }
    }
    withType<JacocoReport> {
        afterEvaluate {
            classDirectories.setFrom(files(classDirectories.files.map {
                fileTree(it).apply {
                    exclude(
                        "io/github/kryszak/healme/**/configuration/*",
                        "io/github/kryszak/healme/**/*Entity.*"
                    )
                }
            }))
        }
    }
}

jacoco {
    toolVersion = "0.8.9"
}
