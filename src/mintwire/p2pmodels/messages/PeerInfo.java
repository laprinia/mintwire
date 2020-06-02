
package mintwire.p2pmodels.messages;

import rice.p2p.commonapi.Message;
import rice.pastry.NodeHandle;


/**
 *
 * @author Lavinia
 */
public class PeerInfo implements Message{
    private NodeHandle nodeHandle;
    private String alias;
    private String status;
    private boolean isResponse;
   
    public PeerInfo(NodeHandle nh,String alias, String status,boolean isResponse){
        this.isResponse=isResponse;
        this.nodeHandle=nh;
        this.alias=alias;
        this.status=status;
     
             
    }

    public boolean isItResponse() {
        return isResponse;
    }
    

    public NodeHandle getNodeHandle() {
        return nodeHandle;
    }

    public String getAlias() {
        return alias;
    }

    public String getStatus() {
        return status;
    }
    
    @Override
    public int getPriority() {
       return Message.LOW_PRIORITY;
    }

  
    

    @Override
    public String toString() {
        return alias+" "+status+"["+nodeHandle.getId()+"]";
    }
    
    
}
