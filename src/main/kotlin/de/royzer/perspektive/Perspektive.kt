package de.royzer.perspektive

import com.mojang.blaze3d.platform.InputConstants
import de.royzer.perspektive.settings.PerspektiveSettings
import de.royzer.perspektive.settings.loadConfig
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.minecraft.client.CameraType
import net.minecraft.client.KeyMapping
import net.minecraft.client.KeyMapping.Category
import net.minecraft.client.Minecraft
import net.minecraft.resources.Identifier

object Perspektive {
    // the pitch and yaw of the player when in freecam mode
    @JvmStatic
    var pitch: Float = 0F

    @JvmStatic
    var yaw: Float = 0F

    var freeLookEnabled = false
    var freeLookToggled = false
    private var perspectiveBefore = CameraType.FIRST_PERSON

    private val modKeybindCategory = Category.register(
        Identifier.fromNamespaceAndPath("perspektive", "perspektive")
    )

    private val useKeybind: KeyMapping = KeyMappingHelper.registerKeyMapping(
        KeyMapping("Freelook", InputConstants.Type.KEYBOARD, InputConstants.KEY_Y, modKeybindCategory)
    )
    private val toggleKeybind: KeyMapping = KeyMappingHelper.registerKeyMapping(
        KeyMapping("Toggle Freelook", InputConstants.Type.KEYBOARD, InputConstants.KEY_M, modKeybindCategory)
    )

    fun init() {
        loadConfig()

        ClientTickEvents.END_CLIENT_TICK.register {
            while (toggleKeybind.consumeClick()) {
                if (!freeLookToggled) {
                    perspectiveBefore = Minecraft.getInstance().options.cameraType
                    // Capture player rotation so camera starts behind the character
                    Minecraft.getInstance().player?.let {
                        pitch = it.xRot
                        yaw = it.yRot
                    }
                }
                freeLookEnabled = true
                Minecraft.getInstance().options.cameraType = CameraType.THIRD_PERSON_BACK
                freeLookToggled = !freeLookToggled
            }
            if (useKeybind.isDown) {
                if (freeLookToggled) return@register
                else {
                    if (!freeLookEnabled) {
                        perspectiveBefore = Minecraft.getInstance().options.cameraType
                        // Capture player rotation so camera starts behind the character
                        Minecraft.getInstance().player?.let {
                            pitch = it.xRot
                            yaw = it.yRot
                        }
                    }
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