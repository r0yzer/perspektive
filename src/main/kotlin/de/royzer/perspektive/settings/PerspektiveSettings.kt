package de.royzer.perspektive.settings

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.minecraft.client.Minecraft
import java.io.File

@kotlinx.serialization.Serializable
object PerspektiveSettings {
    var shouldReturnToFirstPerson = false
    var cameraDistance = 0.0
}

@kotlinx.serialization.Serializable
data class PerspektiveSettingsFile(
    var shouldReturnToFirstPerson: Boolean?,
    var cameraDistance: Double?,
)

fun loadConfig() {
    configFile.createNewFile()
    val settings = try {
        Json.decodeFromString<PerspektiveSettingsFile>(configFile.readText())
    } catch (e: Exception) {
        saveConfig() // if config file is empty create one by saving defaults
        loadConfig()
        return
    }
    PerspektiveSettings.shouldReturnToFirstPerson = settings.shouldReturnToFirstPerson ?: PerspektiveSettings.shouldReturnToFirstPerson
    PerspektiveSettings.cameraDistance = settings.cameraDistance ?: PerspektiveSettings.cameraDistance
}

fun saveConfig() {
    configFile.writeText(Json.encodeToString(PerspektiveSettingsFile(
        PerspektiveSettings.shouldReturnToFirstPerson, PerspektiveSettings.cameraDistance
    )))
}

val configFile = File(Minecraft.getInstance().gameDirectory , "perspektive.json")
