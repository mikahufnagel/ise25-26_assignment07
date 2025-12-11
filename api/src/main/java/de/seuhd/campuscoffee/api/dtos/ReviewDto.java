package de.seuhd.campuscoffee.api.dtos;
import java.time.LocalDateTime;

import org.jspecify.annotations.Nullable;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * DTO record for POS metadata.
 */
@Builder(toBuilder = true)
public record ReviewDto (
    @Nullable Long id,
    // TODO: Implement ReviewDto
    @NotNull Integer approvalCount, // is updated by the domain module
    @NotNull Boolean approved, // is determined by the domain module
    @Nullable LocalDateTime createdAt,
    @Nullable LocalDateTime updatedAt,
    @NotNull Long posId,
    @NotNull Long authorId,
    @NotEmpty
    @NotNull String review
) implements Dto<Long> {
    @Override
    public @Nullable Long getId() {
        return id;
    }
}
