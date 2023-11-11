------------------------------ SCHEMA yan ------------------------------
CREATE SCHEMA IF NOT EXISTS yan;

SET search_path to yan;
------------------------------ SCHEMA yan ------------------------------

------------------------------ DROP triggers ------------------------------
DROP TRIGGER IF EXISTS check_city_exists_trigger on tsk_address;
DROP TRIGGER IF EXISTS update_good_quantity_trigger on tsk_good_order;
DROP TRIGGER IF EXISTS check_currency_exists_trigger  on tsk_good;
------------------------------ DROP triggers ------------------------------


------------------------------ DROP table ------------------------------
DROP TABLE IF EXISTS tsk_city_list;
DROP TABLE IF EXISTS tsk_currency_list;
------------------------------ DROP table ------------------------------

------------------------------ Update Cities ------------------------------
UPDATE tsk_address SET city = upper(city);
------------------------------ Update Cities ------------------------------






