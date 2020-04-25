package mintwire.p2pmodels;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import org.graalvm.compiler.nodes.BreakpointNode;
import rice.environment.Environment;
import rice.pastry.NodeHandle;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.PastryNodeFactory;
import rice.pastry.leafset.LeafSet;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.RandomNodeIdFactory;

public class MintNode {

    private Environment environment;
    private PastryNode node;
    private JLabel label;

    public MintNode(int bindport, InetSocketAddress bootaddr, String alias, String status,Environment env) throws InterruptedException, IOException,BindException {
       
        environment = new Environment();
        environment.getParameters().setString("nat_search_policy", "never");

        NodeIdFactory nodeIdFactory = new RandomNodeIdFactory(env);
      
       PastryNodeFactory pastryNodeFactory = new SocketPastryNodeFactory(nodeIdFactory, bindport, environment);
      
            node = pastryNodeFactory.newNode();
            
            
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
                  label = new JLabel("<html><center> You succesfully entered the ring" );
            label.setHorizontalAlignment(SwingConstants.CENTER);
            JOptionPane.showMessageDialog(null, label, "You are in!", JOptionPane.INFORMATION_MESSAGE);
            }

        
        

    }

    public Environment getEnvironment() {
        return environment;
    }

    public PastryNode getNode() {
        return node;
    }

    public Map<String, Entry<String, String>> getLeafSetData() {
        Map<String, Entry<String, String>> leafSetData = new HashMap<String, Entry<String, String>>();

        LeafSet set = node.getLeafSet();
        List<NodeHandle> handles = set.asList();

        for (NodeHandle nh : handles) {
            if (!(leafSetData.containsKey(nh.getId().toString()))) {
                leafSetData.put(nh.getId().toString(), new SimpleEntry(nh.getLocalNode().alias, nh.getLocalNode().status));
            }
        }
        return leafSetData;

    }

}
