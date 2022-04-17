package de.royzer.perspektive

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.option.Perspective
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

object Perspektive {
    // the pitch and yaw of the player when in freecam mode
    @JvmStatic
    var pitch: Float = 0F

    @JvmStatic
    var yaw: Float = 0F
    var freeLookEnabled = false
    var freeLookToggled = false
    private var perspectiveBefore = Perspective.FIRST_PERSON

    private val useKeybind: KeyBinding = KeyBindingHelper.registerKeyBinding(
        KeyBinding("Freelook", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Y, "Perspektive")
    )
    private val toggleKeybind: KeyBinding = KeyBindingHelper.registerKeyBinding(
        KeyBinding("Toggle Freelook", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "Perspektive")
    )

    fun init() {
        ClientTickEvents.END_CLIENT_TICK.register {
            while (toggleKeybind.wasPressed()) {
                freeLookEnabled = true
                MinecraftClient.getInstance().options.perspective = Perspective.THIRD_PERSON_BACK
                freeLookToggled = !freeLookToggled
            }
            if (useKeybind.isPressed) {
                if (freeLookToggled) return@register
                else {
                    if (!freeLookEnabled)
                        perspectiveBefore = MinecraftClient.getInstance().options.perspective
                    freeLookEnabled = true
                    MinecraftClient.getInstance().options.perspective = Perspective.THIRD_PERSON_BACK
                }
            } else if (freeLookEnabled && !freeLookToggled) {
                freeLookEnabled = false
                MinecraftClient.getInstance().options.perspective = perspectiveBefore
            }
        }
    }
}