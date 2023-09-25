create database ooad_oauth_server;

create table if not exists auth_user
(
    id         integer default (floor((random() * ('9223372036854775807'::bigint)::double precision)))::bigint not null
        constraint auth_user_pk
            primary key,
    username   varchar                                                                                         not null
        constraint username_unq
            unique,
    password   varchar                                                                                         not null,
    authority  varchar default 'None'::character varying                                                       not null,
    avatar_url text,
    email      varchar
);

comment on table auth_user is '认证用户';

comment on column auth_user.id is '用户id';

comment on column auth_user.username is '用户名';

comment on column auth_user.password is '密码';

comment on column auth_user.authority is '权限列表';

comment on column auth_user.avatar_url is '头像地址';

comment on column auth_user.email is '认证邮箱';

alter table auth_user
    owner to postgres;

