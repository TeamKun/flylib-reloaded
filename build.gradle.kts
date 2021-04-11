/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

plugins {
    kotlin("jvm") version "1.4.32"
    `maven-publish`
}

group = "kotx.minecraft.libs"
version = "0.0.14"

repositories {
    mavenCentral()
    jcenter()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://kotlin.bintray.com/kotlinx/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.koin:koin-core:2.2.2")
    implementation("org.koin:koin-core-ext:2.2.2")
    implementation("ch.qos.logback", "logback-classic", "1.2.3")
    implementation("com.destroystokyo.paper", "paper-api", "1.16.5-R0.1-SNAPSHOT")
}

tasks {
    compileJava {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
        options.encoding = "UTF-8"
    }

    compileKotlin {
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