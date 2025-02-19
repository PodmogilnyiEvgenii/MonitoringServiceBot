CREATE SCHEMA IF NOT EXISTS monitoring;

CREATE TABLE IF NOT EXISTS monitoring.crash
(
    id                      BIGSERIAL NOT NULL PRIMARY KEY,
    timestamp               TIMESTAMP,
    service_name            VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS monitoring.scheduler
(
    id                      BIGSERIAL NOT NULL PRIMARY KEY,
    chat_id                 VARCHAR(20),
    service_name            VARCHAR(100),
    api_link                VARCHAR(1000),
    good_answer             VARCHAR(100),
    timeout_sec             SMALLINT
);




