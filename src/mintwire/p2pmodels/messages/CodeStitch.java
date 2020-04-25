
package mintwire.p2pmodels.messages;

import rice.p2p.commonapi.Message;


public class CodeStitch implements Message{
    private String sender;
    private String receiver;
    private String language;
    private String code;

    public CodeStitch(String sender, String receiver, String language, String code) {
        this.sender = sender;
        this.receiver = receiver;
        this.language = language;
        this.code = code;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getLanguage() {
        return language;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "CodeStitch{" + "sender=" + sender + ", receiver=" + receiver + ", language=" + language+ '}';
    }
    
    
    @Override
    public int getPriority() {
       return Message.LOW_PRIORITY;
    }
    
}
