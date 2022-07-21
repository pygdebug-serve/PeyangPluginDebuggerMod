package tokyo.peya.mod.peyangplugindebuggermod.ui;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@AllArgsConstructor
public class Text
{
    String text;

    int relativeX;
    int relativeY;

    int color;

    boolean shadow;

    HorizontalAlign horizontalAlign;
    VerticalAlign verticalAlign;

    public Text (String text)
    {
        this(text, 0, 0, 0xFFFFFF, false, HorizontalAlign.LEFT, VerticalAlign.TOP);
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
            return new Text(this.text, this.posX, this.posY, this.color, this.shadow, this.horizontalAlign,
                    this.verticalAlign);
        }
    }
}
