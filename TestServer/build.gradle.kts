/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "kotx.minecraft.libs"
version = "0.0.1"

repositories {
    mavenCentral()
    jcenter()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.destroystokyo.paper", "paper-api", "1.16.5-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation(parent!!)
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

    processResources {
        filesMatching("*.yml") {
            expand(project.properties)
        }

        from(sourceSets.main.get().resources.srcDirs) {
            filter {
                it.replace("@version@", "0.1")
            }
        }
    }

    shadowJar {
        archiveFileName.set("TestPlugin.jar")
    }

    create<Copy>("buildPlugin") {
        group = "plugin"
        from(shadowJar)
        val dest = File(projectDir, "server/plugins")
        if (File(dest, shadowJar.get().archiveFileName.get()).exists()) dest.delete()
        into(dest)
    }

    create<DefaultTask>("setupWorkspace") {
        group = "plugin"
        doLast {
            val paperDir = File(projectDir, "server")

            paperDir.mkdirs()

            val download by registering(de.undercouch.gradle.tasks.download.Download::class) {
                src("https://papermc.io/api/v2/projects/paper/versions/1.16.5/builds/576/downloads/paper-1.16.5-576.jar")
                dest(paperDir)
            }
            val paper = download.get().outputFiles.first()

            download.get().download()

            runCatching {
                javaexec {
                    workingDir(paperDir)
                    main = "-jar"
                    args("./${paper.name}", "nogui")
                }

                val eula = File(paperDir, "eula.txt")
                eula.writeText(eula.readText(Charsets.UTF_8).replace("eula=false", "eula=true"), Charsets.UTF_8)
                val serverProperties = File(paperDir, "server.properties")
                serverProperties.writeText(
                    serverProperties.readText(Charsets.UTF_8)
                        .replace("online-mode=true", "online-mode=false")
                        .replace("difficulty=easy", "difficulty=peaceful")
                        .replace("spawn-protection=16", "spawn-protection=0")
                        .replace("gamemode=survival", "gamemode=creative")
                        .replace("level-name=world", "level-name=dev_world")
                        .replace("level-type=default", "level-type=flat")
                        .replace("motd=A Minecraft Server", "motd=Kotx Development Server")
                        .replace("max-tick-time=60000", "max-tick-time=-1")
                        .replace("view-distance=10", "view-distance=16"), Charsets.UTF_8
                )
                val runBat = File(paperDir, "run.bat")
                if (!runBat.exists()) {
                    runBat.createNewFile()
                    runBat.writeText("java -jar ./${paper.name} nogui", Charsets.UTF_8)
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
}