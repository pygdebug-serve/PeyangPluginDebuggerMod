package tokyo.peya.mod.peyangplugindebuggermod.ui.decorators;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import tokyo.peya.mod.peyangplugindebuggermod.ui.DecoratedGUIBox;
import tokyo.peya.mod.peyangplugindebuggermod.ui.decorators.animations.Animation;


@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class AnimatedGUIBox extends DecoratedGUIBox
{
    private boolean autoStart;

    @Setter(AccessLevel.NONE)
    private Animation startAnimation;  // Implicitly final
    @Setter(AccessLevel.NONE)
    private Animation idleAnimation;  // Implicitly final
    @Setter(AccessLevel.NONE)
    private Animation endAnimation;  // Implicitly final

    private Animation onMouseDownAnimation;
    private Animation onMouseUpAnimation;

    private Animation onMouseOverAnimation;
    private Animation onMouseOutAnimation;

    @Setter(AccessLevel.NONE)
    private boolean started;
    @Setter(AccessLevel.NONE)
    long ticksElapsed;
    @Setter(AccessLevel.NONE)
    private Animation currentAnimation;

    // For on same mouse action
    private long ticksElapsedBackup;
    private Animation currentAnimationBackup;

    public AnimatedGUIBox startAnimation(Animation startAnimation, boolean autoCalibration)
    {
        if (this.startAnimation != null)
            throw new IllegalStateException("startAnimation is already set");

        this.startAnimation = startAnimation;

        if (autoCalibration && this.idleAnimation != null)
            this.startAnimation.to(this.idleAnimation.getFromX(), this.idleAnimation.getFromY());

        return this;
    }
    
    public AnimatedGUIBox idleAnimation(Animation idleAnimation, boolean autoCalibration)
    {
        if (this.idleAnimation != null)
            throw new IllegalStateException("idleAnimation is already set");

        this.idleAnimation = idleAnimation;

        if (autoCalibration)
        {
            if (this.startAnimation != null)
                this.idleAnimation.from(this.startAnimation.getToX(), this.startAnimation.getToY());
            if (this.endAnimation != null)
                this.idleAnimation.to(this.endAnimation.getFromX(), this.endAnimation.getFromY());
        }

        return this;
    }
    
    public AnimatedGUIBox endAnimation(Animation endAnimation, boolean autoCalibration)
    {
        if (this.endAnimation != null)
            throw new IllegalStateException("endAnimation is already set");

        this.endAnimation = endAnimation;

        if (autoCalibration && this.idleAnimation != null)
            this.endAnimation.from(this.idleAnimation.getToX(), this.idleAnimation.getToY());

        return this;
    }

    public AnimatedGUIBox startAnimation(Animation startAnimation)
    {
        return startAnimation(startAnimation, true);
    }

    public AnimatedGUIBox idleAnimation(Animation idleAnimation)
    {
        return idleAnimation(idleAnimation, true);
    }

    public AnimatedGUIBox endAnimation(Animation endAnimation)
    {
        return endAnimation(endAnimation, true);
    }

    public void start()
    {
        if (this.started)
            this.stop();

        this.started = true;

        setCurrentAnimation(this.startAnimation);
    }

    public void stop()
    {
        if (!this.started)
            return;

        setCurrentAnimation(this.endAnimation);
    }

    public void forceStop(boolean runEnded)
    {
        this.started = false;

        if (runEnded && this.currentAnimation != null)
            this.currentAnimation.onEnded(this);

        this.currentAnimation = null;
    }

    private void setCurrentAnimation(Animation animation)
    {
        if (this.currentAnimation != null)
            this.currentAnimation.onEnded(this);

        this.ticksElapsed = 0;

        this.currentAnimation = animation;

        this.currentAnimation.onStarted(this);
    }

    @Override
    public void render(MatrixStack matrixStack, int parentX, int parentWidth, int parentY, int parentHeight)
    {
        this.passMouseCondition();

        if (this.started)
        {
            this.ticksElapsed++;
            this.currentAnimation.onTick(matrixStack, parentX, parentWidth, parentY, parentHeight, this,
                    this.ticksElapsed);
        }

        super.render(matrixStack, parentX, parentWidth, parentY, parentHeight);

        if (this.started && this.currentAnimation.isFinished(this.ticksElapsed))
        {
            if (this.currentAnimation == this.startAnimation)
                setCurrentAnimation(this.idleAnimation);
            else if (this.currentAnimation == this.idleAnimation)
                setCurrentAnimation(this.endAnimation);
            else if (this.currentAnimation == this.endAnimation)
                this.started = false;
        }
    }

    private void makeConditionsBackup()
    {
        this.ticksElapsedBackup = this.ticksElapsed;
        this.currentAnimationBackup = this.currentAnimation;
    }

    private void restoreConditionsFromBackup()
    {
        this.ticksElapsed = this.ticksElapsedBackup;
        this.currentAnimation = this.currentAnimationBackup;
    }

    private void passMouseCondition()
    {
        this.passMouseCondition$1(this.mouseDown(), this.onMouseDownAnimation);
        this.passMouseCondition$1(this.mouseOver(), this.onMouseUpAnimation);
    }

    private void passMouseCondition$1(boolean flag, Animation targetAnimation)
    {
        if (flag)
        {
            if (!(this.currentAnimation == targetAnimation || targetAnimation == null))
            {
                this.currentAnimationBackup();
                this.setCurrentAnimation(targetAnimation);
            }
        }
        else if (this.currentAnimation == targetAnimation)
            this.restoreConditionsFromBackup();
    }
}
