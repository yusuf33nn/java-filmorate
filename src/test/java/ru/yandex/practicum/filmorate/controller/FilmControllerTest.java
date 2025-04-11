package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmService filmService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createMovie_WithValidFilm_ShouldReturnOkAndFilmStored() throws Exception {
        Film film = new Film();
        film.setName("Interstellar");
        film.setDescription("Sci-fi epic by Christopher Nolan");
        film.setReleaseDate(LocalDate.of(2014, 11, 7));
        film.setDuration(169L);
        String filmJson = objectMapper.writeValueAsString(film);

        film.setId(1L);
        when(filmService.createFilm(any(Film.class))).thenReturn(film);
        when(filmService.findAllFilms()).thenReturn(List.of(film));

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Interstellar"))
                .andExpect(jsonPath("$[0].duration").value(169L));
    }

    @Test
    void createMovie_WithReleaseDateBefore18951228_ShouldReturnBadRequest() throws Exception {
        Film film = new Film();
        film.setName("Very Old Film");
        film.setDescription("Released before 1895-12-28");
        film.setReleaseDate(LocalDate.of(1800, 1, 1)); // слишком старая дата
        film.setDuration(100L);

        String filmJson = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateMovie_WithIdZero() throws Exception {

        Film film = new Film();
        film.setId(0L);
        film.setName("Invalid ID");
        film.setDescription("id = 0");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120L);

        String filmJson = objectMapper.writeValueAsString(film);
        when(filmService.updateFilm(any(Film.class))).thenThrow(new ValidationException());

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateMovie_WithIdNull_ShouldReturnBadRequest() throws Exception {

        Film film = new Film();
        film.setId(null);
        film.setName("Invalid ID");
        film.setDescription("id = null");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120L);

        String filmJson = objectMapper.writeValueAsString(film);
        when(filmService.updateFilm(any(Film.class))).thenThrow(new ValidationException());

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isBadRequest());
    }
}
