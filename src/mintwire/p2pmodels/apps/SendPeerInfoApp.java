
package mintwire.p2pmodels.apps;


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
public class SendPeerInfoApp implements Application{
  private PastryNode pastryNode;
  private Endpoint endpoint;
    public SendPeerInfoApp(PastryNode pastryNode){
        this.pastryNode=pastryNode;
        this.endpoint=pastryNode.buildEndpoint(this,"peerinfoinstance");
        this.endpoint.register();
    }
    public void routePeerInfo(Id id,PeerInfo info){
        endpoint.route(id, info, null);
    }
    @Override
    public boolean forward(RouteMessage rm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deliver(Id id, Message msg) {
        PeerInfo peerInfo=(PeerInfo)msg;
        
        
        System.out.println("received request for info from "+peerInfo.getAlias());
        //send back info using nodehandle
        //impacheteaza frumusetea aia de poza de profil si pune o si pe aia
    }

    @Override
    public void update(NodeHandle nh, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
