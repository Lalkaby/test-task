------------------------------ SCHEMA yan ------------------------------
CREATE SCHEMA IF NOT EXISTS yan;

SET search_path to yan;
------------------------------ SCHEMA yan ------------------------------

------------------------------ DROP triggers ------------------------------
DROP TRIGGER IF EXISTS check_city_exists_trigger on address;
DROP TRIGGER IF EXISTS update_good_quantity_trigger on good_order;
DROP TRIGGER IF EXISTS check_currency_exists_trigger  on good;
------------------------------ DROP triggers ------------------------------


------------------------------ DROP table ------------------------------
DROP TABLE IF EXISTS city_list;
DROP TABLE IF EXISTS currency_list;
------------------------------ DROP table ------------------------------

------------------------------ Update Cities ------------------------------
UPDATE address SET city = upper(city);
------------------------------ Update Cities ------------------------------






