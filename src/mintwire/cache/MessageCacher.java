
package mintwire.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import mintwire.p2pmodels.messages.MintMessage;
import rice.p2p.commonapi.Id;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MessageCacher {  
    private Cache<Id, ArrayList<MintMessage>> cache;
 
    
public MessageCacher(){
    cache=Caffeine.newBuilder()
            .expireAfterWrite(4, TimeUnit.HOURS)
            .build();
    
}
public void cache(Id id, MintMessage mm){
    if(cache.getIfPresent(id)!=null){
            ArrayList<MintMessage> current=cache.getIfPresent(id);
            current.add(mm);
            cache.put(id,current);
        }
        else {
            ArrayList<MintMessage> list = new ArrayList<>();
            list.add(mm);
            cache.put(id, list);
        }
}

public ArrayList<MintMessage> getMessagesById(Id id){
    ArrayList<MintMessage> mm=cache.getIfPresent(id);
    
    return mm;
}


}
   
    

