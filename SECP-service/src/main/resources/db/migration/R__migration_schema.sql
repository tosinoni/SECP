create table IF NOT EXISTS Devices (
       id bigint not null,
        name varchar(255) not null,
        public_key varchar(255) not null,
        primary key (id)
) engine=MyISAM;

create table IF NOT EXISTS Groups (
       id bigint not null,
        name varchar(255) not null,
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
        primary key (id)
) engine=MyISAM;

create table IF NOT EXISTS Roles (
       id bigint not null,
        role varchar(255) not null,
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
        primary key (id)
) engine=MyISAM;

create table IF NOT EXISTS user_devices (
       user_id bigint not null,
        device_id bigint not null,
        primary key (user_id, device_id)
) engine=MyISAM;

create table IF NOT EXISTS user_permissions (
       user_id bigint not null,
        permission_id bigint not null,
        primary key (user_id, permission_id)
) engine=MyISAM;

create table IF NOT EXISTS user_roles (
       user_id bigint not null,
        role_id bigint not null,
        primary key (user_id, role_id)
) engine=MyISAM;




#********************** Column changes for table should be added here ************************