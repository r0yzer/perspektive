plugins {
    java
    kotlin("jvm") version "1.5.0"
    id("fabric-loom") version "0.8-SNAPSHOT"
}

group = "de.royzer"
version = "0.1-SNAPSHOT"

val minecraftVersion = "1.17"
val yarnMappingsVersion = "1.17+build.1:v2"
val fabricLoaderVersion = "0.11.3"
val fabricApiVersion = "0.34.9+1.17"
val fabricLanguageKotlinVersion = "1.6.1+kotlin.1.5.10"

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