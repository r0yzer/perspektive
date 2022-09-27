package de.royzer.perspektive.settings

import com.mojang.blaze3d.vertex.PoseStack
import de.royzer.perspektive.CameraDistanceOption
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component


class PerspektiveSettingsScreen(
    private val lastScreen: Screen
) : Screen(Component.literal("Perspektive")) {

    override fun init() {
        addRenderableWidget(
            Button(this.width / 2 - 185, this.height / 6 + 24 - 6, 250, 20, Component.translatable("perspektive.returnToFirstPerson").append(": ").append(if (PerspektiveSettings.shouldReturnToFirstPerson) CommonComponents.OPTION_ON else CommonComponents.OPTION_OFF)) {
                PerspektiveSettings.shouldReturnToFirstPerson = !PerspektiveSettings.shouldReturnToFirstPerson
                this.minecraft?.setScreen(this)
            }
        )
        addRenderableWidget(
            Button(this.width / 2 + 85, this.height / 6 + 24 - 6, 100, 20, Component.nullToEmpty("Reset")) {
                PerspektiveSettings.shouldReturnToFirstPerson = false
                this.minecraft?.setScreen(this)
            }
        )
        addRenderableWidget(CameraDistanceOption.cameraDistanceOption.createButton(this.minecraft!!.options, width / 2 - 185, height / 6 + 48, 250))
        addRenderableWidget(
            Button(this.width / 2 + 85, this.height / 6 + 48, 100, 20, Component.nullToEmpty("Reset")) {
                PerspektiveSettings.cameraDistance = 0.0
                CameraDistanceOption.cameraDistanceOption.set(0)
                this.minecraft?.setScreen(this)
            }
        )
        addRenderableWidget(
            Button(width / 2 - 100, this.height - 35, 200, 20, CommonComponents.GUI_DONE) {
                onClose()
            }
        )
    }

    override fun onClose() {
        saveConfig()
        this.minecraft?.setScreen(lastScreen)
    }

    override fun render(matrices: PoseStack, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        drawCenteredString(matrices, font, title, width / 2, 15, 0xFFFFFF)
        super.render(matrices, mouseX, mouseY, delta)
    }
}

// not usable until remapping issue fix
//no idea what this does it creates this slider thing somehow
//val cameraDistance = OptionInstance("perspektive.distance", OptionInstance.noTooltip(), { optionText: Component, value: Int ->
//    Options.genericValueLabel(optionText, Component.literal("${value.toDouble() / 10.0}"))
//}, OptionInstance.IntRange(0, 640), 0) {
//    PerspektiveSettings.cameraDistance = it / 10.0
//}
