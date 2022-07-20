package tokyo.peya.mod.peyangplugindebuggermod.ui;

public interface IOverlay
{
    int getTop();
    int getLeft();

    int getRight();
    int getBottom();

    default int getWidth()
    {
        return getRight() - getLeft();
    }

    default int getHeight()
    {
        return getBottom() - getTop();
    }
}
