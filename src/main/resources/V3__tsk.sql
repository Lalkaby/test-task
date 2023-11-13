------------------------------ SCHEMA yan ------------------------------
CREATE SCHEMA IF NOT EXISTS yan;

SET search_path to yan;
------------------------------ SCHEMA yan ------------------------------

------------------------------ ALTER order add column ------------------------------
ALTER TABLE tsk_order add column status varchar(50) not null default 'COMPLETED';
ALTER TABLE tsk_order add constraint tsk_order_status_check check (tsk_order.status in ('ACTIVE','CANCELLED','COMPLETED'))
------------------------------ ALTER order add column ------------------------------



