package mintwire.jframes;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.BorderFactory.createEmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import mintwire.classes.HistoryFile;
import mintwire.p2pmodels.MintNode;
import org.jdesktop.swingx.util.OS;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileHaul extends javax.swing.JFrame {

    @Override
    public void setDefaultCloseOperation(int operation) {
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    

    private ArrayList<HistoryFile> historyFiles = new ArrayList<>();
    private MintNode mintNode;

    public class HistoryTableModel extends AbstractTableModel {

        String columns[] = {"Extension", "File", "Size", "Owner's Alias", "Date"};

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public int getRowCount() {
            return historyFiles.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {

            switch (columnIndex) {
                case 0:
                    return historyFiles.get(0).getFileName().getClass();
                case 1:
                    return historyFiles.get(0).getFileName().getClass();
                case 2:
                    return historyFiles.get(0).getSize().getClass();
                case 3:
                    return historyFiles.get(0).getAlias().getClass();
                case 4:
                    return historyFiles.get(0).getDate().getClass();
                default:
                    return null;
            }
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return "";
                case 1:
                    return historyFiles.get(rowIndex).getFileName();
                case 2:
                    return historyFiles.get(rowIndex).getSize();
                case 3:
                    return historyFiles.get(rowIndex).getAlias();
                case 4:
                    return historyFiles.get(rowIndex).getDate();
                default:
                    return 0;
            }
        }

    }

    public class FileExtensionModel implements TableCellRenderer {

        private TableCellRenderer defaultrend = new JTable().getDefaultRenderer(Object.class);

        JLabel label = new JLabel();
        ImageIcon ii;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            DefaultTableCellRenderer c = (DefaultTableCellRenderer) defaultrend.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (column == 0) {

                String ext = historyFiles.get(row).getFileName().substring(historyFiles.get(row).getFileName().indexOf("."));

                try {

                    ii = new ImageIcon(getClass().getResource("/mintwire/res/ext/" + ext.substring(1) + ".png"));

                } catch (Exception ex) {
                    ii = new ImageIcon(getClass().getResource("/mintwire/res/ext/none.png"));
                }

                c.setIcon(new ImageIcon(ii.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));

            } else if (column == 1) {
                label.setText(historyFiles.get(row).getSize());

            } else if (column == 2) {
                label.setText(historyFiles.get(row).getAlias());
            } else if (column == 3) {

                label.setText(historyFiles.get(row).getDate());
            }

            return c;
        }

    }
    private HistoryTableModel historyModel = new HistoryTableModel();
    private static FileHaul instance = null;

    public static FileHaul getInstance(MintNode mintNode) {
        if (instance == null) {
            instance = new FileHaul(mintNode);
        }
        return instance;
    }

    private FileHaul(MintNode mintNode) {
        
        setTitle("File Haul");
        initComponents();
        this.mintNode = mintNode;
        retrieveHistory();
    }

    private FileHaul() {
        initComponents();

    }

    private void retrieveHistory() {
        JSONParser jSONParser = new JSONParser();
        String aliasPath = System.getenv("APPDATA") + "/MINTWIRE/";
        if (OS.isLinux()) {
            aliasPath = System.getProperty("user.home") + "/MINTWIRE/";
        }
        try {

            Object object = jSONParser.parse(new FileReader(aliasPath + mintNode.getNode().alias + "/" + "downloadhistory.json"));

            JSONArray arr = (JSONArray) object;
            for (int i = 0; i < arr.size(); i++) {
                JSONObject objectInArr = (JSONObject) arr.get(i);
                historyFiles.add(new HistoryFile(objectInArr.get("fileName").toString(), objectInArr.get("size").toString(), objectInArr.get("alias").toString(), objectInArr.get("date").toString()));

            }
            System.err.println(historyFiles);

        } catch (FileNotFoundException ex) {
            System.err.println("json parse exc " + ex);
        } catch (IOException ex) {
            System.err.println("json parse exc " + ex);
        } catch (ParseException ex) {
            System.err.println("json parse exc " + ex);
        }
    }
    private void filterTable(String query){
    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(historyTable.getModel()); 
    historyTable.setRowSorter(sorter);
    sorter.setRowFilter(RowFilter.regexFilter(query));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        historyTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setBackground(new java.awt.Color(45, 48, 56));
        jScrollPane1.setBorder(null);

        historyTable.setBackground(new java.awt.Color(45, 48, 56));
        historyTable.setModel(historyModel);
        jScrollPane1.setViewportView(historyTable);
        historyTable.setRowHeight (40);
        historyTable.getColumnModel().getColumn(0).setCellRenderer(new FileExtensionModel());

        jPanel2.setBackground(new java.awt.Color(49, 46, 54));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jButton1.setText("Open Shared Folder");
        jButton1.setPreferredSize(new java.awt.Dimension(134, 78));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel1.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/down-arrow.png")).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));

        jTextField1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(97, 214, 28));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

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
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(31, 31, 31)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(93, Short.MAX_VALUE))
        );

        jScrollPane1.getViewport().setBackground(new java.awt.Color(45, 48, 56));
        jScrollPane1.setBorder(createEmptyBorder());
        jLabel2.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/historysearch.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        File file = new File(mintNode.getSharedPath());
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(FileHaul.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1MouseClicked

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        filterTable(jTextField1.getText());
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        filterTable(jTextField1.getText());
    }//GEN-LAST:event_jLabel2MouseClicked

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
            java.util.logging.Logger.getLogger(FileHaul.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FileHaul.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FileHaul.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FileHaul.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FileHaul().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable historyTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
