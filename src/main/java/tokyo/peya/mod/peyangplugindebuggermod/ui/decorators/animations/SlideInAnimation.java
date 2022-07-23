package tokyo.peya.mod.peyangplugindebuggermod.ui.decorators.animations;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Builder;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import tokyo.peya.mod.peyangplugindebuggermod.ui.GUIBox;

public class SlideInAnimation extends AnimationBase
{
    private final OutOf fromOutOf;
    private final int speed;  // pixel per 2 ticks

    @Builder
    public SlideInAnimation(GUIBox box, OutOf fromOutOf, int speed)
    {
        super(box);
        this.fromOutOf = fromOutOf;

        this.to(box.x(), box.y());

        this.speed = speed;
    }

    public AnimationBase fromOutOf(int delta)
    {
        GUIBox box = this.getBox();

        int fromX;
        int fromY;

        switch (this.fromOutOf)
        {
            default:
            case RIGHT:
                fromX = -delta;
                fromY = box.getY();
                break;
            case LEFT:
                fromX = box.getWidth() + delta;
                fromY = box.getY();
                break;
            case BOTTOM:
                fromX = box.getX();
                fromY = -delta;
                break;
            case TOP:
                fromX = box.getX();
                fromY = box.getHeight() + delta;
                break;
        }

        super.from(fromX, fromY);

        return this;
    }

    public AnimationBase fromOutOfWindow()
    {
        MainWindow window = Minecraft.getInstance().getMainWindow();
        return this.fromOutOf(Math.max(window.getScaledHeight(), window.getScaledWidth()));
    }

    @Override
    public void onTick(MatrixStack matrixStack, int parentX, int parentWidth, int parentY, int parentHeight, long tick)
    {
        GUIBox box = this.getBox();

        int fromX = this.getFromX();
        int fromY = this.getFromY();

        int toX = this.getToX();
        int toY = this.getToY();

        double progress = this.calculateProgress(fromX, fromY, toX, toY, tick);

        box.x(calcPosX(fromX, toX, progress));
        box.y(calcPosY(fromY, toY, progress));
    }

    protected double calculateProgress(int fromX, int fromY, int toX, int toY, long tick)
    {
        int deltaX = toX - fromX;
        int deltaY = toY - fromY;

        int distance = (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        int time = distance / this.speed;

        return (double) tick / (double) time;
    }

    protected int calcPosX(int fromX, int toX, double progress)
    {
        return (int) (fromX + (toX - fromX) * progress);
    }

    protected int calcPosY(int fromY, int toY, double progress)
    {
        return (int) (fromY + (toY - fromY) * progress);
    }

    @Override
    public boolean isFinished(long tick)
    {
        return this.getBox().x() == this.getToX() && this.getBox().y() == this.getToY();
    }

    @Override
    public void onStarted()
    {

    }

    @Override
    public void onEnded()
    {

    }

    public enum OutOf
    {
        RIGHT,
        LEFT,
        BOTTOM,
        TOP
    }
}
