
package mintwire.p2pmodels.apps;

import mintwire.p2pmodels.messages.MintMessage;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.pastry.PastryNode;

public class MintMessagingApp implements Application{
    private PastryNode pastryNode;
    public MintMessagingApp(PastryNode pastryNode){
        this.pastryNode=pastryNode;
    }
    @Override
    public boolean forward(RouteMessage rm) {
        return true;
    }

    @Override
    public void deliver(Id id, Message msg) {
        MintMessage mintMessage=(MintMessage)msg;
        mintMessage.toString();
    }

    @Override
    public void update(NodeHandle nh, boolean bln) {
        
    }
    
}
