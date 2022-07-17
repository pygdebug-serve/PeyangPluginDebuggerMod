package tokyo.peya.mod.peyangplugindebuggermod.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;

public class Palette
{
    private static final FontRenderer RENDERER;

    private static final int RECT_COLOR;

    static {
        RENDERER = Minecraft.getInstance().fontRenderer;

        RECT_COLOR = rgba(0, 0, 0, 160);
    }

    public static int calcStringWidth(String text)
    {
        return RENDERER.getStringWidth(text);
    }

    public static int rgba(int r, int g, int b, int a)
    {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int rgb(int r, int g, int b)
    {
        return rgba(r, g, b, 255);
    }

    public static void drawRect(MatrixStack stack, int x, int y, int width, int height, int color)
    {
        Screen.fill(stack, x, y, x + width, y + height, color);
    }

    public static void drawRect(MatrixStack stack, int x, int y, int width, int height)
    {
        drawRect(stack, x, y, width, height, RECT_COLOR);
    }

    public static void drawText(MatrixStack stack, String text, int x, int y)
    {
        RENDERER.drawStringWithShadow(stack, text, x, y, 0xFFFFFFFF);
    }

    public static void drawTextCenter(MatrixStack stack, String text, int x, int y, int rectWidth)
    {
        int textWidth = calcStringWidth(text);
        int textX = x + (rectWidth - textWidth) / 2;
        drawText(stack, text, textX, y);
    }

    public static void drawTextCenter(MatrixStack stack, String etxt, int x, int y, int rectWidth, int rectHeight)
    {
        int textWidth = calcStringWidth(etxt);
        int textX = x + (rectWidth - textWidth) / 2;
        int textY = y + (rectHeight - RENDERER.FONT_HEIGHT) / 2;
        drawText(stack, etxt, textX, textY);
    }

}
