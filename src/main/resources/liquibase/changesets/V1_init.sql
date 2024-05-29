create table if not exists person
(
    user_id         serial
        primary key,
    credentials     varchar(255)
        unique,
    birth_date      integer                                                   not null,
    username        varchar(100) default 'usr'::character varying             not null,
    password        varchar      default 'pswd'::character varying            not null,
    role            varchar      default 'rl'::character varying              not null,
    email           varchar      default 'email@email.com'::character varying not null,
    activation_code varchar,
    is_active       boolean
);

create table if not exists book
(
    book_id          serial
        primary key,
    user_id          integer
        references person,
    title            varchar(255),
    author           varchar(255),
    publication_year integer not null,
    taken_at         timestamp,
    image            varchar(255)
);

