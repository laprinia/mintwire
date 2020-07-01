package mintwire.jframes;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;

import java.awt.RenderingHints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.image.BufferedImage;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Field;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import static javax.swing.BorderFactory.createEmptyBorder;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import javax.swing.JTable;
import javax.swing.SwingConstants;

import javax.swing.SwingWorker;

import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import mintwire.JDK9ClasspathLibraryInfo;

import mintwire.LangSelector;
import mintwire.PartyState;
import mintwire.RSTALanguageSupport;
import mintwire.borders.ChatBorder;
import mintwire.cache.MessageCacher;
import mintwire.chatpanels.Bubbler;
import mintwire.classes.MintFile;
import mintwire.jframes.MintwireClientGUI.FileSporeTableModel;
import mintwire.p2pmodels.MintNode;
import mintwire.p2pmodels.apps.SendPeerInfoApp;

import mintwire.p2pmodels.messages.CodeStitch;
import mintwire.p2pmodels.messages.HostTopic;
import mintwire.p2pmodels.messages.MintMessage;
import mintwire.p2pmodels.messages.PartyStitch;
import mintwire.p2pmodels.messages.PeerInfo;
import mintwire.panels.mintlynx.LynxPanel;
import mintwire.panels.peerlist.PartyPeerPanel;
import mintwire.utils.StatusChecker;

import mintwire.utils.Utils;
import org.fife.rsta.ac.LanguageSupport;
import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.rsta.ac.java.JavaLanguageSupport;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.jdesktop.swingx.util.OS;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import rice.p2p.scribe.Topic;
import rice.pastry.NodeHandle;

//PFP PRIN PEERINFO la send si connected peers trb refresh
public class MintwireClientGUI extends javax.swing.JFrame {

    public class FileSporeTableModel extends AbstractTableModel {

        String columns[] = {"Extension", "File", "Details", "Owner's Alias", "Checkbox"};

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public int getRowCount() {
            return mintNode.getSharedResourceApp().getMintFiles().size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {

            switch (columnIndex) {
                case 0:
                    return null;
                case 1:
                    return mintNode.getSharedResourceApp().getMintFiles().get(0).getFileName().getClass();
                case 2:
                    return mintNode.getSharedResourceApp().getMintFiles().get(0).getDetails().getClass();
                case 3:
                    return mintNode.getSharedResourceApp().getMintFiles().get(0).getAlias().getClass();
                case 4:
                    return Boolean.class;
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
                    return mintNode.getSharedResourceApp().getMintFiles().get(rowIndex).getFileName();
                case 2:
                    return mintNode.getSharedResourceApp().getMintFiles().get(rowIndex).getDetails();
                case 3:
                    return mintNode.getSharedResourceApp().getMintFiles().get(rowIndex).getAlias();
                case 4:
                    return mintNode.getSharedResourceApp().getMintFiles().get(rowIndex).isItSelected();
                default:
                    return 0;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 4) {
                return true;
            }
            return false;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            mintNode.getSharedResourceApp().getMintFiles().get(rowIndex).setSelected((Boolean) aValue);
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

                String ext = mintNode.getSharedResourceApp().getMintFiles().get(row).getFileName().substring(mintNode.getSharedResourceApp().getMintFiles().get(row).getFileName().indexOf("."));

                try {

                    ii = new ImageIcon(getClass().getResource("/mintwire/res/ext/" + ext.substring(1) + ".png"));

                } catch (Exception ex) {
                    ii = new ImageIcon(getClass().getResource("/mintwire/res/ext/none.png"));
                }

                c.setIcon(new ImageIcon(ii.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));

            } else if (column == 1) {
                label.setText(mintNode.getSharedResourceApp().getMintFiles().get(row).getDetails());

            } else if (column == 2) {
                label.setText(mintNode.getSharedResourceApp().getMintFiles().get(row).getAlias());
            } else if (column == 3) {

                label.setText("");
            }

            return c;
        }

    }

    //my vars
    public static PartyState partyState = PartyState.NotStarted;
    private MessageCacher cacher = new MessageCacher();
    private Box partyBox = new Box(BoxLayout.X_AXIS);
    private Box lynxBox = new Box(BoxLayout.Y_AXIS);
    private Color availableColor = new Color(168, 255, 104);
    private Color awayColor = new Color(255, 190, 104);
    private Color doNotDisturbColor = new Color(255, 104, 168);
    private Color invisibleColor = new Color(104, 168, 255);
    private MintNode mintNode;
    private PeerInfo currentPeerChat;
    private SendPeerInfoApp peerInfoApp;
    private String aliasPath = System.getenv("APPDATA") + "/MINTWIRE/";

    private FileSporeTableModel sporeModel = new FileSporeTableModel();

    private Bubbler bubbler;
    private ChatBorder cB = new ChatBorder(45);
    final Color SENT = new Color(244, 101, 101);

    private JLabel infoLabel;

    final JPanel scrollablePanel = new JPanel(new GridLayout(0, 1));
    private Utils utils = new Utils();
    private final boolean isLinux = OS.isLinux();
    private String alias;
    private String password;
    private RSyntaxTextArea partyTextArea = new RSyntaxTextArea(20, 16);
    private RSyntaxTextArea requestTextArea = new RSyntaxTextArea(20, 60);
    private RSyntaxTextArea sendTextArea = new RSyntaxTextArea(20, 60);
    private LanguageSupport partyLang;
    private LanguageSupport requestLang;
    private LanguageSupport sendLang;
    private String filePath;
    ;
    private final String langPre = "SyntaxConstants.SYNTAX_STYLE_";
    private static JMenu requestLanguageToggle;
    private static JMenu sendLanguageToggle;
    private static JMenu partyLanguageToggle;
    private Border emptyBorder = BorderFactory.createLineBorder(new Color(45, 48, 56));

    //end of my vars
    public MintwireClientGUI() {

        initComponents();
    }

    public MintwireClientGUI(String password, MintNode mintNode) {

        this.alias = mintNode.getNode().alias;
        this.mintNode = mintNode;

        mintNode.getSharedResourceApp().setMintNode(mintNode);
        mintNode.getMessagingApp().setScrollablePanel(scrollablePanel);
        mintNode.getMessagingApp().setCacher(cacher);
        peerInfoApp = mintNode.getPeerInfoApp();
        mintNode.getFileSporeApp().setCurrentAlias(alias);
        this.password = password;
        System.out.println(mintNode.getNode().getId());

        if (isLinux) {
            aliasPath = System.getProperty("user.home") + "/MINTWIRE/";
        }

        initComponents();
        initLayered();
        initRSyntax(requestTextArea, RequestSPanel, requestLanguageToggle, "request", requestLang);
        initRSyntax(sendTextArea, SendSPanel, sendLanguageToggle, "send", sendLang);
        initRSyntax(partyTextArea, PartyPanel, partyLanguageToggle, "party", partyLang);

        setTabbedDesign();
        setStitchLabelOn();

        initFileStructure(alias);

        setPfp();

    }
    //FUNCTIONS THAT REQUIRE P2P

