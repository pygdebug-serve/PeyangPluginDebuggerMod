package tokyo.peya.mod.peyangplugindebuggermod.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.Value;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Data
@Builder
public class GUIBox implements IOverlay
{
    @Singular
    private final List<GUIBox> children = new LinkedList<>();

    @Singular("onClick")
    private final List<Consumer<MouseContext>> onClickListeners = new ArrayList<>();

    @Singular("onHover")
    private final List<Consumer<MouseContext>> onHoverListeners = new ArrayList<>();

    private int top;
    private int left;
    private int right;
    private int bottom;

    private int absoluteTop;
    private int absoluteLeft;

    private int color;

    @Getter(AccessLevel.PROTECTED)
    private Supplier<Text> text;

    private boolean visible;

    private boolean transparent;

    public GUIBox()
    {
    }

    public GUIBox(int top, int left, int right, int bottom, int absoluteTop, int absoluteLeft, int color, Supplier<Text> text, boolean visible, boolean transparent)
    {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.absoluteTop = absoluteTop;
        this.absoluteLeft = absoluteLeft;
        this.color = color;
        this.text = text;
        this.visible = visible;
        this.transparent = transparent;
    }

    public GUIBox(GUIBox source)
    {
        this.top = source.top;
        this.left = source.left;
        this.right = source.right;
        this.bottom = source.bottom;
        this.absoluteTop = source.absoluteTop;
        this.absoluteLeft = source.absoluteLeft;
        this.color = source.color;
        this.text = source.text;
        this.visible = source.visible;
        this.transparent = source.transparent;
    }

    public void onClick(int parentX, int parentY, int button)
    {
        int x = parentX - this.left;
        int y = parentY - this.top;

        if (this.isMouseOver(parentX, parentY))
            this.onClickListeners.forEach(listener -> listener.accept(new MouseContext(this, x, y, button)));

        this.children.forEach(child -> {
            if (child.visible)
                child.onClick(parentX, parentY, button);
        });
    }

    public void onHover(int parentX, int parentY)
    {
        int x = parentX - this.left;
        int y = parentY - this.top;

        if (this.isMouseOver(parentX, parentY))
            this.onHoverListeners.forEach(listener -> listener.accept(new MouseContext(this, x, y, -1)));

        this.children.forEach(child -> {
            if (child.visible)
                child.onHover(parentX, parentY);
        });
    }

    private boolean isMouseOver(int x, int y)
    {
        return x >= this.left && x <= this.right && y >= this.top && y <= this.bottom;
    }

    public void render(MatrixStack matrixStack, int x, int y)
    {
        if (!this.visible)
            return;

        this.absoluteTop = y + this.top;
        this.absoluteLeft = x + this.left;

        if (!this.transparent)
            Palette.drawRect(matrixStack, this.absoluteLeft, this.absoluteTop, this.right - this.left, this.bottom - this.top, this.color);

        if (this.text != null)
        {
            Text text = this.text.get();

            int textX = text.getAbsoluteX(this.absoluteLeft, this.right - this.left);
            int textY = text.getAbsoluteY(this.absoluteTop, this.bottom - this.top);

            if (text.isShadow())
                Palette.drawTextShadow(matrixStack, text.getText(), textX, textY, text.getColor());
            else
                Palette.drawText(matrixStack, text.getText(), textX, textY, text.getColor());
        }

        this.children.forEach(child -> child.render(matrixStack, this.absoluteLeft, this.absoluteTop));
    }

    public void addChild(GUIBox child)
    {
        this.children.add(child);
    }

    @Value
    public static class MouseContext
    {
        GUIBox item;
        int x;
        int y;
        int button;

        public boolean isLeftClick()
        {
            return this.button == 0;
        }

        public boolean isRightClick()
        {
            return this.button == 1;
        }

        public boolean isMiddleClick()
        {
            return this.button == 2;
        }
    }
}
