package ru.yandex.practicum.filmorate.service.api;

import ru.yandex.practicum.filmorate.model.entity.MpaRating;

import java.util.List;

public interface MpaRatingService {

    List<MpaRating> getAllMpaRatings();

    MpaRating getMpaRatingById(int id);
}
