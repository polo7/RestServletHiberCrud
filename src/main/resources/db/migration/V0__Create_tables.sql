CREATE TABLE files (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    filePath VARCHAR(255) NOT NULL,
    PRIMARY KEY(id)
);


--CREATE TABLE labels (
--    id BIGINT NOT NULL AUTO_INCREMENT,
--    name VARCHAR(30) NOT NULL,
--    status VARCHAR(10) NOT NULL,
--    PRIMARY KEY(id)
--);
--
--CREATE TABLE writers (
--    id BIGINT NOT NULL AUTO_INCREMENT,
--    firstName VARCHAR(50) NOT NULL,
--    lastName VARCHAR(50) NOT NULL,
--    status VARCHAR(10) NOT NULL,
--    PRIMARY KEY(id)
--);
--
--CREATE TABLE posts (
--    id BIGINT NOT NULL AUTO_INCREMENT,
--    title VARCHAR(50) NOT NULL,
--    content TEXT,
--    status VARCHAR(10) NOT NULL,
--    writerId BIGINT,
--    PRIMARY KEY(id),
--    FOREIGN KEY(writerId) REFERENCES writers(id)
--);
--
--CREATE TABLE post_labels (
--    postId BIGINT NOT NULL,
--    labelId BIGINT NOT NULL,
--    FOREIGN KEY(postId) REFERENCES posts(id),
--    FOREIGN KEY (labelId) REFERENCES labels(id),
--    UNIQUE (postId, labelId)
--);