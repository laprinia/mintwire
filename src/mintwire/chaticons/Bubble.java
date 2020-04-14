/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mintwire.chaticons;

import java.awt.Color;
import javax.swing.Icon;

/**
 *
 * @author Lavinia
 */
public abstract class Bubble implements Icon{
    
    private Color color;
    private int width;
    private int height;
    private int strokeThickness = 5;
    private int padding = strokeThickness / 2;
    private int arrowSize = 6;
    private int radius = 10;

    public Bubble(Color color, int width, int height) {
        this.color = color;
        this.width = width;
        this.height = height;
    }
      public int getIconWidth()
    {
        return width;
    }

    public int getIconHeight()
    {
        return height;
    }

    public Color getColor() {
        return color;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getStrokeThickness() {
        return strokeThickness;
    }

    public int getPadding() {
        return padding;
    }

    public int getArrowSize() {
        return arrowSize;
    }

    public int getRadius() {
        return radius;
    }
   
}
