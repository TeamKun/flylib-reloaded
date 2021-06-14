/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

import com.github.jengelman.gradle.plugins.shadow.*
import com.github.jengelman.gradle.plugins.shadow.tasks.*

plugins {
    kotlin("jvm") version "1.5.10"
    id("com.github.johnrengelman.shadow") version "5.2.0"
    java
    `maven-publish`
    signing
}

val projectName = "flylib-reloaded"
val projectGroup = "dev.kotx"
val projectVersion = "0.1.22"

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

    val relocateShadow by registering(ConfigureShadowRelocation::class) {
        target = shadowJar.get()
        prefix = "dev.kotx"
    }

    shadowJar {
        archiveBaseName.set(projectName)
        archiveClassifier.set("")

        dependsOn(relocateShadow)
    }
}

publishing {
    publications {
        create<MavenPublication>("release") {
            configure<ShadowExtension> {
                component(this@create)
            }
            artifactId = projectName
            groupId = projectGroup
            version = projectVersion

            pom {
                name.set(projectName)
                description.set("a utility library for Minecraft Paper that provides commands, menus, Kotlin extensions, and more.")
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
                        email.set("developer.hazuku@gmail.com")
                    }
                }

                scm {
                    connection.set("https://github.com/TeamKun/flylib-reloaded.git")
                    developerConnection.set("https://github.com/Kotlin-chan")
                    url.set("https://github.com/TeamKun/flylib-reloaded")
                }
            }
        }
    }

    repositories {
        maven {
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            val credentialFile = file("./.credential")
            val credential = credentialFile.readText().split("\n")
            credentials {
                username = credential[0]
                password = credential[1]
            }
        }
    }
}

signing {
    sign(publishing.publications["release"])
}