package dev.cudzer.cobblemonsizevariation.network;

import com.cobblemon.mod.common.net.PacketRegisterInfo;
import dev.cudzer.cobblemonsizevariation.network.handler.client.SizeChangeHandler;

import java.util.ArrayList;
import java.util.List;

public class ModNetwork {
    public static List<PacketRegisterInfo<?>> s2cPayloads = generateS2CPacketInfoList();

    public  static List<PacketRegisterInfo<?>> generateS2CPacketInfoList(){
        List<PacketRegisterInfo<?>> list = new ArrayList<>();

        list.add(new PacketRegisterInfo<>(SizeChangedPacket.ID, SizeChangedPacket::decode, new SizeChangeHandler(), null));

        return list;
    }
}
