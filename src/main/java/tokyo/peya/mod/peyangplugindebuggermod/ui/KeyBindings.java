package tokyo.peya.mod.peyangplugindebuggermod.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.commons.lang3.ArrayUtils;
import tokyo.peya.lib.pygdebug.common.GLFWKey;
import tokyo.peya.lib.pygdebug.common.config.ClientConfig;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

public class KeyBindings
{
    public static final KeyBinding showGUI = new KeyBinding("Show GUI", GLFWKey.LEFT_ALT.getKeyCode(),
            "PeyangPluginDebuggerMod");

    public static final HashMap<String, KeyBinding> availableKeyBindings = new HashMap<String, KeyBinding>()
    {
        {
            this.put("gui.show", KeyBindings.showGUI);
        }
    };

    private static final Field keybindField;
    
    static
    {
        try
        {
            keybindField = KeyBinding.class.getDeclaredField("keyCode");
            keybindField.setAccessible(true);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void init()
    {
        for (KeyBinding keyBinding : availableKeyBindings.values())
            ClientRegistry.registerKeyBinding(keyBinding);
    }

    public static void updateKeys(ClientConfig clientConfig)
    {
        Minecraft.getInstance().gameSettings.keyBindings = Arrays.stream(Minecraft.getInstance().gameSettings.keyBindings)
                    .filter(keyBinding -> !keyBinding.getKeyCategory().startsWith("PeyangPluginDebuggerMod"))
                .toArray(KeyBinding[]::new);
        
        clientConfig.getKeyBindings().entrySet().stream()
                .filter(entry -> KeyBindings.availableKeyBindings.containsKey(entry.getKey()))
                .forEach(entry -> {
                    KeyBinding keyBinding = KeyBindings.availableKeyBindings.get(entry.getKey());
                    
                    try
                    {
                        keybindField.set(keyBinding,
                                InputMappings.Type.KEYSYM.getOrMakeInput(entry.getValue().getKey()));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    ClientRegistry.registerKeyBinding(keyBinding);
                });
    }
}
