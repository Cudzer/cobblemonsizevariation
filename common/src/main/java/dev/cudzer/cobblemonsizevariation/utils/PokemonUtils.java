package dev.cudzer.cobblemonsizevariation.utils;

import com.cobblemon.mod.common.client.CobblemonResources;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.awt.*;

import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

public class PokemonUtils {

    public static void buildSizeText(GuiGraphics context, float scaleMultiplier, float x, float y){
        drawScaledText(
                context,
                CobblemonResources.INSTANCE.getDEFAULT_LARGE(),
                getSizeText(scaleMultiplier),
                x ,
                y,
                0.7f,
                1.0f,
                Integer.MAX_VALUE,
                getSizeColor(scaleMultiplier),
                false,
                true,
                null,
                null

        );
    }

    private static MutableComponent getSizeText(float size){
        if(size >= 0.2 && size <= 0.5){
            return Component.literal("Tiny");
        }
        else if(size >= 0.51 && size <= 0.9){
            return Component.literal("Small");
        }
        else if(size >= 0.91 && size <= 1.2){
            return Component.literal("Average");
        }
        else if(size >= 1.21 && size <= 1.6){
            return Component.literal("Big");
        }
        else if(size >= 1.61 && size <= 1.9){
            return Component.literal("Large");
        }
        else {
            return Component.literal("Huge");
        }
    }

    private static int getSizeColor(float size){
        if(size >= 0.2 && size <= 0.5){
            return Color.CYAN.getRGB();
        }
        else if(size >= 0.51 && size <= 0.9){
            return Color.YELLOW.getRGB();
        }
        else if(size >= 0.91 && size <= 1.2){
            return Color.WHITE.getRGB();
        }
        else if(size >= 1.21 && size <= 1.6){
            return Color.GREEN.getRGB();
        }
        else if(size >= 1.61 && size <= 1.9){
            return Color.MAGENTA.getRGB();
        }
        else {
            return Color.RED.getRGB();
        }
    }
}
