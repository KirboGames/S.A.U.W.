package com.kgc.sauw.core.gui.elements;

import com.kgc.sauw.core.gui.InterfaceElement;

public class Notification extends InterfaceElement {
    /*private Texture background;
    private BitmapFont BF;
    public float timer = 0;
    private int time = 0;
    private String title = "";
    private String txt = "";
    private Texture img;
    private BitmapFont.TextBounds TB;
    private boolean hideOnClick = false;

    public Notification(Texture background) {
        this.background = background;
        BF = new BitmapFont(Gdx.files.internal("ttf.fnt"));
        hide(true);
    }

    public void show(int x, int y, int w, int h, String title, String txt, int timeS) {
        hide(false);
        setPosition(x, y);
        setSize(w, h);
        this.img = null;
        this.title = title;
        this.txt = txt;
        this.time = timeS;
    }

    public void show(int x, int y, int w, int h, String title, String txt, Texture img, int timeS) {

        hide(false);
        setPosition(x, y);
        setSize(w, h);
        TB = BF.getBounds(txt);
        int t = h / 8;
        int tW = w - h - t;
        BF.setScale(tW / TB.width);
        BF.setColor(Color.BLACK);
        this.title = title;
        this.txt = txt;
        this.img = img;
        this.time = timeS;
    }

    @Override
    public void tick(Camera2D cam) {
        timer += Gdx.graphics.getRawDeltaTime();
        if (timer >= time) {
            timer = 0;
            hide(true);
        }
    }

    @Override
    public void onClick(boolean onButton) {
        super.onClick(onButton);
        if (hideOnClick) {
            hide(true);
            timer = 0;
        }
    }

    public void hideOnClick(boolean b) {
        this.hideOnClick = b;
    }

    public void renderTick(SpriteBatch b, Camera2D cam) {
        b.draw(background, cam.X + X, cam.Y + Y, width, height);
        if (img != null) {
            float t = height / 8;
            b.draw(img, cam.X + X + t, cam.Y + Y + t, height - t * 2, height - t * 2);
            BF.setColor(Color.BLUE);
            BF.draw(b, title, X + cam.X + height, Y + height - BF.getBounds(title).height);
            BF.setColor(Color.BLACK);
            BF.drawMultiLine(b, txt, X + cam.X + height, Y + height - BF.getBounds(title).height - BF.getBounds(txt).height);
        } else {
            BF.setColor(Color.BLUE);
            BF.drawMultiLine(b, title, X + cam.X + height / 32, Y + height + cam.Y - height / 32, width - height / 16, BitmapFont.HAlignment.CENTER);
            BF.setColor(Color.BLACK);
            BF.drawWrapped(b, txt, X + cam.X + height / 32, Y + height + cam.Y - height / 8, width - height / 16);
        }
    }*/
}
