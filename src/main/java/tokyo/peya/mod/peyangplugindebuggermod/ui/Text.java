package tokyo.peya.mod.peyangplugindebuggermod.ui;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@RequiredArgsConstructor
@Value
@Builder
public class Text
{
    String text;

    int relativeX;
    int relativeY;

    int color;

    boolean shadow;

    @Singular("align")
    List<Align> align;

    public int getAbsoluteX(int parentX, int parentWidth)
    {
        int textSize = Palette.calcStringWidth(this.text);

        if (this.align.contains(Align.CENTER))
            return parentX + (parentWidth - textSize) / 2;
        else if (this.align.contains(Align.RIGHT))
            return parentX + parentWidth - textSize;
        else
            return parentX;
    }

    public int getAbsoluteY(int parentY, int parentHeight)
    {
        int textSize = Palette.FONT_HEIGHT;

        if (this.align.contains(Align.MIDDLE))
            return (parentY + Palette.FONT_HEIGHT) / 2;
        else if (this.align.contains(Align.BOTTOM))
            return parentY + parentHeight - textSize;
        else
            return parentY;
    }

    public enum Align
    {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        CENTER,
        MIDDLE
    }
}
