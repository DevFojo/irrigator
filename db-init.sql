CREATE TABLE land
(
    id                   UUID NOT NULL PRIMARY KEY,
    create_time          TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    update_time          TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    last_irrigation_time TIMESTAMP(6),
    size                 REAL NOT NULL,
    soil_type            VARCHAR(10)
);

CREATE TABLE schedule
(
    id            UUID    NOT NULL PRIMARY KEY,
    create_time   TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    update_time   TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    attempts_left INTEGER NOT NULL,
    run_time      TIMESTAMP(6),
    state         VARCHAR(10),
    land_id       UUID    NOT NULL
        CONSTRAINT schedule_land
            REFERENCES land
);

ALTER TABLE public.schedule
    owner TO docker;

