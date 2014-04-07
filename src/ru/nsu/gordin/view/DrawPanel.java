package ru.nsu.gordin.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class DrawPanel extends JPanel implements MouseMotionListener, MouseWheelListener{
    private JFrame frame;
    private boolean isScrollable = true;
    private boolean isMovable = true;
    private int pixelUnit;
    private int mouseX;
    private int mouseY;
    private int centerX;
    private int centerY;
    private int stepMove;
    private int stepScale;
    static final int STEP_MIN = 1;
    static final int SCALE_MIN = 1;
    static final int STEP_MAX = 100;
    static final int SCALE_MAX = 100;
    static final int STEP_INIT = 10;
    static final int SCALE_INIT = 10;
    static final int PIXEL_UNIT_MIN = 20;
    static final int PIXEL_UNIT_INIT = 40;
    private int radius = 5;
    private CanvasPanel canvasPanel;
    private BufferedImage canvas;

    public DrawPanel(JFrame frame) {
        super(new BorderLayout());
        this.frame = frame;
        stepMove = STEP_INIT;
        stepScale = SCALE_INIT;
        pixelUnit = PIXEL_UNIT_INIT;
        canvasPanel = new CanvasPanel();
        canvasPanel.addMouseMotionListener(this);
        canvasPanel.addMouseWheelListener(this);
        add(canvasPanel, BorderLayout.CENTER);
    }



    public boolean isScrollable() {
        return isScrollable;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public void setScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }

    public void setMovable(boolean isMovable) {
        this.isMovable = isMovable;
    }


    public int getStepMove() {
        return stepMove;
    }

    public int getStepScale() {
        return stepScale;
    }

    public void setStepScale(int stepScale) {
        this.stepScale = stepScale;
    }

    public void setStepMove(int stepMove) {
        this.stepMove = stepMove;
    }

    public void increase() {
        pixelUnit += stepScale;
        canvasPanel.repaint();
    }

    public void decrease() {
        if(pixelUnit - stepScale >= PIXEL_UNIT_MIN)
            pixelUnit -= stepScale;
        canvasPanel.repaint();
    }

    public void moveLeft() {
        centerX += stepMove;
        canvasPanel.repaint();
    }

    public void moveRight() {
        centerX -= stepMove;
        canvasPanel.repaint();
    }

    public void moveUp() {
        centerY += stepMove;
        canvasPanel.repaint();
    }

    public void moveDown() {
        centerY -= stepMove;
        canvasPanel.repaint();
    }

    public void reset() {
        centerX = canvas.getWidth() / 2;
        centerY = canvas.getHeight() / 2;
        pixelUnit = PIXEL_UNIT_INIT;
        canvasPanel.repaint();
    }



    public void showSettingDialog() {
        SettingDialog settingDialog = new SettingDialog(frame, this);
        settingDialog.setVisible(true);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(!isMovable) return;
        centerX += e.getX() - mouseX;
        centerY += e.getY() - mouseY;
        mouseX = e.getX();
        mouseY = e.getY();
        canvasPanel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!isMovable) return;
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(!isScrollable) return;
        int step = stepScale * (-e.getWheelRotation());
        double mx = (mouseX - centerX) * 1.0 / pixelUnit;
        double my = (centerY - mouseY) * 1.0 / pixelUnit;
        if(pixelUnit + step >= PIXEL_UNIT_MIN) {
            pixelUnit += step;
        }
        else {
            pixelUnit = PIXEL_UNIT_MIN;
        }
        centerX = mouseX - (int)(mx * pixelUnit);
        centerY = (int)(my * pixelUnit) + mouseY;
        canvasPanel.repaint();
    }


    protected class CanvasPanel extends JPanel {
        private boolean init = false;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            if(!init) {
                init = true;
                centerX = getWidth() / 2;
                centerY = getHeight() / 2;
            }
            drawPlane();
            drawLemniscate();
            g.drawImage(canvas, 0, 0, null);

        }

        private void drawLemniscate() {
            int width = getWidth();
            int height = getHeight();
            double x = 0;
            double y = 0;
            double stopX = Math.sqrt(3) / 2; // х, при котором y' = 0
            double d = 1.0 / pixelUnit;
            int i = 0;
            int j = 0;
            int lastI = 0;
            int lastJ = 0;

            while(x <= stopX) {
                plot(centerX + i, centerY + j, width, height, Color.CYAN);
                plot(centerX - i, centerY + j, width, height, Color.CYAN);
                plot(centerX + i, centerY - j, width, height, Color.CYAN);
                plot(centerX - i, centerY - j, width, height, Color.CYAN);

                i += 1;
                x = i * 1.0 / pixelUnit;
                y = j * 1.0 / pixelUnit;
                if(Math.abs(func(x, y)) > Math.abs(func(x, y + d))) {
                    j += 1;
                }
            }

            stopX = Math.sqrt((9 + Math.sqrt(17)) / 8);
            while(x < stopX) {
                plot(centerX + i, centerY + j, width, height, Color.GREEN);
                plot(centerX - i, centerY + j, width, height, Color.GREEN);
                plot(centerX + i, centerY - j, width, height, Color.GREEN);
                plot(centerX - i, centerY - j, width, height, Color.GREEN);

                i += 1;
                x = i * 1.0 / pixelUnit;
                y = j * 1.0 / pixelUnit;
                if(Math.abs(func(x, y)) > Math.abs(func(x, y - d))) {
                    j -= 1;
                }
            }

            while(y >= 0) {
                plot(centerX + i, centerY + j, width, height, Color.RED);
                plot(centerX - i, centerY + j, width, height, Color.RED);
                plot(centerX + i, centerY - j, width, height, Color.RED);
                plot(centerX - i, centerY - j, width, height, Color.RED);

                j -= 1;
                x = i * 1.0 / pixelUnit;
                y = j * 1.0 / pixelUnit;
                if(Math.abs(func(x, y)) > Math.abs(func(x + d, y))) {
                    i += 1;
                }
            }
        }

        private void plot(int i, int j, int width, int height, Color color) {
//            System.out.println(i+" "+j);
            if (j < height && j >= 0
                    && i < width && i >= 0) {

                canvas.setRGB(i, j, color.getRGB());
            }
        }

        private double func(double x, double y) {
            double xx = x*x;
            double yy = y*y;
            return (xx+yy)*(xx+yy) - 2*(xx-yy);
        }

        public void drawPlane() {
            int width = getWidth();
            int height = getHeight();
            int i = centerX % pixelUnit;
            if(i < 0) i += pixelUnit;
            for(; i < width; i += pixelUnit) {
                for(int j = 0; j < height; j++) {
                    canvas.setRGB(i, j, Color.GRAY.getRGB());
                }
            }
            i = centerY % pixelUnit;
            if(i < 0) i += pixelUnit;
            for(; i < height; i += pixelUnit) {
                for(int j = 0; j < width; j++) {
                    canvas.setRGB(j, i, Color.GRAY.getRGB());
                }
            }
        }

    }
}
