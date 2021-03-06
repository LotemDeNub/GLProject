package com.gl.graphics;

import com.gl.graphics.relative_items.RelativeItem;
import com.gl.graphics.relative_items.RelativeParent;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menu extends JPanelWithBackground implements RelativeParent {

    private static final Image DEFAULT_BG_IMG = GraphicUtils.loadImage("MenuBG");

    private List<RelativeItem> items;
    private List<MenuButton> buttons;

    public Menu() {
        this(DEFAULT_BG_IMG);
    }

    public Menu(Image bg) {
        super(bg);

        items = Collections.synchronizedList(new ArrayList<>());
        buttons = Collections.synchronizedList(new ArrayList<>());

        MouseAdapter mouseListener = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                buttons.forEach(MenuButton::sendMouseClick);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                buttons.forEach(b -> b.sendMousePos(e.getX(), e.getY()));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttons.forEach(b -> b.sendMousePos(e.getX(), e.getY()));
            }
        };

        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    protected void addItem(RelativeItem item) {
        items.add(item);

        if (item instanceof MenuButton) {
            buttons.add((MenuButton) item);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = GraphicUtils.getGraphicsWithHints(g);

        items.forEach(i -> i.draw(g2d));
    }

    public void reset() {
        Point mousePos = getMousePosition(true);

        buttons.forEach(b -> {
            if (mousePos == null) {
                b.setSelected(false);
            } else {
                b.sendMousePos((int) mousePos.getX(), (int) mousePos.getY());
            }
        });
    }

    @Override
    public int getStartingX() {
        return 0;
    }

    @Override
    public int getStartingY() {
        return 0;
    }
}
