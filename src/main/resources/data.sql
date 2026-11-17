-- Use TRUNCATE CASCADE to handle foreign key dependencies
-- This will also truncate dependent tables (trainers, trainings, etc.)
TRUNCATE TABLE training_types RESTART IDENTITY CASCADE;

INSERT INTO training_types (id, training_type_name) VALUES (1, 'Functional Training');
INSERT INTO training_types (id, training_type_name) VALUES (2, 'Mobility Training');
INSERT INTO training_types (id, training_type_name) VALUES (3, 'Strength Training');
INSERT INTO training_types (id, training_type_name) VALUES (4, 'Balance Training');
INSERT INTO training_types (id, training_type_name) VALUES (5, 'Stretching');
INSERT INTO training_types (id, training_type_name) VALUES (6, 'Agility Training');