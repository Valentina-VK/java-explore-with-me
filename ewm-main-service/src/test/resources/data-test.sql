INSERT INTO users (id, name, email)
VALUES (11, 'user1', 'user11@yandex.ru'),
       (12, 'user2', 'user12@yandex.ru'),
       (15, 'user5', 'user15@yandex.ru'),
       (13, 'user3', 'user13@yandex.ru');

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