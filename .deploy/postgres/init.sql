CREATE TABLE IF NOT EXISTS FILM
(
    ID            BIGINT PRIMARY KEY,
    ORIGINAL_NAME VARCHAR(255) NOT NULL,
    RUSSIAN_NAME  VARCHAR(255) NOT NULL,
    YEAR          INTEGER      NOT NULL,
    CAST_IMAGE_ID INTEGER      NOT NULL UNIQUE,
    QUOTE         VARCHAR(255) NOT NULL,
    RATING        FLOAT        NOT NULL,
    CHECK ((ORIGINAL_NAME != '')
        AND (RUSSIAN_NAME != '')
        AND (RUSSIAN_NAME != '')
        AND (QUOTE != '')
        AND (YEAR > 1895)
        AND (CAST_IMAGE_ID > 0)
        AND (RATING <= 10)
        AND (RATING >= 0))
);

CREATE TABLE IF NOT EXISTS COUNTRY
(
    ID   BIGINT PRIMARY KEY,
    NAME VARCHAR(30) NOT NULL,
    CHECK (NAME != '')
);

CREATE TABLE IF NOT EXISTS FILM_COUNTRY
(
    FILM_ID    BIGINT REFERENCES FILM (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    COUNTRY_ID BIGINT REFERENCES COUNTRY (ID) ON DELETE NO ACTION ON UPDATE CASCADE,
    PRIMARY KEY (FILM_ID, COUNTRY_ID)
);

CREATE TABLE IF NOT EXISTS GENRE
(
    ID   BIGINT PRIMARY KEY,
    NAME VARCHAR(30) NOT NULL,
    CHECK (NAME != '')
);

CREATE TABLE IF NOT EXISTS FILM_GENRE
(
    FILM_ID  BIGINT REFERENCES FILM (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    GENRE_ID BIGINT REFERENCES GENRE (ID) ON DELETE NO ACTION ON UPDATE CASCADE,
    PRIMARY KEY (FILM_ID, GENRE_ID)
);

CREATE TABLE IF NOT EXISTS BOT_USER
(
    CHAT_ID       BIGINT PRIMARY KEY,
    FIRST_NAME    VARCHAR(255),
    LAST_NAME     VARCHAR(255),
    USERNAME      VARCHAR(255),
    REGISTERED_AT TIMESTAMP NOT NULL,
    CHECK ((BOT_USER.FIRST_NAME != '')
        AND (BOT_USER.LAST_NAME != '')
        AND (BOT_USER.USERNAME != ''))
);

------------------- FILMS -------------------

INSERT INTO FILM VALUES
       (1, 'The Green Mile', 'Зеленая миля', 1999, 2,
        'Miracles do happen. Miracles happen in the most unexpected places. Paul Edgecomb did not believe in miracles.', 9.2),
       (2, 'Forrest Gump', 'Форест Гамп', 1994, 3,
        'The world will never be the same once you have seen it through the eyes of Forrest Gump.', 8.9),
       (3, 'Coco', 'Тайна Коко', 2017, 4,
        'If there is no one left in the living world to remember you, you disappear from this world.', 8.8),
       (4, 'Interstellar', 'Интерстеллар', 2014, 5,
        'Time cannot put anything in your hands until you let go off the time.', 8.6),
       (5, 'Fight Club', 'Бойцовский клуб', 1999, 6,
        'It’s only after we’ve lost everything that we’re free to do anything', 8.6),
       (6, 'WALL·E', 'ВАЛЛ·И', 2008, 7, 'Name? WALL-E', 8.3),
       (7, 'Inception', 'Начало', 2010, 8,
        'Building a dream from your memory is the easiest way of losing your grasp on what’s real and what is a dream.', 8.7),
       (8 ,'Autumn in New York', 'Осень в Нью-Йорке', 2000, 1, 'I would go with you in a heartbeat.', 7.4);

------------------- GENRES -------------------

INSERT INTO GENRE
VALUES (1, 'DRAMA'), (2, 'CRIME'), (3, 'COMEDY'), (4, 'MELODRAMA'), (5, 'WAR'), (6, 'HISTORY'), (7, 'CARTOON'),
       (8, 'FANTASY'), (9, 'ADVENTURE'), (10, 'FAMILY'), (11, 'THRILLER'), (12, 'ACTION'), (13, 'DETECTIVE');

------------------- COUNTRIES -------------------

INSERT INTO COUNTRY
VALUES (1, 'USA'), (2, 'UK'), (3, 'CANADA'), (4, 'GERMANY');

------------------- FILM_COUNTRY -------------------

INSERT INTO FILM_COUNTRY (FILM_ID, COUNTRY_ID)
VALUES (1, 1), (2, 1), (3, 1), (6, 1), (8, 1), (4, 1), (5, 1), (7, 1),
       (4, 2), (7, 2),
       (4, 3),
       (5, 4);

------------------- FILM_GENRE -------------------

INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID)
VALUES (1, 1), (2, 1), (4, 1), (5, 1), (7, 1), (8, 1),
       (1, 2), (5, 2),
       (2, 3), (3, 3),
       (2, 4), (8, 4),
       (2, 5),
       (2, 6),
       (3, 7), (6, 7),
       (3, 8), (4, 8), (6, 8), (7, 8),
       (3, 9), (4, 9), (6, 9),
       (3, 10), (6, 10),
       (5, 11), (7, 11),
       (7, 12),
       (7, 13);
