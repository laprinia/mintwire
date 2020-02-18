package mintwire;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Lavinia
 */
public class CodeStitch {

    private boolean isSending;
    private StringBuffer codeString;
    private String language;
    private String address;
    private String port;

    public CodeStitch(boolean isSending, StringBuffer codeString, String language, String address, String port) {
        this.isSending = isSending;
        this.codeString = codeString;
        this.language = language;
        this.address = address;
        this.port = port;
    }

    public boolean stitch() {
        boolean status=false;
        if (isSending) {
            try {
                Socket socket = new Socket(address, Integer.parseInt(port));
                PrintWriter output = new PrintWriter(socket.getOutputStream());
                output.print(language);
                output.print(codeString);
                output.flush();
                output.close();
                status=true;
            } catch (Exception ex) {
                ex.getMessage();
            }

        } else {
            try{
                ServerSocket socket=new ServerSocket(Integer.parseInt(port));
                Socket clSocket=socket.accept();
                BufferedReader input=new BufferedReader(new InputStreamReader(clSocket.getInputStream()));
                String recv=null;
                
                while((recv=input.readLine())!=null)
                {
                    codeString.append(recv);
                }
                input.close();
                clSocket.close();
                socket.close();
                status=true;
                
            }catch(Exception ex)
            {
                ex.getMessage();
            }

        }
        return status;
    }

}
