package ru.yandex.practicum.filmorate.service.api;

import ru.yandex.practicum.filmorate.model.dto.response.MpaDto;

import java.util.List;

public interface MpaRatingService {

    List<MpaDto> getAllMpaRatings();

    MpaDto getMpaRatingById(int id);
}
