package com.kgc.sauw.core.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.kgc.sauw.core.gui.elements.Elements;
import com.kgc.sauw.core.utils.Camera2D;
import com.kgc.sauw.core.utils.GravityAdapter;

import static com.kgc.sauw.core.graphic.Graphic.*;

public class InterfaceElement {
    public enum Sides {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        CENTER,
        LEFT_TOP,
        LEFT_BOTTOM,
        RIGHT_TOP,
        RIGHT_BOTTOM
    }

    public float x, y, width, height;

    public float BX = 0f, BY = 0f, BWidth = 0f, BHeight = 0f;
    protected boolean hidden;
    protected boolean isTouched;
    protected boolean this_touched;
    public String ID = "";
    public boolean wasClicked;
    public boolean wasUp;

    public InterfaceElement attachedTo;
    protected Sides attachableSide, attachTo;

    public float marginTop, marginBottom, marginLeft, marginRight;
    public float translationX = 0, translationY = 0;

    public final void update(Camera2D cam) {
        wasClicked = false;
        wasUp = false;
        if (!hidden) {
            if (Gdx.input.isTouched()) {
                if (!isTouched) {
                    if (Gdx.input.getX() > x && Gdx.input.getX() < x + width && cam.H - Gdx.input.getY() > y && cam.H - Gdx.input.getY() < y + height)
                        this_touched = true;
                    isTouched = true;

                }
            }
            if (this_touched && !Gdx.input.isTouched()) {
                if (Gdx.input.getX() > x && Gdx.input.getX() < x + width && cam.H - Gdx.input.getY() > y && cam.H - Gdx.input.getY() < y + height) {
                    onClick(true);
                    wasClicked = true;
                } else {
                    onClick(false);
                }
                wasUp = true;
                this_touched = false;
            }
            if (!Gdx.input.isTouched())
                isTouched = false;
            tick(cam);
        }
    }

    protected void tick(Camera2D cam) {
    }

    public final void render(SpriteBatch batch, Camera2D cam) {
        if (!hidden) renderTick(batch, cam);
    }

    protected void renderTick(SpriteBatch batch, Camera2D cam) {
    }

    public void setMarginLeft(float marginLeft) {
        this.marginLeft = marginLeft;
    }

    public void setMarginRight(float marginRight) {
        this.marginRight = marginRight;
    }

    public void setMarginTop(float marginTop) {
        this.marginTop = marginTop;
    }

    public void setMarginBottom(float marginBottom) {
        this.marginBottom = marginBottom;
    }

    public void setTranslationX(float translationX) {
        this.translationX = translationX;
    }

    public void setTranslationY(float translationY) {
        this.translationY = translationY;
    }

    public void setMargin(float right, float left, float top, float bottom) {
        this.marginLeft = left;
        this.marginRight = right;
        this.marginTop = top;
        this.marginBottom = bottom;
    }

    public void setMargin(float margin) {
        this.marginLeft = margin;
        this.marginRight = margin;
        this.marginTop = margin;
        this.marginBottom = margin;
    }

    public void attachTo(InterfaceElement element, Sides attachableSide, Sides attachTo) {
        this.attachedTo = element;
        this.attachableSide = attachableSide;
        this.attachTo = attachTo;
        Vector2 position = GravityAdapter.getPosition(this, element, attachableSide, attachTo);

        setPosition(position.x, position.y);
        if (attachTo == Sides.RIGHT || attachTo == Sides.RIGHT_BOTTOM || attachTo == Sides.RIGHT_TOP)
            x += element.marginRight * BLOCK_SIZE;
        if (attachTo == Sides.LEFT || attachTo == Sides.LEFT_BOTTOM || attachTo == Sides.LEFT_TOP)
            x -= element.marginLeft * BLOCK_SIZE;
        if (attachTo == Sides.TOP || attachTo == Sides.LEFT_TOP || attachTo == Sides.RIGHT_TOP)
            y += element.marginTop * BLOCK_SIZE;
        if (attachTo == Sides.BOTTOM || attachTo == Sides.LEFT_BOTTOM || attachTo == Sides.RIGHT_BOTTOM)
            y -= element.marginBottom * BLOCK_SIZE;
        setPositionInBlocks(x / BLOCK_SIZE + translationX, y / BLOCK_SIZE + translationY);
    }

    public void create() {
        Elements.addElement(this);
    }

    public void onClick(boolean onButton) {

    }

    public boolean isTouched() {
        return this_touched;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void hide(boolean b) {
        this.hidden = b;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void dispose() {
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(float w, float h) {
        this.width = w;
        this.height = h;
    }

    public void setPositionInBlocks(float x, float y) {
        this.BX = x;
        this.BY = y;
        setPosition(x * BLOCK_SIZE, y * BLOCK_SIZE);
    }

    public void setSizeInBlocks(float w, float h) {
        this.BWidth = w;
        this.BHeight = h;
        setSize(w * BLOCK_SIZE, h * BLOCK_SIZE);
    }

    public void resize() {
        setPositionInBlocks(BX, BY);
        setSizeInBlocks(BWidth, BHeight);
    }
}