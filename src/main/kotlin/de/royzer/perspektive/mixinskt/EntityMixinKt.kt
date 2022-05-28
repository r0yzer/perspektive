package de.royzer.perspektive.mixinskt

import de.royzer.perspektive.Perspektive
import net.minecraft.util.Mth
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

object EntityMixinKt {
    @JvmStatic
    fun blockPlayerRotation(deltaX: Double, deltaY: Double, ci: CallbackInfo) {
        if (Perspektive.freeLookEnabled) {
            Perspektive.pitch = Mth.clamp((Perspektive.pitch + (deltaY * 0.15)).toFloat(), -90F, 90F)
            Perspektive.yaw += (deltaX * 0.15).toFloat()
            ci.cancel()
        }
    }
}

