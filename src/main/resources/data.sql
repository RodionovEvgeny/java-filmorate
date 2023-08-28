MERGE INTO "Genres"( "Genre_id", "Genre_name")
VALUES (1, 'Комедия');
MERGE INTO "Genres"( "Genre_id", "Genre_name")
VALUES (2, 'Драма');
MERGE INTO "Genres"( "Genre_id", "Genre_name")
VALUES (3, 'Мультфильм');
MERGE  INTO "Genres"( "Genre_id", "Genre_name")
VALUES (4, 'Триллер');
MERGE INTO "Genres"( "Genre_id", "Genre_name")
VALUES (5, 'Документальный');
MERGE INTO "Genres"( "Genre_id", "Genre_name")
VALUES (6, 'Боевик');

MERGE INTO "Rating" ( "Rating_id", "Rating_name")
VALUES (1, 'G');
MERGE INTO "Rating" ( "Rating_id", "Rating_name")
VALUES (2, 'PG');
MERGE INTO "Rating" ( "Rating_id", "Rating_name")
VALUES (3, 'PG-13');
MERGE INTO "Rating" ( "Rating_id", "Rating_name")
VALUES (4, 'R');
MERGE INTO "Rating" ( "Rating_id", "Rating_name")
VALUES (5, 'NC-17');
