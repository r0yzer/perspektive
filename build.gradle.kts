import com.modrinth.minotaur.dependencies.ModDependency


val minecraftVersion = project.property("minecraft_version") as String
val modVersion = project.property("mod_version") as String
val loaderVersion = project.property("loader_version") as String
val fabricAPIVersion = project.property("fabric_version") as String
val kotlinVersion = project.property("fabric_language_kotlin_version") as String
val modmenuVersion = project.property("modmenu_version") as String
val groupString = project.property("group") as String

plugins {
    kotlin("jvm") version "2.4.21"
    kotlin("plugin.serialization") version "2.4.21"
    id("net.fabricmc.fabric-loom") version "1.19-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.10.0"
    id("com.matthewprenger.cursegradle") version "1.4.0"
}

group = groupString
version = modVersion

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com")
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    // No mappings needed for MC 26.1+ (unobfuscated)
    implementation("net.fabricmc:fabric-loader:$loaderVersion")
    implementation("net.fabricmc.fabric-api:fabric-api:$fabricAPIVersion")
    implementation("net.fabricmc:fabric-language-kotlin:$kotlinVersion")
    api("com.terraformersmc:modmenu:$modmenuVersion")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(25)
    }
    compileKotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25)
        }
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
    uploadFile.set(tasks.named("jar").get())
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
        mainArtifact(tasks.getByName("jar").outputs.files.first())

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