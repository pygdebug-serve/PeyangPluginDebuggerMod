package tokyo.peya.mod.peyangplugindebuggermod.debugger;

import lombok.Getter;
import lombok.Setter;
import tokyo.peya.lib.pygdebug.common.debugger.DebuggerOption;
import tokyo.peya.lib.pygdebug.common.packets.debug.PacketDebugOptionRequest;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketInformationRequest;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketPlatformInformation;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketServerStatus;
import tokyo.peya.mod.peyangplugindebuggermod.PacketIO;

@Getter
public class DebugClient
{
    private final PacketIO debugChannel;

    private boolean enabled;

    @Setter
    private DebuggerOption debuggerOption;

    @Setter
    private PacketPlatformInformation platformInformation;
    @Setter
    private PacketServerStatus serverStatus;

    public DebugClient(PacketIO debugChannel)
    {
        this.debugChannel = debugChannel;
        this.debuggerOption = new DebuggerOption();

        this.platformInformation = null;
        this.serverStatus = null;

        this.enabled = false;
    }

    private void requestFetchDebuggerOption()
    {
        System.out.println("Fetching debugger option...");
        this.debugChannel.sendPacket(new PacketDebugOptionRequest());
    }

    public void serverReady()
    {
        this.requestFetchDebuggerOption();

        this.enabled = true;
    }

    public void dispose()
    {
        System.out.println("disposing DebugClient...");
        this.debuggerOption = new DebuggerOption();
        this.platformInformation = null;
        this.serverStatus = null;

        this.enabled = false;
    }
}
