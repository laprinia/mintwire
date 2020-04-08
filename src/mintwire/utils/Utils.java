
package mintwire.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;


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

}
 