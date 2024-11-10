-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema stockearte
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema stockearte
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `stockearte` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `stockearte` ;

-- -----------------------------------------------------
-- Table `stockearte`.`orden_compra`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stockearte`.`orden_compra` (
  `codigo` BIGINT NOT NULL AUTO_INCREMENT,
  `estado` VARCHAR(45) NOT NULL,
  `observaciones` VARCHAR(45) NULL DEFAULT NULL,
  `orden_despacho` VARCHAR(45) NULL DEFAULT NULL,
  `fecha_solicitud` DATE NULL DEFAULT NULL,
  `fecha_recepcion` DATE NULL DEFAULT NULL,
  `tienda_id` INT NULL DEFAULT NULL,
  `pausada` TINYINT(1) NULL DEFAULT '0',
  PRIMARY KEY (`codigo`))
ENGINE = InnoDB
AUTO_INCREMENT = 29
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stockearte`.`orden_despacho`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stockearte`.`orden_despacho` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `id_orden_compra` BIGINT NOT NULL,
  `fecha_estimada_envio` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_orden_compra` (`id_orden_compra` ASC) VISIBLE,
  CONSTRAINT `orden_despacho_ibfk_1`
    FOREIGN KEY (`id_orden_compra`)
    REFERENCES `stockearte`.`orden_compra` (`codigo`))
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stockearte`.`producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stockearte`.`producto` (
  `codigo` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(255) NOT NULL,
  `talle` VARCHAR(10) NOT NULL,
  `foto` VARCHAR(255) NULL DEFAULT NULL,
  `color` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`codigo`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stockearte`.`item_despacho`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stockearte`.`item_despacho` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `producto_id` BIGINT NOT NULL,
  `cantidad` INT NOT NULL,
  `orden_despacho_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `orden_despacho_id` (`orden_despacho_id` ASC) VISIBLE,
  INDEX `item_despacho_ibfk_2` (`producto_id` ASC) VISIBLE,
  CONSTRAINT `item_despacho_ibfk_1`
    FOREIGN KEY (`orden_despacho_id`)
    REFERENCES `stockearte`.`orden_despacho` (`id`),
  CONSTRAINT `item_despacho_ibfk_2`
    FOREIGN KEY (`producto_id`)
    REFERENCES `stockearte`.`producto` (`codigo`))
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stockearte`.`item_orden_compra`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stockearte`.`item_orden_compra` (
  `codigo` BIGINT NOT NULL AUTO_INCREMENT,
  `color` VARCHAR(45) NULL DEFAULT NULL,
  `talle` VARCHAR(45) NULL DEFAULT NULL,
  `cantidad` INT NULL DEFAULT NULL,
  `producto_codigo` BIGINT NULL DEFAULT NULL,
  `orden_compra_codigo` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  INDEX `fk_item_orden_compra_producto_idx` (`producto_codigo` ASC) VISIBLE,
  INDEX `fk_item_orden_compra_orden_compra1_idx` (`orden_compra_codigo` ASC) VISIBLE,
  CONSTRAINT `fk_item_orden_compra_orden_compra1`
    FOREIGN KEY (`orden_compra_codigo`)
    REFERENCES `stockearte`.`orden_compra` (`codigo`),
  CONSTRAINT `fk_item_orden_compra_producto`
    FOREIGN KEY (`producto_codigo`)
    REFERENCES `stockearte`.`producto` (`codigo`))
ENGINE = InnoDB
AUTO_INCREMENT = 19
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stockearte`.`tienda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stockearte`.`tienda` (
  `codigo` BIGINT NOT NULL,
  `direccion` VARCHAR(255) NOT NULL,
  `ciudad` VARCHAR(100) NOT NULL,
  `provincia` VARCHAR(100) NOT NULL,
  `habilitada` TINYINT(1) NOT NULL,
  PRIMARY KEY (`codigo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stockearte`.`stock`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stockearte`.`stock` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tienda_id` BIGINT NOT NULL,
  `producto_codigo` BIGINT NOT NULL,
  `cantidad` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `tienda_id` (`tienda_id` ASC) VISIBLE,
  INDEX `producto_codigo` (`producto_codigo` ASC) VISIBLE,
  CONSTRAINT `stock_ibfk_1`
    FOREIGN KEY (`tienda_id`)
    REFERENCES `stockearte`.`tienda` (`codigo`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stockearte`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stockearte`.`usuario` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre_usuario` VARCHAR(50) NOT NULL,
  `contrasena` VARCHAR(255) NOT NULL,
  `tienda_id` BIGINT NULL DEFAULT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `apellido` VARCHAR(100) NOT NULL,
  `rol` VARCHAR(100) NOT NULL,
  `habilitado` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `nombre_usuario` (`nombre_usuario` ASC) VISIBLE,
  INDEX `tienda_id` (`tienda_id` ASC) VISIBLE,
  CONSTRAINT `usuario_ibfk_1`
    FOREIGN KEY (`tienda_id`)
    REFERENCES `stockearte`.`tienda` (`codigo`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
