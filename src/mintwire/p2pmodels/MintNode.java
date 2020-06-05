package mintwire.p2pmodels;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import mintwire.p2pmodels.apps.CodeStitchApp;
import mintwire.p2pmodels.apps.CodeStitchPartyApp;
import mintwire.p2pmodels.apps.FileSporeApp;
import mintwire.p2pmodels.apps.MintMessagingApp;
import mintwire.p2pmodels.apps.ProfilePictureApp;
import mintwire.p2pmodels.apps.SendPeerInfoApp;
import mintwire.p2pmodels.apps.SendSharedResourceApp;

import org.jdesktop.swingx.util.OS;

import rice.environment.Environment;


import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.PastryNodeFactory;

import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.RandomNodeIdFactory;

public class MintNode {
    private final boolean isLinux = OS.isLinux();
    
    private String sharedPath = "C:\\MINTWIRE Shared";
    private ProfilePictureApp pfpApp;
    private SendSharedResourceApp sharedResourceApp;
    private FileSporeApp fileSporeApp;
    private MintMessagingApp messagingApp;
    private SendPeerInfoApp peerInfoApp;
    private CodeStitchApp codeStitchApp;
    private CodeStitchPartyApp codeStitchPartyApp;
    private Environment env;
    private PastryNode node;
    private JLabel label;

    public MintNode(int bindport, InetSocketAddress bootaddr, String alias, String status) throws InterruptedException, IOException, BindException {
         if (isLinux) {
            this.sharedPath = System.getProperty("user.home") + "/MINTWIRE Shared/";
        }
         //TODO READ PREF FILE FOR SHAREDPATH
        env = new Environment();
        env.getParameters().setString("nat_search_policy", "never");
        env.getParameters().setString(status, status);

        NodeIdFactory nodeIdFactory = new RandomNodeIdFactory(env);

        PastryNodeFactory pastryNodeFactory = new SocketPastryNodeFactory(nodeIdFactory, bindport, env);

        node = pastryNodeFactory.newNode();
        //init apps
        
        codeStitchApp = new CodeStitchApp(node);
        peerInfoApp = new SendPeerInfoApp(node);
        messagingApp=new MintMessagingApp(node);
        fileSporeApp=new FileSporeApp(node, sharedPath);
        pfpApp=new ProfilePictureApp(node);
        
        sharedResourceApp=new SendSharedResourceApp(node);
        node.boot(bootaddr);

        node.alias = alias;
        node.status = status;

        synchronized (node) {
            while (!node.isReady() && !node.joinFailed()) {

                node.wait(10);

                if (node.joinFailed()) {
                    System.err.println("node fail");
                    throw new InterruptedException(node.joinFailedReason().getMessage());

                }
            }
            label = new JLabel("<html><center> You succesfully entered the ring");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            JOptionPane.showMessageDialog(null, label, "You are in!", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public Environment getEnvironment() {
        return env;
    }

    public PastryNode getNode() {
        return node;
    }

    public SendPeerInfoApp getPeerInfoApp() {
        return peerInfoApp;
    }

    public CodeStitchApp getCodeStitchApp() {
        return codeStitchApp;
    }

    public MintMessagingApp getMessagingApp() {
        return messagingApp;
    }

    public String getSharedPath() {
        return sharedPath;
    }

    public SendSharedResourceApp getSharedResourceApp() {
        return sharedResourceApp;
    }

    public CodeStitchPartyApp getCodeStitchPartyApp() {
        return codeStitchPartyApp;
    }

    public FileSporeApp getFileSporeApp() {
        return fileSporeApp;
    }

    public ProfilePictureApp getPfpApp() {
        return pfpApp;
    }
    
    


}
