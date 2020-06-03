package com.cnqisoft.entity;

public class Model {

    private Long id;

    private String modelName;

    private String fileType;

    private String translateStatus;

    private Integer sync;

    private MultipartFile modelFile;

    private Long fileId;

    private String integrate;

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

    public void setModelFile(MultipartFile modelFile) {
        this.modelFile = modelFile;
    }

    public MultipartFile getModelFile() {
        return modelFile;
    };

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getFileId() {
        return fileId;
    };

    public void setIntegrate(String integrate) {
        this.integrate = integrate;
    }

    public String getIntegrate() {
        return integrate;
    };


}
