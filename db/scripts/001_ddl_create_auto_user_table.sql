create table if not exists auto_user (
    id serial primary key,
    login varchar,
    password varchar
);