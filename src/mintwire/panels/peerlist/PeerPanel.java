package mintwire.panels.peerlist;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.RenderingHints;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;

import mintwire.p2pmodels.messages.PeerInfo;
import mintwire.utils.Utils;
import org.jdesktop.swingx.util.OS;

/**
 *
 * @author Lavinia
 */
public class PeerPanel extends javax.swing.JPanel {
   
    private String aliasPath = System.getenv("APPDATA") + "/MINTWIRE/";
    private Utils utils=new Utils();
    private boolean isLinux=OS.isLinux();
    private JLabel infoLabel;
    private String filePath="";
    private Color availableColor = new Color(168, 255, 104);
    private Color awayColor = new Color(255, 190, 104);
    private Color doNotDisturbColor = new Color(255, 104, 168);
    private Color invisibleColor = new Color(104, 168, 255);
    private PeerInfo peerInfo;

    private PeerPanel() {
        
        initComponents();
        
    }

    
    public PeerPanel(PeerInfo peerInfo) {
        try {
            setFocusable(true);
            setRequestFocusEnabled(true);
           
            if (isLinux) {
                aliasPath = System.getProperty("user.home") + "/MINTWIRE/";
            }
            this.peerInfo=peerInfo;
            
            initComponents();
            setPreferredSize(new Dimension(299,92));
            revalidate();
            utils.setPfp(pfpLabel,aliasPath,peerInfo);
        } catch (IOException ex) {
            Logger.getLogger(PeerPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public boolean getCheckState(){
        return checkBox.isSelected();
    }
    public PeerInfo getPeerInfo(){
        return peerInfo;
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
                switch(peerInfo.getStatus()){
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
        checkBox = new javax.swing.JCheckBox();

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
        pfpLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pfpLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(pfpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pfpLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        try{
            utils.setPfp(pfpLabel, aliasPath, peerInfo);
        }catch(Exception ex){ex.printStackTrace();}

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(aliasLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(checkBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(aliasLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(checkBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        aliasLabel.setText(peerInfo.getAlias());
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
       requestFocus();
    }//GEN-LAST:event_formKeyPressed

    private void pfpLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pfpLabelMouseClicked
        
      
    }//GEN-LAST:event_pfpLabelMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel aliasLabel;
    private javax.swing.JCheckBox checkBox;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel pfpLabel;
    // End of variables declaration//GEN-END:variables
}
