package ru.yandex.practicum.filmorate.storage.api;

import ru.yandex.practicum.filmorate.model.entity.MpaRating;

import java.util.List;
import java.util.Optional;

public interface MpaRatingStorage {

    List<MpaRating> getAllMpaRatings();

    Optional<MpaRating> getMpaRatingById(int id);
}
