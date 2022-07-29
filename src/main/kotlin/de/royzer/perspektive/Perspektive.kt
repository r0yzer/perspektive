package de.royzer.perspektive

import com.mojang.blaze3d.platform.InputConstants
import com.terraformersmc.modmenu.ModMenu
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import com.terraformersmc.modmenu.config.ModMenuConfig
import com.terraformersmc.modmenu.gui.ModsScreen
import de.royzer.perspektive.settings.PerspektiveSettings
import de.royzer.perspektive.settings.PerspektiveSettingsScreen
import de.royzer.perspektive.settings.configFile
import de.royzer.perspektive.settings.loadConfig
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.CameraType
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.OptionsScreen
import net.minecraft.client.gui.screens.PauseScreen
import net.minecraft.client.gui.screens.TitleScreen
import org.lwjgl.glfw.GLFW

object Perspektive : ModMenuApi {
    // the pitch and yaw of the player when in freecam mode
    @JvmStatic
    var pitch: Float = 0F

    @JvmStatic
    var yaw: Float = 0F

    var freeLookEnabled = false
    var freeLookToggled = false
    private var perspectiveBefore = CameraType.FIRST_PERSON

    private val useKeybind: KeyMapping = KeyBindingHelper.registerKeyBinding(
        KeyMapping("Freelook", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Y, "Perspektive")
    )
    private val toggleKeybind: KeyMapping = KeyBindingHelper.registerKeyBinding(
        KeyMapping("Toggle Freelook", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, "Perspektive")
    )

    fun init() {
        loadConfig()

        ClientTickEvents.END_CLIENT_TICK.register {
            while (toggleKeybind.consumeClick()) {
                freeLookEnabled = true
                if (!freeLookToggled) perspectiveBefore = Minecraft.getInstance().options.cameraType
                Minecraft.getInstance().options.cameraType = CameraType.THIRD_PERSON_BACK
                freeLookToggled = !freeLookToggled
            }
            if (useKeybind.isDown) {
                if (freeLookToggled) return@register
                else {
                    if (!freeLookEnabled)
                        perspectiveBefore = Minecraft.getInstance().options.cameraType
                    freeLookEnabled = true
                    Minecraft.getInstance().options.cameraType = CameraType.THIRD_PERSON_BACK
                }
            } else if (freeLookEnabled && !freeLookToggled) {
                freeLookEnabled = false
                Minecraft.getInstance().options.cameraType =
                    if (PerspektiveSettings.shouldReturnToFirstPerson) CameraType.FIRST_PERSON else perspectiveBefore
            }
        }
    }
}