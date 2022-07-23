package tokyo.peya.mod.peyangplugindebuggermod.ui;

import com.mojang.blaze3d.matrix.MatrixStack;

import java.util.List;
import java.util.function.Consumer;

public class DecoratedGUIBox extends GUIBox
{
    private final GUIBox source;

    public DecoratedGUIBox(GUIBox source)
    {
        this.source = source;
    }

    public DecoratedGUIBox()
    {
        this(new GUIBox());
    }

    public DecoratedGUIBox(List<? extends GUIBox> children, List<? extends Consumer<MouseContext>> onClickListeners,
                           List<? extends Consumer<MouseContext>> onHoverListeners, int x, int y, int width, int height,
                           int absoluteTop, int absoluteLeft, int color, Text text, boolean visible,
                           boolean transparent, VerticalAlign verticalAlign, HorizontalAlign horizontalAlign)
    {
        GUIBox src = new GUIBox();

        children.forEach(src::child);
        onClickListeners.forEach(src::onClick);
        onHoverListeners.forEach(src::onHover);

        src.x(x);
        src.y(y);

        src.width(width);
        src.height(height);

        src.absoluteTop(absoluteTop);
        src.absoluteLeft(absoluteLeft);

        src.color(color);

        src.text(text);

        src.visible(visible);
        src.transparent(transparent);
        src.align(verticalAlign);

        src.align(horizontalAlign);

        this.source = src;
    }

    @Override
    public List<GUIBox> children()
    {
        return this.source.children();
    }

    @Override
    public List<Consumer<MouseContext>> onClickListeners()
    {
        return this.source.onClickListeners();
    }

    @Override
    public List<Consumer<MouseContext>> onHoverListeners()
    {
        return this.source.onHoverListeners();
    }

    @Override
    public int y()
    {
        return this.source.y();
    }

    @Override
    public int x()
    {
        return this.source.x();
    }

    @Override
    public int width()
    {
        return this.source.width();
    }

    @Override
    public int height()
    {
        return this.source.height();
    }

    @Override
    public int color()
    {
        return this.source.color();
    }

    @Override
    public Text text()
    {
        return this.source.text();
    }

    @Override
    public boolean visible()
    {
        return this.source.visible();
    }

    @Override
    public boolean transparent()
    {
        return this.source.transparent();
    }

    @Override
    public VerticalAlign verticalAlign()
    {
        return this.source.verticalAlign();
    }

    @Override
    public HorizontalAlign horizontalAlign()
    {
        return this.source.horizontalAlign();
    }

    @Override
    public GUIBox y(int y)
    {
        this.source.y(y);
        return this;
    }

    @Override
    public GUIBox x(int x)
    {
        this.source.x(x);
        return this;
    }

    @Override
    public GUIBox width(int width)
    {
        this.source.width(width);
        return this;
    }

    @Override
    public GUIBox height(int height)
    {
        this.source.height(height);
        return this;
    }

    @Override
    public GUIBox absoluteTop(int absoluteTop)
    {
        this.source.absoluteTop(absoluteTop);
        return this;
    }

    @Override
    public GUIBox absoluteLeft(int absoluteLeft)
    {
        this.source.absoluteLeft(absoluteLeft);
        return this;
    }

    @Override
    public GUIBox color(int color)
    {
        this.source.color(color);
        return this;
    }

    @Override
    public GUIBox visible(boolean visible)
    {
        this.source.visible(visible);
        return this;
    }

    @Override
    public GUIBox transparent(boolean transparent)
    {
        this.source.transparent(transparent);
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        return this.source.equals(o);
    }

    @Override
    protected boolean canEqual(Object other)
    {
        return this.source.canEqual(other);
    }

    @Override
    public int hashCode()
    {
        return this.source.hashCode();
    }

    @Override
    public String toString()
    {
        return "DecoratedGUIBox{" +
                "source=" + this.source +
                '}';
    }

    @Override
    protected int absoluteTop()
    {
        return this.source.absoluteTop();
    }

    @Override
    protected int absoluteLeft()
    {
        return this.source.absoluteLeft();
    }

    @Override
    public void onClicked(MouseContext context)
    {
        this.source.onClicked(context);
    }

    @Override
    public int getX()
    {
        return this.source.getX();
    }

    @Override
    public int getY()
    {
        return this.source.getY();
    }

    @Override
    public int getWidth()
    {
        return this.source.getWidth();
    }

    @Override
    public int getHeight()
    {
        return this.source.getHeight();
    }

    @Override
    public void render(MatrixStack matrixStack, int parentX, int parentWidth, int parentY, int parentHeight)
    {
        this.source.render(matrixStack, parentX, parentWidth, parentY, parentHeight);
    }

    @Override
    public GUIBox child(GUIBox child)
    {
        this.source.child(child);
        return this;
    }

    @Override
    public GUIBox removeChild(GUIBox child)
    {
        this.source.removeChild(child);
        return this;
    }

    @Override
    public GUIBox onClick(Consumer<MouseContext> listener)
    {
        this.source.onClick(listener);
        return this;
    }

    @Override
    public GUIBox onHover(Consumer<MouseContext> listener)
    {
        this.source.onHover(listener);
        return this;
    }

    @Override
    public GUIBox align(VerticalAlign alignVertical)
    {
        this.source.align(alignVertical);
        return this;
    }

    @Override
    public GUIBox text(String text)
    {
        this.source.text(text);
        return this;
    }

    @Override
    public GUIBox text(Text text)
    {
        this.source.text(text);
        return this;
    }

    @Override
    public GUIBox align(HorizontalAlign alignHorizontal)
    {
        this.source.align(alignHorizontal);
        return this;
    }
}
