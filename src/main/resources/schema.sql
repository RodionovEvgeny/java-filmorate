
CREATE TABLE IF NOT EXISTS "User"  (
    "User_id" integer generated by default as identity NOT NULL,
    "Name" varchar   NULL,
    "Login" varchar   NOT NULL,
    "Email" varchar   NOT NULL,
    "Birthday" timestamp   NULL,
    CONSTRAINT "pk_User" PRIMARY KEY (
        "User_id"
     )
);

CREATE TABLE  IF NOT EXISTS "Film" (
    "Film_id" integer generated by default as identity NOT NULL,
    "Name" varchar   NOT NULL,
    "Description" varchar   NOT NULL,
    "Duration" int   NOT NULL,
    "Release_date" timestamp   NOT NULL,
    "Rating_id" integer NOT NULL,
    "Genre_id" integer   NOT NULL,
    CONSTRAINT "pk_Film" PRIMARY KEY (
        "Film_id"
     )
);

CREATE TABLE  IF NOT EXISTS "Likes" (
    "Film_id" integer   NOT NULL,
    "User_id" integer   NOT NULL    
);

CREATE TABLE  IF NOT EXISTS "Friends" (
    "User_id" integer   NOT NULL,
    "Friend_id" integer   NOT NULL,
    "Approved" boolean   NOT NULL
);

CREATE TABLE  IF NOT EXISTS "Genres" (
    "Genre_id" integer   NOT NULL,
    "Genre_name" varchar   NOT NULL,
    CONSTRAINT "pk_Genres" PRIMARY KEY (
        "Genre_id"
     )
);

CREATE TABLE  IF NOT EXISTS "Rating" (
    "Rating_id" integer NOT NULL,
    "Rating_name" varchar NOT NULL,
CONSTRAINT "pk_Rating" PRIMARY KEY ("Rating_id")
);



ALTER TABLE "Film" ADD CONSTRAINT  IF NOT EXISTS "fk_Film_Genre_id" FOREIGN KEY("Genre_id")
REFERENCES "Genres" ("Genre_id");

ALTER TABLE "Film" ADD CONSTRAINT IF NOT EXISTS "fk_Film_Rating_id" FOREIGN KEY("Rating_id")
REFERENCES "Rating" ("Rating_id");

ALTER TABLE "Likes" ADD CONSTRAINT IF NOT EXISTS "fk_Likes_Film_id" FOREIGN KEY("Film_id")
REFERENCES "Film" ("Film_id");

ALTER TABLE "Likes" ADD CONSTRAINT  IF NOT EXISTS "fk_Likes_User_id" FOREIGN KEY("User_id")
REFERENCES "User" ("User_id");

ALTER TABLE "Friends" ADD CONSTRAINT  IF NOT EXISTS "fk_Friends_User_id" FOREIGN KEY("User_id")
REFERENCES "User" ("User_id");

ALTER TABLE "Friends" ADD CONSTRAINT  IF NOT EXISTS "fk_Friends_Friend_id" FOREIGN KEY("Friend_id")
REFERENCES "User" ("User_id");

