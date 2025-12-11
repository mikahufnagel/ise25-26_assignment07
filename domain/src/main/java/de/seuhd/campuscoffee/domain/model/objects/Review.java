package de.seuhd.campuscoffee.domain.model.objects;

import java.time.LocalDateTime;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import lombok.Builder;

/**
 * Domain record that stores a review for a point of sale.
 * Reviews are approved once they received a configurable number of approvals.
 */
@Builder(toBuilder = true)
public record Review(
        @Nullable Long id, // null when the review has not been created yet
        //TODO: Implement review domain model.
        @NonNull Integer approvalCount, // is updated by the domain module
        @NonNull Boolean approved, // is determined by the domain module
        @Nullable LocalDateTime createdAt,
        @Nullable LocalDateTime updatedAt,
        //@NonNull Long posId,      //statt posId
        //@NonNull Long authorId,   //statt authorId
        @NonNull String review,
        @Nullable User author,
        @NonNull Pos pos




) implements DomainModel<Long> {
    @Override
    public Long getId() {
        return id;
    }
}