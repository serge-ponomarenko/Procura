DROP TABLE IF EXISTS "roles";
DROP SEQUENCE IF EXISTS roles_id_seq;
CREATE SEQUENCE roles_id_seq;

CREATE TABLE roles (
    id bigint DEFAULT nextval('roles_id_seq') NOT NULL,
    name character varying(255) NOT NULL,

    CONSTRAINT roles_pkey PRIMARY KEY (id),
    CONSTRAINT roles_name_unique UNIQUE (name)
) WITH (oids = false);

DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS users_id_seq;
CREATE SEQUENCE users_id_seq;

CREATE TABLE users (
    id bigint DEFAULT nextval('users_id_seq') NOT NULL,
    email character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    provider character varying(15) NOT NULL,

    CONSTRAINT users_email_unique UNIQUE (email),
    CONSTRAINT users_pkey PRIMARY KEY (id)
) WITH (oids = false);

DROP TABLE IF EXISTS users_roles;
CREATE TABLE users_roles (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
) WITH (oids = false);

ALTER TABLE ONLY users_roles ADD CONSTRAINT users_roles_user_id_fk FOREIGN KEY (user_id) REFERENCES users(id) NOT DEFERRABLE;
ALTER TABLE ONLY users_roles ADD CONSTRAINT "users_roles_role_id_fk" FOREIGN KEY (role_id) REFERENCES roles(id) NOT DEFERRABLE;