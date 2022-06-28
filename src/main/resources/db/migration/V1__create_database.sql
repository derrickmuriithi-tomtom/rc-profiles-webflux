
create extension if not exists "uuid-ossp";

create table company
(
    id   uuid primary key default uuid_generate_v1(),
    name text
);

create table profile
(
    id    uuid primary key default uuid_generate_v1(),
    company_id         uuid                            not null,
    name               text                            not null
        unique,
    description        text,
    area               text                            not null,
    state              text    default 'CREATED'::text not null,
    source_map_version text,
    rc_map_version     integer default 0               not null,
    profile_id         text                            not null
);

alter table profile
    owner to postgres;

create unique index profile_profile_id_uindex
    on profile (profile_id);

INSERT INTO company(id, name) VALUES('ebb43330-7c38-48d3-9ddd-565c76bd2ab5', 'AutoStream');
INSERT INTO profile (profile_id, company_id, name, description, area, state, source_map_version, rc_map_version) VALUES( 'AS-123',
                                                                                                               'ebb43330-7c38-48d3-9ddd-565c76bd2ab5', 'AutoStream' ,'Mock data for AutoStream Tests', '42.283862,-84.024605,42.304178,-83.901696', 'ACTIVE', '{"majorVersion":71,"baseLevel":12,"layerMinorVersions":{"HDMAPHDROAD":1}}', 2);