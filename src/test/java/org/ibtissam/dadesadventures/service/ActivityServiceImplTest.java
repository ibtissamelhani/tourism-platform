package org.ibtissam.dadesadventures.service;

import jakarta.mail.MessagingException;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityDTOMapper;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityRequest;
import org.ibtissam.dadesadventures.DTO.Activity.ActivityResponse;
import org.ibtissam.dadesadventures.DTO.Activity.ActivitySearchDTO;
import org.ibtissam.dadesadventures.domain.entities.*;
import org.ibtissam.dadesadventures.domain.enums.ActivityStatus;
import org.ibtissam.dadesadventures.exception.activity.ActivityDeletionException;
import org.ibtissam.dadesadventures.exception.activity.ActivityNotFoundException;
import org.ibtissam.dadesadventures.exception.reservation.FailedToSendEmailException;
import org.ibtissam.dadesadventures.exception.user.GuideIsBusyException;
import org.ibtissam.dadesadventures.repository.ActivityImageRepository;
import org.ibtissam.dadesadventures.repository.ActivityRepository;
import org.ibtissam.dadesadventures.service.implementation.ActivityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ActivityServiceImplTest {
    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ActivityImageRepository activityImageRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private PlaceService placeService;

    @Mock
    private UserService userService;

    @Mock
    private ActivityDTOMapper activityMapper;

    @Mock
    private MailService mailService;

    @InjectMocks
    private ActivityServiceImpl activityService;

    private ActivityRequest activityRequest;
    private Category category;
    private Place place;
    private User guide;
    private Activity activity;
    private ActivityResponse activityResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // ActivityRequest
        activityRequest = new ActivityRequest();
        activityRequest.setName("Activity 1");
        activityRequest.setDescription("Description de l'activité");
        activityRequest.setCapacity(20);
        activityRequest.setPrice(100.0);
        activityRequest.setDate(LocalDateTime.now().plusDays(5));
        activityRequest.setRegistrationDeadline(LocalDateTime.now().plusDays(3));
        activityRequest.setCategoryId(UUID.randomUUID());
        activityRequest.setPlaceId(UUID.randomUUID());
        activityRequest.setGuideId(UUID.randomUUID());
        activityRequest.setImageUrls(List.of("http://example.com/img1.jpg", "http://example.com/img2.jpg"));

        // Préparation des entités associées
        category = Category.builder().id(activityRequest.getCategoryId()).name("Catégorie 1").build();
        place = Place.builder().id(activityRequest.getPlaceId()).name("Lieu 1").build();
        guide = User.builder().id(activityRequest.getGuideId()).firstName("John").build();

        // Préparation d'une activité retournée par le repository
        activity = Activity.builder()
                .id(UUID.randomUUID())
                .name(activityRequest.getName())
                .description(activityRequest.getDescription())
                .capacity(activityRequest.getCapacity())
                .price(activityRequest.getPrice())
                .date(activityRequest.getDate())
                .registrationDeadline(activityRequest.getRegistrationDeadline())
                .availability(true)
                .status(ActivityStatus.ACTIVE)
                .category(category)
                .place(place)
                .guide(guide)
                .images(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Ajout d'images à l'activité
        activity.getImages().add(ActivityImage.builder().id(UUID.randomUUID()).imageUrl("http://example.com/img1.jpg").activity(activity).build());
        activity.getImages().add(ActivityImage.builder().id(UUID.randomUUID()).imageUrl("http://example.com/img2.jpg").activity(activity).build());

        // Préparation d'un ActivityResponse simulé
        activityResponse = ActivityResponse.builder()
                .id(activity.getId())
                .name(activity.getName())
                .description(activity.getDescription())
                .capacity(activity.getCapacity())
                .price(activity.getPrice())
                .date(activity.getDate())
                .registrationDeadline(activity.getRegistrationDeadline())
                .availability(activity.getAvailability())
                .name(category.getName())
                .place(place.getName())
                .guideFirstName(guide.getFirstName())
                .imageUrls(List.of("http://example.com/img1.jpg", "http://example.com/img2.jpg"))
                .build();
    }


    @Test
    void ActivityService_createActivity_returnsActivityResponse_whenValid() {
        // Given
        when(categoryService.findById(activityRequest.getCategoryId())).thenReturn(category);
        when(placeService.findById(activityRequest.getPlaceId())).thenReturn(place);
        when(userService.findById(activityRequest.getGuideId())).thenReturn(guide);
        when(activityRepository.isGuideBusy(activityRequest.getGuideId(), activityRequest.getDate())).thenReturn(false);
        when(activityRepository.save(any(Activity.class))).thenReturn(activity);
        when(activityMapper.toResponse(activity)).thenReturn(activityResponse);

        // When
        ActivityResponse result = activityService.createActivity(activityRequest);

        // Then
        verify(categoryService).findById(activityRequest.getCategoryId());
        verify(placeService).findById(activityRequest.getPlaceId());
        verify(userService).findById(activityRequest.getGuideId());
        verify(activityRepository).isGuideBusy(activityRequest.getGuideId(), activityRequest.getDate());
        verify(activityRepository).save(any(Activity.class));
        assertEquals(activityResponse, result);
    }

    @Test
    void ActivityService_createActivity_throwsGuideIsBusyException_whenGuideIsBusy() {
        // Given
        when(categoryService.findById(activityRequest.getCategoryId())).thenReturn(category);
        when(placeService.findById(activityRequest.getPlaceId())).thenReturn(place);
        when(userService.findById(activityRequest.getGuideId())).thenReturn(guide);
        when(activityRepository.isGuideBusy(activityRequest.getGuideId(), activityRequest.getDate())).thenReturn(true);

        // When & Then
        assertThrows(GuideIsBusyException.class, () -> activityService.createActivity(activityRequest));
        verify(activityRepository).isGuideBusy(activityRequest.getGuideId(), activityRequest.getDate());
        verify(activityRepository, never()).save(any());
    }

    @Test
    void ActivityService_getAllActivities_returnsPageOfActivityResponses() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Activity> page = new PageImpl<>(List.of(activity), pageable, 1);
        when(activityRepository.findAll(pageable)).thenReturn(page);
        when(activityMapper.toResponse(activity)).thenReturn(activityResponse);

        // When
        Page<ActivityResponse> resultPage = activityService.getAllActivities(pageable);

        // Then
        verify(activityRepository).findAll(pageable);
        assertEquals(1, resultPage.getTotalElements());
        assertEquals(activityResponse, resultPage.getContent().get(0));
    }

    @Test
    void ActivityService_deleteActivity_throwsActivityDeletionException_whenReservationsExist() {
        // Given
        activity.setReservations(List.of(new Reservation()));
        UUID id = activity.getId();
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));

        // When & Then
        assertThrows(ActivityDeletionException.class, () -> activityService.deleteActivity(id));
        verify(activityRepository).findById(id);
        verify(activityImageRepository, never()).deleteAll(any());
        verify(activityRepository, never()).delete(any());
    }

    @Test
    void ActivityService_deleteActivity_deletesActivityAndImages_whenNoReservations() {
        // Given
        activity.setReservations(new ArrayList<>());
        UUID id = activity.getId();
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));

        // When
        activityService.deleteActivity(id);

        // Then
        verify(activityRepository).findById(id);
        verify(activityImageRepository).deleteAll(activity.getImages());
        verify(activityRepository).delete(activity);
    }

    @Test
    void ActivityService_updateActivity_returnsUpdatedActivityResponse_whenValid() {
        // Given
        UUID id = activity.getId();
        ActivityRequest updateRequest = new ActivityRequest();
        updateRequest.setName("Updated Activity");
        updateRequest.setDescription("Updated description");
        updateRequest.setCapacity(30);
        updateRequest.setPrice(150.0);
        updateRequest.setDate(LocalDateTime.now().plusDays(10));
        updateRequest.setRegistrationDeadline(LocalDateTime.now().plusDays(8));
        updateRequest.setCategoryId(category.getId());
        updateRequest.setPlaceId(place.getId());
        updateRequest.setGuideId(guide.getId());
        updateRequest.setAvailability(false);
        updateRequest.setStatus(ActivityStatus.CANCELED.name());
        updateRequest.setImageUrls(List.of("http://example.com/new1.jpg"));

        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        when(categoryService.findById(category.getId())).thenReturn(category);
        when(placeService.findById(place.getId())).thenReturn(place);
        when(userService.findById(guide.getId())).thenReturn(guide);
        when(activityRepository.save(activity)).thenReturn(activity);
        when(activityMapper.toResponse(activity)).thenReturn(activityResponse);

        // When
        ActivityResponse result = activityService.updateActivity(id, updateRequest);

        // Then
        verify(activityRepository).findById(id);
        verify(categoryService).findById(category.getId());
        verify(placeService).findById(place.getId());
        if (updateRequest.getGuideId() != null) {
            verify(userService).findById(guide.getId());
        }
        verify(activityRepository).save(activity);
        assertEquals(activityResponse, result);
    }

    @Test
    void ActivityService_searchActivities_returnsPaginatedActivityResponses() {
        // Given
        ActivitySearchDTO searchDTO = new ActivitySearchDTO();
        searchDTO.setName("Activity");
        List<Activity> activityList = List.of(activity);
        when(activityRepository.findByCriteria(searchDTO)).thenReturn(activityList);
        when(activityMapper.toResponse(activity)).thenReturn(activityResponse);

        Pageable pageable = PageRequest.of(0, 10);
        // When
        Page<ActivityResponse> resultPage = activityService.searchActivities(searchDTO, pageable);

        // Then
        verify(activityRepository).findByCriteria(searchDTO);
        assertEquals(1, resultPage.getTotalElements());
        assertEquals(activityResponse, resultPage.getContent().get(0));
    }


    @Test
    void ActivityService_findById_returnsActivity_whenFound() {
        // Given
        UUID id = activity.getId();
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));

        // When
        Activity result = activityService.findById(id);

        // Then
        verify(activityRepository).findById(id);
        assertEquals(activity, result);
    }


    @Test
    void ActivityService_findById_throwsActivityNotFoundException_whenNotFound() {
        // Given
        UUID id = UUID.randomUUID();
        when(activityRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ActivityNotFoundException.class, () -> activityService.findById(id));
        verify(activityRepository).findById(id);
    }


    @Test
    void ActivityService_cancelActivity_changesStatusToCanceled_andNotifiesParticipants() throws MessagingException {
        // Given
        UUID id = activity.getId();
        activity.setReservations(new ArrayList<>());
        Reservation reservation = Reservation.builder().id(UUID.randomUUID()).user(User.builder().id(UUID.randomUUID()).firstName("Alice").email("alice@example.com").build()).build();
        activity.getReservations().add(reservation);

        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        when(activityRepository.save(activity)).thenReturn(activity);

        // When
        activityService.cancelActivity(id);

        // Then
        verify(activityRepository).findById(id);
        verify(activityRepository).save(activity);
        assertEquals(ActivityStatus.CANCELED, activity.getStatus());
        verify(mailService, atLeastOnce()).sendHtmlEmail(anyString(), anyString(), anyString(), any());
    }

    @Test
    void ActivityService_cancelActivity_throwsFailedToSendEmailException_whenEmailFails() throws MessagingException {
        // Given
        UUID id = activity.getId();
        activity.setReservations(new ArrayList<>());
        Reservation reservation = Reservation.builder().id(UUID.randomUUID()).user(User.builder().id(UUID.randomUUID()).firstName("Alice").email("alice@example.com").build()).build();
        activity.getReservations().add(reservation);
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        when(activityRepository.save(activity)).thenReturn(activity);
        doThrow(new FailedToSendEmailException("Failed to send email")).when(mailService)
                .sendHtmlEmail(anyString(), anyString(), anyString(), any());

        // When & Then
        assertThrows(FailedToSendEmailException.class, () -> activityService.cancelActivity(id));
        verify(activityRepository).findById(id);
        verify(mailService, atLeastOnce()).sendHtmlEmail(anyString(), anyString(), anyString(), any());
    }
}