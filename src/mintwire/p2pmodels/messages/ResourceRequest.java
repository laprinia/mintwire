
package mintwire.p2pmodels.messages;

import java.util.ArrayList;
import rice.p2p.commonapi.Message;
import rice.pastry.NodeHandle;


public class ResourceRequest implements Message{
   private ArrayList<String> filePaths=new ArrayList<>();
   private NodeHandle nh;
   private String alias;

   
   public ResourceRequest(String alias,ArrayList<String> paths, NodeHandle nh){
       this.alias=alias;
       this.filePaths=paths;
       this.nh=nh;
   }

    public ArrayList<String> getFilePaths() {
        return filePaths;
    }

    public NodeHandle getNh() {
        return nh;
    }
   
    @Override
    public int getPriority() {
        return LOW_PRIORITY;
    }

    public String getAlias() {
        return alias;
    }
   
   
}
