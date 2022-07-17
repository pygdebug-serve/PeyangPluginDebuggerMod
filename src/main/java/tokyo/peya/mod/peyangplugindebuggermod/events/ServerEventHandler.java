package tokyo.peya.mod.peyangplugindebuggermod.events;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tokyo.peya.mod.peyangplugindebuggermod.PeyangPluginDebuggerMod;

public class ServerEventHandler
{
    @SubscribeEvent
    public void onTryLeave(ClientPlayerNetworkEvent.LoggedOutEvent event)
    {
        PeyangPluginDebuggerMod.INSTANCE.debugger.dispose();
    }
}
