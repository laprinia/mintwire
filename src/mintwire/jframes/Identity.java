package mintwire.jframes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.Image;
import java.awt.RenderingHints;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JList;

import javax.swing.SwingWorker;
import mintwire.p2pmodels.MintNode;
import mintwire.utils.Status;

import mintwire.utils.Utils;
import org.jdesktop.swingx.util.OS;
import rice.pastry.NodeHandle;

public class Identity extends javax.swing.JFrame {

    private MintNode mintNode;
    private ImageIcon ii;
    private String aliasPath = System.getenv("APPDATA") + "/MINTWIRE/";

    private ImageIcon finalIcon;
    private Image resultScaled;
    private String alias = "no alias";
    private Utils utils = new Utils();
    private final boolean isLinux = OS.isLinux();
    private static Identity instance = null;

    public Identity() {

        initComponents();

    }


    public static Identity getIndentityInstance(MintNode mn) {
        if (instance == null) {
            instance = new Identity(mn);
        }
        return instance;
    }

    public Identity getInstance() {
        return this.instance;
    }


    public class StatusRenderer extends DefaultListCellRenderer {

        private Map<String, ImageIcon> iconMap = new HashMap<>();
        private String[] statuses = {"Available", "Away", "Do not Disturb", "Invisible"};
        private ArrayList<ImageIcon> icons = new ArrayList<>();

        public StatusRenderer() {

            icons.add(new ImageIcon(getClass().getResource("/mintwire/res/pngs/available.png")));
            icons.add(new ImageIcon(getClass().getResource("/mintwire/res/pngs/away.png")));
            icons.add(new ImageIcon(getClass().getResource("/mintwire/res/pngs/donotdisturb.png")));
            icons.add(new ImageIcon(getClass().getResource("/mintwire/res/pngs/invisible.png")));

        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (index >= 0) {
                this.setText(statuses[index]);
                this.setIcon(icons.get(index));
            }
            return this;
        }
    }

    //end of custom render 
    public Identity(MintNode mn) {
        setTitle("Identity");
        if (isLinux) {
            aliasPath = System.getProperty("user.home") + "/MINTWIRE/";
        }
        this.mintNode = mn;
        this.alias = mn.getNode().alias;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        editCombo();

        jPanel3.add(new JLabel(alias));
        initPfp(alias);

    }

    //MY METHODS
    private void initPfp(String alias) {
        File pfp = new File(aliasPath + alias + "/pfp/pfp.png");
        try {
            BufferedImage bi = ImageIO.read(pfp);

            BufferedImage biR = utils.makeRound(bi);
            ImageIcon ico = new ImageIcon(biR);

            pfpLabel.setIcon(ico);
        } catch (Exception ex) {
            System.out.print(ex.getMessage());

        }

    }

    private void editCombo() {

        jComboBox1.setRenderer(new StatusRenderer());

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        pfpLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Dimension arcs = new Dimension(15,15);
                int width = getWidth();
                int height = getHeight();
                Graphics2D graphics = (Graphics2D) g;
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                graphics.setColor(getBackground());
                graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);
                graphics.setColor(getForeground());
                graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);
            }
        };
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(94, 87, 104));

        jPanel1.setBackground(new java.awt.Color(94, 87, 104));
        jPanel1.setMinimumSize(new java.awt.Dimension(600, 600));
        jPanel1.setPreferredSize(new java.awt.Dimension(600, 600));

        jComboBox1.setBackground(new java.awt.Color(45, 48, 56));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Available", "Away", "Do not Disturb", "Invisible" }));
        jComboBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jPanel2.setBackground(new java.awt.Color(49, 46, 54));
        jPanel2.setMaximumSize(new java.awt.Dimension(600, 600));
        jPanel2.setMinimumSize(new java.awt.Dimension(600, 600));

        pfpLabel.setBackground(new java.awt.Color(49, 46, 54));
        pfpLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pfpLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel4.setBackground(new java.awt.Color(49, 46, 54));
        jLabel4.setFont(new java.awt.Font("Monospaced", 3, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(232, 232, 232));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jPanel3.setBackground(new java.awt.Color(49, 46, 54));
        jPanel3.setForeground(new java.awt.Color(153, 255, 153));
        jPanel3.setLayout(new GridBagLayout());
        GridBagConstraints cl;
        cl = new GridBagConstraints();
        cl.gridy = 0;
        JLabel label=new JLabel(this.alias);
        label.setForeground(Color.white);

        jPanel1.add(label, cl);

        jLabel3.setBackground(new java.awt.Color(49, 46, 54));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(67, 207, 137));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel3.setText(this.alias);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(203, 203, 203)
                .addComponent(pfpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(211, 211, 211)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(pfpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setBackground(new java.awt.Color(94, 87, 104));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(56, 217, 148));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/selectprofile.png"))); // NOI18N
        jLabel1.setText("Select another picture");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel1MouseEntered(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(94, 87, 104));
        jLabel2.setFont(new java.awt.Font("Monospaced", 3, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(56, 217, 148));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/updateprofile.png"))); // NOI18N
        jLabel2.setToolTipText("");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(207, 207, 207)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(264, 264, 264))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jComboBox1.setAlignmentY(CENTER_ALIGNMENT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseEntered

    }//GEN-LAST:event_jLabel1MouseEntered

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        //CHOOSE PFP
        JFileChooser jfc = new JFileChooser();
        jfc.showOpenDialog(null);
        final File f = jfc.getSelectedFile();
        //DO A CHECK FOR OTHER EXTENSIONS
        if (f == null) {
            return;
        }
        SwingWorker sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                Thread.sleep(500);
                ii = new ImageIcon(ImageIO.read(new File(f.getAbsolutePath())));
                Image img = ii.getImage();
                BufferedImage buffImg = (BufferedImage) img;

                BufferedImage result = utils.makeRound(buffImg);

                resultScaled = result.getScaledInstance(pfpLabel.getWidth(), pfpLabel.getHeight(), Image.SCALE_SMOOTH);

                return null;
            }

            @Override
            protected void done() {
                super.done();
                finalIcon = new ImageIcon(resultScaled);
                pfpLabel.setIcon(finalIcon);
                 for(NodeHandle nh: utils.getNodeHandleList(mintNode)){
                  mintNode.getPfpApp().sendFile(nh);
                 }

            }
        };
        sw.execute();
       
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        if (finalIcon != null) {
            try {
                Image img = finalIcon.getImage();

                BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

                Graphics2D g2 = bi.createGraphics();
                g2.drawImage(img, 0, 0, null);
                g2.dispose();
                ImageIO.write(bi, "png", new File(aliasPath + alias + "/pfp/pfp.png"));

            } catch (IOException e) {
                //err
                e.printStackTrace();
            }
        } else {
          

        }
        //SERVER ACTION
        //schimbare 
        switch (jComboBox1.getSelectedIndex()) {
            case 0:
                mintNode.getNode().status = Status.available.toString();
                break;
            case 1:
                mintNode.getNode().status = Status.away.toString();
                break;
            case 2:
                mintNode.getNode().status = Status.donotdisturb.toString();
                break;
            case 3:
                mintNode.getNode().status = Status.invisible.toString();
                break;
            default:
                mintNode.getNode().status = Status.available.toString();
                break;
        }

        dispose();
        instance = null;
    }//GEN-LAST:event_jLabel2MouseClicked

    public static void main(String args[]) {
       
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Identity.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Identity.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Identity.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Identity.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Identity().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel pfpLabel;
    // End of variables declaration//GEN-END:variables
}
