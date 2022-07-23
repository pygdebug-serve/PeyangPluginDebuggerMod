package tokyo.peya.mod.peyangplugindebuggermod.ui.decorators.animations;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Getter;
import tokyo.peya.mod.peyangplugindebuggermod.ui.GUIBox;

@Getter
public abstract class AnimationBase
{
    private int fromX;
    private int fromY;

    private int toX;
    private int toY;

    private GUIBox box;

    public void setBox(GUIBox box)
    {
        if (this.box != null)
            throw new IllegalStateException("box is already set");

        this.box = box;
    }

    public AnimationBase from(int fromX, int fromY)
    {
        this.fromX = fromX;
        this.fromY = fromY;

        return this;
    }

    public AnimationBase to(int toX, int toY)
    {
        this.toX = toX;
        this.toY = toY;

        return this;
    }

    public abstract void onTick(MatrixStack matrixStack, int parentX, int parentWidth, int parentY, int parentHeight, long tick);

    public abstract boolean isFinished(long tick);

    public abstract void onStarted();
    public abstract void onEnded();
}
