USE moviedb;

DROP PROCEDURE IF EXISTS get_metadata;
DELIMITER //
CREATE PROCEDURE get_metadata()
BEGIN
    SHOW COLUMNS FROM creditcards FROM moviedb;
    SHOW COLUMNS FROM customers FROM moviedb;
    SHOW COLUMNS FROM employees FROM moviedb;
    SHOW COLUMNS FROM genres FROM moviedb;
    SHOW COLUMNS FROM movies FROM moviedb;
    SHOW COLUMNS FROM ratings FROM moviedb;
    SHOW COLUMNS FROM sales FROM moviedb;
    SHOW COLUMNS FROM stars FROM moviedb;
    SHOW COLUMNS FROM stars_in_movies FROM moviedb;
    SHOW COLUMNS FROM genres_in_movies FROM moviedb;

END //
DELIMITER ;

-- shown in demo video at 6:31
DROP PROCEDURE IF EXISTS add_star;
DELIMITER $$
CREATE PROCEDURE add_star(IN name varchar(100), IN birthYear integer)
BEGIN
    DECLARE starid char(10);

    SELECT @id := SUBSTRING(max(id), 3, 8) FROM moviedb.stars; 
    SELECT @integerid := CAST(@id AS SIGNED) + 1;
    SET starid = CONCAT('nm', @integerid);
    INSERT INTO moviedb.stars (id, name, birthYear) VALUES(starid, name, birthYear);
    SELECT starid;
END $$
DELIMITER ;

-- shown in demo video at 6:54
-- DROP PROCEDURE IF EXISTS add_movie;
-- DELIMITER $$
-- CREATE PROCEDURE add_movie(IN newTitle varchar(100), IN newYear integer, IN newDirector varchar(100), IN newStar varchar(100), IN newGenre varchar(32))
-- sp: BEGIN
--     DECLARE starid varchar(10);
--     DECLARE genreid integer;
--     DECLARE movieid varchar(10);

--     IF ( SELECT EXISTS (SELECT * FROM movies WHERE title = newTitle )) THEN 
--         SELECT 'MOVIE EXISTS';
--         LEAVE sp;
--     ELSE
--         SELECT 'MOVIE DOES NOT EXIST';
--         SELECT @currmaxmovieid := SUBSTRING(max(id), 3, 7) FROM stars; 
--         SELECT @integermovieid := CAST(@currmaxmovieid AS SIGNED) + 1;
--         SELECT @movieid := CONCAT('tt', @integermovieid); -- the id of the newly created movie
--         INSERT INTO movies (id, title, year, director) VALUES(@movieid, newTitle, newYear, newDirector);
--     END IF; 
--     IF ( SELECT EXISTS (SELECT * FROM stars WHERE name = newStar )) THEN 
--         SELECT 'STAR EXISTS';
--         SELECT id INTO starid FROM stars WHERE name = newStar;
--         SELECT starid;
--     ELSE
--         SELECT 'STAR DOES NOT EXIST';
--         SELECT @currmaxstarid := SUBSTRING(max(id), 3, 8) FROM stars; 
--         SELECT @integerstarid := CAST(@currmaxstarid AS SIGNED) + 1;
--         SELECT @starid := CONCAT('nm', @integerstarid); -- the id of the newly created star
--         INSERT INTO stars (id, name, birthYear) VALUES(@starid, newStar, birthYear);
--     END IF; 
--     IF ( SELECT EXISTS (SELECT * FROM genres WHERE name = newGenre )) THEN 
--         SELECT 'GENRE EXISTS';
--         SELECT id INTO genreid FROM genres WHERE name = newGenre;
--         SELECT genreid;
--     ELSE
--         SELECT 'GENRE DOES NOT EXIST';
--         SELECT @currmaxgenreid := max(id) FROM genres;
--         SET @genreid = @currmaxgenreid + 1;
--         SELECT @genreid; -- this line is only for debugging purposes
--         INSERT INTO genres (id, name) VALUES(@genreid, newGenre);

--     INSERT INTO genres_in_movies (genreId, movieId) VALUES()
        

--     END IF; 
-- END $$
-- DELIMITER ;


DROP PROCEDURE IF EXISTS add_movie;
DELIMITER $$
CREATE PROCEDURE add_movie(IN newTitle varchar(100), IN newYear integer, IN newDirector varchar(100), IN newStar varchar(100), IN newGenre varchar(32))
sp: BEGIN
    DECLARE starid varchar(10);
    DECLARE genreid integer;
    DECLARE movieid varchar(10);
    DECLARE newmovieid varchar(10);
    DECLARE newstarid varchar(10);
    DECLARE newgenreid integer;

    IF ( SELECT EXISTS (SELECT * FROM movies WHERE title = newTitle )) THEN 
        -- SELECT 'MOVIE EXISTS';
        LEAVE sp;
    ELSE
        -- SELECT 'MOVIE DOES NOT EXIST';
        SELECT @currmaxmovieid := SUBSTRING(max(id), 3, 7) FROM movies; 
        SET @integermovieid := CAST(@currmaxmovieid AS SIGNED) + 1;
        SET movieid = CONCAT('tt', @integermovieid); -- the id of the newly created movie
        INSERT INTO movies (id, title, year, director) VALUES(movieid, newTitle, newYear, newDirector);
        -- SELECT CONCAT('New movieid: ', movieid); -- select id of newly created movie
        SET newmovieid = movieid;
    END IF; 
    IF ( SELECT EXISTS (SELECT * FROM stars WHERE name = newStar )) THEN 
        -- SELECT 'STAR EXISTS';
        SELECT id INTO starid FROM stars WHERE name = newStar;
        SELECT CONCAT('Existing starid: ', starid); -- select id of existing star
    ELSE
        -- SELECT 'STAR DOES NOT EXIST';
        SELECT @currmaxstarid := SUBSTRING(max(id), 3, 8) FROM stars; 
        SET @integerstarid := CAST(@currmaxstarid AS SIGNED) + 1;
        SET starid = CONCAT('nm', @integerstarid); -- the id of the newly created star
        -- SELECT CONCAT('New starid: ', starid); -- select id of new star
        SET newstarid = starid;
        INSERT INTO stars (id, name, birthYear) VALUES(starid, newStar, birthYear);
    END IF; 
    IF ( SELECT EXISTS (SELECT * FROM genres WHERE name = newGenre )) THEN 
        -- SELECT 'GENRE EXISTS';
        SELECT id INTO genreid FROM genres WHERE name = newGenre;
        -- SELECT CONCAT('Existing genreid: ', genreid); -- select id of existing genre
    ELSE
        -- SELECT 'GENRE DOES NOT EXIST';
        SELECT @currmaxgenreid := max(id) FROM genres;
        SET genreid = @currmaxgenreid + 1;
        -- SELECT CONCAT('New genreid: ', genreid); -- select id of new genre
        SET newgenreid = genreid;
        INSERT INTO genres (id, name) VALUES(genreid, newGenre);
        

    END IF; 

    INSERT INTO stars_in_movies (starId, movieId) VALUES(starid, movieid);
    INSERT INTO genres_in_movies (genreId, movieId) VALUES(genreid, movieid);
    SELECT newmovieid, newstarid, newgenreid;
    
END $$
DELIMITER ;


