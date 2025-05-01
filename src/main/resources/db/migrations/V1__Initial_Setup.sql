-- Create Users Table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Create Admins Table
CREATE TABLE IF NOT EXISTS admins (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    user_id INT NOT NULL UNIQUE,
    CONSTRAINT fk_admin_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Create Drivers Table
CREATE TABLE IF NOT EXISTS drivers (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    license_number VARCHAR(50) NOT NULL UNIQUE,
    vehicle_registration VARCHAR(50) NOT NULL,
    vehicle_type VARCHAR(50) NOT NULL,
    user_id INT NOT NULL UNIQUE,
    CONSTRAINT fk_driver_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Create Customers Table
CREATE TABLE IF NOT EXISTS customers (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    user_id INT NOT NULL UNIQUE,
    CONSTRAINT fk_customer_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Insert Admin User
INSERT INTO users (id, email, phone, password, role)
VALUES (1, 'admin@dummy.com', '1111111111', '$2a$10$cR1F1UJUYXz6iBjC5t/txuIMW20sDHRFFuaUW8TvKsmwJcJkO2zP2', 'ADMIN');

INSERT INTO admins (id, first_name, last_name, user_id)
VALUES (1, 'John', 'Doe', 1);

-- Insert Driver User
INSERT INTO users (id, email, phone, password, role)
VALUES (2, 'driver@dummy.com', '2222222222', '$2a$10$gjJpCwltXzg/6jLF.wsjzOpQzLpQ.gwz7hbhw0U2cMGe48S5zjlRm', 'DRIVER');

INSERT INTO drivers (id, first_name, last_name, license_number, vehicle_registration, vehicle_type, user_id)
VALUES (1, 'Jane', 'Smith', 'DUMMY123', 'REG456', 'TRUCK', 2);

-- Insert Customer User
INSERT INTO users (id, email, phone, password, role)
VALUES (3, 'customer@dummy.com', '3333333333', '$2a$10$JmN4DwGKfDcQXKVNz2E0XeNlwfBxODijFJrEBD7hV0MV/puuoT26y', 'CUSTOMER');

INSERT INTO customers (id, first_name, last_name, user_id)
VALUES (1, 'Alice', 'Johnson', 3);