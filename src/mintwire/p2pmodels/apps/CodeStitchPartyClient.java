package mintwire.p2pmodels.apps;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.swing.JOptionPane;
import mintwire.RandomString;
import mintwire.p2pmodels.MintNode;
import mintwire.p2pmodels.messages.HostTopic;
import mintwire.p2pmodels.messages.PartyStitch;
import mintwire.p2pmodels.messages.PeerInfo;
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
        topic = new Topic(new PastryIdFactory(environment), "partytopic");

        scribe.subscribe(topic, this, null, nh);
     


    }
     public void setPublishInfo(MintNode mn, RSyntaxTextArea textArea) {
        mintNode = mn;
        partyArea = textArea;
       
        
    }
    public HostTopic sendCredentials(){
        List<rice.pastry.NodeHandle> handles = node.getLeafSet().asList();
        HashSet<Id> uniqueHandles = new HashSet<>();
        handles.removeIf(e -> !uniqueHandles.add(e.getId()));
        HostTopic hostTopic=new HostTopic(RandomString.generatePassphrase(), topic,new PeerInfo(mintNode.getNode().getLocalHandle(), mintNode.getNode().alias, mintNode.getNode().status, false));
        for (NodeHandle h : handles) {
            System.err.println("sending topic to "+h.toString());
            endpoint.route(null,hostTopic, h);
        }
       return hostTopic;
    }

    public void joinTopic(NodeHandle nodeHandle,Topic topic) {
      
            this.topic = topic;
            scribe.subscribe(topic, this, null, nh);
   
    }

   

    public void unsubscribe() {
        scribe.unsubscribe(topic, this);
       
    }
    public void destroy(){
         scribe.destroy();
    }

    public void startPublishTask() {

        publishTask = endpoint.scheduleMessage(new PublishContent(), 5000, 5000);

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

        }
    }

    public void sendMulticast() {
      scribe.publish(topic, new PartyStitch(mintNode.getNode().alias, partyArea.getSyntaxEditingStyle(), partyArea.getText()));

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
       // System.err.println("IN DELIVERY PART");
        PartyStitch partyStitch = (PartyStitch) sc;
        //int caretPosition = partyArea.getCaretPosition();
        
        partyArea.setText(partyStitch.getCode());
         System.out.println(partyStitch.toString());
        partyArea.setSyntaxEditingStyle(partyStitch.getLanguage());
       // partyArea.setCaretPosition(caretPosition);

    }

    @Override
    public void childAdded(Topic topic, NodeHandle nh) {
        //todo paint them in
        System.err.println("Child added " + nh);
    }

    @Override
    public void childRemoved(Topic topic, NodeHandle nh) {
        System.err.println("Child removed " + nh);
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
