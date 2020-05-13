
package mintwire.p2pmodels.apps;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import mintwire.cache.MessageCacher;
import mintwire.chatpanels.Bubbler;
import mintwire.p2pmodels.messages.MintMessage;
import mintwire.p2pmodels.messages.PeerInfo;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.pastry.PastryNode;

public class MintMessagingApp implements Application{
    final Color RECEIVED = new Color(170, 207, 255);
    private MessageCacher cacher;
    private JPanel scrollablePanel;
    private PastryNode pastryNode;
    private Endpoint endpoint;
    private Id currentId=null;
    
    public MintMessagingApp(PastryNode pastryNode){
        this.pastryNode=pastryNode;
        this.endpoint=pastryNode.buildEndpoint(this, "messageinstance");
        this.endpoint.register();
    }
    public void routeMessage(NodeHandle nh,MintMessage message){
        
          endpoint.route(null, message, nh);
      
    }
   
    @Override
    public boolean forward(RouteMessage rm) {
        return true;
    }

    @Override
    public void deliver(Id id, Message msg) {
        MintMessage mintMessage=(MintMessage)msg;
        System.err.println(msg.toString());
        if(currentId==null || scrollablePanel==null) return; //cache
        else{
           
            if(currentId.equals(mintMessage.getSenderId())){
                
                Bubbler bubbler = new Bubbler(mintMessage.getText(), RECEIVED);
                bubbler.paintLeftBubble(scrollablePanel, mintMessage.getDateStamp());
                //TODO NU PICTEZI BINE
            }
        }
        cacher.cache(mintMessage.getSenderId(), mintMessage);
    }

    @Override
    public void update(NodeHandle nh, boolean bln) {
        
    }

    public Id getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Id currentId) {
        this.currentId = currentId;
    }

    public void setScrollablePanel(JPanel scrollablePanel) {
        this.scrollablePanel = scrollablePanel;
    }
    public void setCacher(MessageCacher cacher){
        this.cacher=cacher;
    }

   
    
    
}
