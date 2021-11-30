import com.modrinth.minotaur.request.Dependency.DependencyType
import com.modrinth.minotaur.TaskModrinthUpload
import com.modrinth.minotaur.request.VersionType

plugins {
    java
    kotlin("jvm") version "1.6.0"
    id("fabric-loom") version "0.10-SNAPSHOT"
    id("com.matthewprenger.cursegradle") version "1.4.0"
    id("com.modrinth.minotaur") version "1.2.1"
}

group = "de.royzer"
version = "1.0.1"

val minecraftVersion = "1.18"
val yarnMappingsVersion = "1.18+build.1:v2"
val fabricLoaderVersion = "0.12.5"
val fabricApiVersion = "0.43.1+1.18"
val fabricLanguageKotlinVersion = "1.7.0+kotlin.1.6.0"

java.sourceCompatibility = JavaVersion.VERSION_16
java.targetCompatibility = JavaVersion.VERSION_16

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

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
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

tasks {
    register<TaskModrinthUpload>("uploadModrinth") {
        group = "upload"
        onlyIf {
            findProperty("modrinth.token") != null
        }

        token = findProperty("modrinth.token").toString()

        projectId = "santxgdT"
        versionNumber = rootProject.version.toString()
        addGameVersion(minecraftVersion)
        addLoader("fabric")
        addDependency("gjN9CB30", DependencyType.REQUIRED)
        addDependency("1qsZV7U7", DependencyType.REQUIRED)
        versionType = VersionType.RELEASE

        uploadFile = remapJar.get()
    }
}