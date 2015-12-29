package net.loyin.netService.vo.fileVo;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Chao on 2015/11/30.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FileSaveMainData {


    @XmlElement(name = "file_id")
    @JsonProperty("file_id")
    private String fileId;


    @XmlElement(name = "file_name")
    @JsonProperty("file_name")
    private String fileName;

    @XmlElement(name = "file_path")
    @JsonProperty("file_path")
    private String filePath;

    @XmlElement(name = "fsize")
    @JsonProperty("fsize")
    private int fsize;
    @XmlElement(name = "save_path")
    @JsonProperty("save_path")
    private String savePath;

    @XmlElement(name = "relation_id")
    @JsonProperty("relation_id")
    private String relationId;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFsize() {
        return fsize;
    }

    public void setFsize(int fsize) {
        this.fsize = fsize;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }
}
