package de.royzer.perspektive.settings

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.minecraft.client.Minecraft
import java.io.File

@kotlinx.serialization.Serializable
object PerspektiveSettings {
    var shouldReturnToFirstPerson = false
    var cameraDistance = 0.0
    var blockInventoryScrolling = true
    var scrollingEnabled = true
    var cameraDistanceAlsoIn3rdPerson = false
}

@kotlinx.serialization.Serializable
data class PerspektiveSettingsFile(
    var shouldReturnToFirstPerson: Boolean?,
    var cameraDistance: Double?,
    var blockInventoryScrolling: Boolean?,
    var scrollingEnabled: Boolean?,
    var cameraDistanceAlsoIn3rdPerson: Boolean?,
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
    PerspektiveSettings.shouldReturnToFirstPerson =
        settings.shouldReturnToFirstPerson ?: PerspektiveSettings.shouldReturnToFirstPerson
    PerspektiveSettings.cameraDistance = settings.cameraDistance ?: PerspektiveSettings.cameraDistance
    PerspektiveSettings.blockInventoryScrolling =
        settings.blockInventoryScrolling ?: PerspektiveSettings.blockInventoryScrolling
    PerspektiveSettings.scrollingEnabled =
        settings.scrollingEnabled ?: PerspektiveSettings.scrollingEnabled
    PerspektiveSettings.cameraDistanceAlsoIn3rdPerson =
        settings.cameraDistanceAlsoIn3rdPerson ?: PerspektiveSettings.cameraDistanceAlsoIn3rdPerson
}

fun saveConfig() {
    configFile.writeText(
        Json.encodeToString(
            PerspektiveSettingsFile(
                PerspektiveSettings.shouldReturnToFirstPerson,
                PerspektiveSettings.cameraDistance,
                PerspektiveSettings.blockInventoryScrolling,
                PerspektiveSettings.scrollingEnabled,
                PerspektiveSettings.cameraDistanceAlsoIn3rdPerson,
            )
        )
    )
}

val configFile = File(Minecraft.getInstance().gameDirectory.path + "/config", "perspektive.json")
