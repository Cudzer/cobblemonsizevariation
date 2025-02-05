package dev.cudzer.cobblemonsizevariation.mixin;

import com.cobblemon.mod.common.CobblemonNetwork;
import com.cobblemon.mod.common.client.net.pokemon.update.PokemonUpdatePacketHandler;
import com.cobblemon.mod.common.net.PacketRegisterInfo;
import dev.cudzer.cobblemonsizevariation.network.SizeChangedPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = CobblemonNetwork.class)
public class CobblemonNetworkMixin {

    @Inject(method = "generateS2CPacketInfoList", at= @At("RETURN"), cancellable = true, remap = false)
    private void generateS2CPacketInfoList(CallbackInfoReturnable<List<PacketRegisterInfo<?>>> cir){
        List<PacketRegisterInfo<?>> list = cir.getReturnValue();

        list.add(new PacketRegisterInfo<>(SizeChangedPacket.ID, SizeChangedPacket::decode, new PokemonUpdatePacketHandler<>(), null));
        cir.setReturnValue(list);
    }
}
