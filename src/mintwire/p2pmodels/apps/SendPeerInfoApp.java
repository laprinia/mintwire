package mintwire.p2pmodels.apps;

import java.util.ArrayList;
import mintwire.p2pmodels.MintNode;
import mintwire.p2pmodels.messages.CodeStitch;
import mintwire.p2pmodels.messages.PeerInfo;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.pastry.PastryNode;

/**
 *
 * @author Lavinia
 */
public class SendPeerInfoApp implements Application {

    private PastryNode pastryNode;
    private Endpoint endpoint;
    private ArrayList<PeerInfo> peerList = new ArrayList<>();

    public SendPeerInfoApp(PastryNode pastryNode) {
        this.pastryNode = pastryNode;
        this.endpoint = pastryNode.buildEndpoint(this, "peerinfoinstance");
        this.endpoint.register();
    }

    public void requestPeerInfo(Id id, PeerInfo info) {
        endpoint.route(id, info, null);
    } 

    public ArrayList<PeerInfo> getPeerList() {
        return peerList;
    }

    @Override
    public boolean forward(RouteMessage rm) {
        return true;
    }

    @Override
    public void deliver(Id id, Message msg) {
        PeerInfo peerInfo = (PeerInfo) msg;

        if (peerInfo.isItResponse()) {
           
            if (!peerList.contains(peerInfo)) {
                
                peerList.add(peerInfo);
              
            }

        } else {
           
            requestPeerInfo(peerInfo.getNodeHandle().getId(), new PeerInfo(pastryNode.getLocalHandle(), pastryNode.alias, pastryNode.status, true));
          
        }
    }

    @Override
    public void update(NodeHandle nh, boolean bln) {

    }

}
