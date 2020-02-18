/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mintwire;

import mintwire.jframes.Login;

/**
 *
 * @author laprinia
 */
public class Mintwire {

   
    public static void main(String[] args) {
        
        Login login=new Login();
	  login.pack();
          login.setLocationRelativeTo(null);
          login.setVisible(true);
   
    }
    
}
