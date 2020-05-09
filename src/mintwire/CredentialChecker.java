
package mintwire;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;


public class CredentialChecker {
private static JLabel label;
private static final int ACCOUNT_MIN_CHARS = 4;
public static boolean check(String alias, String passw){
    Pattern pattern = Pattern.compile("[a-z0-9 ]", Pattern.CASE_INSENSITIVE);
    
    Matcher hasPattern=pattern.matcher(alias);
   
    if (alias.length() <= ACCOUNT_MIN_CHARS|| alias == null || passw.length() <= ACCOUNT_MIN_CHARS || passw == null) {
              label = new JLabel("<html><center>Please have your password or alias of at least 5 characters!");
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    JOptionPane.showMessageDialog(null, label, "Not valid credentials", JOptionPane.INFORMATION_MESSAGE);
            
           return false;
        } 
    else if(hasPattern.find() ){
           label = new JLabel("<html><center>Please make sure your alias contains lowercase  letters and or numbers!");
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    JOptionPane.showMessageDialog(null, label, "Not valid credentials", JOptionPane.INFORMATION_MESSAGE);
            
           return false;
    }
    
    return true;
}
    
}
