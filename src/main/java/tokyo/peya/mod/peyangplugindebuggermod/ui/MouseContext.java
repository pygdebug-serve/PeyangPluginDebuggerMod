package tokyo.peya.mod.peyangplugindebuggermod.ui;

import lombok.Value;

import javax.annotation.Nullable;

@Value
public class MouseContext
{
    @Nullable
    IOverlay parent;
    int relativeX;
    int relativeY;

    double wheel;

    boolean isLeftClicking;
    boolean isRightClicking;
    boolean isMiddleClicking;

    public MouseContext changeParent(IOverlay parent)
    {
        return new MouseContext(parent, this.relativeX, this.relativeY, this.wheel, this.isLeftClicking, this.isRightClicking, this.isMiddleClicking);
    }
}
