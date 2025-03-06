package org.ibtissam.dadesadventures.service;

import org.ibtissam.dadesadventures.DTO.Review.ReviewRequest;
import org.ibtissam.dadesadventures.DTO.Review.ReviewResponse;

import java.util.List;
import java.util.UUID;

public interface ReviewService {

    ReviewResponse createReview(ReviewRequest reviewRequest);
    List<ReviewResponse> getAllReviews();
    ReviewResponse getReviewById(UUID id);
    ReviewResponse updateReview(UUID id, ReviewRequest reviewRequest);
    void deleteReview(UUID id);
}
