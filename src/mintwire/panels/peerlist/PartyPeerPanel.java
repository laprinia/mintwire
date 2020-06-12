/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mintwire.panels.peerlist;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mintwire.p2pmodels.messages.PeerInfo;
import mintwire.utils.Utils;
import org.jdesktop.swingx.util.OS;


public class PartyPeerPanel extends javax.swing.JPanel {
    private String aliasPath = System.getenv("APPDATA") + "/MINTWIRE/";
    private Utils utils=new Utils();
    private boolean isLinux=OS.isLinux();
    private Color hostColor=new Color(174,65,255);
    private Color availableColor = new Color(168, 255, 104);
    private Color awayColor = new Color(255, 190, 104);
    private Color doNotDisturbColor = new Color(255, 104, 168);
    private Color invisibleColor = new Color(104, 168, 255);
private PeerInfo peerInfo;

    public PartyPeerPanel(PeerInfo peerInfo) {
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
            utils.setPfp(pfpLabel,aliasPath,peerInfo,true);
        } catch (IOException ex) {
            Logger.getLogger(ConnectedPeerPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel()
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
                    case "host": setForeground(hostColor);break;
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
        jLabel1 = new javax.swing.JLabel();

        panel.setPreferredSize(new java.awt.Dimension(80, 80));

        pfpLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pfpLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pfpLabel.setPreferredSize(new java.awt.Dimension(80, 80));
        pfpLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pfpLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(pfpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pfpLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        try{
            utils.setPfp(pfpLabel, aliasPath, peerInfo,true);
        }catch(Exception ex){ex.printStackTrace();}

        jLabel1.setForeground(new java.awt.Color(97, 214, 28));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(47, 47, 47)
                    .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(38, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(96, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(46, Short.MAX_VALUE)))
        );

        if("host".equals(peerInfo.getStatus())) {
            jLabel1.setText(peerInfo.getAlias()+"(host)");
        }else {
            jLabel1.setText(peerInfo.getAlias());
        }
    }// </editor-fold>//GEN-END:initComponents

    private void pfpLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pfpLabelMouseClicked

    }//GEN-LAST:event_pfpLabelMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel panel;
    private javax.swing.JLabel pfpLabel;
    // End of variables declaration//GEN-END:variables
}
