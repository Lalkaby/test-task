ALTER TABLE orders drop constraint  tsk_order_status_check;
ALTER TABLE orders add constraint order_status_check check
    (orders.status in ('ACTIVE','CANCELLED','COMPLETED','DRAFT'))



