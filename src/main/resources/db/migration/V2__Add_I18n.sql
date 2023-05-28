DROP TABLE IF EXISTS "locales";
DROP SEQUENCE IF EXISTS locales_id_seq;
CREATE SEQUENCE locales_id_seq;

CREATE TABLE locales (
    id          int DEFAULT nextval('locales_id_seq') NOT NULL,
    name        character varying(2)     NOT NULL,
    flag_url    character varying(20)    NOT NULL,
    full_name   character varying(30)    NOT NULL,

    CONSTRAINT locales_pkey            PRIMARY KEY (id),
    CONSTRAINT locales_name_unique     UNIQUE (name)
) WITH (oids = false);

DROP TABLE IF EXISTS "messages";
DROP SEQUENCE IF EXISTS messages_id_seq;
CREATE SEQUENCE messages_id_seq;

CREATE TABLE messages (
    id          bigint  DEFAULT nextval('messages_id_seq') NOT NULL,
    locale_id   int                     NOT NULL,
    key         character varying(255)  NOT NULL,
    message     character varying(3000),

    CONSTRAINT messages_pkey PRIMARY KEY (id),
    CONSTRAINT messages_key_locale_unique UNIQUE (locale_id, key)
) WITH (oids = false);

ALTER TABLE ONLY messages ADD CONSTRAINT messages_locales_id_fk FOREIGN KEY (locale_id) REFERENCES locales(id) NOT DEFERRABLE;