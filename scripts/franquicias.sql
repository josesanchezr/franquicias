-- Tabla para almacenar la información de franquicias
CREATE TABLE franquicia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL
);

-- Tabla para almacenar la información de sucursales
CREATE TABLE sucursal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    franquicia_id BIGINT,
    CONSTRAINT fk_franquicia FOREIGN KEY (franquicia_id) REFERENCES franquicia(id)
);

-- Tabla para almacenar la información de productos
CREATE TABLE producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    stock INT NOT NULL,
    sucursal_id BIGINT,
    CONSTRAINT fk_sucursal FOREIGN KEY (sucursal_id) REFERENCES sucursal(id)
);