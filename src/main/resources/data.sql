INSERT INTO workout_ofthe_day (created_at, is_deleted, updated_at, date, description, name, type) VALUES
('2022-01-01 00:00:00.000000', false, '2022-01-01 00:00:00.000000', '2023-04-08', 'description 1', 'WOD 1', 'type 1'),
('2022-01-02 00:00:00.000000', false, '2022-01-02 00:00:00.000000', '2023-04-07', 'description 2', 'WOD 2', 'type 2'),
('2022-01-03 00:00:00.000000', false, '2022-01-03 00:00:00.000000', '2023-04-06', 'description 3', 'WOD 3', 'type 3'),
('2022-01-03 00:00:00.000000', false, '2022-01-03 00:00:00.000000', '2023-04-02', 'description 4', 'WOD 4', 'type 4'),
('2022-01-03 00:00:00.000000', false, '2022-01-03 00:00:00.000000', '2023-04-03', 'description 5', 'WOD 5', 'type 5');
INSERT INTO user (created_at, is_deleted, updated_at, account_non_expired, account_non_locked, credentials_non_expired, email, enabled, gender, last_logined, password, role, username) VALUES
('2022-01-01 00:00:00.000000', false, '2022-01-01 00:00:00.000000', true, true, true, 'testuser1@example.com', true, 0, '2022-01-01 00:00:00.000000', 'password1', 0, 'testuser1'),
('2022-01-02 00:00:00.000000', false, '2022-01-02 00:00:00.000000', true, true, true, 'testuser2@example.com', true, 1, '2022-01-02 00:00:00.000000', 'password2', 2, 'testuser2'),
('2022-01-03 00:00:00.000000', false, '2022-01-03 00:00:00.000000', true, true, true, 'testuser3@example.com', true, 1, '2022-01-03 00:00:00.000000', 'password3', 0, 'testuser3');
INSERT INTO record (created_at, is_deleted, updated_at, date, note, user_id, wod_id, result) VALUES
('2022-01-01 00:00:00.000000', false, '2022-01-01 00:00:00.000000', '2022-01-01', 'note 1', 1, 1, '{time: 14:01}'),
('2022-01-02 00:00:00.000000', false, '2022-01-02 00:00:00.000000', '2022-01-02', 'note 2', 2, 2, '{time: 13:00}'),
('2022-01-03 00:00:00.000000', false, '2022-01-03 00:00:00.000000', '2022-01-03', 'note 3', 3, 3, '{reps: 213, time: 14:00}');
INSERT INTO movement_record (id, created_at, is_deleted, updated_at, cal, distance, height, reps, weight, name, record_id, wod_id, ord)
VALUES
(1, NOW(), 0, NOW(), 50.0, 0.0, 0.0, 10, 70.0, 'Push Press', 1, null, 1),
(2, NOW(), 0, NOW(), 0.0, 1000.0, 0.0, 0, 0.0, 'Row', 1, null, 2),
(3, NOW(), 0, NOW(), 20.0, 0.0, 0.0, 5, 45.0, 'Deadlift', 2, null, 1),
(4, NOW(), 0, NOW(), 30.0, 0.0, 0.0, 8, 65.0, 'Back Squat', 2, null, 2),
(5, NOW(), 0, NOW(), 0.0, 5000.0, 0.0, 0, 0.0, 'Run', 3, null, 1),
(6, NOW(), 0, NOW(), 15.0, 0.0, 0.0, 3, 35.0, 'Snatch', 3, null, 2),
(7, NOW(), 0, NOW(), 0.0, 1500.0, 0.0, 0, 0.0, 'Row', 2, null, 3),
(8, NOW(), 0, NOW(), 25.0, 0.0, 0.0, 6, 55.0, 'Power Clean', 2, null, 4),
(9, NOW(), 0, NOW(), 0.0, 8000.0, 0.0, 0, 0.0, 'Bike', 1, null, 3),
(10, NOW(), 0, NOW(), 40.0, 0.0, 0.0, 12, 85.0, 'Front Squat', 3, null, 3),
(11, NOW(), 0, NOW(), 40.0, 0.0, 0.0, 12, 85.0, 'Front Squat', null, 1, 1),
(12, NOW(), 0, NOW(), 25.0, 0.0, 0.0, 6, 55.0, 'Power Clean', null, 1, 2)
;
INSERT INTO movement (description, name) VALUES
('The burpee is a full-body exercise used in strength training and as an aerobic exercise.', 'Burpee'),
('The kettlebell swing is a ballistic exercise that targets the posterior chain.', 'Kettlebell Swing'),
('The squat is a compound, full-body exercise that works more than one muscle group.', 'Squat'),
('The pull-up is a compound exercise that works the muscles of the upper body.', 'Pull-up'),
('The deadlift is a weight training exercise that works the posterior chain.', 'Deadlift');
