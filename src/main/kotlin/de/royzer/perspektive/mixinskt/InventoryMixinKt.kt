package de.royzer.perspektive.mixinskt

import de.royzer.perspektive.Perspektive
import de.royzer.perspektive.settings.PerspektiveSettings
import net.minecraft.client.CameraType
import net.minecraft.client.Minecraft
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import kotlin.math.sign

object InventoryMixinKt {
    @JvmStatic
    fun onScroll(d: Double, ci: CallbackInfo) {
        if (Minecraft.getInstance().options.cameraType == CameraType.FIRST_PERSON) return
        if (!Perspektive.freeLookEnabled && !PerspektiveSettings.cameraDistanceAlsoIn3rdPerson) return
        if (!PerspektiveSettings.scrollingEnabled) return
        val i = sign(d)
        val nextDistance = PerspektiveSettings.cameraDistance - i
        if (nextDistance < 0.0) PerspektiveSettings.cameraDistance = 0.0
        else if (nextDistance > 64.0) PerspektiveSettings.cameraDistance = 64.0
        else PerspektiveSettings.cameraDistance -= i
        if ((PerspektiveSettings.blockInventoryScrolling && Perspektive.freeLookEnabled) || (PerspektiveSettings.cameraDistanceAlsoIn3rdPerson && PerspektiveSettings.blockInventoryScrolling))
            ci.cancel()
    }
}