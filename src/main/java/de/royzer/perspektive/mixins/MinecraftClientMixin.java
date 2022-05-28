package de.royzer.perspektive.mixins;

import de.royzer.perspektive.Perspektive;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
    @Redirect(
            method = "handleKeybinds",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Options;setCameraType(Lnet/minecraft/client/CameraType;)V"
            )
    )
    public void blockPerspectiveChange(Options instance, CameraType perspective) {
        if (!Perspektive.INSTANCE.getFreeLookToggled()) {
            Minecraft.getInstance().options.setCameraType(Minecraft.getInstance().options.getCameraType().cycle());
        }
    }
}
