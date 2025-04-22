

insert into staff (version, id, first_name, last_name, email, phone, hashed_password) values (1, '1', 'admin','admin2', 'admin@a.com', '020202', '$2a$10$jpLNVNeA7Ar/ZQ2DKbKCm.MuT2ESe.Qop96jipKMq7RaUgCoQedV.')
insert into staff_role (staff_id, role) values (1, 'ADMIN')

insert into staff (version, id, first_name, last_name, email, phone, hashed_password) values (1, '2', 'staff1', 'lastname', 'staff@a.com', '030303', '$2a$12$tRYLi./KO826YRMcsso4HOLME0MFYNtLY55J2yJpurBM6gQjFiuhG')
insert into staff_role (staff_id, role) values (2, 'STAFF')

insert into staff (version, id, first_name, last_name, email, phone, hashed_password) values (1, '3', 'staff2', 'lastname', 'staff2@a.com', '030303', '$2a$12$tRYLi./KO826YRMcsso4HOLME0MFYNtLY55J2yJpurBM6gQjFiuhG')
insert into staff_role (staff_id, role) values (3, 'STAFF')

insert into staff (version, id, first_name, last_name, email, phone, hashed_password) values (1, '4', 'staff3', 'lastname', 'staff3@a.com', '030303', '$2a$12$tRYLi./KO826YRMcsso4HOLME0MFYNtLY55J2yJpurBM6gQjFiuhG')
insert into staff_role (staff_id, role) values (4, 'STAFF')

insert into staff (version, id, first_name, last_name, email, phone, hashed_password) values (1, '5', 'staff4', 'lastname', 'staff4@a.com', '030303', '$2a$12$tRYLi./KO826YRMcsso4HOLME0MFYNtLY55J2yJpurBM6gQjFiuhG')
insert into staff_role (staff_id, role) values (5, 'STAFF')

insert into customer (version, id, name, phone, email, hashed_password) values (1, '6', 'Customer1', '040404', 'customer@a.com', '$2a$12$K28lI4OIrPK145tpbp2KoO9HX7YK9MJWCrm4DGacffTC6uwEL7ry.')
insert into customer_role (customer_id, role) values (6, 'USER')
insert into customer (version, id, name, phone, email, hashed_password) values (1, '7', 'Customer2', '040404', 'customer2@a.com', '$2a$12$K28lI4OIrPK145tpbp2KoO9HX7YK9MJWCrm4DGacffTC6uwEL7ry.')
insert into customer_role (customer_id, role) values (7, 'USER')
insert into customer (version, id, name, phone, email, hashed_password) values (1, '8', 'Customer3', '040404', 'customer3@a.com', '$2a$12$K28lI4OIrPK145tpbp2KoO9HX7YK9MJWCrm4DGacffTC6uwEL7ry.')
insert into customer_role (customer_id, role) values (8, 'USER')

insert into operation (version, id, name, price) values (1, '1', 'Moottoriöljyn vaihto', '34.99')
insert into operation (version, id, name, price) values (1, '2', 'Jarrupalan vaihto', '55.00')
insert into operation (version, id, name, price) values (1, '3', 'Ilmasuodattimen vaihto', '25.50')
insert into operation (version, id, name, price) values (1, '4', 'Renkään vaihto', '40.00')
insert into operation (version, id, name, price) values (1, '5', 'Diagnostiikka', '20.00')

