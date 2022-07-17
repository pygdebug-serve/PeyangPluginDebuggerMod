package tokyo.peya.mod.peyangplugindebuggermod.events;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketInformationRequest;
import tokyo.peya.mod.peyangplugindebuggermod.PeyangPluginDebuggerMod;
import tokyo.peya.mod.peyangplugindebuggermod.debugger.DebugClient;

public class TickEventHandler
{
    private final DebugClient debugClient;

    private long last;

    public TickEventHandler(DebugClient debugClient)
    {
        this.debugClient = debugClient;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (!this.debugClient.isEnabled())
            return;

        long now = System.currentTimeMillis();
        if (now - this.last > 1000)
        {
            this.last = now;

            PeyangPluginDebuggerMod.INSTANCE.pollServerInformation(PacketInformationRequest.Action.SERVER_STATUS);
        }
    }
}
