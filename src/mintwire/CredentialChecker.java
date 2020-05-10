
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
   boolean hasSpecial = alias.matches(".*[!@#$%^&*].*");
   boolean hasUpper=alias.matches("[A-Z ]*");
   
    if (alias.length() <= ACCOUNT_MIN_CHARS|| alias == null || passw.length() <= ACCOUNT_MIN_CHARS || passw == null) {
              label = new JLabel("<html><center>Please have your password or alias of at least 5 characters!");
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    JOptionPane.showMessageDialog(null, label, "Not valid credentials", JOptionPane.INFORMATION_MESSAGE);
            
           return false;
        } 
    else if(hasUpper||hasSpecial ){
           label = new JLabel("<html><center>Please make sure your alias contains lowercase letters and or numbers!");
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    JOptionPane.showMessageDialog(null, label, "Not valid credentials", JOptionPane.INFORMATION_MESSAGE);
            
           return false;
    }
    
    return true;
}
    
}
