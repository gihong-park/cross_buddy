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