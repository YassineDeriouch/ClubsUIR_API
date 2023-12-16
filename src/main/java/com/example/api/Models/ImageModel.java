package com.example.api.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author Yassine Deriouch
 * @project ClubsUIR
 */

@Data
public class ImageModel {

    @JsonIgnore
    private byte[] imageFile;

    private String fileName;

    @JsonIgnore
    private String fileType,filePath;
}
