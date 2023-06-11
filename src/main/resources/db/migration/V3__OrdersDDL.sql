DROP TABLE IF EXISTS "orders";
DROP SEQUENCE IF EXISTS orders_id_seq;
CREATE SEQUENCE orders_id_seq;

CREATE TABLE orders (
    id              bigint DEFAULT nextval('orders_id_seq') NOT NULL,
    internal_id     character varying(30)       NOT NULL,
    name            character varying(255)      NOT NULL,
    client_name     character varying(60),
    price           decimal(12,2),
    finish_date     date,

    CONSTRAINT orders_pkey                      PRIMARY KEY (id),
    CONSTRAINT orders_internal_id_unique        UNIQUE (internal_id)
) WITH (oids = false);
