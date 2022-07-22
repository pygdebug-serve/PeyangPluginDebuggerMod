package tokyo.peya.mod.peyangplugindebuggermod;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tokyo.peya.mod.peyangplugindebuggermod.debugger.DebugClient;
import tokyo.peya.mod.peyangplugindebuggermod.ui.IGUI;
import tokyo.peya.mod.peyangplugindebuggermod.ui.KeyBindings;
import tokyo.peya.mod.peyangplugindebuggermod.ui.MouseContext;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DebugGUIManager extends Screen
{
    private final List<IGUI> bindings;

    private final MainWindow window;
    private final DebugClient debugClient;

    @Setter
    private boolean overlayEnabled;

    private int mouseX;
    private int mouseY;
    private double mouseWheel;
    private boolean isMouseLeftDown;
    private boolean isMouseRightDown;
    private boolean isMouseMiddleDown;

    public DebugGUIManager(DebugClient debugClient)
    {
        super(new StringTextComponent("Debugger"));
        this.bindings = new ArrayList<>();

        this.window = Minecraft.getInstance().getMainWindow();
        this.debugClient = debugClient;

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void bind(IGUI overlay)
    {
        this.bindings.add(overlay);
    }

    public void unbind(IGUI overlay)
    {
        this.bindings.remove(overlay);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event)
    {
        if (!this.overlayEnabled || event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS)
            return;

        this.bindings.forEach(overlay -> overlay.render(event.getMatrixStack(), 0, this.window.getScaledWidth(),
                0, this.window.getScaledHeight()));
    }


    @SubscribeEvent
    public void onMouseScroll(GuiScreenEvent.MouseScrollEvent event)
    {
        if (!this.overlayEnabled)
            return;

        this.mouseWheel = event.getScrollDelta();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if (!this.overlayEnabled)
            return;

        this.mouseX = mouseX;
        this.mouseY = mouseY;

        assert this.minecraft != null;
        if (this.minecraft.currentScreen == this)
            return;

        this.bindings.forEach(overlay -> overlay.render(matrixStack, 0, this.window.getScaledWidth(),
                0, this.window.getScaledHeight()));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (!this.overlayEnabled)
            return super.mouseClicked(mouseX, mouseY, button);

        if (button == 0)
            this.isMouseLeftDown = true;
        else if (button == 1)
            this.isMouseRightDown = true;
        else if (button == 2)
            this.isMouseMiddleDown = true;

        MouseContext context =  new MouseContext(null, this.mouseX, this.mouseY, this.mouseWheel, this.isMouseLeftDown,
                this.isMouseRightDown, this.isMouseMiddleDown);
        this.bindings.forEach(overlay -> overlay.onClicked(context));

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        if (!this.overlayEnabled)
            return super.mouseReleased(mouseX, mouseY, button);

        if (button == 0)
            this.isMouseLeftDown = false;
        else if (button == 1)
            this.isMouseRightDown = false;
        else if (button == 2)
            this.isMouseMiddleDown = false;

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event)
    {
        if (!(this.debugClient.isEnabled()))
            return;

        Minecraft minecraft = Minecraft.getInstance();

        if (event.getKey() == KeyBindings.showGUI.getKey().getKeyCode())
        {
            if (event.getAction() == 1)
                    minecraft.displayGuiScreen(this);
            else if (event.getAction() == 0)
                this.closeScreen();
        }

    }
}
