------------------------------ TABLE city list ------------------------------
CREATE TABLE  IF NOT EXISTS city_list(
    city_name varchar(255) not null
);

alter table city_list add constraint city_list_pk
    primary key(city_name);
------------------------------ TABLE city list ------------------------------

------------------------------ INSERT city list ------------------------------
insert into city_list(city_name) values('Vitebsk');
insert into city_list(city_name) values('Minsk');
------------------------------ INSERT city list ------------------------------


------------------------------ TABLE address ------------------------------
CREATE TABLE IF NOT EXISTS address(
                                      id int generated always as identity not null,
                                      city varchar(255) not null,
                                      street varchar(255) not null,
                                      house varchar(255) not null
);

alter table address add constraint address_id_pk
    primary key(id);
alter table address add constraint address_unique
    unique (city,street,house);
------------------------------ TABLE address ------------------------------

------------------------------ TRIGGER address ------------------------------
CREATE OR REPLACE FUNCTION check_city_exists()
    RETURNS TRIGGER AS $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM city_list WHERE city_name = NEW.city
    ) THEN
        RAISE EXCEPTION 'City does not exist in city list';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_city_exists_trigger
    BEFORE INSERT OR UPDATE ON address
    FOR EACH ROW
EXECUTE FUNCTION check_city_exists();
------------------------------ TRIGGER address ------------------------------


------------------------------ INSERT address ------------------------------
insert into address(city,street,house) values('Vitebsk','Moscow avenue','37');
insert into address(city,street,house) values('Vitebsk','Moscow avenue','42');
insert into address(city,street,house) values('Vitebsk','Bilevo street','105');
insert into address(city,street,house) values('Minsk','Sharangovich street','76');
insert into address(city,street,house) values('Vitebsk','Zamkovaya street','5/2A');
insert into address(city,street,house) values('Vitebsk','Frunze avenue','30');
insert into address(city,street,house) values('Vitebsk','Stroiteley avenue','12');
insert into address(city,street,house) values('Minsk','Yakubovsky street','49');
insert into address(city,street,house) values('Minsk','Yakubovsky street','34');
insert into address(city,street,house) values('Minsk', 'Karla Marksa Street','29');
------------------------------ INSERT address ------------------------------


------------------------------ TABLE orders ------------------------------
CREATE TABLE IF NOT EXISTS orders(
                                    id int generated always as identity not null,
                                    id_address int not null,
                                    username varchar(127) not null,
                                    order_time timestamp default current_timestamp not null,
                                    phone_number text not null,
                                    user_email text
);

alter table orders add constraint order_id_pk
    primary key(id);
alter table orders add constraint order_address_fk
    foreign key(id_address) references address(id);

------------------------------ TABLE orders ------------------------------

------------------------------ INDEX orders(username,order_time,phone_number) ------------------------------
CREATE INDEX IF NOT EXISTS order_username_idx on orders(username);
CREATE INDEX IF NOT EXISTS order_order_time_idx on orders(order_time);
CREATE INDEX IF NOT EXISTS order_phone_number_idx on orders(phone_number);
------------------------------ INDEX orders(username,order_time,phone_number) ------------------------------


------------------------------ INSERT orders ------------------------------
INSERT INTO orders (id_address,username,phone_number,user_email)
VALUES
    (4,'Justine Fitzpatrick','+375253536529','dolor.egestas@protonmail.ca'),
    (6,'Allen Cooley','+375297858283','consectetuer@outlook.org'),
    (2,'Alea Wright','+375446183390','fames.ac.turpis@protonmail.couk'),
    (4,'Tobias Harris','+375335253792','neque.sed@yahoo.edu'),
    (5,'Grady Graham','+375443613813','sed.pede@hotmail.edu'),
    (2,'Shaine Hunt','+375335467727','ullamcorper.magna@yahoo.edu'),
    (1,'Elijah Barlow','+375296348601','ante.bibendum@hotmail.edu'),
    (3,'Daquan Kirkland','+375254630726','auctor.nunc@protonmail.com'),
    (4,'Jaden Shepard','+375294393825','mi.eleifend@protonmail.net'),
    (3,'Nicholas Mckinney','+375253697329','magna.lorem@hotmail.com');
