import com.modrinth.minotaur.dependencies.ModDependency

plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
    id("fabric-loom") version "1.8-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.8.7"
    id("org.quiltmc.quilt-mappings-on-loom") version "4.2.3"
    id("com.matthewprenger.cursegradle") version "1.4.0"
}

group = "de.royzer"
version = "1.4.1"

val minecraftVersion = "1.21.3"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com")
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.16.9")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.109.0+1.21.3")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.12.3+kotlin.2.0.21")
    modApi("com.terraformersmc:modmenu:12.0.0-beta.1")
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
    projectId.set("perspektive")
    versionNumber.set(rootProject.version.toString())
    versionType.set("release")
    uploadFile.set(tasks.remapJar.get())
    gameVersions.set(listOf(minecraftVersion))
    loaders.addAll(listOf("fabric", "quilt"))

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
            optionalDependency("modmenu")
        })
    })
    options(closureOf<com.matthewprenger.cursegradle.Options> {
        forgeGradleIntegration = false
    })
}


configurations.all {
    resolutionStrategy {
//        force("net.fabricmc:fabric-loader:0.14.21")
    }
}