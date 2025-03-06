package org.ibtissam.dadesadventures.service;

import org.ibtissam.dadesadventures.DTO.Review.ReviewDTOMapper;
import org.ibtissam.dadesadventures.DTO.Review.ReviewRequest;
import org.ibtissam.dadesadventures.DTO.Review.ReviewResponse;
import org.ibtissam.dadesadventures.domain.entities.Review;
import org.ibtissam.dadesadventures.domain.entities.User;
import org.ibtissam.dadesadventures.exception.review.ReviewNotFoundException;
import org.ibtissam.dadesadventures.repository.ReviewRepository;
import org.ibtissam.dadesadventures.service.implementation.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceImplTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserService userService;

    @Mock
    private ReviewDTOMapper reviewDTOMapper;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private ReviewRequest reviewRequest;
    private Review review;
    private ReviewResponse reviewResponse;
    private User user;
    private UUID reviewId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // user
        user = User.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .build();

        // ReviewRequest
        reviewRequest = new ReviewRequest();
        reviewRequest.setUserId(user.getId());
        reviewRequest.setRating(4);
        reviewRequest.setComment("Excellent service!");

        //  Review
        reviewId = UUID.randomUUID();
        review = Review.builder()
                .id(reviewId)
                .user(user)
                .rating(reviewRequest.getRating())
                .comment(reviewRequest.getComment())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // ReviewResponse
        reviewResponse = ReviewResponse.builder()
                .id(review.getId())
                .userFirstName(user.getFirstName())
                .userLastName(user.getLastName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    @Test
    void ReviewService_createReview_returnsReviewResponse_whenValid() {
        // Given
        when(userService.findById(reviewRequest.getUserId())).thenReturn(user);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewDTOMapper.toResponse(review)).thenReturn(reviewResponse);

        // When
        ReviewResponse resultedResponse = reviewService.createReview(reviewRequest);

        // Then
        verify(userService).findById(reviewRequest.getUserId());
        verify(reviewRepository).save(any(Review.class));
        assertEquals(reviewResponse, resultedResponse);
    }

    @Test
    void ReviewService_getAllReviews_returnsListOfAllReviews() {
        // Given
        List<Review> expectedList = List.of(review);
        when(reviewRepository.findAll()).thenReturn(expectedList);
        when(reviewDTOMapper.toResponse(review)).thenReturn(reviewResponse);

        // When
        List<ReviewResponse> resultedList = reviewService.getAllReviews();

        // Then
        verify(reviewRepository).findAll();
        assertEquals(1, resultedList.size());
        assertEquals(reviewResponse, resultedList.get(0));
    }

    @Test
    void ReviewService_getReviewById_returnsReviewResponse_whenFound() {
        // Given
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(reviewDTOMapper.toResponse(review)).thenReturn(reviewResponse);

        // When
        ReviewResponse resultedResponse = reviewService.getReviewById(reviewId);

        // Then
        verify(reviewRepository).findById(reviewId);
        assertEquals(reviewResponse, resultedResponse);
    }

    @Test
    void ReviewService_getReviewById_throwsReviewNotFoundException_whenNotFound() {
        // Given
        UUID randomId = UUID.randomUUID();
        when(reviewRepository.findById(randomId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(ReviewNotFoundException.class, () -> reviewService.getReviewById(randomId));
        assertEquals("Review not found with ID: " + randomId, exception.getMessage());
        verify(reviewRepository).findById(randomId);
    }

    @Test
    void ReviewService_updateReview_returnsUpdatedReviewResponse_whenValid() {
        // Given
        ReviewRequest updateRequest = new ReviewRequest();
        updateRequest.setUserId(user.getId()); // Normalement on ne modifie pas l'utilisateur
        updateRequest.setRating(5);
        updateRequest.setComment("Excellent service, updated!");

        Review updatedReview = Review.builder()
                .id(reviewId)
                .user(user)
                .rating(updateRequest.getRating())
                .comment(updateRequest.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        ReviewResponse updatedResponse = ReviewResponse.builder()
                .id(reviewId)
                .userFirstName(user.getFirstName())
                .userLastName(user.getLastName())
                .rating(updateRequest.getRating())
                .comment(updateRequest.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(updatedReview.getUpdatedAt())
                .build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(reviewRepository.save(review)).thenReturn(updatedReview);
        when(reviewDTOMapper.toResponse(updatedReview)).thenReturn(updatedResponse);

        // When
        ReviewResponse resultedResponse = reviewService.updateReview(reviewId, updateRequest);

        // Then
        verify(reviewRepository).findById(reviewId);
        verify(reviewRepository).save(review);
        assertEquals(updatedResponse, resultedResponse);
    }

    @Test
    void ReviewService_deleteReview_deletesReview_whenFound() {
        // Given
        when(reviewRepository.existsById(reviewId)).thenReturn(true);

        // When
        reviewService.deleteReview(reviewId);

        // Then
        verify(reviewRepository).existsById(reviewId);
        verify(reviewRepository).deleteById(reviewId);
    }

    @Test
    void ReviewService_deleteReview_throwsReviewNotFoundException_whenNotFound() {
        // Given
        UUID randomId = UUID.randomUUID();
        when(reviewRepository.existsById(randomId)).thenReturn(false);

        // When & Then
        assertThrows(ReviewNotFoundException.class, () -> reviewService.deleteReview(randomId));
        verify(reviewRepository).existsById(randomId);
        verify(reviewRepository, never()).deleteById(any());
    }
}