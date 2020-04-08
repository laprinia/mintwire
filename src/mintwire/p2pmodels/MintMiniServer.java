//package mintwire.p2pmodels;
//
//import java.net.ServerSocket;
//import java.net.Socket;
//import javax.swing.JOptionPane;
//
//public class MintMiniServer implements Runnable {
//    String alias;
//    Thread thread;
//    Socket socket;
//    ServerSocket miniServerSocket;
//    MintRequestHandler requestHandler;
//
//    MintMiniServer(String alias) {
//        this.alias=alias;
//        thread = new Thread(this);
//        thread.start();
//
//    }
//
//    @Override
//    public void run() {
//        try {
//            miniServerSocket = new ServerSocket(6424);
//            while (true) {
//                socket = miniServerSocket.accept();
//                //TODO PRIMIRE ALIAS SI STARE
//                
//                requestHandler=new MintRequestHandler(socket,alias);
//                
//                
//            }
//
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(null, "Error in MiniServer Thread", "Mini Server Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//}
