/*
 * Copyright (c) 2021 kotx__
 */

plugins {
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
    id("org.jetbrains.dokka") version "1.5.30"
    `maven-publish`
    signing
}

val projectName: String = "flylib-reloaded"
val projectGroup: String = "dev.kotx"
val projectVersion: String = "0.4.1"

group = projectGroup
version = projectVersion

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
    api("io.insert-koin:koin-core:3.1.3")
    api("io.insert-koin:koin-core-ext:3.0.2")

    compileOnly("com.destroystokyo.paper", "paper-api", "1.16.5-R0.1-SNAPSHOT")
    compileOnly(fileTree("./libs"))
    dokkaGfmPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.5.31")
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks {
    javadoc {
        options.encoding = "UTF-8"
    }

    dokkaHtml.configure {
        outputDirectory.set(file("./dokka/html"))
    }

    dokkaJavadoc.configure {
        outputDirectory.set(file("./dokka/javadoc"))
    }
}

val packageJavadoc: TaskProvider<Jar> by tasks.registering(Jar::class) {
    from(tasks.javadoc.get().destinationDir)
    archiveClassifier.set("javadoc")
    archiveExtension.set("jar")

    dependsOn(tasks.javadoc)
}

val packageSources: TaskProvider<Jar> by tasks.registering(Jar::class) {
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
    archiveExtension.set("jar")
}

publishing {
    publications {
        create<MavenPublication>("release") {
            from(components["kotlin"])

            artifactId = projectName
            groupId = projectGroup
            version = projectVersion

            artifact(packageJavadoc)
            artifact(packageSources)

            pom {
                name.set(projectName)
                description.set("An utility library for Minecraft Paper that provides commands, menus, Kotlin extensions, and more.")
                url.set("https://github.com/TeamKun/flylib-reloaded")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/mit-license.php")
                    }
                }

                developers {
                    developer {
                        id.set("kotx__")
                        name.set("kotlin chan")
                    }
                }

                organization {
                    name.set("TeamKun")
                    url.set("https://github.com/TeamKun")
                }
            }
        }
    }

    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")

            val mavenUserName = if (hasProperty("maven_username")) findProperty("maven_username") as String else ""
            val mavenPassword = if (hasProperty("maven_password")) findProperty("maven_password") as String else ""

            credentials {
                username = mavenUserName
                password = mavenPassword
            }
        }
    }
}

signing {
    sign(publishing.publications["release"])
}