INSERT INTO orders (version, id, customer_id, staff_id, start_date, end_date) VALUES (1, 1, 6, 2, '2024-01-02', '2024-01-03'),(1, 2, 7, 3, '2024-01-05', '2024-01-07'),(1, 3, 8, 4, '2024-01-08', '2024-01-08'),(1, 4, 6, 5, '2024-01-10', '2024-01-12'),(1, 5, 7, 2, '2024-01-13', '2024-01-14'),(1, 6, 8, 3, '2024-01-15', '2024-01-16'),(1, 7, 6, 4, '2024-01-18', '2024-01-19'),(1, 8, 7, 5, '2024-01-20', '2024-01-22'),(1, 9, 8, 2, '2024-01-23', '2024-01-24'),(1, 10, 6, 3, '2024-01-25', '2024-01-26'),(1, 11, 7, 4, '2024-01-27', '2024-01-29'),(1, 12, 8, 5, '2024-01-30', '2024-02-01'),(1, 13, 6, 2, '2024-02-02', '2024-02-03'),(1, 14, 7, 3, '2024-02-04', '2024-02-06'),(1, 15, 8, 4, '2024-02-07', '2024-02-08'),(1, 16, 6, 5, '2024-02-09', '2024-02-11'),(1, 17, 7, 2, '2024-02-12', '2024-02-13'),(1, 18, 8, 3, '2024-02-14', '2024-02-16'),(1, 19, 6, 4, '2024-02-17', '2024-02-18'),(1, 20, 7, 5, '2024-02-19', '2024-02-20'),(1, 21, 8, 2, '2024-02-21', '2024-02-22'),(1, 22, 6, 3, '2024-02-23', '2024-02-24'),(1, 23, 7, 4, '2024-02-25', '2024-02-27'),(1, 24, 8, 5, '2024-02-28', '2024-03-01'),(1, 25, 6, 2, '2024-03-02', '2024-03-03'),(1, 26, 7, 3, '2024-03-04', '2024-03-06'),(1, 27, 8, 4, '2024-03-07', '2024-03-08'),(1, 28, 6, 5, '2024-03-09', '2024-03-10'),(1, 29, 7, 2, '2024-03-11', '2024-03-12'),(1, 30, 8, 3, '2024-03-13', '2024-03-15'),(1, 31, 6, 4, '2024-03-16', '2024-03-17'),(1, 32, 7, 5, '2024-03-18', '2024-03-19'),(1, 33, 8, 2, '2024-03-20', '2024-03-22'),(1, 34, 6, 3, '2024-03-23', '2024-03-24'),(1, 35, 7, 4, '2024-03-25', '2024-03-26'),(1, 36, 8, 5, '2024-03-27', '2024-03-29'),(1, 37, 6, 2, '2024-03-30', '2024-04-01'),(1, 38, 7, 3, '2024-04-02', '2024-04-03'),(1, 39, 8, 4, '2024-04-04', '2024-04-05'),(1, 40, 6, 5, '2024-04-06', '2024-04-07'),(1, 41, 7, 2, '2024-04-08', '2024-04-09'),(1, 42, 8, 3, '2024-04-10', '2024-04-12'),(1, 43, 6, 4, '2024-04-13', '2024-04-14'),(1, 44, 7, 5, '2024-04-15', '2024-04-16'),(1, 45, 8, 2, '2024-04-17', '2024-04-18'),(1, 46, 6, 3, '2024-04-19', '2024-04-20'),(1, 47, 7, 4, '2024-04-21', '2024-04-23'),(1, 48, 8, 5, '2024-04-24', '2024-04-25'),(1, 49, 6, 2, '2024-04-26', '2024-04-27'),(1, 50, 7, 3, '2024-04-28', '2024-04-29')

INSERT INTO order_operation (order_id, operation_id) VALUES (1, 1), (1, 3),(2, 2),(3, 5), (3, 4),(4, 1),(5, 2), (5, 3),(6, 4),(7, 1), (7, 5),(8, 2),(9, 3), (9, 4),(10, 5),(11, 1),(12, 2), (12, 3),(13, 4),(14, 5), (14, 1),(15, 2),(16, 3), (16, 4),(17, 5),(18, 1), (18, 2),(19, 3),(20, 4), (20, 5),(21, 1),(22, 2), (22, 3),(23, 4),(24, 5), (24, 1),(25, 2),(26, 3), (26, 4),(27, 5),(28, 1), (28, 2),(29, 3),(30, 4), (30, 5),(31, 1),(32, 2), (32, 3),(33, 4),(34, 5), (34, 1),(35, 2),(36, 3), (36, 4),(37, 5),(38, 1), (38, 2),(39, 3),(40, 4), (40, 5),(41, 1),(42, 2), (42, 3),(43, 4),(44, 5), (44, 1),(45, 2),(46, 3), (46, 4),(47, 5),(48, 1), (48, 2),(49, 3),(50, 4), (50, 5)


