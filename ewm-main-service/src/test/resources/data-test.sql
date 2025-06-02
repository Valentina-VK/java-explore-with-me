INSERT INTO users (id, name, email, subscription_permission)
VALUES (11, 'user1', 'user1@yandex.ru', true),
       (12, 'user2', 'user2@yandex.ru', false),
       (13, 'user3', 'user3@yandex.ru', false),
       (14, 'user4', 'user4@yandex.ru', false),
       (15, 'user5', 'user5@yandex.ru', true),
       (16, 'user6', 'user6@yandex.ru', false),
       (17, 'user7', 'user7@yandex.ru', true),
       (18, 'user8', 'user8@yandex.ru', false),
       (19, 'user9', 'user9@yandex.ru', true),
       (20, 'user10', 'user10@yandex.ru', true);

INSERT INTO categories (id, name)
       VALUES (11, 'Концерты'),
              (12, 'Кино'),
              (15, 'Театр'),
              (13, 'Выставки');

INSERT INTO locations (id, lat, lon)
       VALUES (17, 10.0, 12.0);

INSERT INTO events (id, annotation, title, description, initiator_id, category_id, location_id, event_date,
paid, request_moderation, participant_limit, state, created_on, published_on, views, confirmed_requests)
       VALUES (21, 'annotation', 'title', 'description', 11, 12, 17, TIMESTAMP '2026-06-25 15:15:15',
       true, true, 10,'PUBLISHED',  TIMESTAMP '2025-01-25 15:15:15',  TIMESTAMP '2025-02-25 15:15:15', 0, 0),
              (22, 'annotation22', 'title22', 'description22', 12, 13, 17, TIMESTAMP '2026-08-11 15:15:15',
              true, false, 10,'PENDING',  TIMESTAMP '2025-03-25 15:15:15',  TIMESTAMP '2025-03-25 17:15:15', 0, 0);

INSERT INTO requests (id, created, event_id, requester_id, status)
       VALUES (31, TIMESTAMP '2025-05-25 15:15:15', 21, 15,'PENDING');

INSERT INTO compilations (id, pinned, title)
       VALUES (41, false, 'Test Compilation');

INSERT INTO compilations_events (event_id, compilation_id)
       VALUES (21, 41), (22, 41);

INSERT INTO subscriptions (initiator_id, user_id)
       VALUES (11, 12), (11, 13), (15,12), (11, 14), (11, 15), (15,16),
       (11, 16), (11, 17), (15,17), (11, 18), (11, 19), (19,13);