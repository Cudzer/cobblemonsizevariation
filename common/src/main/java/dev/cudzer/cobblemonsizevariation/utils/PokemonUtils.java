package dev.cudzer.cobblemonsizevariation.utils;

import com.cobblemon.mod.common.client.CobblemonResources;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import dev.cudzer.cobblemonsizevariation.config.ModConfig;
import dev.cudzer.cobblemonsizevariation.config.SizeDefinition;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.awt.*;

import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

public class PokemonUtils {

    public static void buildSizeText(GuiGraphics context, float scaleMultiplier, float x, float y){
        SizeDefinition sizeDefinition = ModConfig.getSizeDefinition(scaleMultiplier);
        if(sizeDefinition != null){
            drawScaledText(
                    context,
                    CobblemonResources.INSTANCE.getDEFAULT_LARGE(),
                    Component.literal(sizeDefinition.getName()),
                    x ,
                    y,
                    0.7f,
                    1.0f,
                    Integer.MAX_VALUE,
                    Color.decode(sizeDefinition.getColor()).getRGB(),
                    false,
                    true,
                    null,
                    null

            );
        }
        else{
            CobblemonSizeVariation.LOGGER.error("Was not able to get size definition from the config. Please check that the configuration is complete.");
        }
    }
}
