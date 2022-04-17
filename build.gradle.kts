import com.modrinth.minotaur.dependencies.ModDependency

plugins {
    java
    kotlin("jvm") version "1.6.20"
    id("fabric-loom") version "0.11-SNAPSHOT"
    id("com.matthewprenger.cursegradle") version "1.4.0"
    id("com.modrinth.minotaur") version "2.1.1"
}

group = "de.royzer"
version = "1.0.2"

val minecraftVersion = "1.18.2"
val yarnMappingsVersion = "1.18.2+build.3"
val fabricLoaderVersion = "0.13.3"
val fabricApiVersion = "0.50.0+1.18.2"
val fabricLanguageKotlinVersion = "1.7.3+kotlin.1.6.20"

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnMappingsVersion")
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricLanguageKotlinVersion")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    compileKotlin {
        targetCompatibility = "17"
    }
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
}

curseforge {
    apiKey = findProperty("curseforge.token") ?: ""
    project(closureOf<com.matthewprenger.cursegradle.CurseProject> {
        mainArtifact(tasks.getByName("remapJar").outputs.files.first())

        id = "501553"
        releaseType = "release"
        addGameVersion(minecraftVersion)

        relations(closureOf<com.matthewprenger.cursegradle.CurseRelation> {
            requiredDependency("fabric-api")
            requiredDependency("fabric-language-kotlin")
        })
    })
    options(closureOf<com.matthewprenger.cursegradle.Options> {
        forgeGradleIntegration = false
    })
}

modrinth {
    token.set(findProperty("modrinth.token").toString())
    projectId.set("santxgdT")
    versionNumber.set(rootProject.version.toString())
    versionType.set("release")
    uploadFile.set(tasks.remapJar.get())
    gameVersions.addAll("1.18", "1.18.1", "1.18.2")
    loaders.add("fabric")

    dependencies.set(
        mutableListOf(
            ModDependency("P7dR8mSH", "required"),
            ModDependency("Ha28R6CL", "required")
        )
    )
}