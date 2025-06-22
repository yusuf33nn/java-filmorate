package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.dto.response.MpaDto;
import ru.yandex.practicum.filmorate.service.api.MpaRatingService;
import ru.yandex.practicum.filmorate.storage.api.MpaRatingStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultMpaService implements MpaRatingService {

    private final MpaMapper mpaMapper;
    private final MpaRatingStorage mpaRatingStorage;

    @Override
    public List<MpaDto> getAllMpaRatings() {
        return mpaRatingStorage.getAllMpaRatings().stream().map(mpaMapper::toDto).toList();
    }

    @Override
    public MpaDto getMpaRatingById(int id) {
        return mpaRatingStorage.getMpaRatingById(id)
                .map(mpaMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Mpa rating with ID = %d not found".formatted(id)));
    }
}
