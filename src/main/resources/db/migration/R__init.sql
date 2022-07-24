drop table if exists post_entity;
create table post_entity
(
    uid        bigint auto_increment primary key,
    author     varchar(255)  not null,
    content    varchar(1000) not null,
    created_dt datetime(6)   not null,
    title      varchar(255)  not null
);

create fulltext index titleAndContent
    on post_entity (title, content);

