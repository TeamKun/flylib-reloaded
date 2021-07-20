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

val projectName = "flylib-reloaded"
val projectGroup = "dev.kotx"
val projectVersion = "0.3.5"

group = projectGroup
version = projectVersion

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    api(kotlin("stdlib-jdk8"))
    api("io.insert-koin:koin-core:2.2.2")
    api("io.insert-koin:koin-core-ext:2.2.2")
    implementation("com.destroystokyo.paper", "paper-api", "1.16.5-R0.1-SNAPSHOT")
    implementation(fileTree("./libs"))
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks {
    compileJava {
        sourceCompatibility = "15"
        targetCompatibility = "15"
        options.isFork = true
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = "15"
        }
    }

    javadoc {
        options.encoding = "UTF-8"
    }
}

val packageJavadoc by tasks.registering(Jar::class) {
    from(tasks.javadoc.get().destinationDir)
    archiveClassifier.set("javadoc")
    archiveExtension.set("jar")

    dependsOn(tasks.javadoc)
}

val packageSources by tasks.registering(Jar::class) {
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