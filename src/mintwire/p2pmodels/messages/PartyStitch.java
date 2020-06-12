
package mintwire.p2pmodels.messages;

import rice.p2p.scribe.ScribeContent;

/**
 *
 * @author Lavinia
 */
public class PartyStitch implements ScribeContent{
    private String sender;
    private String language;
    private String code;

    public PartyStitch(String sender, String language, String code) {
        this.sender = sender;
        this.language = language;
        this.code = code;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PartyStitch{sender=").append(sender);
        sb.append(", language=").append(language);
        sb.append(", code=").append(code);
        sb.append('}');
        return sb.toString();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    
    
    
}
