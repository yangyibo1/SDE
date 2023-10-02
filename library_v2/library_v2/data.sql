INSERT INTO `category` (`name`)
VALUES
    ('Fiction'),
    ('Non-Fiction'),
    ('Mystery'),
    ('Science Fiction'),
    ('Romance');

INSERT INTO `book` (`author`, `can_be_borrowed`, `description`, `language`, `publisher_year`, `quantity`, `title`, `category_id`, `remain`)
VALUES
    ('Jane Austen', 1, 'Pride and Prejudice is a novel by Jane Austen.', 'English', 1813, 5, 'Pride and Prejudice', 1, 5),
    ('William Shakespeare', 1, 'Hamlet is a tragedy written by William Shakespeare.', 'English', 1603, 3, 'Hamlet', 1, 3),
    ('J.K. Rowling', 1, 'Harry Potter and the Sorcerer''s Stone is a fantasy novel by J.K. Rowling.', 'English', 1997, 8, 'Harry Potter and the Sorcerer''s Stone', 1, 8),
    ('George Orwell', 1, '1984 is a dystopian novel by George Orwell.', 'English', 1949, 4, '1984', 1, 4),
    ('Agatha Christie', 1, 'Murder on the Orient Express is a detective novel by Agatha Christie.', 'English', 1934, 6, 'Murder on the Orient Express', 3, 6),
    ('F. Scott Fitzgerald', 1, 'The Great Gatsby is a novel by F. Scott Fitzgerald.', 'English', 1925, 7, 'The Great Gatsby', 1, 7),
    ('Harper Lee', 1, 'To Kill a Mockingbird is a novel by Harper Lee.', 'English', 1960, 5, 'To Kill a Mockingbird', 1, 5),
    ('J.D. Salinger', 1, 'The Catcher in the Rye is a novel by J.D. Salinger.', 'English', 1951, 4, 'The Catcher in the Rye', 1, 4),
    ('J.R.R. Tolkien', 1, 'The Hobbit is a fantasy novel by J.R.R. Tolkien.', 'English', 1937, 3, 'The Hobbit', 1, 3),
    ('Aldous Huxley', 1, 'Brave New World is a dystopian novel by Aldous Huxley.', 'English', 1932, 5, 'Brave New World', 1, 5);
