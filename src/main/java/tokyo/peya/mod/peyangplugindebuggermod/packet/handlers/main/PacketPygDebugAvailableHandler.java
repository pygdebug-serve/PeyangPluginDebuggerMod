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


        System.out.println("Server has PygDebugger(ping received).");
        System.out.println("Sending pong packet.");
        PeyangPluginDebuggerMod.INSTANCE.mainChannel.sendPacket(
                new PacketPygDebugAvailable()
        );

        System.out.println("Attempting to get platform information.");
        PeyangPluginDebuggerMod.INSTANCE.mainChannel.sendPacket(
                new PacketInformationRequest(PacketInformationRequest.Action.PLATFORM));
        System.out.println("Attempting to get server status.");
        PeyangPluginDebuggerMod.INSTANCE.mainChannel.sendPacket(
                new PacketInformationRequest(PacketInformationRequest.Action.SERVER_STATUS));

        PeyangPluginDebuggerMod.INSTANCE.debugger.serverReady();
    }
}
