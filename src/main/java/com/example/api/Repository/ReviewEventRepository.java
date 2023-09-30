package com.example.api.Repository;

import com.example.api.Models.ReviewEventModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReviewEventRepository extends JpaRepository<ReviewEventModel,Integer> {

}
