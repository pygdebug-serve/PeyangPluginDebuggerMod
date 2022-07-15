package tokyo.peya.mod.peyangplugindebuggermod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import tokyo.peya.mod.peyangplugindebuggermod.events.ServerEventHandler;
import tokyo.peya.mod.peyangplugindebuggermod.packet.handlers.main.PacketInformationHandler;

@Mod("peyangplugindebuggermod")
public class PeyangPluginDebuggerMod
{
    public static PeyangPluginDebuggerMod INSTANCE;

    public static final String NAMESPACE_ROOT = "pydebug";

    public final PacketIO mainChannel;

    public PeyangPluginDebuggerMod()
    {
        INSTANCE = this;

        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());

        this.mainChannel = new PacketIO(this, "main");

        this.mainChannel.registerHandler(new PacketInformationHandler());
    }
}
