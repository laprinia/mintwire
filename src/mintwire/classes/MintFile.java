package mintwire.classes;

import java.io.Serializable;
import java.text.DecimalFormat;
import rice.pastry.NodeHandle;

public class MintFile implements Serializable {
    private String filePath;
    private String fileName;
    private long size;
    private String alias;
    private NodeHandle handle;
    private Boolean isSelected = false;

    public MintFile(String filePath,String fileName, long size, String alias, NodeHandle handle){
        this.filePath=filePath;
        this.fileName = fileName;
        this.size = size;
        this.alias=alias;
        this.handle=handle;

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDetails() {
       if(size <= 0) return "0";
    final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
    return "File Size: "+ new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public String getAlias() {
        return alias;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setAlias(String alias) {
        
        this.alias = alias;
    }

    public boolean isItSelected() {
        return isSelected;
    }

    public NodeHandle getHandle() {
        return handle;
    }

    public void setHandle(NodeHandle handle) {
        this.handle = handle;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MintFile{filePath=").append(filePath);
        sb.append(", fileName=").append(fileName);
        sb.append(", size=").append(size);
        sb.append(", alias=").append(alias);
        sb.append(", handle=").append(handle);
        sb.append(", isSelected=").append(isSelected);
        sb.append('}');
        return sb.toString();
    }
    
     
   

}
