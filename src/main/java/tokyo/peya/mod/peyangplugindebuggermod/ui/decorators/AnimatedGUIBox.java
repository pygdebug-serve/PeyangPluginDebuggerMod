package tokyo.peya.mod.peyangplugindebuggermod.ui.decorators;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import tokyo.peya.mod.peyangplugindebuggermod.ui.DecoratedGUIBox;
import tokyo.peya.mod.peyangplugindebuggermod.ui.GUIBox;
import tokyo.peya.mod.peyangplugindebuggermod.ui.decorators.animations.AnimationBase;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class AnimatedGUIBox extends DecoratedGUIBox
{
    private AnimationBase startAnimation;  // Implicitly final
    private AnimationBase idleAnimation;  // Implicitly final
    private AnimationBase endAnimation;  // Implicitly final

    private AnimationBase onMouseDownAnimation;  // Implicitly final
    private AnimationBase onMouseUpAnimation;  // Implicitly final

    private AnimationBase onMouseOverAnimation;  // Implicitly final
    private AnimationBase onMouseOutAnimation;  // Implicitly final

    @Setter(AccessLevel.NONE)
    private boolean isEverStarted;
    private boolean started;
    long ticksElapsed;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private AnimationBase currentAnimation;

    // For on same mouse action
    private long ticksElapsedBackup;
    private AnimationBase currentAnimationBackup;

    public AnimatedGUIBox(GUIBox source)
    {
        super(source);
    }

    public AnimatedGUIBox onStarted(AnimationBase startAnimation, boolean autoCalibration)
    {
        if (this.startAnimation != null)
            throw new IllegalStateException("startAnimation is already set");

        this.startAnimation = startAnimation;

        if (autoCalibration && this.idleAnimation != null)
            this.startAnimation.to(this.idleAnimation.getFromX(), this.idleAnimation.getFromY());

        return this;
    }
    
    public AnimatedGUIBox onIdling(AnimationBase idleAnimation, boolean autoCalibration)
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
    
    public AnimatedGUIBox onEnded(AnimationBase endAnimation, boolean autoCalibration)
    {
        if (this.endAnimation != null)
            throw new IllegalStateException("endAnimation is already set");

        this.endAnimation = endAnimation;

        if (autoCalibration && this.idleAnimation != null)
            this.endAnimation.from(this.idleAnimation.getToX(), this.idleAnimation.getToY());

        return this;
    }

    public AnimatedGUIBox onStarted(AnimationBase startAnimation)
    {
        return this.onStarted(startAnimation, true);
    }

    public AnimatedGUIBox onIdling(AnimationBase idleAnimation)
    {
        return this.onIdling(idleAnimation, true);
    }

    public AnimatedGUIBox onEnded(AnimationBase endAnimation)
    {
        return this.onEnded(endAnimation, true);
    }

    public AnimatedGUIBox onMouseDown(AnimationBase onMouseDownAnimation)
    {
        if (this.onMouseDownAnimation != null)
            throw new IllegalStateException("onMouseDownAnimation is already set");

        this.onMouseDownAnimation = onMouseDownAnimation;

        return this;
    }

    public AnimatedGUIBox onMouseUp(AnimationBase onMouseUpAnimation)
    {
        if (this.onMouseUpAnimation != null)
            throw new IllegalStateException("onMouseUpAnimation is already set");

        this.onMouseUpAnimation = onMouseUpAnimation;

        return this;
    }

    public AnimatedGUIBox onMouseOver(AnimationBase onMouseOverAnimation)
    {
        if (this.onMouseOverAnimation != null)
            throw new IllegalStateException("onMouseOverAnimation is already set");

        this.onMouseOverAnimation = onMouseOverAnimation;

        return this;
    }

    public AnimatedGUIBox onMouseOut(AnimationBase onMouseOutAnimation)
    {
        if (this.onMouseOutAnimation != null)
            throw new IllegalStateException("onMouseOutAnimation is already set");

        this.onMouseOutAnimation = onMouseOutAnimation;

        return this;
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
        this.isEverStarted = false;

        if (!this.started)
            return;

        setCurrentAnimation(this.endAnimation);

        this.children().forEach(child -> {
            if (child instanceof AnimatedGUIBox)
                ((AnimatedGUIBox) child).stop();
        });

    }

    public void forceStop(boolean runEnded)
    {
        this.started = false;

        if (runEnded && this.currentAnimation != null)
            this.currentAnimation.onEnded();

        this.currentAnimation = null;
    }

    private void setCurrentAnimation(AnimationBase animation)
    {
        if (this.currentAnimation != null)
            this.currentAnimation.onEnded();

        this.ticksElapsed = 0;

        this.currentAnimation = animation;

        this.currentAnimation.onStarted();
    }

    @Override
    public void render(MatrixStack matrixStack, int parentX, int parentWidth, int parentY, int parentHeight)
    {
        this.passMouseCondition();

        if (this.started)
        {
            this.ticksElapsed++;
            this.currentAnimation.onTick(matrixStack, parentX, parentWidth, parentY, parentHeight, this.ticksElapsed);
        }
        else if (!this.isEverStarted)
        {
            this.isEverStarted = true;
            this.start();
            return;
        }

        super.render(matrixStack, parentX, parentWidth, parentY, parentHeight);

        if (this.started && this.currentAnimation.isFinished(this.ticksElapsed))
        {
            if (this.currentAnimation == this.startAnimation)
            {
                if (this.idleAnimation != null)
                    setCurrentAnimation(this.idleAnimation);

                this.currentAnimation = null;
            }
            else if (this.currentAnimation == this.idleAnimation)
            {
                if (this.endAnimation != null)
                    setCurrentAnimation(this.endAnimation);

                this.currentAnimation = null;
            }

            if (this.currentAnimation == null ||this.currentAnimation == this.endAnimation)
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
        this.passMouseCondition$1(this.mouseDown(), this.onMouseDownAnimation, this.onMouseUpAnimation);
        this.passMouseCondition$1(this.mouseOver(), this.onMouseOverAnimation, this.onMouseOutAnimation);
    }

    private void passMouseCondition$1(boolean flag, AnimationBase targetAnimation, AnimationBase invertedTargetAnimation)
    {
        if (flag)
        {
            if (!(this.currentAnimation == targetAnimation || targetAnimation == null))
            {
                this.makeConditionsBackup();
                this.setCurrentAnimation(targetAnimation);
            }
        }
        else if (this.currentAnimation != null && this.currentAnimation == targetAnimation)
            this.setCurrentAnimation(invertedTargetAnimation);
        else if (this.currentAnimation != null && this.currentAnimation == invertedTargetAnimation)
            this.restoreConditionsFromBackup();
    }
}
