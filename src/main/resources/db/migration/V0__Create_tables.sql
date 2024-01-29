CREATE TABLE files (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    status VARCHAR(7) NOT NULL
);

CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    status VARCHAR(7) NOT NULL
);

CREATE TABLE events (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    file_id INT NOT NULL,
    status VARCHAR(7) NOT NULL,
    FOREIGN KEY(user_id) REFERENCES users(id),
    FOREIGN KEY(file_id) REFERENCES files(id)
);

INSERT INTO users(name, status) VALUES("guest", "ACTIVE");

--CREATE TABLE labels(
--    id SERIAL PRIMARY KEY,
--    name VARCHAR,
--    status VARCHAR
--);
--
--CREATE TABLE writers(
--    id SERIAL PRIMARY KEY,
--    first_name VARCHAR,
--    last_name VARCHAR,
--    status VARCHAR
--);
--
--CREATE TABLE posts(
--    id SERIAL PRIMARY KEY,
--    title VARCHAR,
--    content VARCHAR,
--    status VARCHAR,
--    writer_id BIGINT REFERENCES writers(id)
--);
--
--CREATE TABLE post_labels(
--    post_id BIGINT REFERENCES posts(id),
--    label_id BIGINT REFERENCES labels(id),
--    UNIQUE (post_id, label_id)
--);