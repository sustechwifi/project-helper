create database ooad_project_helper;



create table public.oauth_user
(
    third_id     integer                              not null,
    login        varchar                              not null,
    platform     varchar       default 'spring'       not null,
    uuid         integer       default floor(random() * 9223372036854775807)::bigint
        constraint uuid
            primary key,
    avatar_url   text,
    authority    varchar       default 'ROLE_STUDENT' not null,
    name         varchar(63),
    email        text,
    introduction varchar(1023) default 'no set yet'   not null,
    grade        varchar(63),
    major        varchar,
    college      varchar,
    sex          varchar,
    enabled      bool          default true           not null,
    constraint platform_username_unq
        unique (login, platform)
);

comment on table public.oauth_user is 'oauth2 认证用户表';

comment on column public.oauth_user.third_id is '该平台的用户id';

comment on column public.oauth_user.login is '该平台登录用户名';

comment on column public.oauth_user.platform is '认证平台的名称';

comment on column public.oauth_user.uuid is '认证用户主键';

comment on constraint uuid on public.oauth_user is '认证用户主键';

comment on column public.oauth_user.avatar_url is '头像路径';

comment on column public.oauth_user.authority is '权限';

comment on column public.oauth_user.name is '用户昵称';

comment on column public.oauth_user.email is '绑定的邮箱';

comment on column public.oauth_user.introduction is '个人简介';

comment on column public.oauth_user.grade is '年级';

comment on column public.oauth_user.major is '专业';

comment on column public.oauth_user.college is '书院';

comment on column public.oauth_user.sex is '性别';

comment on column public.oauth_user.enabled is '是否启用';

comment on constraint platform_username_unq on public.oauth_user is '已认证的登录名';

