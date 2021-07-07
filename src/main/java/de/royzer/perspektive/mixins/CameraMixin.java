package de.royzer.perspektive.mixins;

import de.royzer.perspektive.Perspektive;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    private boolean firstPress = true;

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Inject(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V",
                    shift = At.Shift.AFTER,
                    ordinal = 0
            )
    )
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (Perspektive.INSTANCE.getFreeLookEnabled()) {
            if (firstPress) {
                Perspektive.setPitch(focusedEntity.getPitch());
                Perspektive.setYaw(focusedEntity.getYaw());
            }
            firstPress = false;
            this.setRotation(Perspektive.getYaw(), Perspektive.getPitch());
        } else {
            firstPress = true;
        }
    }
}