INSERT INTO orders (id_address,username,phone_number,user_email)
VALUES
    (2,'Alfonso Wong','+375299588812','pellentesque.sed@protonmail.couk'),
    (7,'Rudyard Haynes','+375259746521','ipsum@hotmail.edu'),
    (2,'Elton White','+375258719626','ultricies.adipiscing@icloud.net'),
    (7,'Miranda Workman','+375444492743','velit.justo@aol.couk'),
    (5,'Inez Monroe','+375441148842','curabitur.consequat.lectus@protonmail.com'),
    (8,'Carla Duran','+375339362711','amet.consectetuer.adipiscing@google.ca'),
    (8,'Cyrus Church','+375332517856','id@google.ca'),
    (8,'Elton Singleton','+375332242849','posuere.enim.nisl@hotmail.edu'),
    (2,'Summer Woodward','+375255597740','tellus.nunc.lectus@google.couk'),
    (6,'Bree Madden','+375443233374','eu.metus@outlook.com');
INSERT INTO orders (id_address,username,phone_number,user_email)
VALUES
    (8,'Lane Coffey','+375447923207','eu@hotmail.couk'),
    (7,'Darius Floyd','+375338469943','in.faucibus@icloud.couk'),
    (4,'Jescie Moon','+375332684139','sed.sem@aol.edu'),
    (7,'Dexter Stewart','+375294371131','sit@outlook.ca'),
    (10,'Gabriel Bradford','+375251526658','ridiculus.mus@protonmail.org'),
    (4,'Len Benton','+375336468435','ante.blandit@google.net'),
    (2,'Hermione Bonner','+375257430423','nunc.interdum@icloud.org'),
    (9,'Mara Craig','+375445844802','feugiat.metus@hotmail.org'),
    (4,'Benjamin Anderson','+375447556439','proin@protonmail.edu'),
    (9,'Lisandra Franklin','+375444703631','enim.mauris.quis@hotmail.ca');
INSERT INTO orders (id_address,username,phone_number,user_email,order_time)
VALUES
    (8,'Kellie Luna','+375335885316','lectus.pede@hotmail.ca','2023-11-07 15:13:30'),
    (6,'Ivor Newton','+375292981362','elementum.lorem.ut@google.couk','2023-11-07 15:13:30'),
    (8,'Hilel Hahn','+375335207002','odio.aliquam@yahoo.edu','2023-11-04 18:55:13'),
    (5,'Phyllis Finley','+375334869222','integer@yahoo.com','2023-11-04 18:55:13'),
    (8,'Magee Strickland','+375293921372','fringilla@protonmail.com','2023-11-04 18:55:13'),
    (2,'Noah Carter','+375296134889','blandit.mattis.cras@icloud.org','2023-11-07 15:13:30'),
    (2,'Rana Olsen','+375339623837','donec.consectetuer@protonmail.net','2023-11-07 15:13:30'),
    (7,'Brennan Chandler','+375295884824','mi@icloud.net','2023-11-07 15:13:30'),
    (9,'Patrick Kirby','+375339628491','at@protonmail.ca','2023-11-07 15:13:30'),
    (6,'Maggy Anthony','+375297354636','risus.donec@outlook.com','2023-11-07 15:13:30');
INSERT INTO orders (id_address,username,phone_number,user_email,order_time)
VALUES
    (7,'Jin Mosley','+375448913866','aliquet.magna.a@yahoo.net','2023-11-07 15:13:30'),
    (2,'Ria Wolf','+375330124347','vel.vulputate@aol.ca','2023-11-07 15:13:30'),
    (2,'Bianca Alvarado','+375446534426','eleifend.cras@outlook.org','2023-11-04 18:55:13'),
    (8,'Barclay Osborn','+375331477673','tortor.integer.aliquam@aol.com','2023-11-04 18:55:13'),
    (8,'Lavinia Steele','+375259829510','arcu.vestibulum@yahoo.ca','2023-11-04 18:55:13'),
    (6,'Otto Baldwin','+375294875974','morbi.vehicula.pellentesque@google.com','2023-11-04 18:55:13'),
    (2,'Nathaniel Roman','+375255297967','congue.in@hotmail.couk','2023-11-04 18:55:13'),
    (7,'Anthony Bailey','+375447431957','commodo.auctor@outlook.com','2023-11-07 15:13:30'),
    (8,'Ferris Rich','+375295144512','feugiat.lorem.ipsum@outlook.couk','2023-11-04 18:55:13'),
    (2,'Mannix Summers','+375337634367','fringilla.euismod.enim@yahoo.couk','2023-11-04 18:55:13');
