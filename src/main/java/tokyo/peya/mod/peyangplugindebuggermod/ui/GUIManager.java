package tokyo.peya.mod.peyangplugindebuggermod.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Data;
import lombok.Getter;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tokyo.peya.mod.peyangplugindebuggermod.debugger.DebugClient;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GUIManager extends Screen
{
    private final List<IOverlay> bindings;

    private final MainWindow window;
    private final DebugClient debugClient;

    private int mouseX;
    private int mouseY;
    private double mouseWheel;
    private boolean isMouseLeftDown;
    private boolean isMouseRightDown;
    private boolean isMouseMiddleDown;

    public GUIManager(DebugClient debugClient)
    {
        super(new StringTextComponent("Debugger"));
        this.bindings = new ArrayList<>();
        this.window = Minecraft.getInstance().getMainWindow();
        this.debugClient = debugClient;

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void bind(IOverlay overlay)
    {
        this.bindings.add(overlay);
    }

    public void unbind(IOverlay overlay)
    {
        this.bindings.remove(overlay);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event)
    {
        if (!this.debugClient.isEnabled() || event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS)
            return;

        this.bindings.forEach(overlay -> overlay.render(event.getMatrixStack(), 0, this.window.getScaledWidth(),
                0, this.window.getScaledHeight()));
    }


    @SubscribeEvent
    public void onMouseScroll(GuiScreenEvent.MouseScrollEvent event)
    {
        if (!(this.debugClient.isEnabled()))
            return;

        this.mouseWheel = event.getScrollDelta();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if (!(this.debugClient.isEnabled()))
            return;

        this.mouseX = mouseX;
        this.mouseY = mouseY;

        this.bindings.forEach(overlay -> overlay.render(matrixStack, 0, this.window.getScaledWidth(),
                0, this.window.getScaledHeight()));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
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
        if (button == 0)
            this.isMouseLeftDown = false;
        else if (button == 1)
            this.isMouseRightDown = false;
        else if (button == 2)
            this.isMouseMiddleDown = false;

        return super.mouseReleased(mouseX, mouseY, button);
    }
}
