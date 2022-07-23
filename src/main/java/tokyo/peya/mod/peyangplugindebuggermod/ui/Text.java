package tokyo.peya.mod.peyangplugindebuggermod.ui;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
public class Text
{
    String text;

    int relativeX;
    int relativeY;

    double scale;

    int color;

    boolean shadow;

    HorizontalAlign horizontalAlign;
    VerticalAlign verticalAlign;

    int width;

    public Text(String text, int relativeX, int relativeY, double scale, int color, boolean shadow, HorizontalAlign horizontalAlign, VerticalAlign verticalAlign)
    {
        this.text = text;
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.scale = scale;
        this.color = color;
        this.shadow = shadow;
        this.horizontalAlign = horizontalAlign;
        this.verticalAlign = verticalAlign;

        this.width = Palette.calcStringWidth(text);
    }

    public Text (String text)
    {
        this(text, 0, 0, 1, 0xFFFFFF, false, HorizontalAlign.LEFT, VerticalAlign.TOP);
    }


    public int getAbsoluteX(int parentX, int parentWidth)
    {
        int textWidth = Palette.calcStringWidth(this.text);

        switch (this.horizontalAlign)
        {
            default:
            case LEFT:
                return parentX + this.relativeX;
            case CENTER:
                return parentX + (parentWidth - textWidth) / 2 + this.relativeX;
            case RIGHT:
                return parentX + parentWidth - textWidth + this.relativeX;
        }
    }

    public int getAbsoluteY(int parentY, int parentHeight)
    {
        int textHeight = Palette.FONT_HEIGHT;

        switch (this.verticalAlign)
        {
            default:
            case TOP:
                return parentY + this.relativeY;
            case CENTER:
                return parentY + (parentHeight - textHeight) / 2 + this.relativeY;
            case BOTTOM:
                return parentY + parentHeight - textHeight + this.relativeY;
        }
    }

    public static TextBuilder builder()
    {
        return new TextBuilder();
    }

    @Accessors(fluent = true, chain = true)
    @Data
    public static class TextBuilder
    {
        private String text;
        private int posX;
        private int posY;
        private int color;
        private boolean shadow;

        private double scale;

        @Setter(AccessLevel.NONE)
        private HorizontalAlign horizontalAlign;
        @Setter(AccessLevel.NONE)
        private VerticalAlign verticalAlign;

        public TextBuilder()
        {
            this.text = "";
            this.posX = 0;
            this.posY = 0;
            this.color = 0xFFFFFF;
            this.shadow = false;
            this.scale = 1;
            this.horizontalAlign = HorizontalAlign.LEFT;
            this.verticalAlign = VerticalAlign.TOP;
        }

        public TextBuilder align(HorizontalAlign horizontalAlign)
        {
            this.horizontalAlign = horizontalAlign;
            return this;
        }

        public TextBuilder align(VerticalAlign verticalAlign)
        {
            this.verticalAlign = verticalAlign;
            return this;
        }

        public Text build()
        {
            return new Text(this.text, this.posX, this.posY, this.scale, this.color, this.shadow, this.horizontalAlign,
                    this.verticalAlign);
        }
    }
}