------------------------------ INSERT orders ------------------------------



------------------------------ TABLE currency list ------------------------------
CREATE TABLE  IF NOT EXISTS currency_list(
    currency_name varchar(50) not null
);

alter table currency_list add constraint currency_list_pk
    primary key(currency_name);
------------------------------ TABLE currency list ------------------------------

------------------------------ INSERT city list ------------------------------
insert into currency_list(currency_name) values('USD');
insert into currency_list(currency_name) values('BYN');
insert into currency_list(currency_name) values('RUB');
------------------------------ INSERT city list ------------------------------


------------------------------ TABLE good  ------------------------------
CREATE TABLE IF NOT EXISTS good(
                                   id int generated always as identity not null,
                                   title text not null,
                                   amount int default 0 not null,
                                   producer text not null,
                                   price numeric(7,5) not null,
                                   currency text not null
);

alter table good add constraint good_id_pk
    primary key(id);
alter table good add constraint good_title_producer_unique
    unique (title,producer,currency);
alter table good add constraint good_price_check
    check (price >= 0.0);
alter table good add constraint good_amount_check
    check (price >= 0);
------------------------------ TABLE good  ------------------------------

------------------------------ TRIGGER good  ------------------------------
CREATE OR REPLACE FUNCTION check_currency_exists()
    RETURNS TRIGGER AS $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM currency_list WHERE currency_name = NEW.currency
    ) THEN
        RAISE EXCEPTION 'Currency does not exist in currency list';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_currency_exists_trigger
    BEFORE INSERT OR UPDATE ON good
    FOR EACH ROW
EXECUTE FUNCTION check_currency_exists();
------------------------------ TRIGGER good  ------------------------------


------------------------------ INDEX good(amount, price) ------------------------------
CREATE INDEX IF NOT EXISTS good_amount_idx on good(amount);
CREATE INDEX IF NOT EXISTS good_price_idx on good(price);
------------------------------ INDEX good(amount, price) ------------------------------


------------------------------ INSERT good  ------------------------------
INSERT INTO good (title, amount, producer, price, currency) VALUES
                                                                ('Buckwheat 1000g Bag', 100, 'Golden Fields Farm', 10.99, 'USD'),
                                                                ('Milk 2.5% 1 liter', 150, 'Sunrise Dairy Farm', 15.50, 'BYN'),
                                                                ('Fresh Vegetable Assortment', 200, 'Green Corner Organic Farm', 5.99, 'RUB'),
                                                                ('Granulated Sugar 1 kg', 150, 'Slavic Sugar Plant', 2.49, 'USD'),
                                                                ('Butter 200g', 180, 'Delightful Taste Dairy Combine', 3.99, 'BYN'),
                                                                ('Borodinsky Bread 500g', 120, 'Home Comfort Bakery', 1.99, 'RUB'),
                                                                ('Gala Apples 1 kg', 190, 'Garden Paradise Farm', 2.49, 'USD'),
                                                                ('Red Scarlet Potatoes 2 kg', 160, 'Golden Earth Farm', 1.99, 'BYN'),
                                                                ('Fresh Chicken Eggs 10 pcs', 100, 'Egg Meadow Farm', 3.99, 'RUB'),
                                                                ('Natural Honey 500g', 130, 'Golden Hive Apiary', 9.99, 'USD');
------------------------------ INSERT good  ------------------------------

------------------------------ TABLE good_order  ------------------------------
CREATE TABLE IF NOT EXISTS good_order(
                                         id_good int not null,
                                         id_order int not null,
                                         amount int not null
);

alter table good_order add constraint good_order_pk
    primary key(id_good,id_order);
