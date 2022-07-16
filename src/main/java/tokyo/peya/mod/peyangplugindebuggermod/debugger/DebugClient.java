package tokyo.peya.mod.peyangplugindebuggermod.debugger;

import tokyo.peya.lib.pygdebug.common.debugger.DebuggerOption;
import tokyo.peya.lib.pygdebug.common.packets.debug.PacketDebugOptionRequest;
import tokyo.peya.mod.peyangplugindebuggermod.PacketIO;

public class DebugClient
{
    private final PacketIO debugChannel;
    private DebuggerOption debuggerOption;

    public DebugClient(PacketIO debugChannel)
    {
        this.debugChannel = debugChannel;
        this.debuggerOption = new DebuggerOption();
    }

    private void requestFetchDebuggerOption()
    {
        System.out.println("Fetching debugger option...");
        this.debugChannel.sendPacket(new PacketDebugOptionRequest());
    }

    public DebuggerOption getDebuggerOption()
    {
        return this.debuggerOption;
    }

    public void setDebuggerOption(DebuggerOption debuggerOption)
    {
        this.debuggerOption = debuggerOption;

        System.out.println("Debugger option is set.");
    }

    public void serverReady()
    {
        this.requestFetchDebuggerOption();
    }
}
