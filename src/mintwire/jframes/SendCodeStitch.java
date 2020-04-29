
package mintwire.jframes;

import java.awt.Dimension;

import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import mintwire.p2pmodels.MintNode;
import mintwire.p2pmodels.messages.PeerInfo;
import mintwire.panels.peerlist.PeerPanel;
import rice.pastry.NodeHandle;


import rice.pastry.PastryNode;

import rice.pastry.leafset.LeafSet;



public class SendCodeStitch extends javax.swing.JFrame {
private Box box = new Box(BoxLayout.Y_AXIS);
private static SendCodeStitch instance=null;
private MintNode mintNode; 
private List<NodeHandle> handles ;


    public static SendCodeStitch getInstance(String codeStitch, MintNode mainNode){
        if(instance==null){
            return new SendCodeStitch(codeStitch,mainNode);
        }else
            return instance;
    }
    private SendCodeStitch() {
        initComponents();
    }
    
    private SendCodeStitch(String codeStitch, MintNode mainNode){
        setTitle("Send a stitch");
        this.mintNode=mainNode;
        LeafSet set = mintNode.getNode().getLeafSet();
        System.out.println("Leafset:\n"+set);
        handles= set.asList();
        
        initComponents();
        peerScroll.setPreferredSize(new Dimension(299,276));
        peerScroll.revalidate();
        System.out.println("Nodehandle leafset ids");
         for(NodeHandle h:handles){
            
           mintNode.getPeerInfoApp().routePeerInfo(h.getId(), new PeerInfo(mintNode.getNode().getLocalHandle(),mintNode.getNode().alias, mintNode.getNode().status));
        }
        peerScroll.revalidate();
        
    }

    public void paintRequest(PastryNode pastryNode){
        
        PeerPanel panel=new PeerPanel(pastryNode);
        panel.setPreferredSize(new Dimension(299,92));
        panel.revalidate();
        box.add(panel);
       
        box.revalidate();
       
       
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        peerScroll = new javax.swing.JScrollPane(box);
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
        setVisible(false);
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
