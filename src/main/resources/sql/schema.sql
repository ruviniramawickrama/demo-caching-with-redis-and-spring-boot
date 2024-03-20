DROP TABLE IF EXISTS public.Book;

CREATE TABLE public.Book(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    genre VARCHAR(50),
    author VARCHAR(50)
);