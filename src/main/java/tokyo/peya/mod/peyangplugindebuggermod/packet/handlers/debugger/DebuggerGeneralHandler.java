package tokyo.peya.mod.peyangplugindebuggermod.packet.handlers.debugger;

import tokyo.peya.lib.pygdebug.common.PacketBase;
import tokyo.peya.lib.pygdebug.common.packets.debug.PacketDebugOption;
import tokyo.peya.lib.pygdebug.common.packets.debug.PacketDebugOptionRequest;
import tokyo.peya.mod.peyangplugindebuggermod.PacketHandler;
import tokyo.peya.mod.peyangplugindebuggermod.PacketIO;
import tokyo.peya.mod.peyangplugindebuggermod.Utils;
import tokyo.peya.mod.peyangplugindebuggermod.debugger.DebugClient;

public class DebuggerGeneralHandler implements PacketHandler
{
    private final DebugClient client;

    public DebuggerGeneralHandler(DebugClient client)
    {
        this.client = client;
    }

    @Override
    public void registerPackets(PacketIO channel)
    {
        channel.registerPacket(PacketDebugOption.class);
        channel.registerPacket(PacketDebugOptionRequest.class);
    }

    @Override
    public void handlePacket(byte id, PacketBase packet)
    {
        if (id == Utils.getPacketId(PacketDebugOption.class))
        {
            PacketDebugOption option = (PacketDebugOption) packet;
            this.client.setDebuggerOption(option.getDebuggerOption());
        }
    }
}
