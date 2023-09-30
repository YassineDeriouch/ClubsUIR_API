package com.example.api.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseData {
    private String fileName;
    private String downloadURL;
    private String fileType;
    private long fileSize;

}
