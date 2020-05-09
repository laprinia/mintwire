package mintwire.p2pmodels;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import mintwire.p2pmodels.apps.CodeStitchApp;
import mintwire.p2pmodels.apps.MintMessagingApp;
import mintwire.p2pmodels.apps.SendPeerInfoApp;

import rice.environment.Environment;


import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.PastryNodeFactory;

import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.RandomNodeIdFactory;

public class MintNode {
    private MintMessagingApp messagingApp;
    private SendPeerInfoApp peerInfoApp;
    private CodeStitchApp codeStitchApp;
    private Environment env;
    private PastryNode node;
    private JLabel label;

    public MintNode(int bindport, InetSocketAddress bootaddr, String alias, String status) throws InterruptedException, IOException, BindException {

        env = new Environment();
        env.getParameters().setString("nat_search_policy", "never");

        NodeIdFactory nodeIdFactory = new RandomNodeIdFactory(env);

        PastryNodeFactory pastryNodeFactory = new SocketPastryNodeFactory(nodeIdFactory, bindport, env);

        node = pastryNodeFactory.newNode();
        //init apps
        codeStitchApp = new CodeStitchApp(node);
        peerInfoApp = new SendPeerInfoApp(node);
        messagingApp=new MintMessagingApp(node);
        node.boot(bootaddr);

        node.alias = alias;
        node.status = status;

        synchronized (node) {
            while (!node.isReady() && !node.joinFailed()) {

                node.wait(100);

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
    


}
