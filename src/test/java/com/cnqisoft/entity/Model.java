package com.cnqisoft.entity;

import javax.validation.constraints.NotNull;
import com.cnqisoft.annotation.EnumValue;
import com.cnqisoft.enums.FileType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.cnqisoft.enums.TranslateStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.cnqisoft.enums.Sync;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Model {

    private Long id;

    @NotNull
    private String modelName;

    @EnumValue(FileType.class)
    private String fileType;

    @EnumValue(TranslateStatus.class)
    private String translateStatus;

    @EnumValue(Sync.class)
    private Integer sync;

    private String modelFilePath;

    @NotNull
    private Long fileId;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    };

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    };

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileType() {
        return fileType;
    };

    public void setTranslateStatus(String translateStatus) {
        this.translateStatus = translateStatus;
    }

    public String getTranslateStatus() {
        return translateStatus;
    };

    public void setSync(Integer sync) {
        this.sync = sync;
    }

    public Integer getSync() {
        return sync;
    };

    public void setModelFilePath(String modelFilePath) {
        this.modelFilePath = modelFilePath;
    }

    public String getModelFilePath() {
        return modelFilePath;
    };

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getFileId() {
        return fileId;
    };


}
