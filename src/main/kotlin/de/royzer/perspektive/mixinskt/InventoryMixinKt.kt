package de.royzer.perspektive.mixinskt

import de.royzer.perspektive.Perspektive
import de.royzer.perspektive.settings.PerspektiveSettings
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import kotlin.math.sign

object InventoryMixinKt {
    @JvmStatic
    fun onScroll(d: Double, ci: CallbackInfo) {
        val i = sign(d)
        val nextDistance = PerspektiveSettings.cameraDistance - i
        if (nextDistance < 0.0) PerspektiveSettings.cameraDistance = 0.0
        else if (nextDistance > 64.0) PerspektiveSettings.cameraDistance = 64.0
        else PerspektiveSettings.cameraDistance -= i
        if (PerspektiveSettings.blockInventoryScrolling && Perspektive.freeLookEnabled)
            ci.cancel()
    }
}