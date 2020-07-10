package mintwire.utils;

import java.util.ArrayList;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import mintwire.ExtensionSelector;

import mintwire.chatpanels.Bubbler;
import mintwire.classes.HistoryFile;
import mintwire.p2pmodels.MintNode;
import mintwire.p2pmodels.messages.MintMessage;
import mintwire.p2pmodels.messages.PeerInfo;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.jdesktop.swingx.util.OS;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rice.p2p.commonapi.Id;
import rice.pastry.NodeHandle;

public class Utils {
    private JLabel label;

    //MAKES A BUFFERED IMG ROUND 4 PROFILE PICS
    public BufferedImage makeRound(BufferedImage master) throws IOException {

        int diameter = Math.min(master.getWidth(), master.getHeight());
        BufferedImage mask = new BufferedImage(master.getWidth(), master.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = mask.createGraphics();

        g2d.fillOval(0, 0, diameter - 1, diameter - 1);
        g2d.dispose();

        BufferedImage masked = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        g2d = masked.createGraphics();

        int x = (diameter - master.getWidth()) / 2;
        int y = (diameter - master.getHeight()) / 2;
        g2d.drawImage(master, x, y, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
        g2d.drawImage(mask, 0, 0, null);
        g2d.dispose();
        return masked;
    }

    public static String insertPeriodically(String text, String insert, int period) {
        StringBuilder builder = new StringBuilder(
                text.length() + insert.length() * (text.length() / period) + 1);

        int index = 0;
        String prefix = "";
        while (index < text.length()) {

            builder.append(prefix);
            prefix = insert;
            builder.append(text.substring(index,
                    Math.min(index + period, text.length())));
            index += period;
        }
        return builder.toString();
    }

    public Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    

    public void setPfp(JLabel pfpLabel, String aliasPath, PeerInfo peerInfo, boolean isPeer) throws IOException {
        BufferedImage bi;
        File pfp;
        try {
            pfp = new File(aliasPath + peerInfo.getAlias() + "/pfp/pfp.png");
            if (isPeer) {
                pfp = new File(aliasPath + peerInfo.getAlias() + "/pfp/" + peerInfo.getAlias() + ".png");
            }
            bi = ImageIO.read(pfp);
            BufferedImage biR = makeRound(bi);

            Dimension d = pfpLabel.getPreferredSize();

            Image resultScaled = biR.getScaledInstance(d.width - 12, d.height - 12, Image.SCALE_SMOOTH);

            ImageIcon ico = new ImageIcon(resultScaled);

            pfpLabel.setIcon(ico);

        } catch (Exception ex) {

        }

    }

    public List<NodeHandle> getNodeHandleList(MintNode mintNode) {
        List<NodeHandle> handles = mintNode.getNode().getLeafSet().asList();
        HashSet<Id> uniqueHandles = new HashSet<>();
        handles.removeIf(e -> !uniqueHandles.add(e.getId()));
        mintNode.getPeerInfoApp().getPeerList().clear();
        return handles;
    }

    public void updatePeerInfo(MintNode mintNode) {

        List<NodeHandle> handles = mintNode.getNode().getLeafSet().asList();
        HashSet<Id> uniqueHandles = new HashSet<>();
        handles.removeIf(e -> !uniqueHandles.add(e.getId()));
        mintNode.getPeerInfoApp().getPeerList().clear();
        for (NodeHandle h : handles) {
            NodeHandle lh = mintNode.getNode().getLocalHandle();

            mintNode.getPeerInfoApp().requestPeerInfo(h.getId(), new PeerInfo(lh, mintNode.getNode().alias, mintNode.getNode().status, false));

        }

    }

    public void updatePeerPfp(MintNode mn, ArrayList<PeerInfo> peers) {
        String appString = System.getenv("APPDATA") + "/MINTWIRE/";
        if (OS.isLinux()) {
            appString = System.getProperty("user.home") + "/MINTWIRE/";
        }
        for (PeerInfo info : peers) {
            String alias = info.getAlias();
            File aliasFolder = new File(appString + alias);
            if (!(aliasFolder.exists())) {
                aliasFolder.mkdir();

                File pfpFolder = new File(appString + alias + "/pfp");
                pfpFolder.mkdir();
            }
            String composedString = appString + alias + "/pfp/" + alias + ".png";
            System.err.println("cmp str: " + composedString);

            File file = new File(composedString);
            if (!(file.exists())) {
                
                mn.getPfpApp().requestPfp(new PeerInfo(mn.getNode().getLocalHandle(), mn.getNode().alias, alias, false), info.getNodeHandle());
            }
        }
    }

    public void paintCachedMessages(MintNode mintNode, ArrayList<MintMessage> msgs, JPanel scrollable) {

        Id currId = mintNode.getNode().getId();
        for (MintMessage m : msgs) {
            if (m.getSenderId().equals(currId)) {
                Bubbler bubbler = new Bubbler(m.getText(), new Color(244, 101, 101));
                bubbler.paintRightBubble(scrollable, m.getDateStamp());
            } else {
                Bubbler bubbler = new Bubbler(m.getText(), new Color(170, 207, 255));
                bubbler.paintLeftBubble(scrollable, m.getDateStamp());
            }

        }
    }

    public void saveStitch(RSyntaxTextArea area, MintNode mintNode) {
        String sharedPath = mintNode.getSharedPath();

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy-hh-mm");
        String language = area.getSyntaxEditingStyle();
        //TODO GET EXT
        
        String stitchPath = sharedPath + "/" + "CodeStitch-" + formatter.format(new Date(System.currentTimeMillis())) + ExtensionSelector.select(language);
        
        PrintWriter printWriter;
        try {
            printWriter = new PrintWriter(stitchPath);
            
            printWriter.println(area.getText());
            printWriter.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
         label=new JLabel("<html><center>File saved succesfully in "+mintNode.getSharedPath());
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    JOptionPane.showMessageDialog(null, label, "Code Stitch Saved", JOptionPane.INFORMATION_MESSAGE);

    }
    public void saveHistoryFile(HistoryFile file, String alias) {
        JSONParser jSONParser=new JSONParser();
        String aliasPath = System.getenv("APPDATA") + "/MINTWIRE/";
        if( OS.isLinux()){
            aliasPath = System.getProperty("user.home") + "/MINTWIRE/";
        }
        try {
            
            Object object=jSONParser.parse(new FileReader(aliasPath + alias +"/"+ "downloadhistory.json"));
            JSONArray array=(JSONArray) object;
            JSONObject currObj=new JSONObject();
           
            currObj.put("fileName", file.getFileName());
            currObj.put("size", file.getSize());
            currObj.put("alias", file.getAlias());
            currObj.put("date", file.getDate());
            array.add(currObj);
           
            FileWriter fw = new FileWriter(aliasPath + alias +"/"+ "downloadhistory.json");
            fw.write(array.toJSONString());
            fw.flush();
            fw.close();
            
        } catch (FileNotFoundException ex) {
            System.err.println("json parse exc "+ ex);
        } catch (IOException ex) {
            System.err.println("json parse exc "+ ex);
        } catch (ParseException ex) {
            System.err.println("json parse exc "+ ex);
        }
        
                
        
    }
    public void loadPreferences(File file, MintNode node){
        JSONParser jSONParser = new JSONParser();
        try {
            Object object = jSONParser.parse(new FileReader(file));
            JSONObject jsonobj = (JSONObject) object;
            String sharedPath= (String) jsonobj.get("SharedPath");
            node.setSharedPath(sharedPath);
            node.getFileSporeApp().setSharedPath(sharedPath);
            String codeComments= (String) jsonobj.get("CodeComments");
            node.setCodeComments(codeComments);
            String languageAssist= (String) jsonobj.get("LanguageAssist");
            node.setLanguageAssist(languageAssist);
            
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
