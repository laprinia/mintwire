
package mintwire.p2pmodels.apps;


import mintwire.p2pmodels.messages.CodeStitch;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.pastry.PastryNode;
import java.util.ArrayList;

public class CodeStitchApp implements Application{
private final int STITCH_CAP=70;
private Endpoint endpoint;
private PastryNode pastryNode;
private ArrayList<CodeStitch> codeStitches=new ArrayList<>();

    public CodeStitchApp(PastryNode pastryNode) {
        this.pastryNode = pastryNode;
        this.endpoint=pastryNode.buildEndpoint(this, "stitchinstance");
        
        this.endpoint.register();
    }
    public ArrayList<CodeStitch> getCodeArrayList(){
        return this.codeStitches;
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
       if(codeStitches.size()<=STITCH_CAP){
        codeStitches.add(stitch); 
       }else{
           System.err.println("Get rid of some stitches");
       }
       
    }

    @Override
    public void update(NodeHandle nh, boolean bln) {
       
    }
    
}