alter table good_order add constraint good_order_good_fk
    foreign key (id_good) references good(id);
alter table good_order add constraint good_order_order_fk
    foreign key (id_order) references orders(id);
alter table good_order add constraint good_order_amount_check
    check (amount > 0);
------------------------------ TABLE good_order  ------------------------------


------------------------------ TRIGGER good_order  ------------------------------
CREATE OR REPLACE FUNCTION update_good_quantity()
    RETURNS TRIGGER AS $$
BEGIN
    IF NEW.amount > (
        SELECT amount FROM good WHERE id = NEW.id_good
    ) THEN
        RAISE EXCEPTION 'Amount requested exceeds amount available';
    END IF;

    UPDATE good
    SET amount = amount - NEW.amount
    WHERE id = NEW.id_good;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_good_quantity_trigger
    AFTER INSERT ON good_order
    FOR EACH ROW
EXECUTE FUNCTION update_good_quantity();
------------------------------ TRIGGER good_order  ------------------------------


------------------------------ INSERT good_order  ------------------------------
INSERT INTO good_order (id_good, id_order, amount)
VALUES
    (1, 1, 5),
    (2, 1, 3),
    (1, 2, 2),
    (3, 2, 7),
    (4, 3, 1),
    (5, 3, 10),
    (6, 4, 4),
    (7, 4, 2),
    (8, 5, 6),
    (9, 5, 8),
    (10, 6, 3),
    (1, 6, 2),
    (2, 7, 5),
    (3, 7, 1),
    (4, 8, 9),
    (5, 8, 3),
    (6, 9, 2),
    (7, 9, 4),
    (8, 10, 7),
    (9, 10, 5),
    (10, 11, 3),
    (1, 11, 2),
    (2, 12, 5),
    (3, 12, 1),
    (4, 13, 9),
    (5, 13, 3),
    (6, 14, 2),
    (7, 14, 4),
    (8, 15, 7),
    (9, 15, 5),
    (10, 16, 3),
    (1, 16, 2),
    (2, 17, 5),
    (3, 17, 1),
    (4, 18, 9),
    (5, 18, 3),
    (6, 19, 2),
    (7, 19, 4),
    (8, 20, 7),
    (9, 20, 5),
    (10, 21, 3),
    (1, 21, 2),
    (2, 22, 5),
    (3, 22, 1),
    (4, 23, 9),
    (5, 23, 3),
    (6, 24, 2),
    (7, 24, 4),
    (8, 25, 7),
    (9, 25, 5),
    (10, 26, 3),
    (1, 26, 2),
    (2, 27, 5),
    (3, 27, 1),
    (4, 28, 9),
    (5, 28, 3),
    (6, 29, 2),
    (7, 29, 4),
    (8, 30, 7),
    (9, 30, 5),
    (10, 31, 3),
    (1, 31, 2),
    (2, 32, 5),
    (3, 32, 1),
    (4, 33, 9),
    (5, 33, 3),
    (6, 34, 2),
    (7, 34, 4),
    (8, 35, 7),
    (9, 35, 5),
    (10, 36, 3),
    (1, 36, 2),
    (2, 37, 5),
    (3, 37, 1),
    (4, 38, 9),
    (5, 38, 3),
    (6, 39, 2),
    (7, 39, 4),
    (8, 40, 7),
    (9, 40, 5),
    (10, 41, 3),
    (1, 41, 2),
    (2, 42, 5),
    (3, 42, 1),
    (4, 43, 9),
    (5, 43, 3),
    (6, 44, 2),
    (7, 44, 4),
    (8, 45, 7),
    (9, 45, 5),
    (10, 46, 3),
    (1, 46, 2),
    (2, 47, 5),
    (3, 47, 1),
    (4, 48, 9);
------------------------------ INSERT good_order  ------------------------------

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

------------------------------ ALTER orders add column ------------------------------
ALTER TABLE orders add column status varchar(50) not null default 'COMPLETED';
ALTER TABLE orders add constraint tsk_order_status_check check (orders.status in ('ACTIVE','CANCELLED','COMPLETED'))
------------------------------ ALTER orders add column ------------------------------







