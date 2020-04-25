
package mintwire.p2pmodels.apps;

import mintwire.p2pmodels.MintNode;
import mintwire.p2pmodels.messages.CodeStitch;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;


public class CodeStitchApp implements Application{
private Endpoint endpoint;
private MintNode mintNode;

    public CodeStitchApp(MintNode mintNode) {
        this.mintNode = mintNode;
        this.endpoint=mintNode.getNode().buildEndpoint(this, "stitchinstance");
        
        this.endpoint.register();
    }
    public MintNode getMintNode()
    {
        return mintNode;
    }
    public void routeCodeStitch(Id id,CodeStitch stitch){
        endpoint.route(id, stitch, null);
    }

    @Override
    public boolean forward(RouteMessage rm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deliver(Id id, Message msg) {
       CodeStitch stitch=(CodeStitch) msg;
        System.out.println(stitch.getSender()+" pentru "+stitch.getReceiver());
    }

    @Override
    public void update(NodeHandle nh, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
