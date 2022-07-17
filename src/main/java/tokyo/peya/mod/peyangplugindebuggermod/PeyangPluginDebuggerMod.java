package tokyo.peya.mod.peyangplugindebuggermod;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketInformationRequest;
import tokyo.peya.mod.peyangplugindebuggermod.debugger.DebugClient;
import tokyo.peya.mod.peyangplugindebuggermod.events.ServerEventHandler;
import tokyo.peya.mod.peyangplugindebuggermod.events.TickEventHandler;
import tokyo.peya.mod.peyangplugindebuggermod.packet.handlers.debugger.DebuggerGeneralHandler;
import tokyo.peya.mod.peyangplugindebuggermod.packet.handlers.main.PacketInformationHandler;
import tokyo.peya.mod.peyangplugindebuggermod.packet.handlers.main.PacketPygDebugAvailableHandler;

@Mod("peyangplugindebuggermod")
public class PeyangPluginDebuggerMod
{
    public static PeyangPluginDebuggerMod INSTANCE;

    public static final String NAMESPACE_ROOT = "pygdebug";

    public final PacketIO mainChannel;

    public final PacketIO debugChannel;
    public final DebugClient debugger;

    private final DebuggerUI debuggerUI;

    public PeyangPluginDebuggerMod()
    {
        INSTANCE = this;

        this.mainChannel = new PacketIO(this, "main");

        this.debugChannel = new PacketIO(this, "debug");
        this.debugger = new DebugClient(this.debugChannel);

        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        MinecraftForge.EVENT_BUS.register(new TickEventHandler(this.debugger));
        MinecraftForge.EVENT_BUS.register(this);

        this.mainChannel.registerHandler(new PacketInformationHandler(this.debugger));
        this.mainChannel.registerHandler(new PacketPygDebugAvailableHandler());

        this.debugChannel.registerHandler(new DebuggerGeneralHandler(this.debugger));

        this.debuggerUI = new DebuggerUI(this.debugger);

    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS)
            return;

        this.debuggerUI.render(event.getMatrixStack());
    }

    public void pollServerInformation(PacketInformationRequest.Action action)
    {
        this.mainChannel.sendPacket(new PacketInformationRequest(action));
    }
}
