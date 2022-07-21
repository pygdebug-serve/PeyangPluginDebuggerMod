package tokyo.peya.mod.peyangplugindebuggermod.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.java.games.input.Mouse;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Data
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class GUIBox implements IOverlay
{
    private final GUIManager guiManager;

    private final List<GUIBox> children;

    private final List<Consumer<MouseContext>> onClickListeners;

    private final List<Consumer<MouseContext>> onHoverListeners;

    private int y;
    private int x;
    private int width;
    private int height;

    @Getter(AccessLevel.NONE)
    private int absoluteTop;
    @Getter(AccessLevel.NONE)
    private int absoluteLeft;

    private int color;

    private Text text;

    private boolean visible;

    private boolean transparent;

    @Setter(AccessLevel.PRIVATE)
    private VerticalAlign verticalAlign;
    @Setter(AccessLevel.PRIVATE)
    private HorizontalAlign horizontalAlign;

    public GUIBox(GUIManager manager, GUIBox source)
    {
        this(manager, source.children, source.onClickListeners, source.onHoverListeners, source.y, source.x, source.width,
                source.height, source.absoluteTop, source.absoluteLeft, source.color, source.text, source.visible,
                source.transparent, source.verticalAlign, source.horizontalAlign
        );
    }

    public GUIBox(GUIManager manager)
    {
        this(manager, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0, 0, 0,
                0, 0, 0, -1946157056, null, true, false,
                VerticalAlign.TOP, HorizontalAlign.LEFT);
    }

    @Override
    public void onClicked(MouseContext context)
    {
        this.onClickListeners.forEach(listener -> listener.accept(context));

        this.children.forEach(child -> {
            int mouseX = context.getRelativeX() - child.getX();
            int mouseY = context.getRelativeY() - child.getY();

            MouseContext childContext = new MouseContext(this , mouseX, mouseY, context.getWheel(),
                    context.isLeftClicking(), context.isRightClicking(), context.isMiddleClicking());

            child.onClicked(childContext);
        });
    }

    private boolean isMouseOver(int x, int y)
    {
        return x >= this.x && x <= this.width && y >= this.y && y <= this.height;
    }

    @Override
    public int getX()
    {
        return this.x;
    }

    @Override
    public int getY()
    {
        return this.y;
    }

    @Override
    public int getWidth()
    {
        return this.width;
    }

    @Override
    public int getHeight()
    {
        return this.height;
    }

    @Override
    public void render(MatrixStack matrixStack, int parentX, int parentWidth, int parentY, int parentHeight)
    {
        if (!this.visible)
            return;

        HorizontalAlign horizontalAlign = this.horizontalAlign;
        VerticalAlign verticalAlign = this.verticalAlign;

        if (horizontalAlign == HorizontalAlign.LEFT)
            this.absoluteLeft = parentX + this.x;
        else if (horizontalAlign == HorizontalAlign.CENTER)
            this.absoluteLeft = parentX + this.x + this.width / 2;
        else if (horizontalAlign == HorizontalAlign.RIGHT)
            this.absoluteLeft = parentX + this.x + this.width;
        else
            this.absoluteLeft = parentX + this.x;

        if (verticalAlign == VerticalAlign.TOP)
            this.absoluteTop = parentY + this.y;
        else if (verticalAlign == VerticalAlign.CENTER)
            this.absoluteTop = parentY + this.y + this.height / 2;
        else if (verticalAlign == VerticalAlign.BOTTOM)
            this.absoluteTop = parentY + this.y + this.height;
        else
            this.absoluteTop = parentY + this.y;

        if (!this.transparent)
            Palette.drawRect(matrixStack, this.absoluteLeft, this.absoluteTop, this.width,
                    this.height, this.color);

        if (this.text != null)
        {
            Text text = this.text;

            int textX = text.getAbsoluteX(this.absoluteLeft, this.width);
            int textY = text.getAbsoluteY(this.absoluteTop, this.height);


            if (text.isShadow())
                Palette.drawTextShadow(matrixStack, text.getText(), textX, textY, text.getColor());
            else
                Palette.drawText(matrixStack, text.getText(), textX, textY, text.getColor());
        }


        this.children.forEach(child -> child.render(matrixStack, this.absoluteLeft, this.width,
                this.absoluteTop, this.height
        ));
    }

    public GUIBox child(GUIBox child)
    {
        this.children.add(child);
        return this;
    }

    public GUIBox removeChild(GUIBox child)
    {
        this.children.remove(child);
        return this;
    }

    public GUIBox onClick(Consumer<MouseContext> listener)
    {
        this.onClickListeners.add(listener);
        return this;
    }

    public GUIBox onHover(Consumer<MouseContext> listener)
    {
        this.onHoverListeners.add(listener);
        return this;
    }

    public GUIBox align(VerticalAlign alignVertical)
    {
        this.verticalAlign = alignVertical;
        return this;
    }

    public GUIBox text(String text)
    {
        this.text = new Text(text);
        return this;
    }

    public GUIBox text(Text text)
    {
        this.text = text;
        return this;
    }

    public GUIBox align(HorizontalAlign alignHorizontal)
    {
        this.horizontalAlign = alignHorizontal;
        return this;
    }

}
