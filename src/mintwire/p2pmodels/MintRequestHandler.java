package mintwire.p2pmodels;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

public class MintRequestHandler implements Runnable {

    private Thread mainThread;
    private Socket sock;
    private DataOutputStream out;
    private DataInputStream in;
    private String res;
    private BufferedReader bf;
    private String filePath = "C:\\MINTWIRE Shared";

    MintRequestHandler(Socket socket) {
        this.sock = socket;

        try {
            out = new DataOutputStream(this.sock.getOutputStream());
            in = new DataInputStream(this.sock.getInputStream());
            bf = new BufferedReader(new InputStreamReader(in));
            mainThread = new Thread(this);
            mainThread.start();
        } catch (Exception ex) {
            ex.getMessage();
            System.out.println("Request Handle Ex: ");

        }
    }

    @Override
    public void run() {

        try {
            //check
            res = bf.readLine();
            if (res.equals("minicheck")) {
                out.writeBytes("\nrunning...");
                out.flush();
            }
            String requestSignal = "";

            while (true) {
                requestSignal = bf.readLine();
                if (requestSignal == null) {
                    break;
                }
                if (requestSignal.startsWith("search")) {
                    StringTokenizer tok = new StringTokenizer(requestSignal, ",");
                    tok.nextToken();
                    String search = tok.nextToken();
                    System.out.println("Searching for: " + search);
                    //TODO filepath nu e schimbat niciunde
                    File f = new File(filePath);
                    String[] fileArray = f.list();
                    System.out.println("Files found in filePath " + fileArray.length);
                    int n = 0;
                    for (int i = 0; i < fileArray.length; i++) {
                        if (fileArray[i].indexOf(search) != -1) {
                            n++;
                        }
                        out.writeBytes("size " + n + "\r\n");
                        out.flush();

                        if (n > 0) {
                            for (int j = 0; j < fileArray.length; j++) {
                                if (fileArray[j].indexOf(search) != -1) {
                                    out.writeBytes(fileArray[j] + "\r\n");
                                    out.flush();
                                    File f2 = new File(filePath + "\\" + fileArray[j]);
                                    long fileSize = f2.length();
                                    out.writeLong(fileSize);
                                    out.flush();
                                }
                            }
                        }
                        break;

                    }

                } else if (requestSignal.startsWith("download")) {
                    FileInputStream fis;
                    System.out.println("Download req recognized");
                    try {

                        StringTokenizer st = new StringTokenizer(requestSignal, ",");
                        st.nextToken();
                        String download = st.nextToken();

                        long size2;
                        System.out.println("Download req for : " + download);
                        //TODO in doc era si testare pentru fis anume linia 94
                        File file = new File(filePath + "//" + download);
                        fis = new FileInputStream(file);
                        long size = file.length();
                        out.writeLong(size);
                        out.flush();
                        String actualSize;
                        if (size < 1024) {
                            actualSize = size + " bytes";
                        } else if (size < 1048576) {
                            actualSize = (float) (size / 1024) + " KB";
                        } else if (size < 1024 * 1024 * 1024) {
                            actualSize = (int) (size / 1024 * 1024) + " MB";
                        } else {
                            actualSize = (float) (size / 1024 * 1024 * 1024) + " GB";

                        }
                        //TODO SEARCHFILES PROB LEGAT DE TABLEMODEL1
                        //TABELA : download, sc.getInetAddress().getHostAddress(),actualSize
                        //TODO FIRETABLEDATACHANGED
                        byte kb[] = new byte[1024 * 1024];
                        long contor = 0;
                        while (true) {
                            //TRIMIT KB CU KB PANA E EGAL CU SIZE
                            int k = fis.read(kb, 0, 1024 * 1024);
                            out.write(kb, 0, k);
                            out.flush();
                            contor = contor + k;
                            if (contor == size) {
                                break;
                            }
                            fis.close();
                            System.out.println("File sent?");
                            //TODO SE GOLESTE TABELA 
                            break;

                        }

                    } catch (Exception ex) {
                        ex.getMessage();
                        System.out.println("Downlad Ex: Didnt send file");

                    }
                    break;

                } else if (requestSignal.startsWith("message")) {
                    //TODO SEE IF U CAN REUSE THE SAMETOKENIZER 
                    StringTokenizer tokk = new StringTokenizer(requestSignal, ",");
                    tokk.nextToken();
                    //TODO HANDLE MESSAGE REQUEST
                } else if (requestSignal.startsWith("stitch")) {
                    //TODO HANDLE STITCH
                }

            }

        } catch (Exception ex) {
            ex.getMessage();
        }

    }

}
