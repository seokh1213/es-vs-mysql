-- ${flyway:timestamp}
drop table if exists post_entity;
create table post_entity
(
    uid        bigint auto_increment primary key,
    author     varchar(255)  not null,
    content    varchar(1000) not null,
    created_dt datetime(6)   not null,
    title      varchar(255)  not null
);

create fulltext index idx_titleAndContent
    on post_entity (title, content);

create fulltext index idx_title
    on post_entity (title);

create fulltext index idx_content
    on post_entity (content);
