package de.royzer.perspektive;

import de.royzer.perspektive.settings.PerspektiveSettings;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;

/*
I have to do the OptionInstance in java for now since there is an remapping issue which prevents me from doing so in kotlin
 */
public class CameraDistanceOption {
    public static OptionInstance<Integer> cameraDistanceOption = new OptionInstance<>(
            "perspektive.distance",
            OptionInstance.noTooltip(),
            (optionText, value) -> Options.genericValueLabel(optionText, Component.literal(String.valueOf(Double.parseDouble(String.valueOf(value)) / 10.0))),
            new OptionInstance.IntRange(0, 640), (int) (PerspektiveSettings.INSTANCE.getCameraDistance() * 10),
            integer -> PerspektiveSettings.INSTANCE.setCameraDistance(Double.parseDouble(integer.toString()) / 10)
    );
}
