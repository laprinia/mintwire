package mintwire.chatpanels;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import mintwire.chaticons.Bubble;
import mintwire.chaticons.LeftBubble;
import mintwire.chaticons.RightBubble;
import mintwire.utils.Utils;

public class Bubbler {
    
    private String text;
    private Color color;
    private Bubble bubble;
    private Utils utils = new Utils();
    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm 'on' EE dd-MM-yyyy");
    private Date date = new Date(System.currentTimeMillis());
    private final String DATE_TAG_LEFT="<div style='text-align:left; color:Gray;'>"+formatter.format(date)+"</div>";
    private final String DATE_TAG_RIGHT="<div style='text-align:right; color:rgb(64,64,64);'>"+formatter.format(date)+"</div>";
    
    public Bubbler(String text, Color color) {
        this.text = text;
        this.color = color;

    }

    private ImageIcon translateIcon() {
        BufferedImage bi = new BufferedImage(
                this.bubble.getIconWidth(),
                this.bubble.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();

        this.bubble.paintIcon(null, g, 0, 0);
        g.dispose();
        ImageIcon ii = new ImageIcon(bi);
        return ii;
    }

    public void paintLeftBubble(JPanel scrollable) {
        JLabel aux = new JLabel("<html><body style='padding:12px;'>"+utils.insertPeriodically(this.text,"<br/>",70)+DATE_TAG_LEFT+"</body></html>");
        aux.setFont(new Font("Calibri", Font.BOLD, 18));
        aux.setSize(aux.getPreferredSize());
        this.bubble = new LeftBubble(this.color,  aux.getWidth(),  aux.getHeight());

        JLabel label = new JLabel(bubble);

        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setSize(aux.getPreferredSize());
        label.setFont(new Font("Calibri", Font.PLAIN, 18));
        label.setForeground(Color.BLACK);
        label.setText("<html><body style='text-align:center;padding:12px;'>"+utils.insertPeriodically(this.text,"<br/>",70)+DATE_TAG_LEFT+"</body></html>");
        JPanel intermid = new JPanel();
        intermid.setLayout(new FlowLayout(FlowLayout.LEFT));
        intermid.setSize(aux.getWidth(), aux.getHeight());
        intermid.add(label);

        scrollable.add(intermid);
        scrollable.revalidate();
        int height = (int) scrollable.getPreferredSize().getHeight();
        Rectangle rect = new Rectangle(0, height, 10, 10);
        scrollable.scrollRectToVisible(rect);
    }

    public void paintRightBubble(JPanel scrollable) {
      
        JLabel aux = new JLabel("<html><body style='text-align:right;padding:12px;'>"+utils.insertPeriodically(this.text,"<br/>",70)+DATE_TAG_RIGHT+"</body></html>");
        aux.setMaximumSize(new Dimension(100,200));
        aux.setFont(new Font("Calibri", Font.PLAIN, 18));
        aux.setSize(aux.getPreferredSize());
        aux.setHorizontalTextPosition(JLabel.CENTER);
        aux.setVerticalTextPosition(JLabel.CENTER);
        this.bubble = new RightBubble(this.color, aux.getWidth(),  aux.getHeight());

        JLabel label = new JLabel();
        label.setSize(aux.getPreferredSize());

        label.setIcon(translateIcon());
        label.setMaximumSize(new Dimension(50,200));
        label.setText("<html><body style='text-align:center;padding:12px;'>"+utils.insertPeriodically(this.text,"<br/>",70)+DATE_TAG_RIGHT+"</body></html>");
        label.setFont(new Font("Calibri", Font.PLAIN, 18));
        label.setForeground(Color.BLACK);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.CENTER);

        System.out.println(label.getText());

        JPanel intermid = new JPanel();

        intermid.setLayout(new FlowLayout(FlowLayout.RIGHT));
        intermid.setSize(aux.getWidth(), aux.getHeight());

        intermid.add(label);

        scrollable.add(intermid);
        scrollable.revalidate();
        int height = (int) scrollable.getPreferredSize().getHeight();
        Rectangle rect = new Rectangle(0, height, 10, 10);
        scrollable.scrollRectToVisible(rect);
    }

}
