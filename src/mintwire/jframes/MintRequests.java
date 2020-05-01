
package mintwire.jframes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import mintwire.p2pmodels.MintNode;
import java.util.ArrayList;

import javax.swing.JLabel;
import mintwire.p2pmodels.apps.CodeStitchApp;

import mintwire.p2pmodels.messages.CodeStitch;
import mintwire.panels.requestpanels.BlancPanel;

import mintwire.panels.requestpanels.RequestPanel;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class MintRequests extends javax.swing.JFrame {
    private RSyntaxTextArea textArea;
    private ArrayList<RequestPanel> requests = new ArrayList<>();
    private MintNode mintNode;
    private BlancPanel blancPanel;
    private Box box = new Box(BoxLayout.Y_AXIS);
    
    private static MintRequests instance = null;

    public static MintRequests getInstance(RSyntaxTextArea textArea,MintNode mintNode) {
        if (instance == null) {
            instance = new MintRequests(textArea,mintNode);
        }
        return instance;
    }
    
    private MintRequests(RSyntaxTextArea textArea,MintNode mintNode) {
        this.mintNode = mintNode;
        this.textArea=textArea;
        setTitle("Mint Requests");
        initComponents();
        requestScroll.getViewport().setBackground(new Color(94,87,104));
       
    }
      public void checkForStitches(){
         if(mintNode.getCodeStitchApp().getCodeArrayList().size()==0){
             //aici ar trb verificat daca deja sunt desenate
             //si pentru blanc ca apare de maimulte ori
            blancPanel =new BlancPanel();
            blancPanel.setPreferredSize(new Dimension(593,111));
            box.add(blancPanel);
            box.revalidate();
        }else{
        
        paintRequests();
        addListeners();
        }
    }

    private MintRequests() {

    }
    private void addListeners(){
        for(RequestPanel r:requests){
            r.getAcceptLabel().addMouseListener(new MouseAdapter() {
                  @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("child click for accept");
                JLabel label=(JLabel)e.getSource();
                RequestPanel rP=(RequestPanel)label.getParent();
                box.remove(rP);
                requests.remove(rP);
                box.revalidate();
                requestScroll.repaint();
                CodeStitch acceptedStitch=rP.getStitch();
                textArea.setText(acceptedStitch.getCode());
                textArea.setSyntaxEditingStyle(acceptedStitch.getLanguage());
                setVisible(false);
                          
            }
            });
            
            r.getIgnoreLabel().addMouseListener(new MouseAdapter() {
               public void mouseClicked(MouseEvent e) {
                System.out.println("child click for ignore");
                JLabel label=(JLabel)e.getSource();
                RequestPanel rP=(RequestPanel)label.getParent();
                box.remove(rP);
                requests.remove(rP);
                box.revalidate();
                requestScroll.repaint();
                        
            } 
            });
        }
    }
    private void paintRequests() {
        CodeStitchApp app=mintNode.getCodeStitchApp();
        ArrayList<CodeStitch> codeStitchs=app.getCodeArrayList();
        for(CodeStitch stitch:codeStitchs){
            RequestPanel panel=new RequestPanel(stitch);
            panel.setPreferredSize(new Dimension(593,111));
            box.add(panel);
            requests.add(panel);
            box.revalidate();
        }
        
        
//        requestCount = requestCount + 1;
//        RequestPanel panel = new RequestPanel();
//
//        panel.setSize(requestScroll.getWidth() - 4, 100);
//        box.add(panel);
//        requests.add(panel);
//        box.revalidate();

    }
   

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        requestScroll = new javax.swing.JScrollPane(box);
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(94, 87, 104));

        jPanel1.setBackground(new java.awt.Color(94, 87, 104));

        requestScroll.setBackground(new java.awt.Color(51, 51, 51));
        requestScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jLabel2.setBackground(new java.awt.Color(94, 87, 104));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/updateprofile.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(273, 273, 273)
                .addComponent(jLabel2)
                .addContainerGap(277, Short.MAX_VALUE))
            .addComponent(requestScroll)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(requestScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel2)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        if(blancPanel!=null){
            box.remove(blancPanel);
            box.revalidate();
        }
        setVisible(false);
    }//GEN-LAST:event_jLabel2MouseClicked

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MintRequests().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane requestScroll;
    // End of variables declaration//GEN-END:variables
}
