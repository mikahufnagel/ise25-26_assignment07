package de.seuhd.campuscoffee.api.controller;

import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.seuhd.campuscoffee.api.dtos.ReviewDto;
import de.seuhd.campuscoffee.api.mapper.DtoMapper;
import de.seuhd.campuscoffee.api.mapper.ReviewDtoMapper;
import de.seuhd.campuscoffee.api.openapi.CrudOperation;
import static de.seuhd.campuscoffee.api.openapi.Operation.CREATE;
import static de.seuhd.campuscoffee.api.openapi.Operation.DELETE;
import static de.seuhd.campuscoffee.api.openapi.Operation.FILTER;
import static de.seuhd.campuscoffee.api.openapi.Operation.GET_ALL;
import static de.seuhd.campuscoffee.api.openapi.Operation.GET_BY_ID;
import static de.seuhd.campuscoffee.api.openapi.Operation.UPDATE;
import static de.seuhd.campuscoffee.api.openapi.Resource.REVIEW;
import de.seuhd.campuscoffee.domain.model.objects.Review;
import de.seuhd.campuscoffee.domain.ports.api.CrudService;
import de.seuhd.campuscoffee.domain.ports.api.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



/**
 * Controller for handling reviews for POS, authored by users.
 */
@Tag(name="Reviews", description="Operations for managing reviews for points of sale.")
@Controller
@RequestMapping("/api/reviews")
@Slf4j
@RequiredArgsConstructor
public class ReviewController extends CrudController<Review, ReviewDto, Long> {

    // TODO: Correctly implement the service() and mapper() methods. Note the IntelliJ warning resulting from the @NonNull annotation.
    private final ReviewService reviewService;
    private final ReviewDtoMapper reviewDtoMapper;

    @Override
    protected @NonNull CrudService<Review, Long> service() {
        return reviewService;
    }

    @Override
    protected @NonNull DtoMapper<Review, ReviewDto> mapper() {
        return reviewDtoMapper;
    }

    @Operation
    @CrudOperation(operation=GET_ALL, resource=REVIEW)
    @GetMapping("")
    public @NonNull ResponseEntity<List<ReviewDto>> getAll() {
        return super.getAll();
    }

    // TODO: Implement the missing methods/endpoints.
    @Operation
    @CrudOperation(operation=GET_BY_ID, resource=REVIEW)
    @GetMapping("/{id}")
    public @NonNull ResponseEntity<ReviewDto> getById(
            @Parameter(description="Unique identifier of the review to retrieve.", required=true)
            @PathVariable Long id) {
        return super.getById(id);
    }
    @Operation
    @CrudOperation(operation=CREATE, resource=REVIEW)
    @PostMapping("")
    public @NonNull ResponseEntity<ReviewDto> create(
            @Parameter(description="Data of the Review to create.", required=true)
            @RequestBody @Valid ReviewDto ReviewDto) {
        return super.create(ReviewDto);
    }
    @Operation
    @CrudOperation(operation=UPDATE, resource=REVIEW)
    @PutMapping("/{id}")
    public @NonNull ResponseEntity<ReviewDto> update(
            @Parameter(description="Unique identifier of the review to update.", required=true)
            @PathVariable Long id,
            @Parameter(description="Data of the review to update.", required=true)
            @RequestBody @Valid ReviewDto reviewDto) {
        return super.update(id, reviewDto);
    }
    @Operation
    @CrudOperation(operation=FILTER, resource=REVIEW)
    @GetMapping("/filter")
    public ResponseEntity<ReviewDto> filter(
            @NonNull Long posId, @NonNull Boolean approved) {
        return ResponseEntity.ok(
                reviewDtoMapper.fromDomain(reviewService.getById(posId)))
        ;
    }
    @Operation
    @CrudOperation(operation=DELETE, resource=REVIEW)
    @DeleteMapping("/{id}")
    public @NonNull ResponseEntity<Void> delete(
        @Parameter(description="Unique identifier of the review to delete", required=true)
        @PathVariable long Id){
            return super.delete(Id);

    };

    @Operation
    public ResponseEntity<ReviewDto> approve(
        @PathVariable Long id,
        @RequestParam("user_id") Long userID
    ){
        Review review = reviewService.getById(id);

        Review updatedReview = reviewService.approve(review, userID);
        
        ReviewDto reviewDTO = reviewDtoMapper.fromDomain(updatedReview);

        return  ResponseEntity.ok(reviewDTO);
        };


}
