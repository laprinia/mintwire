package mintwire.p2pmodels.apps;

import java.awt.Dimension;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import mintwire.PartyState;

import mintwire.RandomString;
import mintwire.jframes.ConnectedPeers;
import mintwire.p2pmodels.MintNode;
import mintwire.p2pmodels.messages.HostTopic;
import mintwire.p2pmodels.messages.PartyStitch;
import mintwire.p2pmodels.messages.PeerInfo;
import mintwire.p2pmodels.messages.TerminalMessage;
import mintwire.panels.peerlist.PartyPeerPanel;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import rice.environment.Environment;

import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.p2p.scribe.Scribe;

import rice.p2p.scribe.ScribeContent;
import rice.p2p.scribe.ScribeMultiClient;

import rice.p2p.scribe.Topic;

import rice.p2p.commonapi.CancellableTask;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Node;
import rice.p2p.scribe.ScribeImpl;
import rice.pastry.PastryNode;
import rice.pastry.commonapi.PastryIdFactory;

/**
 *
 * @author Lavinia
 */
public class CodeStitchPartyClient implements Application, ScribeMultiClient {

    private Box box;
    private JLabel label;
    private HashMap<NodeHandle, PartyPeerPanel> peerPanels=new HashMap<>();
    private HashMap<NodeHandle, PeerInfo> connectedPeers = new HashMap<>();
    private HashMap<String, HostTopic> availableTopics = new HashMap<>();
    private CancellableTask publishTask;
    private MintNode mintNode;
    private PastryNode node;
    private RSyntaxTextArea partyArea;
    private Scribe scribe;
    private Topic topic;
    protected Endpoint endpoint;
    private Environment environment;
    private NodeHandle nh;

    public CodeStitchPartyClient(Node node) {
        this.node = (PastryNode) node;
        endpoint = node.buildEndpoint(this, "partyinstance");
        scribe = new ScribeImpl(node, "scribepartyinstance");
        environment = node.getEnvironment();
        endpoint.register();
    }

    public void createTopic(NodeHandle nh, String alias) {
        //TODO daca nu e root, se trimite ultimul stitch al parintelul
        this.nh = nh;
        this.topic = new Topic(new PastryIdFactory(environment), "partytopic");

        scribe.subscribe(topic, this, null, nh);

    }

    public void setPublishInfo(MintNode mn, RSyntaxTextArea textArea, Box box) {
        mintNode = mn;
        partyArea = textArea;
        this.box = box;

    }

    private List<rice.pastry.NodeHandle> getUniquesHandles() {
        List<rice.pastry.NodeHandle> handles = node.getLeafSet().asList();
        HashSet<Id> uniqueHandles = new HashSet<>();
        handles.removeIf(e -> !uniqueHandles.add(e.getId()));
        return handles;
    }

    public HostTopic sendCredentials() {

        System.err.println(topic);
        HostTopic hostTopic = new HostTopic(RandomString.generatePassphrase(), topic, new PeerInfo(mintNode.getNode().getLocalHandle(), mintNode.getNode().alias, mintNode.getNode().status, false));
        for (NodeHandle h : getUniquesHandles()) {
            System.err.println("sending topic to " + h.toString());
            endpoint.route(null, hostTopic, h);
        }
        return hostTopic;
    }

    public void joinTopic(NodeHandle nodeHandle, Topic topic, NodeHandle host) {

        this.topic = topic;
        scribe.subscribe(topic, this, null, nh);
        PeerInfo peerInfo=new PeerInfo((rice.pastry.NodeHandle)mintNode.getNode().getLocalHandle(), mintNode.getNode().alias, mintNode.getNode().status, false);
        endpoint.route(null,peerInfo ,host );

    }

    public void unsubscribe() {
        scribe.unsubscribe(topic, this);
        
        

    }

    public void destroy() {
        Collection<NodeHandle> children= getChildren();
        for (NodeHandle ch : children) {
            endpoint.route(null, new TerminalMessage(mintNode.getNode().alias), ch);
        }
        
        scribe.unsubscribe(topic, this);
        scribe.destroy();
        publishTask.cancel();

    }

