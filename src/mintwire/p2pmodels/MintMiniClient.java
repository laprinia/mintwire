
package mintwire.p2pmodels;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

 public class MintMiniClient implements Runnable
    {   String ipaddr;
        Socket sock,sock2;
        DataInputStream in;
        DataOutputStream out;
        MintMiniClient(String ipaddr)
        {  this.ipaddr=ipaddr;
            try{
                
            }catch(Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }
        

        @Override
        public void run() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }