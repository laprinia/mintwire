package mintwire.chaticons;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;

import java.awt.geom.RoundRectangle2D;
import static jdk.vm.ci.sparc.SPARC.g2;

public class LeftBubble extends Bubble {

    public LeftBubble(Color color, int width, int height) {
        super(color, width, height);
    }

    public int getIconWidth() {
        return super.getIconWidth();
    }

    public int getIconHeight() {
        return super.getIconHeight();
    }

    public void paintIcon(Component c, Graphics g,int x,int y) {
  final Graphics2D graphics2D = (Graphics2D) g;
    RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    graphics2D.setRenderingHints(qualityHints);
    graphics2D.setPaint(this.getColor());
    int width = getWidth();
    int height = getHeight();
    GeneralPath path = new GeneralPath();
    path.moveTo(5, 10);
    path.curveTo(5, 10, 7, 5, 0, 0);
    path.curveTo(0, 0, 12, 0, 12, 5);
    path.curveTo(12, 5, 12, 0, 20, 0);
    path.lineTo(width - 10, 0);
    path.curveTo(width - 10, 0, width, 0, width, 10);
    path.lineTo(width, height - 10);
    path.curveTo(width, height - 10, width, height, width - 10, height);
    path.lineTo(15, height);
    path.curveTo(15, height, 5, height, 5, height - 10);
    path.lineTo(5, 15);
    path.closePath();
    
     
      
    
    graphics2D.fill(path);
  
        
    }

}
