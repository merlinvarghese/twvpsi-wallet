create table Transactions
(id bigint generated by default as identity,
transaction_type varchar(255),
amount double not null,
wallet_id bigint,
primary key (id))
