
package mintwire.p2pmodels.apps;

import mintwire.p2pmodels.MintNode;
import mintwire.p2pmodels.messages.CodeStitch;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.pastry.PastryNode;


public class CodeStitchApp implements Application{
private Endpoint endpoint;
private PastryNode pastryNode;

    public CodeStitchApp(PastryNode pastryNode) {
        this.pastryNode = pastryNode;
        this.endpoint=pastryNode.buildEndpoint(this, "stitchinstance");
        
        this.endpoint.register();
    }
    public PastryNode getPastryNode(){
        return pastryNode;
    }
    public void routeCodeStitch(Id id,CodeStitch stitch){
        endpoint.route(id, stitch, null);
    }

    @Override
    public boolean forward(RouteMessage rm) {
       return true;
    }

    @Override
    public void deliver(Id id, Message msg) {
       CodeStitch stitch=(CodeStitch) msg;
        System.out.println(stitch.getSender()+" pentru "+stitch.getReceiver());
    }

    @Override
    public void update(NodeHandle nh, boolean bln) {
       
    }
    
}
