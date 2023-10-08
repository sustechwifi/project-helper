create table oauth_user
(
    login        varchar(63)                                                                                          not null,
    platform     varchar(63)  default 'local'::character varying                                                      not null,
    uuid         numeric      default (floor((random() * ('9223372036854775807'::bigint)::double precision)))::bigint not null
        constraint uuid
            primary key,
    avatar_url   text,
    authority    varchar(255) default 'UNKNOWN'::character varying                                                    not null,
    name         varchar(63),
    email        text,
    introduction varchar(1023),
    grade        varchar(63),
    major        varchar(63),
    college      varchar(63),
    sex          varchar(15),
    enabled      boolean      default true                                                                            not null,
    create_time  date         default now()                                                                           not null,
    password     varchar(511)                                                                                         not null,
    constraint oauth_user_unq_login_platform
        unique (login, platform)
);

comment on table oauth_user is 'oauth2 认证用户表';

comment on column oauth_user.login is '该平台登录用户名';

comment on column oauth_user.platform is '认证平台的名称';

comment on column oauth_user.uuid is '认证用户主键';

comment on constraint uuid on oauth_user is '认证用户主键';

comment on column oauth_user.avatar_url is '头像路径';

comment on column oauth_user.authority is '权限';

comment on column oauth_user.name is '用户昵称';

comment on column oauth_user.email is '绑定的邮箱';

comment on column oauth_user.introduction is '个人简介';

comment on column oauth_user.grade is '年级';

comment on column oauth_user.major is '专业';

comment on column oauth_user.college is '书院';

comment on column oauth_user.sex is '性别';

comment on column oauth_user.enabled is '是否启用';

comment on column oauth_user.create_time is '创建时间';

comment on column oauth_user.password is '密码';

comment on constraint oauth_user_unq_login_platform on oauth_user is '登录名-平台名 唯一列';

alter table oauth_user
    owner to postgres;

