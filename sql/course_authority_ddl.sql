create table course_authority
(
    id           numeric default (floor((random() * ('922330367'::bigint)::double precision)))::bigint not null
        constraint course_authority_pk
            primary key,
    course_id    numeric                                                                               not null,
    auth_user_id numeric                                                                               not null,
    authority    varchar default 'unknown'::character varying                                          not null
);

comment on table course_authority is '课程内的权限';

comment on constraint course_authority_pk on course_authority is 'id';

comment on column course_authority.course_id is '课程 id';

comment on column course_authority.auth_user_id is '已认证的用户 id';

comment on column course_authority.authority is '权限名';

alter table course_authority
    owner to postgres;

