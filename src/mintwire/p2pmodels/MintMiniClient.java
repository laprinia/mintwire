package mintwire.p2pmodels;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import mintwire.classes.MintFile;

public class MintMiniClient implements Runnable {
    //searchrequest se mai seteaza true cand se apasa butonul
   private ArrayList<MintFile> mintFiles;
   private boolean searchRequest;
   private String ipaddr;
   private Socket miniSocket, sock2;
   private DataInputStream in;
   private DataOutputStream out;
   private Thread thread;
   private String ip;
   private long fileSize;
   private FileWriter fw;
   private String history;

    MintMiniClient(String ipaddr,boolean searchRequest,ArrayList<MintFile> mintFiles) {
        this.ipaddr = ipaddr;
        this.searchRequest=searchRequest;
        this.mintFiles=mintFiles;
        try {
        miniSocket=new Socket(this.ip,6424);
        in=new DataInputStream(miniSocket.getInputStream());
        out=new DataOutputStream(miniSocket.getOutputStream());
        out.writeBytes("minicheck\r\n");
        out.flush();
        thread=new Thread(this);
        thread.start();
        
        

            
            
        } catch (Exception ex) {
           JLabel label=new JLabel("<html><center>"+ex.getMessage());
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    JOptionPane.showMessageDialog(null, label, "Error in Mint Mini Client", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void run()  {
     
        boolean b=true;
        if(searchRequest==true){
            
        }
        
        
        
    }

}
