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
        {
            PacketPlatformInformation platformInformation = (PacketPlatformInformation) packet;
            System.out.println("PlatformInformation received.");

            this.client.setPlatformInformation(platformInformation);
        }
        else if (id == Utils.getPacketId(PacketServerStatus.class))
        {
            PacketServerStatus serverStatus = (PacketServerStatus) packet;
            System.out.println("PacketServerStatus received.");

            this.client.setServerStatus(serverStatus);
        }
    }
}
