/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mintwire.panels.requestpanels;

import javax.swing.JLabel;
import javax.swing.JPanel;
import mintwire.p2pmodels.messages.CodeStitch;

/**
 *
 * @author Lavinia
 */
public class RequestPanel extends javax.swing.JPanel {
    private CodeStitch codeStitch;
    private boolean isAccepted=false;
    private boolean isIgnored=false;
    private String alias="";
    private RequestPanel() {
        initComponents();
    }
     public RequestPanel(CodeStitch codeStitch) {
        initComponents();
        this.codeStitch=codeStitch;
        alias=codeStitch.getSender();
    }
    
    public JLabel getAcceptLabel(){
        return this.acceptLabel;
    }
    public JLabel getIgnoreLabel(){
        return this.ignoreLabel;
    }
    public CodeStitch getStitch(){
        return this.codeStitch;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        aliasLabel = new javax.swing.JLabel();
        acceptLabel = new javax.swing.JLabel();
        ignoreLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(76, 69, 87));

        jLabel1.setBackground(new java.awt.Color(90, 68, 121));
        jLabel1.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Upcoming CodeStitch request from:");

        aliasLabel.setBackground(new java.awt.Color(90, 68, 121));
        aliasLabel.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        aliasLabel.setForeground(new java.awt.Color(255, 102, 102));
        aliasLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        acceptLabel.setBackground(new java.awt.Color(90, 68, 121));
        acceptLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/yes.png"))); // NOI18N

        ignoreLabel.setBackground(new java.awt.Color(90, 68, 121));
        ignoreLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/no.png"))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(53, 45, 55));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(aliasLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(53, 53, 53)
                .addComponent(acceptLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addComponent(ignoreLabel)
                .addGap(56, 56, 56))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(aliasLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(ignoreLabel)
                        .addComponent(acceptLabel)))
                .addGap(17, 17, 17))
        );

        aliasLabel.setText(alias);
    }// </editor-fold>//GEN-END:initComponents

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel acceptLabel;
    private javax.swing.JLabel aliasLabel;
    private javax.swing.JLabel ignoreLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
