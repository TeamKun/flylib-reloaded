/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

plugins {
    kotlin("jvm") version "1.5.0"
    `maven-publish`
    `java-library`
}

group = "kotx.minecraft.libs"
version = "0.1.13"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://libraries.minecraft.net")
    maven("https://kotlin.bintray.com/kotlinx/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api("io.insert-koin:koin-core:2.2.2")
    api("io.insert-koin:koin-core-ext:2.2.2")
    api("ch.qos.logback", "logback-classic", "1.2.3")
    api("org.spigotmc", "spigot-api", "1.16.5-R0.1-SNAPSHOT")
    implementation("com.destroystokyo.paper", "paper-api", "1.16.5-R0.1-SNAPSHOT")
    implementation(fileTree("./libs"))
}

tasks {
    compileJava {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
        options.encoding = "UTF-8"
    }

    compileTestJava {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
        options.encoding = "UTF-8"
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    javadoc {
        options.encoding = "UTF-8"
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "kotx.minecraft.libs"
                artifactId = "flylib-reloaded"
                version = project.version.toString()
                from(components["kotlin"])
            }
        }
    }
}