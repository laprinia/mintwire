
package mintwire.utils;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import mintwire.p2pmodels.MintNode;
import mintwire.p2pmodels.messages.PeerInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import rice.p2p.commonapi.Id;
import rice.pastry.NodeHandle;


public class Utils {
    //MAKES A BUFFERED IMG ROUND 4 PROFILE PICS
    public BufferedImage makeRound(BufferedImage master) throws IOException{
     
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

    public static String insertPeriodically( String text, String insert, int period) {
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

   
    public void writeJSONfiles(String aliasPath, String alias){
        FileWriter fw = null;
        File codetemp;
        try {
            fw = new FileWriter(aliasPath + alias + "/history.json");
            JSONObject obj = new JSONObject();
            JSONArray arr = new JSONArray();
            obj.put(arr, "downloadhistory");
           
            
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
      public void setPfp(JLabel pfpLabel,String aliasPath, PeerInfo peerInfo) throws IOException {
       BufferedImage bi;
        File pfp;
        try {
            pfp = new File(aliasPath +peerInfo.getAlias() + "/pfp/pfp.png");
            bi = ImageIO.read(pfp);
            
           
        } catch (Exception ex) {
           
           pfp = new File(aliasPath +peerInfo.getAlias()+ "/pfp/pfp.png");
            bi = ImageIO.read(pfp);
        }
           BufferedImage biR =makeRound(bi);
           
            Dimension d=pfpLabel.getPreferredSize();
            
            Image resultScaled = biR.getScaledInstance(d.width-12,d.height-12, Image.SCALE_SMOOTH);

            ImageIcon ico = new ImageIcon(resultScaled);

            pfpLabel.setIcon(ico);
    }
    public void updatePeerInfo(MintNode mintNode){
       
         List<NodeHandle> handles = mintNode.getNode().getLeafSet().asList();
        HashSet<Id> uniqueHandles = new HashSet<>();
        handles.removeIf(e -> !uniqueHandles.add(e.getId()));
        mintNode.getPeerInfoApp().getPeerList().clear();
        for (NodeHandle h : handles) {
            NodeHandle lh = mintNode.getNode().getLocalHandle();
            
            mintNode.getPeerInfoApp().requestPeerInfo(h.getId(), new PeerInfo(lh, mintNode.getNode().alias, mintNode.getNode().status, false));
        
        }

    }
   
}
 