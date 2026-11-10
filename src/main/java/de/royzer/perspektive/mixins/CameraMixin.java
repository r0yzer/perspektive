package de.royzer.perspektive.mixins;

import de.royzer.perspektive.Perspektive;
import de.royzer.perspektive.settings.PerspektiveSettings;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Shadow
    protected abstract void move(float x, float y, float z);

    @Shadow
    private float getMaxZoom(float desiredDistance) { throw new AssertionError(); }

    /**
     * Inject inside alignWithEntity, right before the third-person move() call.
     * This overrides the camera rotation to the freelook rotation so that
     * the subsequent move() offsets the camera in the correct direction.
     */
    @Inject(
            method = "alignWithEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Camera;getMaxZoom(F)F"
            )
    )
    public void onAlignWithEntity(float partialTicks, CallbackInfo ci) {
        if (Perspektive.INSTANCE.getFreeLookEnabled()) {
            this.setRotation(Perspektive.getYaw(), Perspektive.getPitch());
        }
    }

    @Inject(
            method = "alignWithEntity",
            at = @At("TAIL")
    )
    public void setDistance(float partialTicks, CallbackInfo ci) {
        if (Perspektive.INSTANCE.getFreeLookEnabled() ||
                (PerspektiveSettings.INSTANCE.getCameraDistanceAlsoIn3rdPerson()
                        && Minecraft.getInstance().options.getCameraType() != CameraType.FIRST_PERSON)) {
            float desiredDistance = (float) PerspektiveSettings.INSTANCE.getCameraDistance();
            float clampedDistance = this.getMaxZoom(desiredDistance);
            this.move(-clampedDistance, 0.0F, 0.0F);
        }
    }
}
