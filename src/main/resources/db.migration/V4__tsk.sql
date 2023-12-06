------------------------------ SCHEMA yan ------------------------------
CREATE SCHEMA IF NOT EXISTS yan;

SET search_path to yan;
------------------------------ SCHEMA yan ------------------------------


CREATE table order_event(
                            uuid uuid not null,
                            status varchar(50),
                            order_json json
)





