
package mintwire.jframes;


import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import mdlaf.MaterialLookAndFeel;
import mintwire.BCrypt;
import mintwire.p2pmodels.MintNode;

import mintwire.theme.CustomTheme;
import mintwire.utils.Status;

import org.jdesktop.swingx.util.OS;
import rice.environment.Environment;


public class Login extends javax.swing.JFrame {
    
  
    private final boolean isLinux=OS.isLinux();
    private final int ACCOUNT_CAP = 20;
    private final int ACCOUNT_MIN_CHARS = 5;
    private JLabel label;
    private String fullPath = System.getenv("APPDATA") + "/MINTWIRE/init.txt";

    
    public Login() {
        
        System.out.println(isLinux+" is linux");
        if(isLinux) fullPath=System.getProperty("user.home")+"/MINTWIRE/init.txt";
        setTitle("Mintwire Login");
        try {
            UIManager.setLookAndFeel(new MaterialLookAndFeel());
            if (UIManager.getLookAndFeel() instanceof MaterialLookAndFeel) {
                MaterialLookAndFeel.changeTheme(new CustomTheme());
            }

        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        initComponents();

    }

    private void connToDB(String alias, String passw, String hAlias, String hPassw) {

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        try {

            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        } catch (ClassNotFoundException ex) {

            JOptionPane.showMessageDialog(null, "Problem with loading MS Access JDBC driver", "Login Error", JOptionPane.ERROR_MESSAGE);

        }

        try {
            String path = "src/mintwire/jframes/dbs/loginDB.accdb";
            String dbURL = "jdbc:ucanaccess://"
                    + path;
            conn = DriverManager.getConnection(dbURL);
            //check if it reaches cap
            String count = "SELECT COUNT(*) AS count from aliases";
            statement = conn.prepareStatement(count);
            result = statement.executeQuery();
            result.next();
            int number = result.getInt("count");
            result.close();
            System.out.println("Number of DB entries: " + number);
            if (number >= ACCOUNT_CAP) {
                label = new JLabel("<html><center>Please use a registered existing account on this PC.");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                JOptionPane.showMessageDialog(null, label, "PC accounts limit reached", JOptionPane.INFORMATION_MESSAGE);

            } else {

                statement = conn.prepareStatement("select * from aliases");

                result = statement.executeQuery();
                boolean isAlias = false;
                boolean isPassw = false;
                while (result.next()) {
                    String tableAlias = result.getString("alias");
                    String tablePassword = result.getString("password");
                    isAlias = BCrypt.checkpw(alias, tableAlias);
                    isPassw = BCrypt.checkpw(passw, tablePassword);
                    if(isAlias&&isPassw==true)
                        break;

                }

                if ((isAlias && isPassw) == false) {

                    statement = conn.prepareStatement("insert into aliases (alias, password) values (?,?)");
                    statement.setString(1, hAlias);
                    statement.setString(2, hPassw);
                    boolean error = statement.execute();
                    if (!error) {
                        label = new JLabel("<html><center>First login");
                        label.setHorizontalAlignment(SwingConstants.CENTER);
                        JOptionPane.showMessageDialog(null, label, "First login", JOptionPane.INFORMATION_MESSAGE);
                    }

                } else if ((isAlias && isPassw)) {
                    label = new JLabel("<html><center>It's you. Welcome back!");
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    JOptionPane.showMessageDialog(null, label, "Alias recognized", JOptionPane.INFORMATION_MESSAGE);

                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, new String(ex.getMessage()), "DB Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (null != conn) {
                    result.close();
                    statement.close();
                    conn.close();

                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, new String(ex.getMessage()), "Closing DB Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private void startApp(String password, MintNode mintNode) {

        MintwireClientGUI mcg = new MintwireClientGUI(password, mintNode);
        mcg.pack();
        mcg.setLocationRelativeTo(null);
        mcg.setVisible(true);
        mcg.setVisible(true);
        setVisible(false);

    }
    private MintNode createMintNode(String alias) throws InterruptedException{
       byte[] encoded;
       ArrayList<String> tokens=new ArrayList<>();
        try {
            encoded = Files.readAllBytes(Paths.get(fullPath));
            String s= new String(encoded, StandardCharsets.US_ASCII);
            StringTokenizer st=new StringTokenizer(s,",");
            
            while(st.hasMoreElements()){
                tokens.add(st.nextToken());
            }
            if(tokens.size()<2) throw new Exception();
        } catch (Exception ex) {
            label=new JLabel("<html><center>Please configure your node first!"+ ex.getMessage());
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    JOptionPane.showMessageDialog(null, label, "Node not configured yet", JOptionPane.INFORMATION_MESSAGE);
            
        }
        Environment env; env = new Environment();
        env.getParameters().setString("nat_search_policy", "never");
        MintNode mintNode;      
        try {
           mintNode=new MintNode(Integer.parseInt(tokens.get(0)), new InetSocketAddress(tokens.get(1),Integer.parseInt(tokens.get(2))),alias,Status.available.toString(),env);
             return mintNode;
        } catch (Exception ex) {
            label=new JLabel("<html><center>Node not configured corectly!"+ ex.getMessage());
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    JOptionPane.showMessageDialog(null, label, "Pastry Node Error", JOptionPane.INFORMATION_MESSAGE);
        }
       return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(58, 68, 93));

        jPanel1.setBackground(new java.awt.Color(94, 87, 104));

        jLabel2.setBackground(new java.awt.Color(94, 87, 104));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/logos/resized-logo.png"))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(94, 87, 104));

        jLabel1.setBackground(new java.awt.Color(94, 87, 104));
        jLabel1.setFont(new java.awt.Font("Loma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(225, 253, 231));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("alias");

        jLabel3.setBackground(new java.awt.Color(94, 87, 104));
        jLabel3.setFont(new java.awt.Font("Loma", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(225, 253, 231));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("password");

        jButton1.setBackground(new java.awt.Color(94, 87, 104));
        jButton1.setFont(new java.awt.Font("Loma", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(225, 253, 231));
        jButton1.setText("get on the wire");
        jButton1.setToolTipText("");
        jButton1.setBorder(null);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(94, 87, 104));
        jLabel4.setFont(new java.awt.Font("Loma", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(204, 204, 204));
        jLabel4.setText("Reconfigure node");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(94, 87, 104));
        jLabel6.setFont(new java.awt.Font("Loma", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 204, 204));
        jLabel6.setText("Get a new identity");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(67, 207, 137));

        jPasswordField1.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        jPasswordField1.setForeground(new java.awt.Color(67, 207, 137));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(38, 38, 38))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(243, 243, 243)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(359, 359, 359)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(275, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                    .addComponent(jPasswordField1))
                .addGap(43, 43, 43)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addGap(41, 41, 41))
        );

        jLabel5.setBackground(new java.awt.Color(94, 87, 104));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/txt-mediumish.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(373, 373, 373)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addGap(30, 30, 30)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        String salt = BCrypt.gensalt();

        String alias = new String(jTextField1.getText()).toLowerCase();
        String passw = new String(jPasswordField1.getPassword()).toLowerCase();
        if (alias.length() <= 4 || alias == null || passw.length() <= 4 || passw == null) {
              label = new JLabel("<html><center>Please have your password or alias of at least 5 characters!");
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    JOptionPane.showMessageDialog(null, label, "Not valid credentials", JOptionPane.INFORMATION_MESSAGE);
            

        } else {
            String hashAlias = BCrypt.hashpw(alias, salt);
            String hashPassw = BCrypt.hashpw(passw, salt);
            //DECOM PENTRU BD
            connToDB(alias, passw, hashAlias, hashPassw);
            MintNode mn;
            try {
                mn = createMintNode(alias);
                startApp(alias, mn);
            } catch (InterruptedException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }

    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        Register r = Register.startRegister();
        r.pack();
        r.setLocationRelativeTo(null);
        r.setVisible(true);
        r.setVisible(true);

    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
         Reconfigure reconfigure = Reconfigure.getInstance();
        reconfigure.pack();
        reconfigure.setLocationRelativeTo(null);
        reconfigure.setVisible(true);
        reconfigure.setVisible(true);
        
    }//GEN-LAST:event_jLabel4MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
