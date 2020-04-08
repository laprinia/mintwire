package mintwire.p2pmodels;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MintMainClient implements Runnable {

    Socket socket;
    Thread mainThread;
    String ipaddr;
    String res;
    BufferedReader sockbf;
    BufferedReader defbf;
    PrintWriter pw;
    ArrayList<String> array;

    MintMainClient(String ipaddr, ArrayList<String> array) {

        try {
            socket = new Socket(ipaddr, 6424);
            sockbf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            defbf = new BufferedReader(new InputStreamReader(System.in));
            pw = new PrintWriter(socket.getOutputStream());
            mainThread = new Thread(this);
            mainThread.start();

        } catch (Exception ex) {
            //cant conn to srvr
            ex.getMessage();
            System.out.println("Main client error: Failed to conn to server");
        }

    }

    @Override
    public void run() {

        //SE CON LA MINISERVER REQ HANDLER
        try {
            while (true) {
                res = sockbf.readLine();
                if (res.startsWith("filenr")) {
                    array.clear();
                    StringTokenizer st = new StringTokenizer(res);
                    st.nextToken();
                    int n = Integer.parseInt(st.nextToken());
                    for (int i = 0; i < n; i++) {   //adauga la array cate chestii se trimit(tokenul dupa size)
                        array.add(sockbf.readLine());
                        //TABELA ADRESE IP E IN ARRAY POTI IN JSON
                        
                        
                    }

                }

            }

        } catch (Exception ex) {
            ex.getMessage();
            System.out.println("Main client error: Failed to conn to server req handler");

        }

    }

}
