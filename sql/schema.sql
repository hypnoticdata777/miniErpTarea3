-- Mini ERP - Carlos Sanchez Gonzalez - Tarea 3
-- Ejecutar este script antes de correr la aplicacion

CREATE DATABASE IF NOT EXISTS mini_erp;
USE mini_erp;

CREATE TABLE IF NOT EXISTS producto (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(100) NOT NULL,
    precio  DECIMAL(10, 2) NOT NULL,
    stock   INT NOT NULL
);
