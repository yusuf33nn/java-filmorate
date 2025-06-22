package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.entity.MpaRating;
import ru.yandex.practicum.filmorate.service.api.MpaRatingService;
import ru.yandex.practicum.filmorate.storage.api.MpaRatingStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultMpaService implements MpaRatingService {
    private final MpaRatingStorage mpaRatingStorage;

    @Override
    public List<MpaRating> getAllMpaRatings() {
        return mpaRatingStorage.getAllMpaRatings();
    }

    @Override
    public MpaRating getMpaRatingById(int id) {
        return mpaRatingStorage.getMpaRatingById(id)
                .orElseThrow(() -> new RuntimeException("Mpa rating with ID = %d not found".formatted(id)));
    }
}
