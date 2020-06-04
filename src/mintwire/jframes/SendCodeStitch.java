package mintwire.jframes;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import mintwire.p2pmodels.MintNode;
import mintwire.p2pmodels.apps.SendPeerInfoApp;
import mintwire.p2pmodels.messages.CodeStitch;
import mintwire.p2pmodels.messages.PeerInfo;
import mintwire.panels.peerlist.PeerPanel;
import mintwire.utils.Utils;




public class SendCodeStitch extends javax.swing.JFrame {
    private Utils utils=new Utils();
    private CodeStitch codeStitch;
    private ArrayList<PeerPanel> peerPanels=new ArrayList<>();
    private JLabel label;
    private Box box = new Box(BoxLayout.Y_AXIS);
    private static SendCodeStitch instance = null;
    private MintNode mintNode;
    
    private SendPeerInfoApp peerInfoApp;

    public static SendCodeStitch getInstance(CodeStitch codeStitch, MintNode mainNode) {
        if (instance == null) {
            instance=new SendCodeStitch(codeStitch, mainNode);
           
        } 
         return instance;
    }

    private SendCodeStitch() {
        initComponents();
    }

    private SendCodeStitch(CodeStitch codeStitch, MintNode mainNode) {
        setTitle("Send a stitch");
        this.mintNode = mainNode;
        this.codeStitch=codeStitch;
        peerInfoApp = mintNode.getPeerInfoApp();
        utils.updatePeerInfo(mintNode);
        utils.updatePeerPfp(mintNode, mintNode.getPeerInfoApp().getPeerList());

        initComponents();
        peerScroll.setPreferredSize(new Dimension(299, 276));
        peerScroll.revalidate();
        peerScroll.revalidate();
        SwingWorker sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                Thread.sleep(60);
                utils.updatePeerPfp(mintNode, mintNode.getPeerInfoApp().getPeerList());
                Thread.sleep(60);
                paintPeers();
                return null;
            }

        };

        sw.execute();
        
    }

    public void setCodeStitch(CodeStitch codeStitch) {
        this.codeStitch = codeStitch;
    }
    

    public void paintPeers() {
        if (peerInfoApp.getPeerList() == null || peerInfoApp.getPeerList().size() < 1) {
            label = new JLabel("<html><center>Your peers are not available at the moment");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            JOptionPane.showMessageDialog(null, label, "Cannot find peers", JOptionPane.INFORMATION_MESSAGE);
            setVisible(false);
        } else {
            for (PeerInfo peerInfo : peerInfoApp.getPeerList()) {
                System.err.println(peerInfo.toString());
                PeerPanel panel = new PeerPanel(peerInfo);
                panel.setPreferredSize(new Dimension(299, 92));
                panel.revalidate();
                box.add(panel);
                box.revalidate();
                peerPanels.add(panel);
            }
        }

    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        peerScroll = new javax.swing.JScrollPane(box);
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(45, 48, 56));

        peerScroll.setBackground(new java.awt.Color(45, 48, 56));
        peerScroll.setPreferredSize(new java.awt.Dimension(299, 276));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/send-message.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(peerScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(peerScroll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        peerScroll.getViewport().setBackground(Color.DARK_GRAY);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        for(PeerPanel p:peerPanels){
            if(p.getCheckState()){
                PeerInfo peer=p.getPeerInfo();
                if(peer.getNodeHandle().checkLiveness()){
                mintNode.getCodeStitchApp().routeCodeStitch(peer.getNodeHandle().getId(), codeStitch);
                }else{
                    
                }
                
            }
        }
        dispose(); instance=null;
    }//GEN-LAST:event_jLabel1MouseClicked

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SendCodeStitch().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane peerScroll;
    // End of variables declaration//GEN-END:variables
}
