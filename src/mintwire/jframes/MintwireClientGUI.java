
package mintwire.jframes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;

import java.awt.RenderingHints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;

import java.net.ServerSocket;
import java.net.Socket;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.StringTokenizer;


import javax.imageio.ImageIO;
import static javax.swing.BorderFactory.createEmptyBorder;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JLabel;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import javax.swing.SwingWorker;

import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import mintwire.LangSelector;
import mintwire.borders.ChatBorder;
import mintwire.chatpanels.Bubbler;
import mintwire.classes.MintFile;
import mintwire.jframes.MintwireClientGUI.FileSporeTableModel;
import mintwire.utils.Utils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


//TODO LEGATURI DE TABELA CU ALIAS STATUS CU RESTUL
//IMPLEMENTARE MESAGERIE: CUM FAC SA STIU CE USER E SELECTAT, UNDE TRIMIT MESAJELE
//IMPLEMENTARE TRIMITERE REQUEST STITCH, TRIMITERE STITCH PROPRIU ZISA
// ACTIVARE IS-URI P2P CAND TREB
//INCEPERE THREADURI
//TODO FILEHISTORY + TABELE CARE TREBUIE IMPLEM + SERVER

//TODO HANDLE PENTRU MESAJE SI CODESTITCH

public class MintwireClientGUI extends javax.swing.JFrame {
public class FileSporeTableModel extends AbstractTableModel{
    
        String columns[]={"File","Details","Owner's Alias","Checkbox"};

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }
        
        @Override
        public int getRowCount() {
            return mintFiles.size();
        }

        @Override
        public int getColumnCount() {
           return columns.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch(columnIndex){
                case 0:
                    return mintFiles.get(rowIndex).getFileName();
                case 1:
                    return mintFiles.get(rowIndex).getDetails();
                case 2:
                    return mintFiles.get(rowIndex).getAlias();
                case 3:
                    return "not yet";
                default:
                    return 0;
            }
        }
    
}
   
public class FileExtensionModel extends DefaultTableCellRenderer{
    JLabel label=new JLabel();
    ImageIcon ii;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
             if(column==0){
             
              String ext=mintFiles.get(row).getFileName().substring(mintFiles.get(row).getFileName().indexOf("."));
              
               try{ii=new ImageIcon(getClass().getResource("/res/ext/"+ext+".png"));
               }catch(Exception ex){
                   ii=new ImageIcon(getClass().getResource("/res/ext/ai.png"));
               }
               label.setIcon(ii);
               label.setText(mintFiles.get(row).getFileName());
             }else if(column==1){
                 label.setText(mintFiles.get(row).getDetails());
                 
             }else if(column==2){
                  label.setText(mintFiles.get(row).getAlias());
             }else if(column==3){
                 label.setText("no check yet");
             }
          
           return label;
        }
    
}
   //my vars
    private String sharedPath = "C:\\MINTWIRE Shared";
    private String selectedFile="";
    private FileSporeTableModel sporeModel=new FileSporeTableModel();
    private boolean isSendStitchRequest=false;
    private boolean isSendStitch=false;
    private boolean isMessageRequest=false;
    private boolean isSearchRequest=false;
    private boolean isDownloadRequest=false;
    
    private ArrayList<MintFile> mintFiles=new ArrayList();
    private Bubbler bubbler;
    private ChatBorder cB=new ChatBorder(45);
    final Color SENT=new Color(244,101,101);
    final Color RECEIVED=new Color(170,207,255);
     private JLabel infoLabel;
    
    final JPanel scrollablePanel = new JPanel(new GridLayout(0,1));
    private Utils utils=new Utils();
    private String alias;
    private String password;
    private RSyntaxTextArea textArea ;
    private String filePath;
    private final String langPre="SyntaxConstants.SYNTAX_STYLE_";
    private JMenu languageToggle;
    private ArrayList<String> array = new ArrayList<String>();
    private ServerSocket miniServerSock;
 
    //end of my vars
    public MintwireClientGUI() {
     
        initComponents();
    }
    public MintwireClientGUI(String alias, String passw)
    {  
       
        this.alias=alias;
        this.password=password;
       
        setTabbedDesign();
        initComponents();
        setStitchLabelOn();
        initRSyntax(RequestSPanel);
        connToServer(alias);
        
   
         setPfp();
         
       
        
         
    }
    //P2P MODELS

  
    //MINTCLIENTP2P
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

