package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createMovie_WithValidFilm_ShouldReturnOkAndFilmStored() throws Exception {
        Film film = new Film();
        film.setName("Interstellar");
        film.setDescription("Sci-fi epic by Christopher Nolan");
        film.setReleaseDate(LocalDate.of(2014, 11, 7));
        film.setDuration(Duration.ofMinutes(169));

        String filmJson = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isCreated());

        String allFilmsJson = mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Film> films = objectMapper.readValue(allFilmsJson, new TypeReference<>() {
        });
        assertEquals(1, films.size());
        Film storedFilm = films.get(0);

        assertEquals(1L, storedFilm.getId());
        assertEquals("Interstellar", storedFilm.getName());
        assertEquals(Duration.ofMinutes(169), storedFilm.getDuration());
    }

    @Test
    void createMovie_WithReleaseDateBefore18951228_ShouldReturnBadRequest() throws Exception {
        Film film = new Film();
        film.setName("Very Old Film");
        film.setDescription("Released before 1895-12-28");
        film.setReleaseDate(LocalDate.of(1800, 1, 1)); // слишком старая дата
        film.setDuration(Duration.ofMinutes(100));

        String filmJson = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/movies")
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
        film.setDuration(Duration.ofMinutes(120));

        String filmJson = objectMapper.writeValueAsString(film);

        mockMvc.perform(put("/movies")
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
        film.setDuration(Duration.ofMinutes(120));

        String filmJson = objectMapper.writeValueAsString(film);

        mockMvc.perform(put("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isBadRequest());
    }
}
