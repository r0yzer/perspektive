package de.royzer.perspektive.mixins;

import de.royzer.perspektive.mixinskt.EntityMixinKt;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(
        method = "turn",
        at = @At(
            value = "HEAD"
        ),
        cancellable = true
    )
    private void changeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        EntityMixinKt.blockPlayerRotation(cursorDeltaX, cursorDeltaY, ci);
    }
}
