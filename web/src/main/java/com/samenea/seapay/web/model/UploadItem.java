package com.samenea.seapay.web.model;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author: Soroosh Sarabadani
 * Date: 6/2/13
 * Time: 11:17 AM
 */

public class UploadItem {
    private String filename;
    private CommonsMultipartFile fileData;


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public CommonsMultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(CommonsMultipartFile fileData) {
        this.fileData = fileData;
    }

}
