
package mintwire.p2pmodels;

import java.net.ServerSocket;
import java.net.Socket;

   public class MintMiniServer implements Runnable
    { Thread mainThread;
      Socket sock;
      ServerSocket miniServerSocket;
        MintMiniServer()
        {
            mainThread=new Thread(this);
            mainThread.start();
            
            
        }
        

        @Override
        public void run() {
           try{
               miniServerSocket=new ServerSocket(6424);
               while(true)
               {
                   sock=miniServerSocket.accept();
                   //REQUESTHANDLER
               }

           }catch(Exception ex)
           {
               System.out.println("Mini Server Exc: ");
               ex.getMessage();
           }
        }
        
    }