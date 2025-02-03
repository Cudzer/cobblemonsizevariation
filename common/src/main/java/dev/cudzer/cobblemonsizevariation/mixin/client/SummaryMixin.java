package dev.cudzer.cobblemonsizevariation.mixin.client;

import com.cobblemon.mod.common.client.gui.pokedex.PokedexGUIConstants;
import com.cobblemon.mod.common.client.gui.summary.Summary;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.cudzer.cobblemonsizevariation.utils.PokemonUtils;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Summary.class)
public class SummaryMixin {
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/Screen;render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V")
    )
    private void displaySizeIcon(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        Summary summary = (Summary) (Object) this;
        int x = (summary.width - Summary.BASE_WIDTH) / 2;
        int y = (summary.height - Summary.BASE_HEIGHT) / 2;

        Pokemon pokemon = summary.getSelectedPokemon$common();
        float scaleModifier = pokemon.getScaleModifier();
                PokemonUtils.buildSizeText(context, scaleModifier, (x + 10), (y + 26));
    }
}