    public void startPublishTask() {

        publishTask = endpoint.scheduleMessage(new PublishContent(), 5000, 5000);

    }

    public NodeHandle getParent() {
        return scribe.getParent(topic);
    }

    public void deliver(Id id, Message message) {
        if (message instanceof PublishContent && topic != null) {
            sendMulticast();
        } else if (message instanceof HostTopic) {
            HostTopic topic = (HostTopic) message;
            if (!availableTopics.containsKey(topic.getPassphrase())) {
                availableTopics.put(topic.getPassphrase(), topic);
                System.err.println("RECEIVED +\n" + topic.toString());
            }

        } else if (message instanceof TerminalMessage) {
            String hostAlias=((TerminalMessage) message).getAlias();
            scribe.unsubscribe(topic, this);
            label = new JLabel("<html><center>"+hostAlias+"(host) stopped the session.");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            JOptionPane.showMessageDialog(null, label, "Party stopped", JOptionPane.INFORMATION_MESSAGE);
            box.removeAll();
            box.revalidate();
            partyArea.setText("");
            partyArea.repaint();
            mintwire.jframes.MintwireClientGUI.partyState = PartyState.NotStarted;
        } else if(message instanceof PeerInfo) {
            PeerInfo peerInfo= (PeerInfo) message;
            connectedPeers.put(peerInfo.getNodeHandle(), peerInfo);
            PartyPeerPanel partyPeerPanel = new PartyPeerPanel(peerInfo);
            partyPeerPanel.setPreferredSize(new Dimension(176, 132));
            peerPanels.put(peerInfo.getNodeHandle(), partyPeerPanel);
            box.add(partyPeerPanel);
            box.revalidate();
        }
    }

    public void sendMulticast() {
        scribe.publish(topic, new PartyStitch(mintNode.getNode().alias, partyArea.getSyntaxEditingStyle(), partyArea.getText()));

    }

    public void sendAnycast() {

        scribe.anycast(topic, new PartyStitch(mintNode.getNode().alias, partyArea.getSyntaxEditingStyle(), partyArea.getText()));

    }

    @Override
    public boolean forward(RouteMessage rm) {
        return true;
    }

    @Override
    public void update(NodeHandle nh, boolean bln) {

    }

    @Override
    public boolean anycast(Topic topic, ScribeContent sc) {
        return true;
    }

    @Override
    public void deliver(Topic topic, ScribeContent sc) {
       
        PartyStitch partyStitch = (PartyStitch) sc;
        partyArea.setText(partyStitch.getCode());
        System.out.println(partyStitch.toString());
        partyArea.setSyntaxEditingStyle(partyStitch.getLanguage());
      

    }

    @Override
    public void childAdded(Topic topic, NodeHandle nh) {
      
    }

    @Override
    public void childRemoved(Topic topic, NodeHandle nh) {
       
       PeerInfo peerInfo=connectedPeers.get(nh);
       connectedPeers.remove(nh);
       box.remove(peerPanels.get(nh)); box.revalidate(); peerPanels.remove(nh);
        label=new JLabel("<html><center>Your peer "+ peerInfo.getAlias()+" left the party.");
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    JOptionPane.showMessageDialog(null, label, "Peer left", JOptionPane.INFORMATION_MESSAGE);
        
    }

    @Override
    public void subscribeFailed(Topic topic) {

        System.err.println("Subscribed fail");
    }

    @Override
    public void subscribeFailed(Collection<Topic> clctn) {
        System.err.println("Subscribed fail");
    }

    @Override
    public void subscribeSuccess(Collection<Topic> clctn) {
        System.err.println("Subscribed succesfully");
    }

    class PublishContent implements Message {

        public int getPriority() {
            return MAX_PRIORITY;
        }
    }

    public boolean isRoot() {
        return scribe.isRoot(topic);
    }

    public Collection<NodeHandle> getChildren() {

        return scribe.getChildrenOfTopic(topic);

    }

    public HashMap<String, HostTopic> getAvailableTopics() {
        return availableTopics;
    }

}
