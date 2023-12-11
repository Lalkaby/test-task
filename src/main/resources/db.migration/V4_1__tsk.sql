------------------------------ SCHEMA yan ------------------------------
CREATE SCHEMA IF NOT EXISTS yan;

SET search_path to yan;
------------------------------ SCHEMA yan ------------------------------

alter table order_event alter column order_json type jsonb;





