package dev.cudzer.cobblemonsizevariation.network;

import com.cobblemon.mod.common.net.messages.client.PokemonUpdatePacket;
import com.cobblemon.mod.common.net.messages.client.pokemon.update.SingleUpdatePacket;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;
import kotlin.jvm.functions.Function0;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SizeChangedPacket extends SingleUpdatePacket<Double, SizeChangedPacket> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(CobblemonSizeVariation.MOD_ID, "pokemon_size_changed");
    private final double newValue;

    public SizeChangedPacket(@NotNull Function0<? extends Pokemon> pokemon, Double value) {
        super(pokemon, value);
        this.newValue = value;
    }


    @Override
    public void encodeValue(@NotNull RegistryFriendlyByteBuf buffer) {
        buffer.writeDouble(newValue);
    }

    @Override
    public void set(@NotNull Pokemon pokemon, Double aDouble) {
        pokemon.setScaleModifier(aDouble.floatValue());
    }

    @NotNull
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static SizeChangedPacket decode(RegistryFriendlyByteBuf buffer){
        var pokemon = PokemonUpdatePacket.Companion.decodePokemon(buffer);
        double newSize = buffer.readDouble();
        return new SizeChangedPacket(pokemon, newSize);
    }
}
