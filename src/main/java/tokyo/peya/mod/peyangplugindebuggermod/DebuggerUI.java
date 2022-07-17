package tokyo.peya.mod.peyangplugindebuggermod;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.command.TextComponentHelper;
import tokyo.peya.mod.peyangplugindebuggermod.debugger.DebugClient;
import tokyo.peya.mod.peyangplugindebuggermod.ui.Palette;

public class DebuggerUI extends Screen
{
    private final DebugClient debugClient;

    public DebuggerUI(DebugClient debugClient)
    {
        super(new StringTextComponent("Debugger"));
        this.debugClient = debugClient;
    }

    public void render(MatrixStack matrixStack)
    {
        if (!this.debugClient.isEnabled())
            return;

        matrixStack.push();

        this.drawPlate(matrixStack);

        matrixStack.pop();
    }

    private void drawPlate(MatrixStack stack)
    {
        Palette.drawRect(stack, 10, 10, 100,  200);
        Palette.drawTextCenter(stack, TextFormatting.RED + "Debugger", 10, 12, 100);
    }
}
