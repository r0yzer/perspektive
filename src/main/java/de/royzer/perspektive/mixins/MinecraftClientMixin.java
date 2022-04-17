package de.royzer.perspektive.mixins;

import de.royzer.perspektive.Perspektive;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Redirect(
            method = "handleInputEvents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/option/GameOptions;setPerspective(Lnet/minecraft/client/option/Perspective;)V"
            )
    )
    public void blockPerspectiveChange(GameOptions instance, Perspective perspective) {
        if (!Perspektive.INSTANCE.getFreeLookToggled()) {
            MinecraftClient.getInstance().options.setPerspective(MinecraftClient.getInstance().options.getPerspective().next());
        }
    }
}
