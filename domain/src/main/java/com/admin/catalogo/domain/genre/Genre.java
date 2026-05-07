package com.admin.catalogo.domain.genre;

import com.admin.catalogo.domain.AggregateRoot;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.NotificationException;
import com.admin.catalogo.domain.utils.InstantUtils;
import com.admin.catalogo.domain.validation.ValidationHandler;
import com.admin.catalogo.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genre extends AggregateRoot<GenreID> {

    @Override
    public void validate(final ValidationHandler handler) {
        new GenreValidator(this, handler).validate();
    }

    private String name;
    private boolean active;
    private List<CategoryID> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    protected Genre(final GenreID anId,
                    final String aName,
                    final boolean isActive,
                    final List<CategoryID> categories,
                    final Instant aCreatedAt,
                    final Instant aUpdatedAt,
                    final Instant aDeletedAt
    ) {
        super(anId);
        this.name = aName;
        this.active = isActive;
        this.categories = categories;
        this.createdAt = aCreatedAt;
        this.updatedAt = aUpdatedAt;
        this.deletedAt= aDeletedAt;

        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Failed to validate Aggregate Genre", notification);
        }
    }

    public static Genre newGenre(final String aName, final boolean isActive)
    {
        final var anId = GenreID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isActive ? null : now;

        return new Genre(anId, aName, isActive, new ArrayList<>(), now, now, deletedAt);
    }

    public static Genre with(final GenreID anId,
                    final String aName,
                    final boolean isActive,
                    final List<CategoryID> categories,
                    final Instant aCreatedAt,
                    final Instant aUpdatedAt,
                    final Instant aDeletedAt
    ) {
        return new Genre(anId, aName, isActive, categories, aCreatedAt, aUpdatedAt, aDeletedAt);
    }

    public static Genre with(final Genre aGenre
    ) {
        return new Genre(
                aGenre.id,
                aGenre.name,
                aGenre.active,
                aGenre.categories,
                aGenre.createdAt,
                aGenre.updatedAt,
                aGenre.deletedAt
        );
    }

    public String getName() {
        return name;
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public boolean isActive() {
        return active;
    }

    public Genre deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = InstantUtils.now();
        }
        this.active = false;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = InstantUtils.now();
        return this;
    }
}