//MINICLIENTP2P
    public class MintMiniClient implements Runnable {
        //searchrequest se mai seteaza true cand se apasa butonul

        private ArrayList<MintFile> mintFiles;
      
        private BufferedReader br;
        private String ipaddr;
        private Socket miniSocket, sock2;
        private DataInputStream in;
        private DataOutputStream out;
        private Thread thread;
        private String ip;
        private long fileSize;
        private FileWriter fw;
        private String history;
       

        MintMiniClient(String ipaddr) {
            this.ipaddr = ipaddr;

            try {
                miniSocket = new Socket(this.ip, 6424);
                in = new DataInputStream(miniSocket.getInputStream());
                br=new BufferedReader(new InputStreamReader(in));
                out = new DataOutputStream(miniSocket.getOutputStream());
                out.writeBytes("minicheck\r\n");
                out.flush();
                thread = new Thread(this);
                thread.start();

            } catch (Exception ex) {
                infoLabel= new JLabel("<html><center>" + ex.getMessage());
                infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                JOptionPane.showMessageDialog(null, infoLabel, "Error in Mint Mini Client", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        @Override
        public void run() {

           
            if (isSearchRequest) {
                mintFiles.clear();
                if (sporeText.getText().trim().equals("")) {
                      infoLabel= new JLabel("<html><center> Enter a valid filename" );
                infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                JOptionPane.showMessageDialog(null, infoLabel, "Input Error", JOptionPane.INFORMATION_MESSAGE);

                } else {
                    try {
                        out.writeBytes("search," + sporeText.getText().trim());
                        out.flush();
                        String response = br.readLine();
                        StringTokenizer st = new StringTokenizer(response);
                        st.nextToken();
                        int size = Integer.parseInt(st.nextToken());
                        if (size == 0) {
                            infoLabel = new JLabel("<html><center> Your peers don't seem to have the file you are looking for");
                            infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                            JOptionPane.showMessageDialog(null, infoLabel, "No trace of the file", JOptionPane.INFORMATION_MESSAGE);

                        }else{
                            for(int i=0;i<size;i++){
                                String fileName=br.readLine();
                                fileSize=Long.parseLong(br.readLine());
                                if(fileSize>=1024&&fileSize<1048576){
                                    fileSize=(long) (fileSize/1024);
                                    mintFiles.add(new MintFile(fileName,size+fileSize+" kB","no alias"));
                                } else if(fileSize>=1048576&&fileSize<(1024*1024*1024)){
                                    fileSize=(long) (fileSize/(1024*1024));
                                    mintFiles.add(new MintFile(fileName,size+fileSize+" mB","no alias"));
                                }else  if(fileSize>=(1024*1024*1024)&&fileSize<(1024*1024*1024*1024)){
                                    fileSize=(long) (fileSize/(1024*1024*1024));
                                    mintFiles.add(new MintFile(fileName,size+fileSize+" gB","no alias"));
                                }else{
                                    mintFiles.add(new MintFile(fileName,size+fileSize+" bytes","no alias"));
                                }
                                sporeModel.fireTableDataChanged();
                               
                            }
                        }
                        
                        
                       

                    } catch (Exception ex) {
                        infoLabel = new JLabel("<html><center> Error when sending search request");
                        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        JOptionPane.showMessageDialog(null, infoLabel, "Mint MiniClient Error", JOptionPane.INFORMATION_MESSAGE);

                    }
                     

                }
                isSearchRequest=false;
            }
            if(isDownloadRequest){
                try {
                    //selected file e setat in click event la buttonul de obtain
                    out.writeBytes("download,"+selectedFile+"\r\n");
                    out.flush();
                    long size=in.readLong();
                    byte b[]=new byte[1024*1024];
                    long count=0;
                    int dwn;
                    int poz=sporeTable.getSelectedRow();
                    FileOutputStream fos;
                    selectedFile=mintFiles.get(poz).getFileName();
                    String extension = selectedFile.substring(selectedFile.lastIndexOf("."));
                    boolean flag = false;
                    File f = new File(sharedPath + "\\" + selectedFile);
                    if (f.exists()) {
                        //TODO POATE O METODA DE REDOWNLOAD
                        infoLabel = new JLabel("<html><center>File already exists");
                        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        JOptionPane.showMessageDialog(null, infoLabel, "You already have "+selectedFile+"! Try another one?", JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        fos=new FileOutputStream(sharedPath+"\\"+selectedFile);
                        while(true){
                            dwn=in.read(b,0,1024*1024);
                            count=count+dwn;
                            fos.write(b,0,dwn);
                            if(count==size){
                                break;
                            }
                               
                        }
                        out.close();
                        sporeTable.clearSelection();
                        
                        //TODO SCRIE FILE HITORY AICI
                    } 
                } catch (Exception ex) {
                    infoLabel = new JLabel("<html><center>" + ex.getMessage());
                    infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    JOptionPane.showMessageDialog(null, infoLabel, "Error when sending download request", JOptionPane.INFORMATION_MESSAGE);
                }


            }
            isDownloadRequest=false;
            if(isMessageRequest){
                try {
                    out.writeBytes("message from," + chatTextArea.getText() + "\r\n");
                    out.flush();
                } catch (Exception ex) {
                    infoLabel = new JLabel("<html><center>" + ex.getMessage());
                    infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    JOptionPane.showMessageDialog(null, infoLabel, "Error when sending messsage request", JOptionPane.INFORMATION_MESSAGE);

                }

                
            }
            isMessageRequest=false;
            if(isSendStitch){
                
            }
            isSendStitch=false;
            if(isSendStitchRequest){
                
            }
            isSendStitchRequest=false;

        }

}
  //P2PMINTMINISERVER
    
public class MintMiniServer implements Runnable {
    String alias;
    Thread thread;
    Socket socket;
    ServerSocket miniServerSocket;
    MintRequestHandler requestHandler;

    MintMiniServer(String alias) {
        this.alias=alias;
        thread = new Thread(this);
        thread.start();

    }

    @Override
    public void run() {
        try {
            miniServerSocket = new ServerSocket(6424);
            while (true) {
                socket = miniServerSocket.accept();
                //TODO PRIMIRE ALIAS SI STARE
                
                requestHandler=new MintRequestHandler(socket,alias);
                
                
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error in MiniServer Thread", "Mini Server Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
 //P2PREQUESTHANDLE
  public class MintRequestHandler implements Runnable {
    private String alias;
    private Thread mainThread;
    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;
    private String res;
    private BufferedReader bf;
    
   

    MintRequestHandler(Socket socket,String alias) {
        this.clientSocket = socket;
        this.alias=alias;

        try {
            out = new DataOutputStream(this.clientSocket.getOutputStream());
            in = new DataInputStream(this.clientSocket.getInputStream());
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
                    //CAUTAREA UNUI singur FISIER LA CEILALTI
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
                        out.writeBytes("filenr " + n + "\r\n");
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

                        StringTokenizer downloadTarget = new StringTokenizer(requestSignal, ",");
                        //st e chiar numarul fis
                        downloadTarget.nextToken();
                        String download = downloadTarget.nextToken();

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
                        //TODO ADAUGARE IN FILE HISTORY O FAC PRBIL IN JSON 
                       try(FileReader reader=new FileReader("C:\\MINTWIRE\\" + alias + "history.json")){
                           JSONParser jsonParser = new JSONParser();
                           Object obj=jsonParser.parse(reader);
                           JSONObject jO=(JSONObject)obj;
                           JSONArray downloadArray = (JSONArray) jO.get("downloadhistory");
                           //creez nodul nou
                           JSONObject newHistory=new JSONObject();
                           newHistory.put("filename", downloadTarget);
                           newHistory.put("address",clientSocket.getInetAddress().getHostAddress());
                           newHistory.put("filesize", actualSize);
                           downloadArray.add(newHistory);
                           
                       }catch(Exception ex){
                           JLabel label = new JLabel("<html><center>"+ex.getMessage());
                           label.setHorizontalAlignment(SwingConstants.CENTER);
                           JOptionPane.showMessageDialog(null, label, "Exception occured", JOptionPane.ERROR_MESSAGE);
                       }
                        
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
                            //TODO SE GOLESTE TABELA why
                            
                            break;

                        }

                    } catch (Exception ex) {
                        ex.getMessage();
                        System.out.println("Downlad Ex: Didnt send file");

                    }
                    break;

                } else if (requestSignal.startsWith("message from,")) {
                    
                    
                    
                    StringTokenizer st = new StringTokenizer(requestSignal, ",");
                    st.nextToken();
                    String message=st.nextToken();
                    //cautare respectivul
                    //adaugare bubble cu message
                    
                    //TODO HANDLE MESSAGE REQUEST
                } else if (requestSignal.startsWith("stitch from,")) {
                    //TODO HANDLE STITCH
                }

            }

        } catch (Exception ex) {
            ex.getMessage();
        }

    }

}


 
    //LAYEREDPANE INITS
   
    public void initMintLynx()
    {
        //chatTextArea.append("Initializare reusita");
        
    
     
       
        
        
        
        
    }
    //END OF LAYERED PANE INITS
   
    //MY METHODS
    
  
    public void connToServer(String alias)
    {
        File f1=new File("C:\\MINTWIRE");
        if (!(f1.exists()))
         {
             f1.mkdir();
         }
        
       
        File f2=new File("C:\\MINTWIRE\\"+alias);
        if (!(f2.exists()))
         {
             f2.mkdir();
             //CREATE PFP FOLDER
             File f3=new File("C:\\MINTWIRE\\"+alias+"\\pfp"); 
             f3.mkdir();
             File f4=new File( "src/mintwire/res/pngs/profilepic.png");
            try{ BufferedImage bi=ImageIO.read(f4);
            //am citit acum o salvez ca pfp pentru prima logare
                File outputF = new File("C:\\MINTWIRE\\" + alias + "\\pfp\\pfp.png");
                ImageIO.write(bi, "PNG", outputF);
                FileWriter fw = new FileWriter("C:\\MINTWIRE\\" + alias + "history.json");
                JSONObject obj = new JSONObject();

                JSONArray arr = new JSONArray();

                obj.put(arr, "downloadhistory");
                fw.write(obj.toJSONString());
            
            }catch(Exception ex)
            {
                System.out.println("err in creare fold:"+ex.getMessage());
            }
            
         }
       
        Path path = Paths.get("C:\\MINTWIRE\\"+alias);
        try{
            Files.setAttribute(path, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        
       
    }
    public void setPfp(){
         File pfp = new File("C:\\MINTWIRE\\" + alias + "\\pfp\\pfp.png");
        try {
            BufferedImage bi = ImageIO.read(pfp);
            
            BufferedImage biR=utils.makeRound(bi);
            Image resultScaled=biR.getScaledInstance(pfpLabel.getWidth(), pfpLabel.getHeight(), Image.SCALE_SMOOTH);
              
            ImageIcon ico = new ImageIcon(resultScaled);
           
            pfpLabel.setIcon(ico);
        } catch (Exception ex) {
            System.out.print("excc in load: "+ex.getMessage());
            

        }


    }
    public void configSbars(RTextScrollPane sp,JPanel panel)
    {
        //config scrollbar color
        JPanel p = new JPanel();
        JPanel r=new JPanel();
        r.setBackground(Color.gray);
        p.setBackground(new Color(43,43,43));
        sp.setCorner(RTextScrollPane.LOWER_LEFT_CORNER, p);
        sp.setCorner(RTextScrollPane.LOWER_RIGHT_CORNER, p);
        sp.setCorner(RTextScrollPane.LOWER_LEADING_CORNER, p);
//        sp.setCorner(RTextScrollPane.UPPER_LEADING_CORNER, p);
        sp.getVerticalScrollBar().setBackground(Color.GRAY);
        
        sp.getHorizontalScrollBar().setBackground(Color.GRAY);
        sp.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
          @Override
          
    protected void configureScrollBarColors() {
        this.thumbColor =new Color(167,150,146);
        this.trackColor=new Color(43,43,43);
        
        
    }
     @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override    
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton jbutton = new JButton();
            jbutton.setPreferredSize(new Dimension(0, 0));
            jbutton.setMinimumSize(new Dimension(0, 0));
            jbutton.setMaximumSize(new Dimension(0, 0));
            return jbutton;
        }
    });
        
        
       sp.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
          @Override
          
    protected void configureScrollBarColors() {
        this.thumbColor = new Color(167,150,146);
        this.trackColor=new Color(43,43,43);
        
        
    }
     @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override    
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton jbutton = new JButton();
            jbutton.setPreferredSize(new Dimension(0, 0));
            jbutton.setMinimumSize(new Dimension(0, 0));
            jbutton.setMaximumSize(new Dimension(0, 0));
            return jbutton;
        }
   
    });
      
        
        panel.add(sp);
        panel.revalidate();
        panel.repaint();
    }
    public void setTabbedDesign()
    {
//        UIManager.put("TabbedPane.selected", new Color(198,89,89));
       UIManager.put("TabbedPane.borderHightlightColor", new ColorUIResource( Color.GRAY ));
        UIManager.put("TabbedPane.darkShadow", new ColorUIResource( Color.GRAY ));
    UIManager.put("TabbedPane.contentAreaColor", Color.GRAY);
       UIManager.put("TabbedPane.highlight",        Color.GRAY);
//        UIManager.put("TabbedPane.unselectedForeground", Color.RED);
UIManager.put("TabbedPane.selectedForeground", new Color(52,203,139));
    }
    public void setStitchLabelOn()
    {
        CStitchLabel.setForeground(new Color(223,102,105));
        CStitchLabel.setBackground(new Color(88,99,91));
    }
    public void configSyntaxMenu()
    {
        JPopupMenu syntaxMenu = textArea.getPopupMenu();
        
         syntaxMenu.addSeparator();
         languageToggle=new JMenu("Change Language...");
        languageToggle.setBackground(new Color(43,43,43));
        languageToggle.setForeground(new Color(238,205,127));
         Field[] languages=SyntaxConstants.class.getFields();
         String separ="public static final java.lang.String org.fife.ui.rsyntaxtextarea.SyntaxConstants.SYNTAX_STYLE_";
         ActionListener actionListener=new MenuActionListener();
         
         String vect="";
         for(Field language:languages)
         {
             String name=language.toString();
             name= (name.split(separ))[1];
             String letter=name.substring(0,1);
             
             if((vect.indexOf(letter ))==-1)
             {
                 vect.concat(letter);
                 JMenu itm=new JMenu(letter);
                 languageToggle.add(itm);
                 JMenuItem subitm=new JMenuItem(name);
                 itm.setOpaque(true);
                 //adding colors
                 itm.setBackground(new Color(43,43,43));
                 itm.setForeground(new Color(238,205,127));
                 subitm.setBackground(new Color(43,43,43));
                 subitm.setForeground(new Color(238,205,127));
                 itm.add(subitm);
                 subitm.addActionListener(actionListener);
                 vect+=letter;
                 
                 
             }else
             {
                 JMenuItem subitm=new JMenuItem(name);
                 subitm.setOpaque(true);
                 //adding colors
                 subitm.setBackground(new Color(43,43,43));
                 subitm.setForeground(new Color(238,205,12));
                 JMenu itm=(JMenu)languageToggle.getMenuComponent(vect.indexOf(letter ));
                 itm.add(subitm);
                 subitm.addActionListener(actionListener);
             }
             
            
         }
         
         syntaxMenu.add(languageToggle);
         
    }
    public void initRSyntax(JPanel panel)
    {
        textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        
        RTextScrollPane sp = new RTextScrollPane(textArea);
        //configure scrollbars for rtextscrollpane
        configSbars(sp,panel);
        
        //popup custom
         configSyntaxMenu();
        //action listen

        InputStream in=getClass().getResourceAsStream("/dark.xml");
        try{
            Theme theme=Theme.load(in);
            theme.apply(textArea);
            
            
        }catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
        textArea.setFont(new Font("Monospace",Font.BOLD,20));
        
        
    }
    //menu function for RSYNTAX
   
    //ACTION LISTENER FOR LANG TOGGLE
   class MenuActionListener implements ActionListener {
  @Override
  public void actionPerformed(ActionEvent e) {
      System.out.println(e.getActionCommand());
      LangSelector lang=new LangSelector();
      String context=lang.select(e.getActionCommand());
      textArea.setSyntaxEditingStyle(context);
      
      
  }
}
      
    
    

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        CStitchLabel = new javax.swing.JLabel();
        CStitchPartyLabel = new javax.swing.JLabel();
        MintLynxLabel = new javax.swing.JLabel();
        FileSporeLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        MintRequestLabel = new javax.swing.JLabel();
        FileHaulLabel = new javax.swing.JLabel();
        PreferencesLabel = new javax.swing.JLabel();
        IdentityLabel = new javax.swing.JLabel();
        pfpLabel = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Dimension arcs = new Dimension(15,15);
                int width = getWidth();
                int height = getHeight();
                Graphics2D graphics = (Graphics2D) g;
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                graphics.setColor(getBackground());
                graphics.fillOval(0, 0, width-1, height-1);
                graphics.setColor(getForeground());
                graphics.drawOval(0, 0, width-1, height-1);
            }
        };
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        MainLayeredPane = new javax.swing.JLayeredPane();
        CodeStitchPanel = new javax.swing.JPanel();
        TabbedPane = new javax.swing.JTabbedPane();
        RequestSPanel = new javax.swing.JPanel();
        SendSPanel = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        sendButton = new javax.swing.JButton();
        CodeStitchPartyPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        MintLynxPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        chatScrollPane = new javax.swing.JScrollPane(scrollablePanel);
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        chatTextArea = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        searchPeerTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel5 = new javax.swing.JLabel();
        FileSporePanel = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        sporeText = new javax.swing.JTextField();
        sporeScroll = new javax.swing.JScrollPane();
        sporeTable = new javax.swing.JTable();
        sporeSearch = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(94, 87, 104));

        jPanel1.setBackground(new java.awt.Color(94, 87, 104));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(49, 46, 54));

        jLabel2.setBackground(new java.awt.Color(49, 46, 54));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/separator.png"))); // NOI18N
        jLabel2.setToolTipText("");
        jLabel2.setOpaque(true);

        CStitchLabel.setBackground(new java.awt.Color(49, 46, 54));
        CStitchLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        CStitchLabel.setForeground(new java.awt.Color(255, 204, 204));
        CStitchLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CStitchLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/codestitch.png"))); // NOI18N
        CStitchLabel.setText("Code Stitch");
        CStitchLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        CStitchLabel.setOpaque(true);
        CStitchLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        CStitchLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CStitchLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CStitchLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CStitchLabelMouseExited(evt);
            }
        });

        CStitchPartyLabel.setBackground(new java.awt.Color(49, 46, 54));
        CStitchPartyLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        CStitchPartyLabel.setForeground(new java.awt.Color(255, 204, 204));
        CStitchPartyLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/codestitch-party.png"))); // NOI18N
        CStitchPartyLabel.setText("Code Stitch Party");
        CStitchPartyLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        CStitchPartyLabel.setOpaque(true);
        CStitchPartyLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        CStitchPartyLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CStitchPartyLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CStitchPartyLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CStitchPartyLabelMouseExited(evt);
            }
        });

        MintLynxLabel.setBackground(new java.awt.Color(49, 46, 54));
        MintLynxLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        MintLynxLabel.setForeground(new java.awt.Color(255, 204, 204));
        MintLynxLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        MintLynxLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/mintlynx.png"))); // NOI18N
        MintLynxLabel.setText("Mint Lynx");
        MintLynxLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        MintLynxLabel.setOpaque(true);
        MintLynxLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        MintLynxLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MintLynxLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                MintLynxLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                MintLynxLabelMouseExited(evt);
            }
        });

        FileSporeLabel.setBackground(new java.awt.Color(49, 46, 54));
        FileSporeLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        FileSporeLabel.setForeground(new java.awt.Color(255, 204, 204));
        FileSporeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        FileSporeLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/filespore.png"))); // NOI18N
        FileSporeLabel.setText("File Spore");
        FileSporeLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        FileSporeLabel.setOpaque(true);
        FileSporeLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        FileSporeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FileSporeLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                FileSporeLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                FileSporeLabelMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(CStitchLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(163, 163, 163)
                .addComponent(CStitchPartyLabel)
                .addGap(155, 155, 155)
                .addComponent(MintLynxLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(189, 189, 189)
                .addComponent(FileSporeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(99, Short.MAX_VALUE))
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MintLynxLabel)
                    .addComponent(CStitchPartyLabel)
                    .addComponent(FileSporeLabel)
                    .addComponent(CStitchLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        CStitchLabel.getAccessibleContext().setAccessibleName("CStitchLabel");

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, -10, 1330, 150));

        jPanel2.setBackground(new java.awt.Color(43, 43, 43));

        jLabel1.setBackground(new java.awt.Color(43, 43, 43));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/logos/logo-mediumish.png"))); // NOI18N
        jLabel1.setOpaque(true);

        MintRequestLabel.setBackground(new java.awt.Color(43, 43, 43));
        MintRequestLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        MintRequestLabel.setForeground(new java.awt.Color(167, 255, 185));
        MintRequestLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        MintRequestLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/questionwhite.png"))); // NOI18N
        MintRequestLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        MintRequestLabel.setOpaque(true);
        MintRequestLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        MintRequestLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MintRequestLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                MintRequestLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                MintRequestLabelMouseExited(evt);
            }
        });

        FileHaulLabel.setBackground(new java.awt.Color(43, 43, 43));
        FileHaulLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        FileHaulLabel.setForeground(new java.awt.Color(167, 255, 185));
        FileHaulLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        FileHaulLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/folderwhite.png"))); // NOI18N
        FileHaulLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        FileHaulLabel.setOpaque(true);
        FileHaulLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        FileHaulLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FileHaulLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                FileHaulLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                FileHaulLabelMouseExited(evt);
            }
        });

        PreferencesLabel.setBackground(new java.awt.Color(43, 43, 43));
        PreferencesLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        PreferencesLabel.setForeground(new java.awt.Color(167, 255, 185));
        PreferencesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PreferencesLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/settingswhite.png"))); // NOI18N
        PreferencesLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        PreferencesLabel.setOpaque(true);
        PreferencesLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        PreferencesLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PreferencesLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PreferencesLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PreferencesLabelMouseExited(evt);
            }
        });

        IdentityLabel.setBackground(new java.awt.Color(43, 43, 43));
        IdentityLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        IdentityLabel.setForeground(new java.awt.Color(167, 255, 185));
        IdentityLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        IdentityLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/userwhite.png"))); // NOI18N
        IdentityLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        IdentityLabel.setOpaque(true);
        IdentityLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        IdentityLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IdentityLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                IdentityLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                IdentityLabelMouseExited(evt);
            }
        });

        pfpLabel.setBackground(new java.awt.Color(43, 43, 43));
        pfpLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jPanel7.setBackground(new java.awt.Color(43, 43, 43));
        jPanel7.setForeground(new java.awt.Color(67, 207, 137));
        jPanel7.setPreferredSize(new java.awt.Dimension(60, 60));

        jLabel6.setBackground(new java.awt.Color(43, 43, 43));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MintRequestLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(FileHaulLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PreferencesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(IdentityLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(pfpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(69, 69, 69))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(MintRequestLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FileHaulLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PreferencesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(IdentityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pfpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 800));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/codestitch-party.png"))); // NOI18N
        jLabel8.setText("jLabel5");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        CodeStitchPanel.setBackground(new java.awt.Color(94, 87, 104));

        TabbedPane.setBackground(new java.awt.Color(51, 51, 51));
        TabbedPane.setForeground(new java.awt.Color(204, 204, 255));
        TabbedPane.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        TabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                TabbedPaneStateChanged(evt);
            }
        });

        RequestSPanel.setBackground(new java.awt.Color(51, 51, 51));
        RequestSPanel.setForeground(new java.awt.Color(51, 255, 0));
        RequestSPanel.setLayout(new java.awt.BorderLayout());
        TabbedPane.addTab("Request a Stitching", RequestSPanel);

        SendSPanel.setBackground(new java.awt.Color(51, 51, 51));
        SendSPanel.setForeground(new java.awt.Color(51, 255, 51));
        SendSPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SendSPanelMouseClicked(evt);
            }
        });
        SendSPanel.setLayout(new java.awt.BorderLayout());
        TabbedPane.addTab("Send a Stitching", SendSPanel);

        saveButton.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        saveButton.setForeground(new java.awt.Color(255, 255, 255));
        saveButton.setText("Save a stitch...");

        sendButton.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        sendButton.setForeground(new java.awt.Color(255, 255, 255));
        sendButton.setText("Send a stitch...");

        javax.swing.GroupLayout CodeStitchPanelLayout = new javax.swing.GroupLayout(CodeStitchPanel);
        CodeStitchPanel.setLayout(CodeStitchPanelLayout);
        CodeStitchPanelLayout.setHorizontalGroup(
            CodeStitchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CodeStitchPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(TabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1036, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(CodeStitchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        CodeStitchPanelLayout.setVerticalGroup(
            CodeStitchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CodeStitchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(CodeStitchPanelLayout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TabbedPane.getAccessibleContext().setAccessibleName("Send a Stitching");

        CodeStitchPartyPanel.setBackground(new java.awt.Color(94, 87, 104));

        jLabel3.setText("CODE STITCH PARTY");

        javax.swing.GroupLayout CodeStitchPartyPanelLayout = new javax.swing.GroupLayout(CodeStitchPartyPanel);
        CodeStitchPartyPanel.setLayout(CodeStitchPartyPanelLayout);
        CodeStitchPartyPanelLayout.setHorizontalGroup(
            CodeStitchPartyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CodeStitchPartyPanelLayout.createSequentialGroup()
                .addGap(343, 343, 343)
                .addComponent(jLabel3)
                .addContainerGap(858, Short.MAX_VALUE))
        );
        CodeStitchPartyPanelLayout.setVerticalGroup(
            CodeStitchPartyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CodeStitchPartyPanelLayout.createSequentialGroup()
                .addGap(163, 163, 163)
                .addComponent(jLabel3)
                .addContainerGap(469, Short.MAX_VALUE))
        );

        MintLynxPanel.setBackground(new java.awt.Color(94, 87, 104));

        jPanel3.setBackground(new java.awt.Color(45, 48, 55));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 518, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 127, Short.MAX_VALUE)
        );

        jPanel11.setBackground(new java.awt.Color(75, 80, 92));

        jScrollPane3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        chatTextArea.setColumns(20);
        chatTextArea.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        chatTextArea.setLineWrap(true);
        chatTextArea.setRows(2);
        Border roundedBorder = new LineBorder(Color.white, 2, true);
        chatTextArea.setBorder(roundedBorder);
        chatTextArea.setCaretColor(new java.awt.Color(255, 255, 255));
        chatTextArea.setCaretPosition(0);
        jScrollPane3.setViewportView(chatTextArea);

        jLabel4.setBackground(new java.awt.Color(75, 80, 92));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/send-message.png"))); // NOI18N
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });
        jLabel4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jLabel4KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane3)
                .addGap(41, 41, 41)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 372, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(chatScrollPane)))
                .addContainerGap())
            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(4, 4, 4)
                .addComponent(chatScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        searchPeerTextField.setBackground(new java.awt.Color(45, 48, 55));
        searchPeerTextField.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        searchPeerTextField.setForeground(new java.awt.Color(255, 204, 204));
        searchPeerTextField.setCaretColor(new java.awt.Color(255, 204, 204));
        searchPeerTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchPeerTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchPeerTextFieldFocusLost(evt);
            }
        });

        jList1.setBackground(new java.awt.Color(45, 48, 55));
        jScrollPane1.setViewportView(jList1);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintwire/res/pngs/search.png"))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchPeerTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane1)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                    .addComponent(searchPeerTextField))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout MintLynxPanelLayout = new javax.swing.GroupLayout(MintLynxPanel);
        MintLynxPanel.setLayout(MintLynxPanelLayout);
        MintLynxPanelLayout.setHorizontalGroup(
            MintLynxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MintLynxPanelLayout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 36, Short.MAX_VALUE))
        );
        MintLynxPanelLayout.setVerticalGroup(
            MintLynxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        FileSporePanel.setBackground(new java.awt.Color(94, 87, 104));

        jPanel8.setBackground(new java.awt.Color(45, 48, 56));

        sporeText.setBackground(new java.awt.Color(45, 48, 56));
        sporeText.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N

        sporeScroll.setBackground(new java.awt.Color(45, 48, 56));
        sporeScroll.setBorder(null);

        sporeTable.setBackground(new java.awt.Color(59, 62, 69));
        sporeTable.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        sporeTable.setForeground(new java.awt.Color(204, 204, 204));
        sporeTable.setModel(
            //new javax.swing.table.DefaultTableModel(
                //    new Object [][] {
                    //
                    //    },
                //    new String [] {
                    //        "File", "Details", "Owner's Alias", "Checkbox"
                    //    }
                //) {
                //    Class[] types = new Class [] {
                    //        java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class
                    //    };
                //    boolean[] canEdit = new boolean [] {
                    //        false, false, false, true
                    //    };
                //
                //    public Class getColumnClass(int columnIndex) {
                    //        return types [columnIndex];
                    //    }
                //
                //    public boolean isCellEditable(int rowIndex, int columnIndex) {
                    //        return canEdit [columnIndex];
                    //    }
                //}

            sporeModel
            //)
    );
    sporeScroll.setViewportView(sporeTable);
    sporeTable.setRowHeight (50);

    sporeSearch.setText("Search");

    javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
    jPanel8.setLayout(jPanel8Layout);
    jPanel8Layout.setHorizontalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addComponent(sporeText)
                    .addGap(35, 35, 35)
                    .addComponent(sporeSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(sporeScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 806, Short.MAX_VALUE))
            .addGap(32, 32, 32))
    );
    jPanel8Layout.setVerticalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel8Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(sporeText, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(sporeSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(sporeScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(25, 25, 25))
    );

    sporeScroll.getViewport().setBackground(new java.awt.Color(45, 48, 56));
    sporeScroll.setBorder(createEmptyBorder());

    jPanel9.setBackground(new java.awt.Color(45, 48, 56));

    jButton2.setText("Obtain selected item");
    jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jButton2MouseClicked(evt);
        }
    });

    jButton1.setText("Transform to CodeStitch...");
    jButton1.setToolTipText("");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
    jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel9Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    jPanel9Layout.setVerticalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel9Layout.createSequentialGroup()
            .addGap(118, 118, 118)
            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout FileSporePanelLayout = new javax.swing.GroupLayout(FileSporePanel);
    FileSporePanel.setLayout(FileSporePanelLayout);
    FileSporePanelLayout.setHorizontalGroup(
        FileSporePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(FileSporePanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(117, Short.MAX_VALUE))
    );
    FileSporePanelLayout.setVerticalGroup(
        FileSporePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FileSporePanelLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(FileSporePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );

    MainLayeredPane.setLayer(CodeStitchPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
    MainLayeredPane.setLayer(CodeStitchPartyPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
    MainLayeredPane.setLayer(MintLynxPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
    MainLayeredPane.setLayer(FileSporePanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

    javax.swing.GroupLayout MainLayeredPaneLayout = new javax.swing.GroupLayout(MainLayeredPane);
    MainLayeredPane.setLayout(MainLayeredPaneLayout);
    MainLayeredPaneLayout.setHorizontalGroup(
        MainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(CodeStitchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(MainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainLayeredPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CodeStitchPartyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap()))
        .addGroup(MainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainLayeredPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MintLynxPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap()))
        .addGroup(MainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainLayeredPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(FileSporePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap()))
    );
    MainLayeredPaneLayout.setVerticalGroup(
        MainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(CodeStitchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(MainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainLayeredPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CodeStitchPartyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap()))
        .addGroup(MainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainLayeredPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MintLynxPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap()))
        .addGroup(MainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainLayeredPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(FileSporePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap()))
    );

    jPanel1.add(MainLayeredPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 140, 1330, 660));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void CStitchPartyLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CStitchPartyLabelMouseEntered
       CStitchPartyLabel.setForeground(new Color(223,102,105));
        CStitchPartyLabel.setBackground(new Color(81,75,88));
    }//GEN-LAST:event_CStitchPartyLabelMouseEntered

    private void CStitchPartyLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CStitchPartyLabelMouseExited
        CStitchPartyLabel.setForeground(new Color(242,194,195));
        CStitchPartyLabel.setBackground(new Color(49,46,54));
    }//GEN-LAST:event_CStitchPartyLabelMouseExited

    private void CStitchLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CStitchLabelMouseEntered
         CStitchLabel.setForeground(new Color(223,102,105));
         CStitchLabel.setBackground(new Color(81,75,88));
    }//GEN-LAST:event_CStitchLabelMouseEntered

    private void CStitchLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CStitchLabelMouseExited
          CStitchLabel.setForeground(new Color(242,194,195));
          CStitchLabel.setBackground(new Color(49,46,54));
    }//GEN-LAST:event_CStitchLabelMouseExited

    private void MintLynxLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MintLynxLabelMouseEntered
        MintLynxLabel.setForeground(new Color(223,102,105));
        MintLynxLabel.setBackground(new Color(81,75,88));
    }//GEN-LAST:event_MintLynxLabelMouseEntered

    private void MintLynxLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MintLynxLabelMouseExited
         MintLynxLabel.setForeground(new Color(242,194,195));
         MintLynxLabel.setBackground(new Color(49,46,54));
    }//GEN-LAST:event_MintLynxLabelMouseExited

    private void FileSporeLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FileSporeLabelMouseEntered
        FileSporeLabel.setForeground(new Color(223,102,105));
        FileSporeLabel.setBackground(new Color(81,75,88));
    }//GEN-LAST:event_FileSporeLabelMouseEntered

    private void FileSporeLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FileSporeLabelMouseExited
      FileSporeLabel.setForeground(new Color(242,194,195));
        FileSporeLabel.setBackground(new Color(49,46,54));
    }//GEN-LAST:event_FileSporeLabelMouseExited

    private void MintRequestLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MintRequestLabelMouseEntered
       MintRequestLabel.setText("Mint Request");
       
    }//GEN-LAST:event_MintRequestLabelMouseEntered

    private void MintRequestLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MintRequestLabelMouseExited
        MintRequestLabel.setText("");
        MintRequestLabel.setBackground(new Color(43,43,43));
    }//GEN-LAST:event_MintRequestLabelMouseExited

    private void FileHaulLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FileHaulLabelMouseEntered
         FileHaulLabel.setText("File Haul");
    }//GEN-LAST:event_FileHaulLabelMouseEntered

    private void FileHaulLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FileHaulLabelMouseExited
        FileHaulLabel.setText("");
        FileHaulLabel.setBackground(new Color(43,43,43));
    }//GEN-LAST:event_FileHaulLabelMouseExited

    private void PreferencesLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PreferencesLabelMouseEntered
       PreferencesLabel.setText("Preferences");
    }//GEN-LAST:event_PreferencesLabelMouseEntered

    private void PreferencesLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PreferencesLabelMouseExited
        PreferencesLabel.setText("");
        PreferencesLabel.setBackground(new Color(43,43,43));
    }//GEN-LAST:event_PreferencesLabelMouseExited

    private void IdentityLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IdentityLabelMouseEntered
       IdentityLabel.setText("Identity");
    }//GEN-LAST:event_IdentityLabelMouseEntered

    private void IdentityLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IdentityLabelMouseExited
        IdentityLabel.setText("");
        IdentityLabel.setBackground(new Color(43,43,43));
    }//GEN-LAST:event_IdentityLabelMouseExited

    private void MintRequestLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MintRequestLabelMouseClicked
       MintRequestLabel.setBackground(new Color(53,53,53));
       //START MINTREQUESTS
         MintRequests mr=new MintRequests();
	  mr.pack();
          mr.setLocationRelativeTo(null);
          mr.setVisible(true);
    }//GEN-LAST:event_MintRequestLabelMouseClicked

    private void FileHaulLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FileHaulLabelMouseClicked
        FileHaulLabel.setBackground(new Color(53,53,53));
    }//GEN-LAST:event_FileHaulLabelMouseClicked

    private void PreferencesLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PreferencesLabelMouseClicked
        PreferencesLabel.setBackground(new Color(53,53,53));
        //START PREFERENCES
          Preferences pr=Preferences.startPreferences(alias);
	  pr.pack();
          pr.setLocationRelativeTo(null);
          pr.setVisible(true);
    }//GEN-LAST:event_PreferencesLabelMouseClicked

    private void IdentityLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IdentityLabelMouseClicked
       IdentityLabel.setBackground(new Color(53,53,53));
       //START IDENTITY
         Identity identity=Identity.startIdentity(alias);
	  identity.pack();
          identity.setLocationRelativeTo(null);
          identity.setVisible(true);
          SwingWorker sw=new SwingWorker() {
           @Override
           protected Object doInBackground() throws Exception {
              while(identity.getInstance()!=null){
                  Thread.sleep(3000);
                  
              }
              setPfp();
            return null;
           }
        
           };
                  
             sw.execute();
          
          
          
        
  
         
    }//GEN-LAST:event_IdentityLabelMouseClicked

    private void CStitchPartyLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CStitchPartyLabelMouseClicked
       CodeStitchPanel.setVisible(false);
       CodeStitchPartyPanel.setVisible(true);
       MintLynxPanel.setVisible(false);
       FileSporePanel.setVisible(false);
    }//GEN-LAST:event_CStitchPartyLabelMouseClicked

    private void CStitchLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CStitchLabelMouseClicked
       CodeStitchPanel.setVisible(true);
       CodeStitchPartyPanel.setVisible(false);
       MintLynxPanel.setVisible(false);
       FileSporePanel.setVisible(false);
    }//GEN-LAST:event_CStitchLabelMouseClicked

    private void MintLynxLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MintLynxLabelMouseClicked
       CodeStitchPanel.setVisible(false);
       CodeStitchPartyPanel.setVisible(false);
       MintLynxPanel.setVisible(true);
       FileSporePanel.setVisible(false);
       initMintLynx();
    }//GEN-LAST:event_MintLynxLabelMouseClicked

    private void FileSporeLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FileSporeLabelMouseClicked
      CodeStitchPanel.setVisible(false);
       CodeStitchPartyPanel.setVisible(false);
       MintLynxPanel.setVisible(false);
       FileSporePanel.setVisible(true);
       
    }//GEN-LAST:event_FileSporeLabelMouseClicked

    private void SendSPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SendSPanelMouseClicked
      
    }//GEN-LAST:event_SendSPanelMouseClicked

    private void TabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabbedPaneStateChanged
       
		    JTabbedPane tabbedPane = (JTabbedPane)evt.getSource();

		    int tab = tabbedPane.getSelectedIndex();
                    if(tab==1)
                    {
                        initRSyntax(SendSPanel);
                    }

    }//GEN-LAST:event_TabbedPaneStateChanged

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        //verif ca e trimis catre cineva
        if (!(chatTextArea.getText().equals(""))) {
            bubbler = new Bubbler(chatTextArea.getText(), SENT);
            bubbler.paintRightBubble(scrollablePanel);
            chatTextArea.setText("");
        }
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel4KeyPressed
     
    }//GEN-LAST:event_jLabel4KeyPressed

    private void searchPeerTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchPeerTextFieldFocusGained
       searchPeerTextField.setText("");
    }//GEN-LAST:event_searchPeerTextFieldFocusGained

    private void searchPeerTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchPeerTextFieldFocusLost
        searchPeerTextField.setText("Find someone...");
    }//GEN-LAST:event_searchPeerTextFieldFocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
       int poz=sporeTable.getSelectedRow();
       selectedFile=mintFiles.get(poz).getFileName();
    }//GEN-LAST:event_jButton2MouseClicked
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MintwireClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MintwireClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MintwireClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MintwireClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MintwireClientGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CStitchLabel;
    private javax.swing.JLabel CStitchPartyLabel;
    private javax.swing.JPanel CodeStitchPanel;
    private javax.swing.JPanel CodeStitchPartyPanel;
    private javax.swing.JLabel FileHaulLabel;
    private javax.swing.JLabel FileSporeLabel;
    private javax.swing.JPanel FileSporePanel;
    private javax.swing.JLabel IdentityLabel;
    private javax.swing.JLayeredPane MainLayeredPane;
    private javax.swing.JLabel MintLynxLabel;
    private javax.swing.JPanel MintLynxPanel;
    private javax.swing.JLabel MintRequestLabel;
    private javax.swing.JLabel PreferencesLabel;
    private javax.swing.JPanel RequestSPanel;
    private javax.swing.JPanel SendSPanel;
    private javax.swing.JTabbedPane TabbedPane;
    private javax.swing.JScrollPane chatScrollPane;
    private javax.swing.JTextArea chatTextArea;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel pfpLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField searchPeerTextField;
    private javax.swing.JButton sendButton;
    private javax.swing.JScrollPane sporeScroll;
    private javax.swing.JButton sporeSearch;
    private javax.swing.JTable sporeTable;
    private javax.swing.JTextField sporeText;
    // End of variables declaration//GEN-END:variables
}
