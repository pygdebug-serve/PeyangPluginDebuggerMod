package tokyo.peya.mod.peyangplugindebuggermod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("PeyangPluginDebuggerMod")
public class PeyangPluginDebuggerMod
{
    public static final String NAMESPACE_ROOT = "pydebug:";

    public PeyangPluginDebuggerMod()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
