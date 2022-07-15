package tokyo.peya.mod.peyangplugindebuggermod.packet.handlers.main;

import tokyo.peya.lib.pygdebug.common.PacketBase;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketInformationRequest;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketPlatformInformation;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketServerStatus;
import tokyo.peya.mod.peyangplugindebuggermod.PacketHandler;
import tokyo.peya.mod.peyangplugindebuggermod.PacketIO;

public class PacketInformationHandler implements PacketHandler
{
    private static final PacketPlatformInformation EMPTY_PLATFORM_INFORMATION;
    private static final PacketServerStatus EMPTY_SERVER_STATUS;

    static {
        EMPTY_PLATFORM_INFORMATION = PacketPlatformInformation.builder().build();
        EMPTY_SERVER_STATUS = PacketServerStatus.builder().build();
    }

    @Override
    public void registerPackets(PacketIO channel)
    {
        channel.registerPacket(new PacketInformationRequest(PacketInformationRequest.Action.PLATFORM));
        channel.registerPacket(EMPTY_PLATFORM_INFORMATION);
        channel.registerPacket(EMPTY_SERVER_STATUS);
    }

    @Override
    public void handlePacket(byte id, PacketBase packet)
    {
        if (id == EMPTY_PLATFORM_INFORMATION.getId())
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
        else if (id == EMPTY_SERVER_STATUS.getId())
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
