//package mintwire.p2pmodels;
//
//import java.io.BufferedReader;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.InputStreamReader;
//import java.net.Socket;
//import java.util.StringTokenizer;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.SwingConstants;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//
//
//public class MintRequestHandler implements Runnable {
//    private String alias;
//    private Thread mainThread;
//    private Socket clientSocket;
//    private DataOutputStream out;
//    private DataInputStream in;
//    private String res;
//    private BufferedReader bf;
//    
//   
//
//    MintRequestHandler(Socket socket,String alias) {
//        this.clientSocket = socket;
//        this.alias=alias;
//
//        try {
//            out = new DataOutputStream(this.clientSocket.getOutputStream());
//            in = new DataInputStream(this.clientSocket.getInputStream());
//            bf = new BufferedReader(new InputStreamReader(in));
//            mainThread = new Thread(this);
//            mainThread.start();
//        } catch (Exception ex) {
//            ex.getMessage();
//            System.out.println("Request Handle Ex: ");
//
//        }
//    }
//
//    @Override
//    public void run() {
//
//        try {
//            //check
//            res = bf.readLine();
//            if (res.equals("minicheck")) {
//                out.writeBytes("\nrunning...");
//                out.flush();
//            }
//            String requestSignal = "";
//
//            while (true) {
//                requestSignal = bf.readLine();
//                if (requestSignal == null) {
//                    break;
//                }
//                if (requestSignal.startsWith("search")) {
//                    //CAUTAREA UNUI singur FISIER LA CEILALTI
//                    StringTokenizer tok = new StringTokenizer(requestSignal, ",");
//                    tok.nextToken();
//                    String search = tok.nextToken();
//                    System.out.println("Searching for: " + search);
//                    //TODO filepath nu e schimbat niciunde
//                   
//                    String[] fileArray = f.list();
//                    System.out.println("Files found in filePath " + fileArray.length);
//                    int n = 0;
//                    for (int i = 0; i < fileArray.length; i++) {
//                        if (fileArray[i].indexOf(search) != -1) {
//                            n++;
//                        }
//                        out.writeBytes("filenr " + n + "\r\n");
//                        out.flush();
//
//                        if (n > 0) {
//                            for (int j = 0; j < fileArray.length; j++) {
//                                if (fileArray[j].indexOf(search) != -1) {
//                                    out.writeBytes(fileArray[j] + "\r\n");
//                                    out.flush();
//                                    File f2 = new File(filePath + "\\" + fileArray[j]);
//                                    long fileSize = f2.length();
//                                    out.writeLong(fileSize);
//                                    out.flush();
//                                }
//                            }
//                        }
//                        break;
//
//                    }
//
//                } else if (requestSignal.startsWith("download")) {
//                    FileInputStream fis;
//                    System.out.println("Download req recognized");
//                    try {
//
//                        StringTokenizer downloadTarget = new StringTokenizer(requestSignal, ",");
//                        //st e chiar numarul fis
//                        downloadTarget.nextToken();
//                        String download = downloadTarget.nextToken();
//
//                        long size2;
//                        System.out.println("Download req for : " + download);
//                        //TODO in doc era si testare pentru fis anume linia 94
////                        File file = new File(filePath + "//" + download);
//                       // fis = new FileInputStream(file);
////                      //  long size = file.length();
////                        out.writeLong(size);
////                        out.flush();
////                        String actualSize;
////                        if (size < 1024) {
////                            actualSize = size + " bytes";
////                        } else if (size < 1048576) {
////                            actualSize = (float) (size / 1024) + " KB";
////                        } else if (size < 1024 * 1024 * 1024) {
////                            actualSize = (int) (size / 1024 * 1024) + " MB";
////                        } else {
////                            actualSize = (float) (size / 1024 * 1024 * 1024) + " GB";
////
////                        }
//                        //TODO ADAUGARE IN FILE HISTORY O FAC PRBIL IN JSON 
//                       try(FileReader reader=new FileReader("C:\\MINTWIRE\\" + alias + "history.json")){
//                           JSONParser jsonParser = new JSONParser();
//                           Object obj=jsonParser.parse(reader);
//                           JSONObject jO=(JSONObject)obj;
//                           JSONArray downloadArray = (JSONArray) jO.get("downloadhistory");
//                           //creez nodul nou
//                           JSONObject newHistory=new JSONObject();
//                           newHistory.put("filename", downloadTarget);
//                           newHistory.put("address",clientSocket.getInetAddress().getHostAddress());
//                           newHistory.put("filesize", actualSize);
//                           downloadArray.add(newHistory);
//                           
//                       }catch(Exception ex){
//                           JLabel label = new JLabel("<html><center>"+ex.getMessage());
//                           label.setHorizontalAlignment(SwingConstants.CENTER);
//                           JOptionPane.showMessageDialog(null, label, "Exception occured", JOptionPane.ERROR_MESSAGE);
//                       }
//                        
//                        //TODO FIRETABLEDATACHANGED
//                        byte kb[] = new byte[1024 * 1024];
//                        long contor = 0;
//                        while (true) {
//                            //TRIMIT KB CU KB PANA E EGAL CU SIZE
//                            int k = fis.read(kb, 0, 1024 * 1024);
//                            out.write(kb, 0, k);
//                            out.flush();
//                            contor = contor + k;
//                            if (contor == size) {
//                                break;
//                            }
//                            fis.close();
//                            System.out.println("File sent?");
//                            //TODO SE GOLESTE TABELA why
//                            
//                            break;
//
//                        }
//
//                    } catch (Exception ex) {
//                        ex.getMessage();
//                        System.out.println("Downlad Ex: Didnt send file");
//
//                    }
//                    break;
//
//                } else if (requestSignal.startsWith("message from,")) {
//                    //TODO SEE IF U CAN REUSE THE SAMETOKENIZER 
//                    StringTokenizer tokk = new StringTokenizer(requestSignal, ",");
//                    tokk.nextToken();
//                    //TODO HANDLE MESSAGE REQUEST
//                } else if (requestSignal.startsWith("stitch")) {
//                    //TODO HANDLE STITCH
//                }
//
//            }
//
//        } catch (Exception ex) {
//            ex.getMessage();
//        }
//
//    }
//
//}
