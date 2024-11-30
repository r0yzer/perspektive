package de.royzer.perspektive.mixins;

import de.royzer.perspektive.mixinskt.MouseHandlerMixinKt;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.ScrollWheelHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Redirect(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/ScrollWheelHandler;getNextScrollWheelSelection(DII)I"))
    public int onScroll(double d, int selectedSlot, int selectionSize) {
        if(MouseHandlerMixinKt.onScroll(d)) {
            return selectedSlot;
        }
        return ScrollWheelHandler.getNextScrollWheelSelection(d, selectedSlot, selectionSize);
    }
}
