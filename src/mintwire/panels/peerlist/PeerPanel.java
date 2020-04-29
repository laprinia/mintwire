package mintwire.panels.peerlist;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import mintwire.utils.Utils;
import rice.pastry.PastryNode;

/**
 *
 * @author Lavinia
 */
public class PeerPanel extends javax.swing.JPanel {
    
    private String aliasPath = System.getenv("APPDATA") + "/MINTWIRE/";
    private Utils utils=new Utils();
    private JLabel infoLabel;
    private String filePath="";
    private Color availableColor = new Color(168, 255, 104);
    private Color awayColor = new Color(255, 190, 104);
    private Color doNotDisturbColor = new Color(255, 104, 168);
    private Color invisibleColor = new Color(104, 168, 255);
    private PastryNode pastryNode;

    private PeerPanel() {
        
        initComponents();
        
    }

    public PeerPanel(PastryNode pastryNode) {
       setFocusable(true);
    setRequestFocusEnabled(true);




        if (utils.isLinux()) {
            aliasPath = System.getProperty("user.home") + "/MINTWIRE/";
        }
        this.pastryNode = pastryNode;
        
        initComponents();
        setPreferredSize(new Dimension(299,92));
        revalidate();
//        setPfp();

    }
      public void setPfp() throws IOException {
       BufferedImage bi;
        File pfp;
        try {
            pfp = new File(aliasPath +pastryNode.alias + "/pfp/pfp.png");
            bi = ImageIO.read(pfp);
            
           
        } catch (Exception ex) {
           
           pfp = new File(aliasPath +pastryNode.alias + "/pfp/pfp.png");
            bi = ImageIO.read(pfp);
        }
           BufferedImage biR = utils.makeRound(bi);
           
            Dimension d=pfpLabel.getPreferredSize();
            
            Image resultScaled = biR.getScaledInstance(d.width-12,d.height-12, Image.SCALE_SMOOTH);

            ImageIcon ico = new ImageIcon(resultScaled);

            pfpLabel.setIcon(ico);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        aliasLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel()
        {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Dimension arcs = new Dimension(15,15);
                int width = getWidth();
                int height = getHeight();
                Graphics2D graphics = (Graphics2D) g;
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                //graphics.setStroke(new BasicStroke(1));
                switch(pastryNode.status){
                    case "available":setForeground(availableColor); break;
                    case "away":setForeground(awayColor);break;
                    case "donotdisturb":setForeground(doNotDisturbColor);break;
                    case "invisible":setForeground(invisibleColor);break;
                    default:setForeground(availableColor);break;
                }
                graphics.setColor(getBackground());
                graphics.fillOval(0, 0, width-1, height-1);
                graphics.setColor(getForeground());
                graphics.drawOval(0, 0, width-1, height-1);
            }
        }
        ;
        pfpLabel = new javax.swing.JLabel()

        ;

        setBackground(new java.awt.Color(45, 48, 56));
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        aliasLabel.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        aliasLabel.setForeground(new java.awt.Color(204, 204, 204));
        aliasLabel.setPreferredSize(new java.awt.Dimension(195, 86));

        jPanel1.setPreferredSize(new java.awt.Dimension(80, 80));

        pfpLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pfpLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pfpLabel.setPreferredSize(new java.awt.Dimension(80, 80));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(pfpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pfpLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        try{setPfp();}catch(Exception ex){ex.getMessage();}

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aliasLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(aliasLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        aliasLabel.setText(pastryNode.alias);
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
       requestFocus();
    }//GEN-LAST:event_formKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel aliasLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel pfpLabel;
    // End of variables declaration//GEN-END:variables
}
