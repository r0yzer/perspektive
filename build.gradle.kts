import com.modrinth.minotaur.dependencies.ModDependency

plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    id("fabric-loom") version "0.12-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.4.3"
    id("org.quiltmc.quilt-mappings-on-loom") version "4.2.1"
    id("io.github.juuxel.loom-quiltflower") version "1.7.2"
    id("com.matthewprenger.cursegradle") version "1.4.0"
}

group = "de.royzer"
version = "1.2.1"

val minecraftVersion = "1.19.1"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com")
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.layered {
        officialMojangMappings()
//        addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:$minecraftVersion+build.1:v2")) (https://github.com/FabricMC/fabric-loom/issues/693 i think)
    })
    modImplementation("net.fabricmc:fabric-loader:0.14.8")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.58.5+1.19.1")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.8.2+kotlin.1.7.10")
    modApi("com.terraformersmc:modmenu:4.0.5")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    processResources {
        val props = mapOf("version" to project.version)

        inputs.properties(props)

        filesMatching("fabric.mod.json") {
            expand(props)
        }
    }
}

modrinth {
    token.set(findProperty("modrinth.token").toString())
    projectId.set("santxgdT")
    versionNumber.set(rootProject.version.toString())
    versionType.set("release")
    uploadFile.set(tasks.remapJar.get())
    gameVersions.set(listOf(minecraftVersion))
    loaders.add("fabric")

    dependencies.set(
        listOf(
            ModDependency("P7dR8mSH", "required"),
            ModDependency("Ha28R6CL", "required"),
            ModDependency("mOgUt4GM", "optional")
        )
    )
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
