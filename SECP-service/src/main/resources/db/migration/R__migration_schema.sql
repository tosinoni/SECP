create table IF NOT EXISTS Devices (
       id bigint not null,
        name varchar(255) not null,
        public_key varchar(255) not null,
        primary key (id)
) engine=MyISAM;

create table IF NOT EXISTS Groups (
        id bigint not null,
        name varchar(255) not null,
        isActive BOOLEAN not null DEFAULT TRUE,
        primary key (id)
) engine=MyISAM;

create table IF NOT EXISTS group_permissions (
       group_id bigint not null,
        permission_id bigint not null,
        primary key (group_id, permission_id)
) engine=MyISAM;

create table IF NOT EXISTS group_roles (
       group_id bigint not null,
        role_id bigint not null,
        primary key (group_id, role_id)
) engine=MyISAM;

create table IF NOT EXISTS group_user (
       group_id bigint not null,
        user_id bigint not null,
        primary key (group_id, user_id)
) engine=MyISAM;

create table IF NOT EXISTS hibernate_sequence (
       next_val bigint
) engine=MyISAM;

create table IF NOT EXISTS message (
       id integer not null,
        body longtext not null,
        timestamp datetime,
        group_id bigint not null,
        user_id bigint not null,
        primary key (id)
) engine=MyISAM;

create table IF NOT EXISTS Permissions (
       id bigint not null,
        level varchar(255) not null,
        color varchar(255) not null,
        primary key (id)
) engine=MyISAM;

create table IF NOT EXISTS Roles (
       id bigint not null,
        role varchar(255) not null,
        color varchar(255) not null,
        primary key (id)
) engine=MyISAM;

create table IF NOT EXISTS Users(
       id bigint not null,
        email varchar(255) not null,
        firstname varchar(255) not null,
        lastname varchar(255) not null,
        login_role varchar(255) not null,
        password varchar(255) not null,
        username varchar(255) not null,
        permission_id bigint not null,
        isActive BOOLEAN not null DEFAULT TRUE,
        primary key (id)
) engine=MyISAM;

create table IF NOT EXISTS user_devices (
       user_id bigint not null,
        device_id bigint not null,
        primary key (user_id, device_id)
) engine=MyISAM;

create table IF NOT EXISTS user_roles (
       user_id bigint not null,
        role_id bigint not null,
        primary key (user_id, role_id)
) engine=MyISAM;

create table IF NOT EXISTS Filters (
        id bigint not null,
        name varchar(255) not null,
        primary key (id)
) engine=MyISAM;

create table IF NOT EXISTS filter_permissions (
       filter_id bigint not null,
        permission_id bigint not null,
        primary key (filter_id, permission_id)
) engine=MyISAM;

create table IF NOT EXISTS filter_roles (
       filter_id bigint not null,
        role_id bigint not null,
        primary key (filter_id, role_id)
) engine=MyISAM;


#********************** Column changes for table should be added here ************************
Alter TABLE Users ADD COLUMN if not exists isActive BOOLEAN DEFAULT TRUE;
Alter TABLE Groups ADD COLUMN if not exists isActive BOOLEAN DEFAULT TRUE;
ALTER TABLE Groups ADD COLUMN if not exists group_type VARCHAR(255);
DROP TABLE IF EXISTS user_permissions;
ALTER TABLE USERS ADD COLUMN if not exists permission_id bigint;
ALTER TABLE Users ADD COLUMN if not exists display_name VARCHAR(255);
ALTER TABLE Users ADD COLUMN if not exists avatar_url VARCHAR (255);
ALTER TABLE Roles ADD COLUMN if not exists color VARCHAR(255);
ALTER TABLE Permissions ADD COLUMN if not exists color VARCHAR(255);
ALTER TABLE Groups ADD COLUMN if not exists avatar_url VARCHAR (255);
ALTER TABLE Groups ADD COLUMN if not exists display_name VARCHAR(255);
