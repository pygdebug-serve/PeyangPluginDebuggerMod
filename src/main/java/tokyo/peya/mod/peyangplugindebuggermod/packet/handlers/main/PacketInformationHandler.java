package tokyo.peya.mod.peyangplugindebuggermod.packet.handlers.main;

import tokyo.peya.lib.pygdebug.common.PacketBase;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketInformationRequest;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketPlatformInformation;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketServerStatus;
import tokyo.peya.mod.peyangplugindebuggermod.PacketHandler;
import tokyo.peya.mod.peyangplugindebuggermod.PacketIO;
import tokyo.peya.mod.peyangplugindebuggermod.Utils;

public class PacketInformationHandler implements PacketHandler
{
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
            System.out.println("PacketPlatformInformation received");
            System.out.println("OS:");
            System.out.println("  name: " + platformInformation.getOs().getName());
            System.out.println("  arch: " + platformInformation.getOs().getArch());
            System.out.println("  version: " + platformInformation.getOs().getVersion());
            System.out.println("Java:");
            System.out.println("  name: " + platformInformation.getJava().getName());
            System.out.println("  arch: " + platformInformation.getJava().getArch());
            System.out.println("  version: " + platformInformation.getJava().getVersion());
            System.out.println("CPU:");
            System.out.println("  name: " + platformInformation.getCpu().getName());
            System.out.println("  cores: " + platformInformation.getCpu().getCores());
            System.out.println("  threads: " + platformInformation.getCpu().getThreads());
            System.out.println("Server:");
            System.out.println("  name: " + platformInformation.getServer().getName());
            System.out.println("  version: " + platformInformation.getServer().getVersion());
            System.out.println("  minecraftVersion: " + platformInformation.getServer().getMinecraftVersion());
            System.out.println("  onlineMode: " + platformInformation.getServer().isOnlineMode());

        }
        else if (id == Utils.getPacketId(PacketServerStatus.class))
        {
            PacketServerStatus serverStatus = (PacketServerStatus) packet;
            System.out.println("PacketServerStatus received");
            System.out.println("Plugins:");
            for (PacketServerStatus.PluginInformation plugin : serverStatus.getPlugins())
            {
                System.out.println("  name: " + plugin.getName());
                System.out.println("    version: " + plugin.getVersion());
                System.out.println("    enabled: " + plugin.isEnabled());
            }

            System.out.println("Load:");
            System.out.println("  ram: " + serverStatus.getLoad().getRam());
            System.out.println("  ram max: " + serverStatus.getLoad().getRamMax());
            System.out.println("  cpu: " + serverStatus.getLoad().getCpu());
            System.out.println("  tps1m: " + serverStatus.getLoad().getTps1());
            System.out.println("  tps5m: " + serverStatus.getLoad().getTps5());
            System.out.println("  tps15m: " + serverStatus.getLoad().getTps15());
        }
    }
}
