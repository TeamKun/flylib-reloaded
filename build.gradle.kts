/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

plugins {
    kotlin("jvm") version "1.5.20"
    java
    `maven-publish`
    signing
}

val projectName: String = "flylib-reloaded"
val projectGroup: String = "dev.kotx"
val projectVersion: String = "0.3.15"

group = projectGroup
version = projectVersion

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.insert-koin:koin-core:3.1.2")
    implementation("io.insert-koin:koin-core-ext:3.0.2")
    compileOnly("com.destroystokyo.paper", "paper-api", "1.16.5-R0.1-SNAPSHOT")
    compileOnly(fileTree("./libs"))
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks {
    compileJava {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
        options.isFork = true
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    javadoc {
        options.encoding = "UTF-8"
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

            artifact(packageJavadoc)
            artifact(packageSources)

            artifactId = projectName
            groupId = projectGroup
            version = projectVersion

            pom {
                name.set(projectName)
                description.set("A utility library for Minecraft Paper that provides commands, menus, Kotlin extensions, and more.")
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
                    connection.set("git@github.com:TeamKun/flylib-reloaded.git")
                    developerConnection.set("git@github.com:TeamKun/flylib-reloaded.git")
                    url.set("https://github.com/TeamKun/flylib-reloaded")
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