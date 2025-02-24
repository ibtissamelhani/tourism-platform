package org.ibtissam.dadesadventures.service.implementation;

import lombok.RequiredArgsConstructor;
import org.ibtissam.dadesadventures.DTO.Review.ReviewDTOMapper;
import org.ibtissam.dadesadventures.DTO.Review.ReviewRequest;
import org.ibtissam.dadesadventures.DTO.Review.ReviewResponse;
import org.ibtissam.dadesadventures.domain.entities.Review;
import org.ibtissam.dadesadventures.domain.entities.User;
import org.ibtissam.dadesadventures.exception.review.ReviewNotFoundException;
import org.ibtissam.dadesadventures.repository.ReviewRepository;
import org.ibtissam.dadesadventures.service.ReviewService;
import org.ibtissam.dadesadventures.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final ReviewDTOMapper reviewDTOMapper;

    @Override
    public ReviewResponse createReview(ReviewRequest reviewRequest) {
        User user = userService.findById(reviewRequest.getUserId());

        Review review = Review.builder()
                .user(user)
                .rating(reviewRequest.getRating())
                .comment(reviewRequest.getComment())
                .build();

        Review savedReview = reviewRepository.save(review);
        return reviewDTOMapper.toResponse(savedReview);
    }

    @Override
    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(reviewDTOMapper::toResponse)
                .toList();
    }

    @Override
    public ReviewResponse getReviewById(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + id));
        return reviewDTOMapper.toResponse(review);
    }

    @Override
    public ReviewResponse updateReview(UUID id, ReviewRequest reviewRequest) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + id));

        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setUpdatedAt(LocalDateTime.now());

        Review updatedReview = reviewRepository.save(review);
        return reviewDTOMapper.toResponse(updatedReview);
    }

    @Override
    public void deleteReview(UUID id) {
        if (!reviewRepository.existsById(id)) {
            throw new ReviewNotFoundException("Review not found with ID: " + id);
        }
        reviewRepository.deleteById(id);
    }
}
