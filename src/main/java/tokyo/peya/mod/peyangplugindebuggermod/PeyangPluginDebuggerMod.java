package tokyo.peya.mod.peyangplugindebuggermod;

import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketInformationRequest;
import tokyo.peya.mod.peyangplugindebuggermod.debugger.DebugClient;
import tokyo.peya.mod.peyangplugindebuggermod.events.ServerEventHandler;
import tokyo.peya.mod.peyangplugindebuggermod.events.TickEventHandler;
import tokyo.peya.mod.peyangplugindebuggermod.packet.handlers.debugger.DebuggerGeneralHandler;
import tokyo.peya.mod.peyangplugindebuggermod.packet.handlers.main.PacketInformationHandler;
import tokyo.peya.mod.peyangplugindebuggermod.packet.handlers.main.PacketPygDebugAvailableHandler;
import tokyo.peya.mod.peyangplugindebuggermod.ui.GUIBox;
import tokyo.peya.mod.peyangplugindebuggermod.ui.HorizontalAlign;
import tokyo.peya.mod.peyangplugindebuggermod.ui.KeyBindings;
import tokyo.peya.mod.peyangplugindebuggermod.ui.Palette;
import tokyo.peya.mod.peyangplugindebuggermod.ui.Text;
import tokyo.peya.mod.peyangplugindebuggermod.ui.VerticalAlign;
import tokyo.peya.mod.peyangplugindebuggermod.ui.decorators.AnimatedGUIBox;
import tokyo.peya.mod.peyangplugindebuggermod.ui.decorators.TitledGUIBox;
import tokyo.peya.mod.peyangplugindebuggermod.ui.decorators.animations.SlideAnimation;

@Getter
@Mod("peyangplugindebuggermod")
public class PeyangPluginDebuggerMod
{
    public static PeyangPluginDebuggerMod INSTANCE;

    public static final String NAMESPACE_ROOT = "pygdebug";

    public final PacketIO mainChannel;

    public final PacketIO debugChannel;
    public final DebugClient debugger;

    private final DebugGUIManager debugGuiManager;

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

        this.debugGuiManager = new DebugGUIManager(this.debugger);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModLoad);
    }

    public void pollServerInformation(PacketInformationRequest.Action action)
    {
        this.mainChannel.sendPacket(new PacketInformationRequest(action));
    }

    public void onModLoad(FMLClientSetupEvent event)
    {
        this.initGUI();

        KeyBindings.init();
    }

    private void initGUI()
    {

        GUIBox box = new TitledGUIBox()
                .title("Title")
                .x(20)
                .y(20)
                .width(40)
                .height(40)
                .align(HorizontalAlign.RIGHT)
                .align(VerticalAlign.BOTTOM)
                .color(Palette.rgba(0, 0, 0, 100))
                .text(Text.builder()
                        .text("Test")
                        .align(HorizontalAlign.RIGHT)
                        .align(VerticalAlign.CENTER)
                        .build())
                .child(new GUIBox()
                        .color(Palette.rgba(0, 0, 0, 60))
                        .align(HorizontalAlign.RIGHT)
                        .height(10)
                        .width(10)
                        .text("S"));

        box = new AnimatedGUIBox(box)
                .startAnimation(new SlideAnimation(box, 20)
                        .fromOutOfWindow(SlideAnimation.WindowOutOf.RIGHT));


        this.debugGuiManager.bind(box);
    }
}
