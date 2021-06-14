/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

import com.github.jengelman.gradle.plugins.shadow.ShadowExtension

plugins {
    kotlin("jvm") version "1.5.10"
    id("com.github.johnrengelman.shadow") version "5.2.0"
    java
    `maven-publish`
}

val projectName = "flylib-reloaded"
val projectGroup = "dev.kotx"
val projectVersion = "0.1.21"

group = projectGroup
version = projectVersion

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    api(kotlin("stdlib-jdk8"))
    api("io.insert-koin:koin-core:2.2.2")
    api("io.insert-koin:koin-core-ext:2.2.2")
    api("ch.qos.logback", "logback-classic", "1.2.3")
    implementation("com.destroystokyo.paper", "paper-api", "1.16.5-R0.1-SNAPSHOT")
    implementation(fileTree("./libs"))
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    javadoc {
        options.encoding = "UTF-8"
    }

    shadowJar {
        archiveBaseName.set(projectName)
        archiveClassifier.set("")
        relocate("kotlin", "dev.kotx.kotlin")
        relocate("org.koin", "dev.kotx.koin")
        relocate("ch.qos.logback", "dev.kotx.logback")
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                configure<ShadowExtension> {
                    component(this@create)
                }
                artifactId = projectName
                groupId = projectGroup
                version = projectVersion
            }
        }
    }
}