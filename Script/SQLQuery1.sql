create database TareaPracticaDB
use TareaPracticaDb


-- Crear tabla 'Cuentas"
CREATE TABLE Cuentas (
CuentaID INT PRIMARY KEY IDENTITY(1,1),
NumeroCuenta VARCHAR(20) NOT NULL UNIQUE,
Nombre VARCHAR(100) NOT NULL,
Tipo VARCHAR(50) NOT NULL,
Nivel INT NOT NULL,
Padre INT,
CONSTRAINT FK_Cuentas_Padre FOREIGN KEY (Padre) REFERENCES Cuentas(CuentaID)
);

-- Crear tabla 'Movimientos'
CREATE TABLE Movimientos (
MovimientoID INT PRIMARY KEY IDENTITY(1,1),
Fecha DATE NOT NULL,
CuentaID INT NOT NULL,
Descripcion VARCHAR(200) NOT NULL,
Debe DECIMAL(10,2) NOT NULL DEFAULT 0,
Haber DECIMAL(10,2) NOT NULL DEFAULT 0,
CONSTRAINT FK_Movimientos_Cuentas FOREIGN KEY (CuentaID) REFERENCES Cuentas(CuentaID)
);

use TareaPracticaDB
insert into Cuentas(NumeroCuenta,Nombre,Tipo,Nivel,Padre) values (2,'Cuenta de Banco','Pago',1,1);
INSERT INTO Cuentas(NumeroCuenta,Nombre,Tipo,Nivel) VALUES (3,'Cuenta de Banco','Pago',1);

USE TareaPractica;
UPDATE Cuentas SET Nombre = 'NCuenta de Bancos', Tipo = 'Pagos', Nivel = 4,  Padre = NULL WHERE NumeroCuenta = '2';

DELETE FROM Cuentas where Cuentas.CuentaID=2;

select * from Cuentas;

INSERT INTO Movimientos (Fecha, CuentaID, Descripcion, Debe, Haber)
VALUES ('2024-06-08', 1, 'Pago de servicios', 100.00, 0.00);

UPDATE Movimientos
SET Fecha = '2024-06-09', 
    CuentaID = 2, 
    Descripcion = 'Pago de alquiler',
    Debe = 500.00,
    Haber = 0.00
WHERE MovimientoID = 1;

DELETE FROM Movimientos
WHERE MovimientoID = 1;


select * from Movimientos;
