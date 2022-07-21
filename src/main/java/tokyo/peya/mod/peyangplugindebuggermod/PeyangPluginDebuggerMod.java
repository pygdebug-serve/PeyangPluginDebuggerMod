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
import tokyo.peya.mod.peyangplugindebuggermod.ui.GUIBox;
import tokyo.peya.mod.peyangplugindebuggermod.ui.GUIManager;
import tokyo.peya.mod.peyangplugindebuggermod.ui.HorizontalAlign;
import tokyo.peya.mod.peyangplugindebuggermod.ui.Text;
import tokyo.peya.mod.peyangplugindebuggermod.ui.VerticalAlign;

@Mod("peyangplugindebuggermod")
public class PeyangPluginDebuggerMod
{
    public static PeyangPluginDebuggerMod INSTANCE;

    public static final String NAMESPACE_ROOT = "pygdebug";

    public final PacketIO mainChannel;

    public final PacketIO debugChannel;
    public final DebugClient debugger;

    private final GUIManager guiManager;

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

        this.guiManager = new GUIManager(this.debugger);

        this.initGUI();
    }

    public void pollServerInformation(PacketInformationRequest.Action action)
    {
        this.mainChannel.sendPacket(new PacketInformationRequest(action));
    }

    private void initGUI()
    {

        GUIBox box = new GUIBox(this.guiManager)
                .x(20)
                .y(20)
                .width(40)
                .height(40)
                .align(HorizontalAlign.RIGHT)
                .align(VerticalAlign.BOTTOM)
                .text(Text.builder()
                        .text("Test")
                        .align(HorizontalAlign.RIGHT)
                        .align(VerticalAlign.CENTER)
                        .build());

        this.guiManager.bind(box);
    }
}
