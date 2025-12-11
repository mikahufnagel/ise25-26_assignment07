package de.seuhd.campuscoffee.domain.implementation;

import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.seuhd.campuscoffee.domain.configuration.ApprovalConfiguration;
import de.seuhd.campuscoffee.domain.exceptions.ValidationException;
import de.seuhd.campuscoffee.domain.model.objects.Review;
import de.seuhd.campuscoffee.domain.model.objects.User;
import de.seuhd.campuscoffee.domain.ports.api.ReviewService;
import de.seuhd.campuscoffee.domain.ports.data.CrudDataService;
import de.seuhd.campuscoffee.domain.ports.data.PosDataService;
import de.seuhd.campuscoffee.domain.ports.data.ReviewDataService;
import de.seuhd.campuscoffee.domain.ports.data.UserDataService;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the Review service that handles business logic related to review entities.
 */
@Slf4j
@Service
public class ReviewServiceImpl extends CrudServiceImpl<Review, Long> implements ReviewService {
    private final ReviewDataService reviewDataService;
    private final UserDataService userDataService;
    private final PosDataService posDataService;
    // TODO: Try to find out the purpose of this class and how it is connected to the application.yaml configuration file.
    private final ApprovalConfiguration approvalConfiguration;

    public ReviewServiceImpl(@NonNull ReviewDataService reviewDataService,
                             @NonNull UserDataService userDataService,
                             @NonNull PosDataService posDataService,
                             @NonNull ApprovalConfiguration approvalConfiguration) {
        super(Review.class);
        this.reviewDataService = reviewDataService;
        this.userDataService = userDataService;
        this.posDataService = posDataService;
        this.approvalConfiguration = approvalConfiguration;
    }

    @Override
    protected CrudDataService<Review, Long> dataService() {
        return reviewDataService;
    }

    @Override
    @Transactional
    public @NonNull Review upsert(@NonNull Review review) {
        // TODO: Implement the missing business logic here
        // Validate that the POS exists (throws NotFoundException if not found)
        posDataService.getById(review.pos().getId());

        // Check that the user hasn't already reviewed this POS
        var existingReviews = reviewDataService.filter(review.pos(), review.author());
        if (!existingReviews.isEmpty()) {
            throw new ValidationException("User cannot submit more than one review per POS.");
        }

        return super.upsert(review);    
    }
    @Override
    @Transactional(readOnly = true)
    public @NonNull List<Review> filter(@NonNull Long posId, @NonNull Boolean approved) {
        return reviewDataService.filter(posDataService.getById(posId), approved);
    }

    @Override
    @Transactional
    public @NonNull Review approve(@NonNull Review review, @NonNull Long userId) {
        log.info("Processing approval request for review with ID '{}' by user with ID '{}'...",
                review.getId(), userId);
                log.info("MH: in approve.1");

        // validate that the user exists
        // TODO: Implement the required business logic here
        User user = userDataService.getById(userId);
    log.info("MH: in approve.2");
        // validate that the review exists
        // TODO: Implement the required business logic here
        Review existingReview = reviewDataService.getById(review.getId());
log.info("MH: in approve.3");
        // a user cannot approve their own review
        // TODO: Implement the required business logic here
        if (existingReview.author().getId().equals(userId)) {
            throw new ValidationException("Users cannot approve their own review."); //statt illegal arge exception

        }

        // increment approval count
        // TODO: Implement the required business logic here
        review=review.toBuilder()
            .approvalCount(review.approvalCount()+1)
            .build();
        // update approval status to determine if the review now reaches the approval quorum
        // TODO: Implement the required business logic here
        review=review.toBuilder()
            .approved(review.approvalCount() >= approvalConfiguration.minCount())
            .build();
            log.info("MH: in approve.4");
        return reviewDataService.upsert(review);
    }

    /**
     * Calculates and updates the approval status of a review based on the approval count.
     * Business rule: A review is approved when it reaches the configured minimum approval count threshold.
     *
     * @param review The review to calculate approval status for
     * @return The review with updated approval status
     */
    Review updateApprovalStatus(Review review) {
        log.debug("Updating approval status of review with ID '{}'...", review.getId());
        return review.toBuilder()
                .approved(isApproved(review))
                .build();
    }
    
    /**
     * Determines if a review meets the minimum approval threshold.
     * 
     * @param review The review to check
     * @return true if the review meets or exceeds the minimum approval count, false otherwise
     */
    private boolean isApproved(Review review) {
        return review.approvalCount() >= approvalConfiguration.minCount();
    }
}
