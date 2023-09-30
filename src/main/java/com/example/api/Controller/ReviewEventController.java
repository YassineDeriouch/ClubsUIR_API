package com.example.api.Controller;

import com.example.api.Models.ReviewEventModel;
import com.example.api.Models.ReviewEvent_Post_Pre;
import com.example.api.Models.RoleModel;
import com.example.api.Service.ReviewEventService;
import com.example.api.Service.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileSystemException;

@RestController
@RequestMapping(value="reviewEvent")
public class ReviewEventController {

    //Ajouter les methodes du services ReviewEventService au controller :
    //Save review event to post_event table and upload the file to the database document table
    //Save review event to pre_event table and upload the file to the database document table

    @Autowired
    private ReviewEventService reviewEventService;


    @CrossOrigin
    @PostMapping(value = "/save/PostEvent/")
    public ResponseEntity<ReviewEventModel> SaveReviewPostEvent(@RequestBody ReviewEventModel reviewEventModel){
        try {
            ReviewEventModel reviewEvent = reviewEventService.saveReview_PostEvent(reviewEventModel);
            return new ResponseEntity<>(reviewEvent, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/save/PreEvent/")
    public ResponseEntity<ReviewEventModel> SaveReviewPreEvent(@RequestBody ReviewEventModel reviewEventModel){
        try {
            ReviewEventModel reviewEvent = reviewEventService.saveReview_PreEvent(reviewEventModel);
            return new ResponseEntity<>(reviewEvent, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/get/AllReviewEvent/")
    public ResponseEntity<Iterable<ReviewEventModel>> getAllReviewEvent(){
        try {
            Iterable<ReviewEventModel> reviewEvent = reviewEventService.getAllReviewEvent();
            return new ResponseEntity<>(reviewEvent, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



}


