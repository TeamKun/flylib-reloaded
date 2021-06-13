/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

plugins {
    kotlin("jvm") version "1.5.10"
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

val projectName = "flylib-reloaded"
val projectGroup = "dev.kotx"
val projectVersion = "0.1.16"

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
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.insert-koin:koin-core:2.2.2")
    implementation("io.insert-koin:koin-core-ext:2.2.2")
    implementation("ch.qos.logback", "logback-classic", "1.2.3")
    implementation("com.destroystokyo.paper", "paper-api", "1.16.5-R0.1-SNAPSHOT")
    implementation(fileTree("./libs"))
}

val sources by tasks.registering(Jar::class) {
    archiveBaseName.set(projectName)
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
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
        archiveVersion.set(projectVersion)
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                artifactId = projectName
                groupId = projectGroup
                version = projectVersion
                from(components["kotlin"])
                artifact(sources.get())
            }
        }
    }
}