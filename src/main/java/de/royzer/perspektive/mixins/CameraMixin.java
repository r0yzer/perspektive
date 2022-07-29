package de.royzer.perspektive.mixins;

import de.royzer.perspektive.Perspektive;
import de.royzer.perspektive.settings.PerspektiveSettings;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    private boolean firstPress = true;

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Shadow
    protected abstract void move(double x, double y, double z);

    @Shadow
    protected abstract double getMaxZoom(double desiredCameraDistance);

    @Inject(
        method = "setup",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Camera;setRotation(FF)V",
            shift = At.Shift.AFTER,
            ordinal = 0
        )
    )
    public void update(BlockGetter area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (Perspektive.INSTANCE.getFreeLookEnabled()) {
            if (firstPress) {
                Perspektive.setPitch(focusedEntity.getXRot());
                Perspektive.setYaw(focusedEntity.getYRot());
            }
            firstPress = false;
            this.setRotation(Perspektive.getYaw(), Perspektive.getPitch());
        } else {
            firstPress = true;
        }
    }

    @Inject(
            method = "setup",
            at = @At("TAIL")
    )
    public void setDistance(BlockGetter area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (Perspektive.INSTANCE.getFreeLookEnabled()) {
            this.move(-this.getMaxZoom(PerspektiveSettings.INSTANCE.getCameraDistance()), 0.0, 0.0);
        }
    }
}
