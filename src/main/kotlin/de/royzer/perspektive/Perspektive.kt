package de.royzer.perspektive

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.option.Perspective
import net.minecraft.client.render.Camera
import net.minecraft.client.util.InputUtil
import net.minecraft.entity.Entity
import net.minecraft.world.BlockView
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.lwjgl.glfw.GLFW

object Perspektive : ModInitializer {

    // the pitch and yaw of the player when in freecam mode
    @JvmStatic
    var pitch: Float = 0F
    @JvmStatic
    var yaw: Float = 0F

    var freeLookEnabled = false
    var freeLookToggled = false

    private var perspectiveBefore = Perspective.FIRST_PERSON

    override fun onInitialize() {
        val useKeybind = KeyBindingHelper.registerKeyBinding(
            KeyBinding("key.perspektive.use", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_CONTROL, "Perspektive")
        )
        val toggleKeybind = KeyBindingHelper.registerKeyBinding(
            KeyBinding("key.perspektive.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "Perspektive")
        )

        ClientTickEvents.END_CLIENT_TICK.register {

            if (toggleKeybind.isPressed)
                freeLookToggled = !freeLookToggled

            if (useKeybind.isPressed) {
                if (!freeLookEnabled)
                    perspectiveBefore = MinecraftClient.getInstance().options.perspective
                freeLookEnabled = true
                MinecraftClient.getInstance().options.perspective = Perspective.THIRD_PERSON_BACK
            } else if (freeLookEnabled) {
                freeLookEnabled = false
                MinecraftClient.getInstance().options.perspective = perspectiveBefore
            }
        }
    }
}