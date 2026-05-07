CREATE DATABASE IF NOT EXISTS festival_pulse;
USE festival_pulse;

SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM crowd_alert;
DELETE FROM crowd_report;
DELETE FROM festival_area;

ALTER TABLE crowd_alert AUTO_INCREMENT = 1;
ALTER TABLE crowd_report AUTO_INCREMENT = 1;
ALTER TABLE festival_area AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO festival_area (id, name, description, area_type) VALUES
(1, 'Main Stage', 'Large outdoor stage near the north entrance', 'STAGE'),
(2, 'Food Village', 'Food trucks, seating area, and water refill point', 'FOOD'),
(3, 'Craft Beer Bar', 'Bar tent beside the acoustic stage', 'BAR'),
(4, 'First Aid Point', 'Medical support beside the information desk', 'SAFETY'),
(5, 'Acoustic Tent', 'Small covered live music tent', 'STAGE'),
(6, 'Merch Stand', 'Festival merchandise and artist stalls', 'RETAIL'),
(7, 'Family Zone', 'Quiet area with seating and children activities', 'FAMILY');

INSERT INTO crowd_report (id, area_id, crowd_level, note, submitted_at) VALUES
(1, 1, 'MEDIUM', 'Crowd building before headline act', '2026-05-07 10:00:00'),
(2, 2, 'LOW', 'Short queues at most food trucks', '2026-05-07 10:10:00'),
(3, 3, 'MEDIUM', 'Steady queue at the bar', '2026-05-07 10:20:00'),
(4, 1, 'FULL', 'Main Stage is at capacity near the front barrier', '2026-05-07 10:35:00'),
(5, 4, 'LOW', 'No queue and area is clear', '2026-05-07 10:45:00'),
(6, 5, 'FULL', 'Acoustic Tent is full before the next set', '2026-05-07 11:00:00'),
(7, 2, 'MEDIUM', 'Lunch rush starting at Food Village', '2026-05-07 11:15:00'),
(8, 6, 'LOW', 'Merch Stand is quiet', '2026-05-07 11:25:00'),
(9, 7, 'LOW', 'Family Zone is calm', '2026-05-07 11:35:00'),
(10, 3, 'FULL', 'Craft Beer Bar queue blocking the walkway', '2026-05-07 11:45:00'),
(11, 2, 'FULL', 'Food Village seating is full', '2026-05-07 12:00:00'),
(12, 1, 'MEDIUM', 'Main Stage crowd easing after set change', '2026-05-07 12:20:00');

INSERT INTO crowd_alert (id, area_id, message, status, created_at) VALUES
(1, 1, 'Area ''Main Stage'' is FULL', 'RESOLVED', '2026-05-07 10:35:30'),
(2, 5, 'Area ''Acoustic Tent'' is FULL', 'ACTIVE', '2026-05-07 11:00:30'),
(3, 3, 'Area ''Craft Beer Bar'' is FULL', 'ACTIVE', '2026-05-07 11:45:30'),
(4, 2, 'Area ''Food Village'' is FULL', 'ACTIVE', '2026-05-07 12:00:30');
