CREATE DATABASE if not exists moviedb;

USE moviedb;

-- all attributes required;
CREATE TABLE if not exists movies(
	id VARCHAR(10) DEFAULT '',
	title VARCHAR(100) DEFAULT '',
	year INTEGER NOT NULL,
	director VARCHAR(100) DEFAULT '',
	PRIMARY KEY (id)
);

-- all attributes but birthyear required;
CREATE TABLE IF NOT EXISTS stars(
        id varchar(10) primary key,
        name varchar(100) not null,
     	birthYear integer
);

-- all attributes required;
CREATE TABLE IF NOT EXISTS stars_in_movies(
  	starId VARCHAR(10) DEFAULT '',
	movieId VARCHAR(10) DEFAULT '',
	FOREIGN KEY (starId) REFERENCES stars(id),
	FOREIGN KEY (movieId) REFERENCES movies(id)
);

-- all attributes required; "id" should be "AUTO_INCREMENT"
CREATE TABLE IF NOT EXISTS genres(
	id integer primary key AUTO_INCREMENT,
	name varchar(32) not null
);

-- all attributes required;
CREATE TABLE IF NOT EXISTS genres_in_movies(
	genreId integer not null,
	movieId varchar(10) not null,
	FOREIGN KEY (genreId) REFERENCES genres(id),
	FOREIGN KEY (movieId) REFERENCES movies(id)
);


-- all attributes required;
CREATE TABLE IF NOT EXISTS creditcards(
	id varchar(20) primary key,
	firstName varchar(50) not null,
	lastName varchar(50) not null,
	expiration date not null
);

-- all attributes required; "id" should be "AUTO_INCREMENT"
CREATE TABLE IF NOT EXISTS customers(
	id integer primary key AUTO_INCREMENT,
	firstName varchar(50) not null,
	lastName varchar(50) not null,
	ccId varchar(20)  DEFAULT '',
	address varchar(200) not null,
	email varchar(50) not null,
	password varchar(20) not null,
	FOREIGN KEY (ccId) REFERENCES creditcards(id)
);

-- all attributes required;"id" should be "AUTO_INCREMENT"
CREATE TABLE IF NOT EXISTS sales(
	id integer primary key AUTO_INCREMENT,
	customerId integer DEFAULT null,
	movieId varchar(10) DEFAULT '',
	saleDate date not null,
	FOREIGN KEY (customerId) REFERENCES customers(id),
	FOREIGN KEY (movieId) REFERENCES movies(id)
);


-- all attributes required;
CREATE TABLE IF NOT EXISTS creditcards(
	id varchar(20) primary key,
	firstName varchar(50) not null,
	lastName varchar(50) not null,
	expiration date not null
);

-- all attributes required;
CREATE TABLE IF NOT EXISTS ratings(
	movieId varchar(10) not null,
	rating float not null,
	numVotes integer not null,
	FOREIGN KEY (movieId) REFERENCES movies(id)
);