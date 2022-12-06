CREATE TABLE studio
(
    id          UUID         NOT NULL PRIMARY KEY,
    studio_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE producer
(
    id            UUID         NOT NULL PRIMARY KEY,
    producer_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE movie
(
    id         UUID         NOT NULL PRIMARY KEY,
    title      VARCHAR(255) NOT NULL UNIQUE,
    movie_year INT2         NOT NULL,
    winner     BOOLEAN      NOT NULL
);

CREATE TABLE movie_studio
(
    movie_id  UUID NOT NULL REFERENCES movie,
    studio_id UUID NOT NULL REFERENCES studio,
    PRIMARY KEY (movie_id, studio_id)
);

CREATE TABLE movie_producer
(
    movie_id    UUID NOT NULL REFERENCES movie,
    producer_id UUID NOT NULL REFERENCES producer,
    PRIMARY KEY (movie_id, producer_id)
);
