CREATE TABLE if not exists users
(
    id       bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email    varchar(100) unique not null,
    login    varchar(100) unique not null,
    name     varchar(255),
    birthday date
    );

CREATE TABLE if not exists friendship
(
    requester_id bigint      NOT NULL,
    addressee_id bigint      NOT NULL,
    status       VARCHAR(50) NOT NULL,
    requested_at TIMESTAMP DEFAULT now(),
    confirmed_at TIMESTAMP,

    PRIMARY KEY (requester_id, addressee_id),
    FOREIGN KEY (requester_id) REFERENCES users (id),
    FOREIGN KEY (addressee_id) REFERENCES users (id)
    );

ALTER TABLE friendship
    ADD CONSTRAINT check_friendship_status_name
        CHECK (status IN ('PENDING', 'CONFIRMED'));

CREATE TABLE IF NOT EXISTS mpa_rating
(
    id                 int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    rating_code        varchar(15) not null,
    rating_description text        not null
    );

ALTER TABLE mpa_rating
    ADD CONSTRAINT check_mpa_rating_code
        CHECK (rating_code IN ('G', 'PG', 'PG-13', 'R', 'NC-17'));

INSERT INTO mpa_rating (rating_code, rating_description)
values ('G', 'у фильма нет возрастных ограничений'),
       ('PG', 'детям рекомендуется смотреть фильм с родителями'),
       ('PG-13', 'детям до 13 лет просмотр не желателен'),
       ('R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
       ('NC-17', 'лицам до 18 лет просмотр запрещён');

CREATE TABLE IF NOT EXISTS film
(
    id            bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name          varchar not null,
    description   varchar(200),
    release_date  date    not null,
    duration      bigint  not null,
    mpa_rating_id int     not null,

    FOREIGN KEY (mpa_rating_id) REFERENCES mpa_rating (id)
    );

CREATE TABLE IF NOT EXISTS film_like
(
    film_id bigint not null,
    user_id bigint not null,

    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES film (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
    );

CREATE TABLE IF NOT EXISTS genre
(
    id   int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(255) not null
    );

INSERT INTO genre (name)
values ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  bigint not null,
    genre_id bigint not null,

    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES film (id),
    FOREIGN KEY (genre_id) REFERENCES genre (id)
    );
