package tokyo.peya.mod.peyangplugindebuggermod.ui.decorators.animations;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Builder;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import tokyo.peya.mod.peyangplugindebuggermod.ui.GUIBox;

public class SlideAnimation extends AnimationBase
{
    private final int speed;  // pixel per 2 ticks

    @Builder
    public SlideAnimation(GUIBox box, int speed)
    {
        super(box);

        this.to(box.x(), box.y());

        this.speed = speed;
    }

    public AnimationBase fromOutOf(WindowOutOf fromWindowOutOf, int delta)
    {
        GUIBox box = this.getBox();

        int fromX;
        int fromY;

        switch (fromWindowOutOf)
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

    public AnimationBase fromOutOfWindow(WindowOutOf fromWindowOutOf)
    {
        MainWindow window = Minecraft.getInstance().getMainWindow();
        return this.fromOutOf(fromWindowOutOf, Math.max(window.getScaledHeight(), window.getScaledWidth()));
    }


    public AnimationBase toOutOf(WindowOutOf toWindowOutOf, int delta)
    {
        GUIBox box = this.getBox();

        int toX;
        int toY;

        switch (toWindowOutOf)
        {
            default:
            case RIGHT:
                toX = box.getWidth() + delta;
                toY = box.getY();
                break;
            case LEFT:
                toX = -delta;
                toY = box.getY();
                break;
            case BOTTOM:
                toX = box.getX();
                toY = box.getHeight() + delta;
                break;
            case TOP:
                toX = box.getX();
                toY = -delta;
                break;
        }

        super.to(toX, toY);

        return this;
    }

    public AnimationBase toOutOfWindow(WindowOutOf toWindowOutOf)
    {
        MainWindow window = Minecraft.getInstance().getMainWindow();
        return this.toOutOf(toWindowOutOf, Math.max(window.getScaledHeight(), window.getScaledWidth()));
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

    public enum WindowOutOf
    {
        RIGHT,
        LEFT,
        BOTTOM,
        TOP
    }
}
