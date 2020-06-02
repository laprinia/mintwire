package mintwire.panels.mintlynx;

import java.awt.Image;
import javax.swing.ImageIcon;
import mintwire.p2pmodels.messages.PeerInfo;
import mintwire.utils.Utils;
import org.jdesktop.swingx.util.OS;

public class LynxPanel extends javax.swing.JPanel {
    private final Utils utils=new Utils();
    private boolean isLinux=OS.isLinux();
    private String aliasPath = System.getenv("APPDATA") + "/MINTWIRE/";
    private PeerInfo peerInfo;
   
    public PeerInfo getPeerInfo(){
        return peerInfo;
    }
    public LynxPanel(PeerInfo peerInfo) {
        if (isLinux) {
                aliasPath = System.getProperty("user.home") + "/MINTWIRE/";
            }
        this.peerInfo = peerInfo;
        initComponents();
        try{utils.setPfp(pfpLabel, aliasPath, peerInfo,true);
        
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    private LynxPanel() {
        initComponents();
    }
    public void setCurrentPanel(){
        arrowLabel.setVisible(true);
    }
    public void setNotCurrentPanel(){
        arrowLabel.setVisible(false);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pfpLabel = new javax.swing.JLabel();
        arrowLabel = new javax.swing.JLabel();
        aliasLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(45, 48, 55));

        pfpLabel.setPreferredSize(new java.awt.Dimension(77, 77));

        arrowLabel.setPreferredSize(new java.awt.Dimension(40, 40));

        aliasLabel.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        aliasLabel.setForeground(new java.awt.Color(204, 204, 204));
        aliasLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jPanel1.setBackground(new java.awt.Color(45, 42, 49));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pfpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(aliasLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(arrowLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pfpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(arrowLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(aliasLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );

        arrowLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/chatarrow.png")).getImage().getScaledInstance(38, 38, Image.SCALE_SMOOTH)));
        arrowLabel.setVisible(false);
        aliasLabel.setText(peerInfo.getAlias());
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel aliasLabel;
    private javax.swing.JLabel arrowLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel pfpLabel;
    // End of variables declaration//GEN-END:variables
}
