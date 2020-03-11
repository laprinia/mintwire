package mintwire.chatpanels;

import java.awt.Color;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import mintwire.chaticons.Bubble;
import mintwire.chaticons.LeftBubble;
import mintwire.chaticons.RightBubble;

public class Bubbler {

    private String text;
    private Color color;

    private Bubble bubble;

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
        JLabel aux = new JLabel(this.text+"    ");
        aux.setFont(new Font("Calibri", Font.BOLD, 18));
        aux.setSize(aux.getPreferredSize());
        this.bubble = new LeftBubble(this.color, 13 + aux.getWidth(), 10 + aux.getHeight());

        JLabel label = new JLabel(bubble);

        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setSize(aux.getPreferredSize());
        label.setFont(new Font("Calibri", Font.PLAIN, 18));
        label.setForeground(Color.BLACK);
        label.setText(this.text);
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
        JLabel aux = new JLabel(this.text);
        aux.setFont(new Font("Calibri", Font.PLAIN, 18));
        aux.setSize(aux.getPreferredSize());
        aux.setHorizontalTextPosition(JLabel.CENTER);
        aux.setVerticalTextPosition(JLabel.CENTER);
        this.bubble = new RightBubble(this.color, 10+aux.getWidth(), 10 + aux.getHeight());

        JLabel label = new JLabel();
        label.setSize(aux.getPreferredSize());

        label.setIcon(translateIcon());
        label.setText(this.text);
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