    //LAYEREDPANE INITS
    public void initLayered() {
        CodeStitchPanel.setVisible(true);
        CodeStitchPartyPanel.setVisible(false);
        MintLynxPanel.setVisible(false);
        FileSporePanel.setVisible(false);
    }

    public void initFileSpore() {

        mintNode.getSharedResourceApp().getMintFiles().clear();
        SwingWorker sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {

                mintNode.getSharedResourceApp().requestResources();
                Thread.sleep(300);
                sporeModel.fireTableDataChanged();
                return null;
            }

        };
        sw.execute();

    }

    public void initMintLynx() {
        ArrayList<LynxPanel> lynxPanels = new ArrayList<>();

        utils.updatePeerInfo(mintNode);

        SwingWorker sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                Thread.sleep(60);
                utils.updatePeerPfp(mintNode, mintNode.getPeerInfoApp().getPeerList());
                Thread.sleep(60);
                paintMintLynx(lynxPanels);
                return null;
            }

        };

        sw.execute();

    }

    public void initCodeStitch() {

    }

    public void paintMintLynx(ArrayList<LynxPanel> lynxPanels) {
        if (lynxBox.getComponentCount() > 0) {
            lynxBox.removeAll();
            lynxBox.repaint();
            lynxPanels.clear();
            lynxScroll.revalidate();
        }
        for (PeerInfo p : mintNode.getPeerInfoApp().getPeerList()) {
            if (StatusChecker.check(p.getStatus())) {
                LynxPanel lynx = new LynxPanel(p);
                lynx.setPreferredSize(new Dimension(376, 92));
                lynxBox.add(lynx);
                lynxPanels.add(lynx);
                lynxBox.revalidate();
                lynxScroll.repaint();
            }
        }

        setMintLynxListeners(lynxPanels);

    }
    //END OF LAYERED PANE INITS

    //MY METHODS
    private void setMintLynxListeners(ArrayList<LynxPanel> lynxPanels) {

        if (lynxPanels == null || lynxPanels.size() < 1) {
            return;
        }
        for (LynxPanel l : lynxPanels) {
            l.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    scrollablePanel.removeAll();
                    scrollablePanel.repaint();
                    chatScrollPane.repaint();
                    try {
                        ArrayList<MintMessage> mm = cacher.getMessagesById(currentPeerChat.getNodeHandle().getId());
                        utils.paintCachedMessages(mintNode, mm, scrollablePanel);
                    } catch (NullPointerException ex) {
                        ex.getCause();
                    }
                    l.setCurrentPanel();
                    for (LynxPanel lx : lynxPanels) {
                        if (!lx.equals(l)) {
                            l.setNotCurrentPanel();
                        }
                    }
                    currentPeerChat = l.getPeerInfo();
                    mintNode.getMessagingApp().setCurrentId(l.getPeerInfo().getNodeHandle().getId());

                    currentPfpPanel.repaint();
                    currentPfpPanel.revalidate();
                    currentAliasLabel.setText(l.getPeerInfo().getAlias());

                    try {

                        utils.setPfp(currentPfpLabel, aliasPath, l.getPeerInfo(), true);
                    } catch (IOException ex) {
                        Logger.getLogger(MintwireClientGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });
        }
    }

    private void initFileStructure(String alias) {
        File f1 = new File(aliasPath);
        if (!(f1.exists())) {
            f1.mkdir();
        }

        File f2 = new File(aliasPath + alias);
        if (!(f2.exists())) {
            f2.mkdir();
            //CREATE PFP FOLDER
            File f3 = new File(aliasPath + alias + "/pfp");
            f3.mkdir();

            try {
                BufferedImage bi = ImageIO.read(getClass().getResourceAsStream("/mintwire/res/pngs/profilepic.png"));

                File outputF = new File(aliasPath + alias + "/pfp/pfp.png");
                ImageIO.write(bi, "PNG", outputF);

            } catch (Exception ex) {
                System.out.println("err in creare fold:" + ex.getMessage());
            }

        }
        File shared = new File(mintNode.getSharedPath());
        if (!(shared.exists())) {
            shared.mkdir();
        }
        
        JSONArray ja = new JSONArray();
        File historyFile = new File(aliasPath + alias + "/" + "downloadhistory.json");
        if (!(historyFile.exists())) {
            FileWriter fw;
            try {
                fw = new FileWriter(historyFile.getAbsolutePath());
                fw.write(ja.toJSONString());
                fw.close();
            } catch (IOException ex) {
                System.err.println("JSON err: " + ex);
            }

        }

        Path path = Paths.get(aliasPath + alias);
        try {
            Files.setAttribute(path, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

    }

    private void setPfp() {
        File pfp = new File(aliasPath + alias + "/pfp/pfp.png");
        try {
            BufferedImage bi = ImageIO.read(pfp);

            BufferedImage biR = utils.makeRound(bi);
            Image resultScaled = biR.getScaledInstance(pfpLabel.getWidth(), pfpLabel.getHeight(), Image.SCALE_SMOOTH);

            ImageIcon ico = new ImageIcon(resultScaled);

            pfpLabel.setIcon(ico);
        } catch (Exception ex) {
            infoLabel = new JLabel("<html><center>" + ex.getMessage());
            infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JOptionPane.showMessageDialog(null, infoLabel, "Cannot load profile picture", JOptionPane.INFORMATION_MESSAGE);

        }

    }

    private void configSbars(RTextScrollPane sp, JPanel panel) {
        //config scrollbar color
        JPanel p = new JPanel();
        JPanel r = new JPanel();
        r.setBackground(Color.gray);
        p.setBackground(new Color(43, 43, 43));
        sp.setCorner(RTextScrollPane.LOWER_LEFT_CORNER, p);
        sp.setCorner(RTextScrollPane.LOWER_RIGHT_CORNER, p);
        sp.setCorner(RTextScrollPane.LOWER_LEADING_CORNER, p);

        sp.getVerticalScrollBar().setBackground(Color.GRAY);

        sp.getHorizontalScrollBar().setBackground(Color.GRAY);
        sp.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override

            protected void configureScrollBarColors() {
                this.thumbColor = new Color(167, 150, 146);
                this.trackColor = new Color(43, 43, 43);

            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
        });

        sp.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
            @Override

            protected void configureScrollBarColors() {
                this.thumbColor = new Color(167, 150, 146);
                this.trackColor = new Color(43, 43, 43);

            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }

        });

        panel.add(sp);

    }

    public void setTabbedDesign() {

        UIManager.put("TabbedPane.borderHightlightColor", new ColorUIResource(Color.GRAY));
        UIManager.put("TabbedPane.darkShadow", new ColorUIResource(Color.GRAY));
        UIManager.put("TabbedPane.contentAreaColor", Color.GRAY);
        UIManager.put("TabbedPane.highlight", Color.GRAY);

        UIManager.put("TabbedPane.selectedForeground", new Color(52, 203, 139));
    }

    public void setStitchLabelOn() {

        setTitle("Mintwire Code Stitch");
    }

    public void initRSyntax(RSyntaxTextArea textArea, JPanel panel, JMenu languageToggle, String place, LanguageSupport languageSupport) {

        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        ActionListener actionListener;

        RTextScrollPane sp = new RTextScrollPane(textArea);

        //configure scrollbars for rtextscrollpane
        configSbars(sp, panel);
        //configure language support 
        LanguageSupportFactory lsf = LanguageSupportFactory.get();

        lsf.register(textArea);
        languageSupport = lsf.getSupportFor(SyntaxConstants.SYNTAX_STYLE_JAVA);
        JavaLanguageSupport javaSupport = (JavaLanguageSupport) languageSupport;
        try {
            javaSupport.getJarManager().addClassFileSource(new JDK9ClasspathLibraryInfo());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

       
        //popup custom
        JPopupMenu syntaxMenu = textArea.getPopupMenu();

        syntaxMenu.addSeparator();
        languageToggle = new JMenu("Change Language...");
        languageToggle.setBackground(new Color(43, 43, 43));
        languageToggle.setForeground(new Color(238, 205, 127));
        Field[] languages = SyntaxConstants.class.getFields();
        String separ = "public static final java.lang.String org.fife.ui.rsyntaxtextarea.SyntaxConstants.SYNTAX_STYLE_";
        //action listener
        if ("request".equals(place)) {
            actionListener = new RequestMenuActionListener();
        } else if ("send".equals(place)) {
            actionListener = new SendMenuActionListener();
        } else {
            actionListener = new PartyMenuActionListener();
        }
        String vect = "";
        for (Field language : languages) {
            String name = language.toString();
            name = (name.split(separ))[1];
            String letter = name.substring(0, 1);

            if ((vect.indexOf(letter)) == -1) {
                vect.concat(letter);
                JMenu itm = new JMenu(letter);
                languageToggle.add(itm);
                JMenuItem subitm = new JMenuItem(name);
                itm.setOpaque(true);

                itm.setBackground(new Color(43, 43, 43));
                itm.setForeground(new Color(238, 205, 127));
                subitm.setBackground(new Color(43, 43, 43));
                subitm.setForeground(new Color(238, 205, 127));
                itm.add(subitm);
                subitm.addActionListener(actionListener);
                vect += letter;

            } else {
                JMenuItem subitm = new JMenuItem(name);
                subitm.setOpaque(true);

                subitm.setBackground(new Color(43, 43, 43));
                subitm.setForeground(new Color(238, 205, 12));
                JMenu itm = (JMenu) languageToggle.getMenuComponent(vect.indexOf(letter));
                itm.add(subitm);
                subitm.addActionListener(actionListener);
            }

        }

        syntaxMenu.add(languageToggle);
        //action listen

        InputStream in = getClass().getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/dark.xml");
        try {
            Theme theme = Theme.load(in);
            theme.apply(textArea);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        textArea.setFont(new Font("Monospace", Font.BOLD, 20));

    }

    //ACTION LISTENERS FOR MENUS
    class PartyMenuActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            LangSelector lang = new LangSelector();
            String context = lang.select(e.getActionCommand());

            partyTextArea.setSyntaxEditingStyle(context);
            RSTALanguageSupport.getLanguageSupport(partyTextArea);
            

        }
    }

    class SendMenuActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            LangSelector lang = new LangSelector();
            String context = lang.select(e.getActionCommand());

            sendTextArea.setSyntaxEditingStyle(context);
            RSTALanguageSupport.getLanguageSupport(sendTextArea);

        }
    }

    class RequestMenuActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            LangSelector lang = new LangSelector();
            String context = lang.select(e.getActionCommand());

            requestTextArea.setSyntaxEditingStyle(context);
            RSTALanguageSupport.getLanguageSupport(requestTextArea);

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        CStitchLabel = new javax.swing.JLabel();
        CStitchPartyLabel = new javax.swing.JLabel();
        MintLynxLabel = new javax.swing.JLabel();
        FileSporeLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        MintRequestLabel = new javax.swing.JLabel();
        FileHaulLabel = new javax.swing.JLabel();
        PreferencesLabel = new javax.swing.JLabel();
        IdentityLabel = new javax.swing.JLabel();
        pfpLabel = new javax.swing.JLabel();
        statusPanel = new javax.swing.JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Dimension arcs = new Dimension(15,15);
                int width = getWidth();
                int height = getHeight();
                Graphics2D graphics = (Graphics2D) g;
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                //graphics.setStroke(new BasicStroke(1));
                switch(mintNode.getNode().status){
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
        };
        jLabel6 = new javax.swing.JLabel();
        IdentityLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        MainLayeredPane = new javax.swing.JLayeredPane();
        CodeStitchPanel = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        TabbedPane = new javax.swing.JTabbedPane();
        RequestSPanel = new javax.swing.JPanel();
        SendSPanel = new javax.swing.JPanel();
        sendButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        CodeStitchPartyPanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        partyScroll = new javax.swing.JScrollPane(partyBox);
        PartyPanel = new javax.swing.JPanel(new BorderLayout());
        leaveSessionButton = new javax.swing.JButton();
        joinSessionButton = new javax.swing.JButton();
        startSessionButton = new javax.swing.JButton();
        saveSessionButton = new javax.swing.JButton();
        MintLynxPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        currentAliasLabel = new javax.swing.JLabel();
        currentPfpPanel = new javax.swing.JPanel()
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
                if(currentPeerChat==null){
                    setForeground(new Color(45,48,55));
                }else if("available".equals(currentPeerChat.getStatus()))
                {
                    setForeground(availableColor);
                }else if("away".equals(currentPeerChat.getStatus()))
                {
                    setForeground(awayColor);
                }else if("donotdisturb".equals(currentPeerChat.getStatus()))
                {
                    setForeground(doNotDisturbColor);
                }else setForeground(Color.YELLOW);

                graphics.setColor(getBackground());
                graphics.fillOval(0, 0, width-1, height-1);
                graphics.setColor(getForeground());
                graphics.drawOval(0, 0, width-1, height-1);
            }
        }
        ;
        currentPfpLabel = new javax.swing.JLabel()

        ;
        chatScrollPane = new javax.swing.JScrollPane(scrollablePanel);
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        chatTextArea = new javax.swing.JTextArea();
        sendTextLabel = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        searchPeerTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        lynxScroll = new javax.swing.JScrollPane(lynxBox);
        FileSporePanel = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        sporeText = new javax.swing.JTextField();
        sporeScroll = new javax.swing.JScrollPane();
        sporeTable = new javax.swing.JTable();
        sporeSearch = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(94, 87, 104));

        jPanel1.setBackground(new java.awt.Color(59, 51, 64));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(49, 46, 54));

        jLabel2.setBackground(new java.awt.Color(49, 46, 54));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/separator.png"))); // NOI18N
        jLabel2.setToolTipText("");
        jLabel2.setOpaque(true);

        CStitchLabel.setBackground(new java.awt.Color(49, 46, 54));
        CStitchLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        CStitchLabel.setForeground(new java.awt.Color(255, 204, 204));
        CStitchLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CStitchLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/codestitch.png"))); // NOI18N
        CStitchLabel.setText("Code Stitch");
        CStitchLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        CStitchLabel.setOpaque(true);
        CStitchLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        CStitchLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CStitchLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CStitchLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CStitchLabelMouseExited(evt);
            }
        });

        CStitchPartyLabel.setBackground(new java.awt.Color(49, 46, 54));
        CStitchPartyLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        CStitchPartyLabel.setForeground(new java.awt.Color(255, 204, 204));
        CStitchPartyLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/codestitch-party.png"))); // NOI18N
        CStitchPartyLabel.setText("Code Stitch Party");
        CStitchPartyLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        CStitchPartyLabel.setOpaque(true);
        CStitchPartyLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        CStitchPartyLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CStitchPartyLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CStitchPartyLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CStitchPartyLabelMouseExited(evt);
            }
        });

        MintLynxLabel.setBackground(new java.awt.Color(49, 46, 54));
        MintLynxLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        MintLynxLabel.setForeground(new java.awt.Color(255, 204, 204));
        MintLynxLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        MintLynxLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/mintlynx.png"))); // NOI18N
        MintLynxLabel.setText("Mint Lynx");
        MintLynxLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        MintLynxLabel.setOpaque(true);
        MintLynxLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        MintLynxLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MintLynxLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                MintLynxLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                MintLynxLabelMouseExited(evt);
            }
        });

        FileSporeLabel.setBackground(new java.awt.Color(49, 46, 54));
        FileSporeLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        FileSporeLabel.setForeground(new java.awt.Color(255, 204, 204));
        FileSporeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        FileSporeLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/filespore.png"))); // NOI18N
        FileSporeLabel.setText("File Spore");
        FileSporeLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        FileSporeLabel.setOpaque(true);
        FileSporeLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        FileSporeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FileSporeLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                FileSporeLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                FileSporeLabelMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(CStitchLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(163, 163, 163)
                .addComponent(CStitchPartyLabel)
                .addGap(178, 178, 178)
                .addComponent(MintLynxLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(166, 166, 166)
                .addComponent(FileSporeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(97, Short.MAX_VALUE))
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MintLynxLabel)
                    .addComponent(CStitchPartyLabel)
                    .addComponent(FileSporeLabel)
                    .addComponent(CStitchLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        CStitchLabel.getAccessibleContext().setAccessibleName("");

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, -10, 1330, 150));

        jPanel2.setBackground(new java.awt.Color(43, 43, 43));

        jLabel1.setBackground(new java.awt.Color(43, 43, 43));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/logos/logo-1-biggest.png")).getImage().getScaledInstance(112, 112, Image.SCALE_SMOOTH)));
        jLabel1.setOpaque(true);
        jLabel1.setPreferredSize(new java.awt.Dimension(112, 112));

        MintRequestLabel.setBackground(new java.awt.Color(43, 43, 43));
        MintRequestLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        MintRequestLabel.setForeground(new java.awt.Color(167, 255, 185));
        MintRequestLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        MintRequestLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/questionwhite.png"))); // NOI18N
        MintRequestLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        MintRequestLabel.setOpaque(true);
        MintRequestLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        MintRequestLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MintRequestLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                MintRequestLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                MintRequestLabelMouseExited(evt);
            }
        });

        FileHaulLabel.setBackground(new java.awt.Color(43, 43, 43));
        FileHaulLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        FileHaulLabel.setForeground(new java.awt.Color(167, 255, 185));
        FileHaulLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        FileHaulLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/folderwhite.png"))); // NOI18N
        FileHaulLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        FileHaulLabel.setOpaque(true);
        FileHaulLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        FileHaulLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FileHaulLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                FileHaulLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                FileHaulLabelMouseExited(evt);
            }
        });

        PreferencesLabel.setBackground(new java.awt.Color(43, 43, 43));
        PreferencesLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        PreferencesLabel.setForeground(new java.awt.Color(167, 255, 185));
        PreferencesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PreferencesLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/settingswhite.png"))); // NOI18N
        PreferencesLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        PreferencesLabel.setOpaque(true);
        PreferencesLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        PreferencesLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PreferencesLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PreferencesLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PreferencesLabelMouseExited(evt);
            }
        });

        IdentityLabel.setBackground(new java.awt.Color(43, 43, 43));
        IdentityLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        IdentityLabel.setForeground(new java.awt.Color(167, 255, 185));
        IdentityLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        IdentityLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/userwhite.png"))); // NOI18N
        IdentityLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        IdentityLabel.setOpaque(true);
        IdentityLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        IdentityLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IdentityLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                IdentityLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                IdentityLabelMouseExited(evt);
            }
        });

        pfpLabel.setBackground(new java.awt.Color(43, 43, 43));
        pfpLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        statusPanel.setBackground(new java.awt.Color(43, 43, 43));
        statusPanel.setForeground(new java.awt.Color(67, 207, 137));
        statusPanel.setPreferredSize(new java.awt.Dimension(60, 60));

        jLabel6.setBackground(new java.awt.Color(43, 43, 43));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        IdentityLabel1.setBackground(new java.awt.Color(43, 43, 43));
        IdentityLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        IdentityLabel1.setForeground(new java.awt.Color(167, 255, 185));
        IdentityLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        IdentityLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/connectedpeers.png")));
        IdentityLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        IdentityLabel1.setOpaque(true);
        IdentityLabel1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        IdentityLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IdentityLabel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                IdentityLabel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                IdentityLabel1MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MintRequestLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(FileHaulLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PreferencesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(IdentityLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(IdentityLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addComponent(pfpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MintRequestLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FileHaulLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PreferencesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(IdentityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(IdentityLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(pfpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 800));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/codestitch-party.png"))); // NOI18N
        jLabel8.setText("jLabel5");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        MainLayeredPane.setBackground(new java.awt.Color(59, 51, 64));

        CodeStitchPanel.setBackground(new java.awt.Color(59, 51, 64));

        TabbedPane.setBackground(new java.awt.Color(51, 51, 51));
        TabbedPane.setForeground(new java.awt.Color(204, 204, 255));
        TabbedPane.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        TabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                TabbedPaneStateChanged(evt);
            }
        });

        RequestSPanel.setBackground(new java.awt.Color(51, 51, 51));
        RequestSPanel.setForeground(new java.awt.Color(51, 255, 0));
        RequestSPanel.setLayout(new java.awt.BorderLayout());
        TabbedPane.addTab("Request a stitch", RequestSPanel);

        SendSPanel.setBackground(new java.awt.Color(51, 51, 51));
        SendSPanel.setForeground(new java.awt.Color(51, 255, 51));
        SendSPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SendSPanelMouseClicked(evt);
            }
        });
        SendSPanel.setLayout(new java.awt.BorderLayout());
        TabbedPane.addTab("Send a stitch", SendSPanel);

        sendButton.setForeground(new java.awt.Color(255, 255, 255));
        sendButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/send.png")).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        sendButton.setText("Send stitch");
        sendButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 102)));
        sendButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        sendButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        sendButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sendButtonMouseClicked(evt);
            }
        });

        saveButton.setForeground(new java.awt.Color(255, 255, 255));
        saveButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/save-file.png")).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        saveButton.setText("Save stitch");
        saveButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 102)));
        saveButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveButtonMouseClicked(evt);
            }
        });
        saveButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                saveButtonKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1083, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))
                .addGap(23, 23, 23))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(sendButton)
                .addGap(18, 18, 18)
                .addComponent(saveButton)
                .addContainerGap(391, Short.MAX_VALUE))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TabbedPane)
                .addContainerGap())
        );

        TabbedPane.getAccessibleContext().setAccessibleName("Send a Stitching");

        javax.swing.GroupLayout CodeStitchPanelLayout = new javax.swing.GroupLayout(CodeStitchPanel);
        CodeStitchPanel.setLayout(CodeStitchPanelLayout);
        CodeStitchPanelLayout.setHorizontalGroup(
            CodeStitchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CodeStitchPanelLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        CodeStitchPanelLayout.setVerticalGroup(
            CodeStitchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CodeStitchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        CodeStitchPartyPanel.setBackground(new java.awt.Color(59, 51, 64));

        partyScroll.setPreferredSize(new java.awt.Dimension(999, 136));

        PartyPanel.setPreferredSize(new java.awt.Dimension(1059, 472));
        PartyPanel.setLayout(new java.awt.BorderLayout());

        leaveSessionButton.setText("Leave Session");
        leaveSessionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                leaveSessionButtonMouseClicked(evt);
            }
        });

        joinSessionButton.setText("Join Session");
        joinSessionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                joinSessionButtonMouseClicked(evt);
            }
        });

        startSessionButton.setText("Start Session");
        startSessionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startSessionButtonMouseClicked(evt);
            }
        });

        saveSessionButton.setText("Save Session");
        saveSessionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveSessionButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(partyScroll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PartyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(leaveSessionButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                    .addComponent(joinSessionButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveSessionButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(startSessionButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(partyScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(startSessionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(joinSessionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(leaveSessionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(saveSessionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(PartyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );

        partyScroll.getViewport().setBackground(new Color(45,48,56));
        leaveSessionButton.setBorder(emptyBorder);
        leaveSessionButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/leave-ses.png")).getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH)));
        joinSessionButton.setBorder(emptyBorder);
        joinSessionButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/join-ses.png")).getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH)));
        Border emptyBorder = BorderFactory.createLineBorder(new Color(45,48,56));
        startSessionButton.setBorder(emptyBorder);
        startSessionButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/start-ses.png")).getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH)));
        saveSessionButton.setBorder(emptyBorder);
        saveSessionButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/save-file.png")).getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH)));

        javax.swing.GroupLayout CodeStitchPartyPanelLayout = new javax.swing.GroupLayout(CodeStitchPartyPanel);
        CodeStitchPartyPanel.setLayout(CodeStitchPartyPanelLayout);
        CodeStitchPartyPanelLayout.setHorizontalGroup(
            CodeStitchPartyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CodeStitchPartyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );
        CodeStitchPartyPanelLayout.setVerticalGroup(
            CodeStitchPartyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CodeStitchPartyPanelLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 15, Short.MAX_VALUE))
        );

        MintLynxPanel.setBackground(new java.awt.Color(59, 51, 64));

        jPanel3.setBackground(new java.awt.Color(45, 48, 55));

        currentAliasLabel.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        currentAliasLabel.setForeground(new java.awt.Color(97, 214, 28));

        currentPfpPanel.setPreferredSize(new java.awt.Dimension(92, 92));

        currentPfpLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentPfpLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        currentPfpLabel.setPreferredSize(new java.awt.Dimension(92, 92));
        currentPfpLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentPfpLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout currentPfpPanelLayout = new javax.swing.GroupLayout(currentPfpPanel);
        currentPfpPanel.setLayout(currentPfpPanelLayout);
        currentPfpPanelLayout.setHorizontalGroup(
            currentPfpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(currentPfpPanelLayout.createSequentialGroup()
                .addComponent(currentPfpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        currentPfpPanelLayout.setVerticalGroup(
            currentPfpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(currentPfpLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(currentPfpPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(currentAliasLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(307, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(currentPfpPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(currentAliasLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(75, 80, 92));

        jScrollPane3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        chatTextArea.setColumns(20);
        chatTextArea.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        chatTextArea.setLineWrap(true);
        chatTextArea.setRows(2);
        Border roundedBorder = new LineBorder(Color.white, 2, true);
        chatTextArea.setBorder(roundedBorder);
        chatTextArea.setCaretColor(new java.awt.Color(255, 255, 255));
        chatTextArea.setCaretPosition(0);
        jScrollPane3.setViewportView(chatTextArea);

        sendTextLabel.setBackground(new java.awt.Color(75, 80, 92));
        sendTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sendTextLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/send-message.png"))); // NOI18N
        sendTextLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sendTextLabelMouseClicked(evt);
            }
        });
        sendTextLabel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sendTextLabelKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 723, Short.MAX_VALUE)
                .addGap(41, 41, 41)
                .addComponent(sendTextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sendTextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(chatScrollPane)))
                .addContainerGap())
            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(4, 4, 4)
                .addComponent(chatScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        searchPeerTextField.setBackground(new java.awt.Color(45, 48, 55));
        searchPeerTextField.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        searchPeerTextField.setForeground(new java.awt.Color(255, 204, 204));
        searchPeerTextField.setCaretColor(new java.awt.Color(255, 204, 204));
        searchPeerTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchPeerTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchPeerTextFieldFocusLost(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/search.png"))); // NOI18N

        lynxScroll.setBackground(new java.awt.Color(45, 48, 55));
        lynxScroll.setBorder(null);
        lynxScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        lynxScroll.setPreferredSize(new java.awt.Dimension(389, 531));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(searchPeerTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(lynxScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                    .addComponent(searchPeerTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lynxScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        lynxScroll.getViewport().setBackground(new Color(45,48,55));

        javax.swing.GroupLayout MintLynxPanelLayout = new javax.swing.GroupLayout(MintLynxPanel);
        MintLynxPanel.setLayout(MintLynxPanelLayout);
        MintLynxPanelLayout.setHorizontalGroup(
            MintLynxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MintLynxPanelLayout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 38, Short.MAX_VALUE))
        );
        MintLynxPanelLayout.setVerticalGroup(
            MintLynxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        FileSporePanel.setBackground(new java.awt.Color(59, 51, 64));

        jPanel8.setBackground(new java.awt.Color(45, 48, 56));

        sporeText.setBackground(new java.awt.Color(45, 48, 56));
        sporeText.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N

        sporeScroll.setBackground(new java.awt.Color(45, 48, 56));
        sporeScroll.setBorder(null);

        sporeTable.setBackground(new java.awt.Color(59, 62, 69));
        sporeTable.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        sporeTable.setForeground(new java.awt.Color(204, 204, 204));
        sporeTable.setModel(sporeModel
        );
        sporeScroll.setViewportView(sporeTable);
        sporeTable.setRowHeight (50);
        sporeTable.getColumnModel().getColumn(0).setCellRenderer(new FileExtensionModel());

        sporeSearch.setText("Search");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(sporeText)
                        .addGap(35, 35, 35)
                        .addComponent(sporeSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(sporeScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 806, Short.MAX_VALUE))
                .addGap(32, 32, 32))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sporeText, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sporeSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(sporeScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        sporeScroll.getViewport().setBackground(new java.awt.Color(45, 48, 56));
        sporeScroll.setBorder(createEmptyBorder());

        jPanel9.setBackground(new java.awt.Color(45, 48, 56));

        jButton2.setText("Request items");
        jButton2.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/requestfile.png")).getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH)));
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        jButton1.setText("Refresh items");
        jButton1.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/refreshfile.png")).getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH)));
        jButton1.setToolTipText("");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout FileSporePanelLayout = new javax.swing.GroupLayout(FileSporePanel);
        FileSporePanel.setLayout(FileSporePanelLayout);
        FileSporePanelLayout.setHorizontalGroup(
            FileSporePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FileSporePanelLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );
        FileSporePanelLayout.setVerticalGroup(
            FileSporePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FileSporePanelLayout.createSequentialGroup()
                .addGroup(FileSporePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        MainLayeredPane.setLayer(CodeStitchPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainLayeredPane.setLayer(CodeStitchPartyPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainLayeredPane.setLayer(MintLynxPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainLayeredPane.setLayer(FileSporePanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout MainLayeredPaneLayout = new javax.swing.GroupLayout(MainLayeredPane);
        MainLayeredPane.setLayout(MainLayeredPaneLayout);
        MainLayeredPaneLayout.setHorizontalGroup(
            MainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(CodeStitchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(MainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(MainLayeredPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(CodeStitchPartyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(MainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(MainLayeredPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(MintLynxPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(MainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(MainLayeredPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(FileSporePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        MainLayeredPaneLayout.setVerticalGroup(
            MainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(CodeStitchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(MainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(MainLayeredPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(CodeStitchPartyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(MainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(MainLayeredPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(MintLynxPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(MainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(MainLayeredPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(FileSporePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jPanel1.add(MainLayeredPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 140, 1330, 660));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void CStitchPartyLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CStitchPartyLabelMouseEntered
        CStitchPartyLabel.setForeground(new Color(223, 102, 105));
        CStitchPartyLabel.setBackground(new Color(81, 75, 88));
        setTitle("Mintwire Code Stitch Party");
    }//GEN-LAST:event_CStitchPartyLabelMouseEntered

    private void CStitchPartyLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CStitchPartyLabelMouseExited
        CStitchPartyLabel.setForeground(new Color(242, 194, 195));
        CStitchPartyLabel.setBackground(new Color(49, 46, 54));

    }//GEN-LAST:event_CStitchPartyLabelMouseExited

    private void CStitchLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CStitchLabelMouseEntered
        CStitchLabel.setForeground(new Color(223, 102, 105));
        CStitchLabel.setBackground(new Color(81, 75, 88));
        setTitle("Mintwire Code Stitch");
    }//GEN-LAST:event_CStitchLabelMouseEntered

    private void CStitchLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CStitchLabelMouseExited
        CStitchLabel.setForeground(new Color(242, 194, 195));
        CStitchLabel.setBackground(new Color(49, 46, 54));
    }//GEN-LAST:event_CStitchLabelMouseExited

    private void MintLynxLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MintLynxLabelMouseEntered
        MintLynxLabel.setForeground(new Color(223, 102, 105));
        MintLynxLabel.setBackground(new Color(81, 75, 88));
        setTitle("Mintwire Mint Lynx");
    }//GEN-LAST:event_MintLynxLabelMouseEntered

    private void MintLynxLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MintLynxLabelMouseExited
        MintLynxLabel.setForeground(new Color(242, 194, 195));
        MintLynxLabel.setBackground(new Color(49, 46, 54));
    }//GEN-LAST:event_MintLynxLabelMouseExited

    private void FileSporeLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FileSporeLabelMouseEntered
        FileSporeLabel.setForeground(new Color(223, 102, 105));
        FileSporeLabel.setBackground(new Color(81, 75, 88));
        setTitle("Mintwire File Spore");
    }//GEN-LAST:event_FileSporeLabelMouseEntered

    private void FileSporeLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FileSporeLabelMouseExited
        FileSporeLabel.setForeground(new Color(242, 194, 195));
        FileSporeLabel.setBackground(new Color(49, 46, 54));
    }//GEN-LAST:event_FileSporeLabelMouseExited

    private void MintRequestLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MintRequestLabelMouseEntered
        MintRequestLabel.setText("Mint Request");

    }//GEN-LAST:event_MintRequestLabelMouseEntered

    private void MintRequestLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MintRequestLabelMouseExited
        MintRequestLabel.setText("");
        MintRequestLabel.setBackground(new Color(43, 43, 43));
    }//GEN-LAST:event_MintRequestLabelMouseExited

    private void FileHaulLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FileHaulLabelMouseEntered
        FileHaulLabel.setText("File Haul");
        
    }//GEN-LAST:event_FileHaulLabelMouseEntered

    private void FileHaulLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FileHaulLabelMouseExited
        FileHaulLabel.setText("");
        FileHaulLabel.setBackground(new Color(43, 43, 43));
    }//GEN-LAST:event_FileHaulLabelMouseExited

    private void PreferencesLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PreferencesLabelMouseEntered
        PreferencesLabel.setText("Preferences");
    }//GEN-LAST:event_PreferencesLabelMouseEntered

    private void PreferencesLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PreferencesLabelMouseExited
        PreferencesLabel.setText("");
        PreferencesLabel.setBackground(new Color(43, 43, 43));
    }//GEN-LAST:event_PreferencesLabelMouseExited

    private void IdentityLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IdentityLabelMouseEntered
        IdentityLabel.setText("Identity");
    }//GEN-LAST:event_IdentityLabelMouseEntered

    private void IdentityLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IdentityLabelMouseExited
        IdentityLabel.setText("");
        IdentityLabel.setBackground(new Color(43, 43, 43));
    }//GEN-LAST:event_IdentityLabelMouseExited

    private void MintRequestLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MintRequestLabelMouseClicked
        MintRequestLabel.setBackground(new Color(53, 53, 53));
        //START MINTREQUESTS

        MintRequests mr = MintRequests.getInstance(requestTextArea, mintNode);
        mr.pack();
        mr.setLocationRelativeTo(null);
        mr.setVisible(true);
        mr.checkForStitches();
    }//GEN-LAST:event_MintRequestLabelMouseClicked

    private void FileHaulLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FileHaulLabelMouseClicked
        FileHaulLabel.setBackground(new Color(53, 53, 53));
        FileHaul fh = FileHaul.getInstance(mintNode);
        
        fh.pack();
        fh.setLocationRelativeTo(null);
        fh.setVisible(true);
        fh.setVisible(true);
    }//GEN-LAST:event_FileHaulLabelMouseClicked

    private void PreferencesLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PreferencesLabelMouseClicked
        PreferencesLabel.setBackground(new Color(53, 53, 53));
        //START PREFERENCES
        Preferences pr = Preferences.startPreferences(alias);
        pr.pack();
        pr.setLocationRelativeTo(null);
        pr.setVisible(true);
    }//GEN-LAST:event_PreferencesLabelMouseClicked

    private void IdentityLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IdentityLabelMouseClicked
        IdentityLabel.setBackground(new Color(53, 53, 53));
        //START IDENTITY
        Identity identity = Identity.getIndentityInstance(mintNode);
        identity.pack();
        identity.setLocationRelativeTo(null);
        identity.setVisible(true);
        SwingWorker sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                while (identity.getInstance() != null) {
                    Thread.sleep(3000);
                }

                setPfp();

                statusPanel.repaint();
                return null;
            }

        };

        sw.execute();


    }//GEN-LAST:event_IdentityLabelMouseClicked

    private void CStitchPartyLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CStitchPartyLabelMouseClicked
        CodeStitchPanel.setVisible(false);
        CodeStitchPartyPanel.setVisible(true);
        MintLynxPanel.setVisible(false);
        FileSporePanel.setVisible(false);
        initCodeStitch();
    }//GEN-LAST:event_CStitchPartyLabelMouseClicked

    private void CStitchLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CStitchLabelMouseClicked
        CodeStitchPanel.setVisible(true);
        CodeStitchPartyPanel.setVisible(false);
        MintLynxPanel.setVisible(false);
        FileSporePanel.setVisible(false);
    }//GEN-LAST:event_CStitchLabelMouseClicked

    private void MintLynxLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MintLynxLabelMouseClicked
        CodeStitchPanel.setVisible(false);
        CodeStitchPartyPanel.setVisible(false);
        MintLynxPanel.setVisible(true);
        FileSporePanel.setVisible(false);
        initMintLynx();
    }//GEN-LAST:event_MintLynxLabelMouseClicked

    private void FileSporeLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FileSporeLabelMouseClicked
        CodeStitchPanel.setVisible(false);
        CodeStitchPartyPanel.setVisible(false);
        MintLynxPanel.setVisible(false);
        FileSporePanel.setVisible(true);
        initFileSpore();

    }//GEN-LAST:event_FileSporeLabelMouseClicked

    private void SendSPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SendSPanelMouseClicked

    }//GEN-LAST:event_SendSPanelMouseClicked

    private void TabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabbedPaneStateChanged


    }//GEN-LAST:event_TabbedPaneStateChanged

    private void sendTextLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendTextLabelMouseClicked

        if (currentPeerChat != null) {
            boolean isSendable = true;
            if (currentPeerChat.getStatus().equals("donotdisturb")) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(null, "Your peer " + currentPeerChat.getAlias() + " doesn't want to be disturbed. Do you wish to continue?", "Do not Disturb Status", dialogButton);
                if (dialogResult == JOptionPane.NO_OPTION) {
                    isSendable = false;
                }
            }
            if (isSendable) {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm 'on' EE dd-MM-yyyy");
                Date date = new Date(System.currentTimeMillis());
                if (!(chatTextArea.getText().equals(""))) {
                    bubbler = new Bubbler(chatTextArea.getText(), SENT);
                    bubbler.paintRightBubble(scrollablePanel, formatter.format(date));

                }

                MintMessage msg = new MintMessage(chatTextArea.getText(), formatter.format(date), mintNode.getNode().getId());
                cacher.cache(currentPeerChat.getNodeHandle().getId(), msg);

                mintNode.getMessagingApp().routeMessage(currentPeerChat.getNodeHandle(), msg);
                chatTextArea.setText("");
            }

        } else {
            infoLabel = new JLabel("<html><center>No correspondent peer selected!");
            infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JOptionPane.showMessageDialog(null, infoLabel, "Mint Lynx", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_sendTextLabelMouseClicked

    private void sendTextLabelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sendTextLabelKeyPressed

    }//GEN-LAST:event_sendTextLabelKeyPressed

    private void searchPeerTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchPeerTextFieldFocusGained
        searchPeerTextField.setText("");
    }//GEN-LAST:event_searchPeerTextFieldFocusGained

    private void searchPeerTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchPeerTextFieldFocusLost
        searchPeerTextField.setText("Find someone...");
    }//GEN-LAST:event_searchPeerTextFieldFocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        ArrayList<NodeHandle> nhs = new ArrayList<>();
        System.err.println("MINTFILES SELECTION");

        for (MintFile file : mintNode.getSharedResourceApp().getMintFiles()) {
            if (file.isItSelected() && (!nhs.contains(file.getHandle()))) {

                nhs.add(file.getHandle());
            }
        }
        for (NodeHandle nh : nhs) {
            MintFile[] files = mintNode.getSharedResourceApp().getMintFiles().stream().filter(e -> e.getHandle().equals(nh) && e.isItSelected()).toArray(MintFile[]::new);

            mintNode.getFileSporeApp().requestFiles(files, nh);

        }

    }//GEN-LAST:event_jButton2MouseClicked

    private void sendButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendButtonMouseClicked
        if (sendTextArea.getText().toString().equals("")) {
            infoLabel = new JLabel("<html><center>Make sure you entered your stitch in the Send Area.");
            infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JOptionPane.showMessageDialog(null, infoLabel, "No stitch found!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            CodeStitch codeStitch = new CodeStitch(alias, "null", sendTextArea.getSyntaxEditingStyle(), sendTextArea.getText().toString());

            SendCodeStitch scs = SendCodeStitch.getInstance(codeStitch, mintNode);
            scs.setCodeStitch(codeStitch);

            scs.pack();
            scs.setLocationRelativeTo(null);
            scs.setVisible(true);
            scs.setVisible(true);
        }

    }//GEN-LAST:event_sendButtonMouseClicked

    private void saveButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saveButtonKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_saveButtonKeyPressed

    private void saveButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveButtonMouseClicked

        String[] buttons = {"Requested Stitch", "Sent Stitch"};
        int returnValue = JOptionPane.showOptionDialog(null, "Which Code Stitch do you wish to save?", "Code Stitch", JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[0]);
        if (returnValue == 0) {
            utils.saveStitch(requestTextArea, mintNode);
        } else {
            utils.saveStitch(sendTextArea, mintNode);
        }


    }//GEN-LAST:event_saveButtonMouseClicked

    private void IdentityLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IdentityLabel1MouseClicked
        ConnectedPeers cp = ConnectedPeers.getInstance(mintNode);
        cp.pack();
        cp.setLocationRelativeTo(null);
        cp.setVisible(true);

    }//GEN-LAST:event_IdentityLabel1MouseClicked

    private void IdentityLabel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IdentityLabel1MouseEntered
        IdentityLabel1.setText("Connected peers");
    }//GEN-LAST:event_IdentityLabel1MouseEntered

    private void IdentityLabel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IdentityLabel1MouseExited
        IdentityLabel1.setText("");
    }//GEN-LAST:event_IdentityLabel1MouseExited

    private void currentPfpLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_currentPfpLabelMouseClicked

    }//GEN-LAST:event_currentPfpLabelMouseClicked

    private void startSessionButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startSessionButtonMouseClicked
        if (partyState.equals(PartyState.NotStarted)) {
            partyState = PartyState.Started;
            PartyPeerPanel partyPeerPanel = new PartyPeerPanel(new PeerInfo(mintNode.getNode().getLocalHandle(), alias, "host", false));
            partyPeerPanel.setPreferredSize(new Dimension(176, 132));
            partyBox.removeAll();
            partyBox.add(partyPeerPanel);
            partyBox.revalidate();
            mintNode.getPartyClient().createTopic(mintNode.getNode().getLocalHandle(), alias);
            mintNode.getPartyClient().setPublishInfo(mintNode, partyTextArea, partyBox);
            HostTopic topic = mintNode.getPartyClient().sendCredentials();
            mintNode.getPartyClient().startPublishTask();
            //
            PassphraseViewer pv = PassphraseViewer.getInstance(topic.getPassphrase());
            pv.pack();
            pv.setLocationRelativeTo(null);
            pv.setVisible(true);
            //

        } else {
            infoLabel = new JLabel("<html><center>Already engaged in a session. Leave it before starting a new one.");
            infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JOptionPane.showMessageDialog(null, infoLabel, "Cannot start session", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_startSessionButtonMouseClicked

    private void joinSessionButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_joinSessionButtonMouseClicked
        // TODO add your handling code here:
        if (partyState.equals(PartyState.NotStarted)) {

            //
            PassphraseGiver pg = PassphraseGiver.getInstance(mintNode, partyTextArea, partyBox);
            pg.pack();
            pg.setLocationRelativeTo(null);
            pg.setVisible(true);
            //

        } else {
            infoLabel = new JLabel("<html><center>Already engaged in a session. Leave it before joining a new one.");
            infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JOptionPane.showMessageDialog(null, infoLabel, "Cannot join session", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_joinSessionButtonMouseClicked

    private void leaveSessionButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_leaveSessionButtonMouseClicked

        if (partyState.equals(PartyState.NotStarted)) {
            infoLabel = new JLabel("<html><center>No party to leave.");
            infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JOptionPane.showMessageDialog(null, infoLabel, "Cannot leave a party that hasn't started", JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (partyState.equals(PartyState.Started)) {
                mintNode.getPartyClient().destroy();
            } else {
                mintNode.getPartyClient().unsubscribe();

            }
            partyState = PartyState.NotStarted;
            partyBox.removeAll();
            partyBox.repaint();
            partyTextArea.setText("");
            partyTextArea.repaint();
        }

    }//GEN-LAST:event_leaveSessionButtonMouseClicked

    private void saveSessionButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveSessionButtonMouseClicked
        // TODO add your handling code here:
        utils.saveStitch(partyTextArea, mintNode);
    }//GEN-LAST:event_saveSessionButtonMouseClicked

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MintwireClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MintwireClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MintwireClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MintwireClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MintwireClientGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CStitchLabel;
    private javax.swing.JLabel CStitchPartyLabel;
    private javax.swing.JPanel CodeStitchPanel;
    private javax.swing.JPanel CodeStitchPartyPanel;
    private javax.swing.JLabel FileHaulLabel;
    private javax.swing.JLabel FileSporeLabel;
    private javax.swing.JPanel FileSporePanel;
    private javax.swing.JLabel IdentityLabel;
    private javax.swing.JLabel IdentityLabel1;
    private javax.swing.JLayeredPane MainLayeredPane;
    private javax.swing.JLabel MintLynxLabel;
    private javax.swing.JPanel MintLynxPanel;
    private javax.swing.JLabel MintRequestLabel;
    private javax.swing.JPanel PartyPanel;
    private javax.swing.JLabel PreferencesLabel;
    private javax.swing.JPanel RequestSPanel;
    private javax.swing.JPanel SendSPanel;
    private javax.swing.JTabbedPane TabbedPane;
    private javax.swing.JScrollPane chatScrollPane;
    private javax.swing.JTextArea chatTextArea;
    private javax.swing.JLabel currentAliasLabel;
    private javax.swing.JLabel currentPfpLabel;
    private javax.swing.JPanel currentPfpPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton joinSessionButton;
    private javax.swing.JButton leaveSessionButton;
    private javax.swing.JScrollPane lynxScroll;
    private javax.swing.JScrollPane partyScroll;
    private javax.swing.JLabel pfpLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton saveSessionButton;
    private javax.swing.JTextField searchPeerTextField;
    private javax.swing.JButton sendButton;
    private javax.swing.JLabel sendTextLabel;
    private javax.swing.JScrollPane sporeScroll;
    private javax.swing.JButton sporeSearch;
    private javax.swing.JTable sporeTable;
    private javax.swing.JTextField sporeText;
    private javax.swing.JButton startSessionButton;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
}
