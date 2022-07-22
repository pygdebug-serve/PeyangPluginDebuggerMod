package tokyo.peya.mod.peyangplugindebuggermod.ui;

import com.mojang.blaze3d.matrix.MatrixStack;

public interface IGUI
{
    int getY();
    int getX();

    int getWidth();
    int getHeight();

    void render(MatrixStack stack, int parentLeft, int parentRight, int parentTop, int parentBottom);

    void onClicked(MouseContext context);
}
