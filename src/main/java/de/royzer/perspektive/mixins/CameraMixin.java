package de.royzer.perspektive.mixins;

import de.royzer.perspektive.Perspektive;
import de.royzer.perspektive.settings.PerspektiveSettings;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
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

    @Shadow
    protected abstract void move(float x, float y, float z);

    @Shadow
    protected abstract float getMaxZoom(float desiredCameraDistance);

    @Shadow
    private Entity entity;

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
        if (Perspektive.INSTANCE.getFreeLookEnabled() ||
                (PerspektiveSettings.INSTANCE.getCameraDistanceAlsoIn3rdPerson()
                        && Minecraft.getInstance().options.getCameraType() != CameraType.FIRST_PERSON)) {
            this.move(-this.getMaxZoom((float)PerspektiveSettings.INSTANCE.getCameraDistance()), 0.0F, 0.0F);
        }
    }
}
