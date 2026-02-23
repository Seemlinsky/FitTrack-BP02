CREATE DATABASE IF NOT EXISTS fittrack;
USE fittrack;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS workout_entry;
DROP TABLE IF EXISTS workout;
DROP TABLE IF EXISTS meal_entry;
DROP TABLE IF EXISTS activity_type;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

-- 1) USERS (avoid reserved word 'user')
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2) ACTIVITY TYPES (running, gym, cycling, ...)
CREATE TABLE activity_type (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- 3) WORKOUTS (1 session per day per user)
CREATE TABLE workout (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    workout_date DATE NOT NULL,
    note VARCHAR(255),
    CONSTRAINT fk_workout_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- 4) WORKOUT ENTRY (details per workout)
CREATE TABLE workout_entry (
    id INT AUTO_INCREMENT PRIMARY KEY,
    workout_id INT NOT NULL,
    activity_type_id INT NOT NULL,
    minutes INT NOT NULL,
    calories INT NOT NULL,
    CONSTRAINT fk_entry_workout
        FOREIGN KEY (workout_id) REFERENCES workout(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_entry_activity
        FOREIGN KEY (activity_type_id) REFERENCES activity_type(id)
        ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 5) MEAL ENTRY (meals per day per user)
CREATE TABLE meal_entry (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    meal_date DATE NOT NULL,
    meal_time TIME NOT NULL,
    meal_type VARCHAR(20) NOT NULL,
    meal_name VARCHAR(100) NOT NULL,
    calories INT NOT NULL,
    CONSTRAINT fk_meal_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- Indexes (performance / beoordeling)
CREATE INDEX idx_workout_user_date ON workout(user_id, workout_date);
CREATE INDEX idx_entry_workout ON workout_entry(workout_id);
CREATE INDEX idx_entry_activity ON workout_entry(activity_type_id);
CREATE INDEX idx_meal_user_date ON meal_entry(user_id, meal_date);

-- Seed data
INSERT IGNORE INTO activity_type(name) VALUES
('Running'), ('Gym'), ('Cycling'), ('Swimming'), ('Walking');
