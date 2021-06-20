/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

plugins {
    kotlin("jvm") version "1.5.10"
    java
    `maven-publish`
    signing
}

val projectName = "flylib-reloaded"
val projectGroup = "dev.kotx"
val projectVersion = "0.2.4"

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

java {
    withJavadocJar()
    withSourcesJar()
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
}

val packageJavadoc by tasks.registering(Jar::class) {
    from(tasks.javadoc.get())
    archiveClassifier.set("javadoc")
}

val packageSources by tasks.registering(Jar::class) {
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
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
            }
        }
    }

    repositories {
        maven {
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