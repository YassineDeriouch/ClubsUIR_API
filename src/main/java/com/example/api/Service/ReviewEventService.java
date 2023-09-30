package com.example.api.Service;

import com.example.api.Models.ClubModel;
import com.example.api.Models.DocumentModel;
import com.example.api.Models.ReviewEventModel;
import com.example.api.Models.ReviewEvent_Post_Pre;
import com.example.api.Repository.ReviewEventRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Data
public class ReviewEventService {
    @Autowired
    private ReviewEventRepository reviewEventRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * SAVE REVIEW EVENT
     *
     * @param reviewEventModel
     * @return
     */

    @Autowired
    private DocumentService documentService;
    //Save review event to post_event table and upload the file to the database document table

    @Transactional
    public ReviewEventModel saveReview_PostEvent(ReviewEventModel reviewEventModel) {
        reviewEventModel.setDescription(reviewEventModel.getDescription());
        reviewEventModel.setNom(reviewEventModel.getNom());
        reviewEventModel.setType(ReviewEvent_Post_Pre.Post_évenement);
        ReviewEventModel reviewEvent = reviewEventRepository.save(reviewEventModel);
        return modelMapper.map(reviewEvent, ReviewEventModel.class);
    }

    @Transactional
    public ReviewEventModel saveReview_PreEvent(ReviewEventModel reviewEventModel) {
        reviewEventModel.setDescription(reviewEventModel.getDescription());
        reviewEventModel.setNom(reviewEventModel.getNom());
        reviewEventModel.setType(ReviewEvent_Post_Pre.Pré_évenement);
        ReviewEventModel reviewEvent = reviewEventRepository.save(reviewEventModel);
        return modelMapper.map(reviewEvent, ReviewEventModel.class);
    }

    public List<ReviewEventModel> getAllReviewEvent() throws EntityNotFoundException {
        return reviewEventRepository.findAll().stream().map(element -> modelMapper.map(element, ReviewEventModel.class))
                .collect(Collectors.toList());
    }



    /*@Transactional
    public ReviewEventModel saveReview_PreEvent(ReviewEventModel reviewEventModel, MultipartFile file) throws IOException {
        reviewEventModel.setDescription(reviewEventModel.getDescription());
        reviewEventModel.setNom(reviewEventModel.getNom());
        reviewEventModel.setType(ReviewEvent_Post_Pre.Pré_évenement);
        DocumentModel document = reviewEventModel.getDocumentModel();

        byte[] fileBytes = file.getBytes(); // Récupérez les bytes du fichier
        document.setFile(fileBytes); // Affectez les bytes au document
        reviewEventModel.setDocumentModel(document);

        // Utilisez les bytes pour uploadFile() au lieu de passer un MultipartFile
        reviewEventModel.setDocumentModel(documentService.uploadFile(file,
                reviewEventModel.getDocumentModel().getReferent().getId_referent(),
                reviewEventModel.getDocumentModel().getLibelle()));

        ReviewEventModel reviewEvent = reviewEventRepository.save(reviewEventModel);
        return modelMapper.map(reviewEvent, ReviewEventModel.class);
    }*/



}
