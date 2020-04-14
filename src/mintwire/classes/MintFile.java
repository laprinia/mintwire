
package mintwire.classes;


public class MintFile {
   private String fileName;
   private String details;
   private String alias;

    public MintFile(String fileName, String details, String alias) {
        this.fileName = fileName;
        this.details = details;
        this.alias = alias;
     
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

  
   
    
}
