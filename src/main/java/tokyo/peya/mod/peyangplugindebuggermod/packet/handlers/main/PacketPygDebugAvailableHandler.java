package tokyo.peya.mod.peyangplugindebuggermod.packet.handlers.main;

import tokyo.peya.lib.pygdebug.common.PacketBase;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketInformationRequest;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketPygDebugAvailable;
import tokyo.peya.mod.peyangplugindebuggermod.PacketHandler;
import tokyo.peya.mod.peyangplugindebuggermod.PacketIO;
import tokyo.peya.mod.peyangplugindebuggermod.PeyangPluginDebuggerMod;
import tokyo.peya.mod.peyangplugindebuggermod.Utils;

public class PacketPygDebugAvailableHandler implements PacketHandler
{
    @Override
    public void registerPackets(PacketIO channel)
    {
        channel.registerPacket(PacketPygDebugAvailable.class);
    }

    @Override
    public void handlePacket(byte id, PacketBase packet)
    {
        if (id != Utils.getPacketId(PacketPygDebugAvailable.class))
            return;

        PeyangPluginDebuggerMod.INSTANCE.mainChannel.sendPacket(
                new PacketPygDebugAvailable()
        );
        PeyangPluginDebuggerMod.INSTANCE.mainChannel.sendPacket(
                new PacketInformationRequest(PacketInformationRequest.Action.PLATFORM));
        PeyangPluginDebuggerMod.INSTANCE.mainChannel.sendPacket(
                new PacketInformationRequest(PacketInformationRequest.Action.SERVER_STATUS));
    }
}
