create table Wallet
(id bigint generated by default as identity,
name varchar(255),
balance double not null,
primary key (id));