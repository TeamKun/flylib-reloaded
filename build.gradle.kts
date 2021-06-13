/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

plugins {
    kotlin("jvm") version "1.5.10"
    `maven-publish`
    `java-library`
}

group = "kotx.minecraft.libs"
version = "0.1.14"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.insert-koin:koin-core:2.2.2")
    implementation("io.insert-koin:koin-core-ext:2.2.2")
    implementation("ch.qos.logback", "logback-classic", "1.2.3")
    implementation("com.destroystokyo.paper", "paper-api", "1.16.5-R0.1-SNAPSHOT")
    implementation(fileTree("./libs"))
}

tasks {
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