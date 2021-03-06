package tokyo.peya.mod.peyangplugindebuggermod.ui.decorators;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import tokyo.peya.mod.peyangplugindebuggermod.ui.DecoratedGUIBox;
import tokyo.peya.mod.peyangplugindebuggermod.ui.GUIBox;
import tokyo.peya.mod.peyangplugindebuggermod.ui.HorizontalAlign;
import tokyo.peya.mod.peyangplugindebuggermod.ui.MouseContext;
import tokyo.peya.mod.peyangplugindebuggermod.ui.Palette;
import tokyo.peya.mod.peyangplugindebuggermod.ui.Text;
import tokyo.peya.mod.peyangplugindebuggermod.ui.VerticalAlign;

import java.util.List;
import java.util.function.Consumer;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class TitledGUIBox extends DecoratedGUIBox
{
    private static final Text TITLE_DEFAULT_TEXT;

    private Text title = TITLE_DEFAULT_TEXT;
    private int titleBackgroundHeight = 13;
    private int titleBackgroundColor = 2013265920;

    private boolean titleBackgroundTransparent;

    static {
        TITLE_DEFAULT_TEXT = Text.builder()
                .text("Title")
                .align(HorizontalAlign.CENTER)
                .align(VerticalAlign.CENTER).build();
    }

    public TitledGUIBox()
    {
    }

    public TitledGUIBox(GUIBox source)
    {
        super(source);
    }

    public TitledGUIBox(List<? extends GUIBox> children, List<? extends Consumer<MouseContext>> onClickListeners, int x, int y, int width, int height, int absoluteTop, int absoluteLeft, int color, Text text, boolean visible, boolean transparent, VerticalAlign verticalAlign, HorizontalAlign horizontalAlign)
    {
        super(children, onClickListeners, x, y, width, height, absoluteTop, absoluteLeft, color, text, visible, transparent, verticalAlign, horizontalAlign);
    }

    public TitledGUIBox title(Text title)
    {
        this.title = title;
        return this;
    }

    public TitledGUIBox title(String title)
    {
        this.title = Text.builder()
                .text(title)
                .align(VerticalAlign.CENTER)
                .align(HorizontalAlign.CENTER)
                .build();
        return this;
    }

    @Override
    public void render(MatrixStack matrixStack, int parentX, int parentWidth, int parentY, int parentHeight)
    {
        super.render(matrixStack,
                parentX,
                parentWidth,
                parentY + this.titleBackgroundHeight,
                parentHeight
        );

        if (!this.titleBackgroundTransparent)
            Palette.drawRect(matrixStack,
                    this.absoluteLeft(),
                    this.absoluteTop() - this.titleBackgroundHeight,
                    this.width(),
                    this.titleBackgroundHeight);

        int textX = this.title.getAbsoluteX(this.absoluteLeft(), this.width());
        int textY = this.title.getAbsoluteY(this.absoluteTop() - this.titleBackgroundHeight, this.titleBackgroundHeight);

        if (this.title.isShadow())
            Palette.drawTextShadow(matrixStack, this.title.getText(), textX, textY, this.title.getColor());
        else
            Palette.drawText(matrixStack, this.title.getText(), textX, textY, this.title.getColor());
    }
}
