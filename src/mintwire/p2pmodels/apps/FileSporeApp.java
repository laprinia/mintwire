package mintwire.p2pmodels.apps;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import mintwire.p2pmodels.MintNode;
import org.mpisws.p2p.filetransfer.BBReceipt;
import org.mpisws.p2p.filetransfer.FileReceipt;
import org.mpisws.p2p.filetransfer.FileTransfer;
import org.mpisws.p2p.filetransfer.FileTransferCallback;
import org.mpisws.p2p.filetransfer.FileTransferImpl;
import org.mpisws.p2p.filetransfer.FileTransferListener;
import org.mpisws.p2p.filetransfer.Receipt;
import rice.Continuation;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.p2p.commonapi.appsocket.AppSocket;
import rice.p2p.commonapi.appsocket.AppSocketReceiver;
import rice.p2p.util.rawserialization.SimpleInputBuffer;
import rice.p2p.util.rawserialization.SimpleOutputBuffer;
import rice.pastry.PastryNode;

/**
 *
 * @author Lavinia
 */
public class FileSporeApp implements Application {

    private final int BYTE_BUFFER_SIZE = 4;
    private String sharedPath;
    private JLabel label;
    private Endpoint endpoint;
    private PastryNode pastryNode;
    private FileTransfer transfer;

    public FileSporeApp(PastryNode pastryNode, String sharedPath) {
        this.pastryNode = pastryNode;
        this.endpoint = pastryNode.buildEndpoint(this, "sporeinstance");
        this.sharedPath = sharedPath;
        endpoint.accept(new AppSocketReceiver() {
            @Override
            public void receiveSocket(AppSocket as) throws IOException {
                transfer = new FileTransferImpl(as, new FileTransferCallback() {
                    @Override
                    public void messageReceived(ByteBuffer bb) {

                    }

                    @Override
                    public void fileReceived(File file, ByteBuffer bb) {
                        try {
                            String fileName = new SimpleInputBuffer(bb).readUTF();
                            File destinationFile = new File(sharedPath + fileName);
                            
             label = new JLabel("<html><center>Trasfer succesful for "+destinationFile);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            JOptionPane.showMessageDialog(null, label, "FileSpore Transfer", JOptionPane.INFORMATION_MESSAGE);
                            

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void receiveException(Exception excptn) {
                        System.err.println("File Transfer Callback ex" + excptn.getMessage());
                    }
                }, FileSporeApp.this.pastryNode.getEnvironment());
                transfer.addListener(new SporeListener());
                endpoint.accept(this);
            }

            @Override
            public void receiveSelectResult(AppSocket as, boolean bln, boolean bln1) throws IOException {
                throw new RuntimeException();
            }

            @Override
            public void receiveException(AppSocket as, Exception excptn) {
                excptn.printStackTrace();
            }
        });

        endpoint.register();
    }

    class SporeListener implements FileTransferListener {

        @Override
        public void fileTransferred(FileReceipt fr, long l, long l1, boolean bln) {
            double percentage = 100.0 * l / l1;

        }

        @Override
        public void msgTransferred(BBReceipt bbr, int i, int i1, boolean bln) {
            double percentage = 100.0 * i / i1;
        }

        @Override
        public void transferCancelled(Receipt rcpt, boolean bln) {
            label = new JLabel("<html><center>Trasfer for " + rcpt + " has been canceled.");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            JOptionPane.showMessageDialog(null, label, "Canceled FileSpore Transfer", JOptionPane.INFORMATION_MESSAGE);
        }

        @Override
        public void transferFailed(Receipt rcpt, boolean bln) {
            label = new JLabel("<html><center>Trasfer for " + rcpt + " has failed.");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            JOptionPane.showMessageDialog(null, label, "Failed FileSpore Transfer", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public void sendFile(NodeHandle nh, String filePath) {
        endpoint.connect(nh, new AppSocketReceiver() {
            @Override
            public void receiveSocket(AppSocket as) throws IOException {
                FileTransfer senderTransfer = new FileTransferImpl(as, null, pastryNode.getEnvironment());
                senderTransfer.addListener(new SporeListener());

                ByteBuffer buffer = ByteBuffer.allocate(BYTE_BUFFER_SIZE);
                for (int i = 0; i < BYTE_BUFFER_SIZE; i++) {
                    buffer.put((byte) i);
                }
                buffer.flip();

                senderTransfer.sendMsg(buffer, (byte) 1, null);
                try {
                    final File f = new File(filePath);
                    if (!f.exists()) {
                        
                    label=new JLabel("<html><center>Your peer tried downloading "+filePath+", but it's not here.");
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    JOptionPane.showMessageDialog(null, label, "Request for spore failed", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(1);
                    }
                    SimpleOutputBuffer sobuf = new SimpleOutputBuffer();
                    sobuf.writeUTF(f.getName());

                    senderTransfer.sendFile(f, sobuf.getByteBuffer(), (byte) 2, new Continuation<FileReceipt, Exception>() {
                        @Override
                        public void receiveResult(FileReceipt r) {
                            System.out.println("Completed send for " + r);
                        }

                        @Override
                        public void receiveException(Exception e) {
                            System.out.println("Exception for sending: " + e.getMessage());
                        }
                    });
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void receiveSelectResult(AppSocket as, boolean bln, boolean bln1) throws IOException {
                throw new RuntimeException();
            }

            @Override
            public void receiveException(AppSocket as, Exception excptn) {
                excptn.printStackTrace();
            }
        }, 30000);
    }

    @Override
    public boolean forward(RouteMessage rm) {
        return true;
    }

    @Override
    public void deliver(Id id, Message msg) {
        System.err.println(msg.toString());
    }

    @Override
    public void update(NodeHandle nh, boolean bln) {

    }

}
