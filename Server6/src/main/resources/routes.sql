-- Удаление таблиц в правильном порядке (с учетом зависимостей)
DROP TABLE IF EXISTS routes CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS coordinates CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
        id SERIAL PRIMARY KEY,
        username TEXT NOT NULL,
        password TEXT NOT NULL
);

-- Создание таблицы для координат (соответствует классу Coordinates)
CREATE TABLE coordinates (
                             id SERIAL PRIMARY KEY,
                             x DOUBLE PRECISION NOT NULL,  -- соответствует double x в Coordinates
                             y REAL NOT NULL CHECK (y > -334)  -- соответствует float y с проверкой
);

-- Создание таблицы для локаций (соответствует классу Location)
CREATE TABLE locations (
                           id SERIAL PRIMARY KEY,
                           x INTEGER NOT NULL,  -- соответствует int x в Location
                           y REAL NOT NULL,     -- соответствует Float y (NOT NULL как в конструкторе)
                           z INTEGER NOT NULL   -- соответствует int z
);

-- Создание таблицы для маршрутов (соответствует классу Route)
CREATE TABLE routes (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        coordinates_id INTEGER NOT NULL,
                        creation_date DATE NOT NULL DEFAULT CURRENT_DATE,
                        from_id INTEGER,
                        to_id INTEGER,
                        distance REAL CHECK (distance > 1 OR distance IS NULL),  -- соответствует Float distance
                        user_id INTEGER NOT NULL,
                        FOREIGN KEY (coordinates_id) REFERENCES coordinates(id) ON DELETE CASCADE,
                        FOREIGN KEY (from_id) REFERENCES locations(id) ON DELETE SET NULL,
                        FOREIGN KEY (to_id) REFERENCES locations(id) ON DELETE SET NULL,
                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

INSERT INTO users (username, password) VALUES
('sss', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3');

-- Создание координат (таблица coordinates)
INSERT INTO coordinates (x, y) VALUES 
(10.5, -300.0), -- id = 1
(20.75, -250.0), -- id = 2
(30.25, -200.0), -- id = 3
(40.5, -150.0), -- id = 4
(50.0, -100.0); -- id = 5

--  Создание локаций (таблица locations)
INSERT INTO locations (x, y, z) VALUES 
(100, 50.5, 200), -- id = 1
(200, 60.25, 300), -- id = 2
(300, 70.75, 400), -- id = 3
(400, 80.0, 500), -- id = 4
(500, 90.5, 600); -- id = 5

--  Создание маршрутов (таблица routes)
INSERT INTO routes (name, coordinates_id, creation_date, from_id, to_id, distance, user_id) VALUES 
('Route 1', 1, '2023-01-01', 1, 2, 100.5, 1),
('Route 2', 2, '2023-02-01', 2, 3, 150.25, 1),
('Route 3', 3, '2023-03-01', 3, 4, 200.75, 1),
('Route 4', 4, '2023-04-01', 4, 5, 250.0, 1),
('Route 5', 5, '2023-05-01', 5, NULL, NULL, 1);

-- Добавляем пользователя ddd (пароль '123' в хешированном виде SHA-256)
INSERT INTO users (username, password) VALUES
('ddd', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3');

-- Добавляем координаты для новых маршрутов пользователя ddd
INSERT INTO coordinates (x, y) VALUES
(15.3, -280.0),       -- id = 6
(25.8, -230.0),       -- id = 7
(35.1, -180.0),       -- id = 8
(45.6, -130.0),       -- id = 9
(55.9, -80.0);        -- id = 10

-- Добавляем локации для новых маршрутов пользователя ddd
INSERT INTO locations (x, y, z) VALUES
(150, 55.5, 250),     -- id = 6
(250, 65.25, 350),    -- id = 7
(350, 75.75, 450),    -- id = 8
(450, 85.0, 550),     -- id = 9
(550, 95.5, 650);     -- id = 10

-- Добавляем маршруты для пользователя ddd (user_id = 2)
INSERT INTO routes (name, coordinates_id, creation_date, from_id, to_id, distance, user_id) VALUES
('ddd Route A', 6, '2023-06-01', 6, 7, 110.5, 2),
('ddd Route B', 7, '2023-07-01', 7, 8, 160.25, 2),
('ddd Route C', 8, '2023-08-01', 8, 9, 210.75, 2),
('ddd Route D', 9, '2023-09-01', 9, 10, 260.0, 2),
('ddd Route E', 10, '2023-10-01', 10, NULL, NULL, 2);