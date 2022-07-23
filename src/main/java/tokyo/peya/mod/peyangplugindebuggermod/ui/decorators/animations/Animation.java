package tokyo.peya.mod.peyangplugindebuggermod.ui.decorators.animations;

import com.mojang.blaze3d.matrix.MatrixStack;
import tokyo.peya.mod.peyangplugindebuggermod.ui.GUIBox;

public interface Animation
{
    Animation from(int x, int y);
    Animation to(int x, int y);

    int getFromX();
    int getFromY();

    int getToX();
    int getToY();

    <T extends GUIBox> void onTick(MatrixStack matrixStack, int parentX, int parentWidth, int parentY, int parentHeight, T box, long tick);

    boolean isFinished(long tick);

    <T extends GUIBox> void onStarted(T box);
    <T extends GUIBox> void onEnded(T box);
}
