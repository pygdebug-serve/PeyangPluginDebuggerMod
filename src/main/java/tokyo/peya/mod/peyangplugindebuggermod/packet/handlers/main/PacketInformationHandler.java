package tokyo.peya.mod.peyangplugindebuggermod.packet.handlers.main;

import tokyo.peya.lib.pygdebug.common.PacketBase;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketInformationRequest;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketPlatformInformation;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketServerStatus;
import tokyo.peya.mod.peyangplugindebuggermod.PacketHandler;
import tokyo.peya.mod.peyangplugindebuggermod.PacketIO;
import tokyo.peya.mod.peyangplugindebuggermod.Utils;
import tokyo.peya.mod.peyangplugindebuggermod.debugger.DebugClient;

public class PacketInformationHandler implements PacketHandler
{
    private final DebugClient client;

    public PacketInformationHandler(DebugClient client)
    {
        this.client = client;
    }

    @Override
    public void registerPackets(PacketIO channel)
    {
        channel.registerPacket(PacketInformationRequest.class);
        channel.registerPacket(PacketPlatformInformation.class);
        channel.registerPacket(PacketServerStatus.class);
    }

    @Override
    public void handlePacket(byte id, PacketBase packet)
    {
        if (id == Utils.getPacketId(PacketPlatformInformation.class))
            this.client.setPlatformInformation((PacketPlatformInformation) packet);
        else if (id == Utils.getPacketId(PacketServerStatus.class))
            this.client.setServerStatus((PacketServerStatus) packet);
    }
}
