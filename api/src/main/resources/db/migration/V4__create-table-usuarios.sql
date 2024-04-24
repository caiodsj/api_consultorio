CREATE TABLE usuarios(
id bigserial not null primary key,
username varchar(100) not null,
password varchar(255) not null
)