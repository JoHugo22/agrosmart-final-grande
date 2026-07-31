# 🌾 Universidad de las Fuerzas Armadas ESPE

## Examen Final Práctico — Programación Avanzada

### Caso integrador: **AgroSmart** — Plataforma de Comercialización Agrícola

# AgroSmart — Plataforma de Comercialización Agrícola

## Información del estudiante

- **Nombre:** Jose Hugo Grande Velasquez
- **Cédula:** 1726353160
- **NRC:** 30405
- **Asignatura:** Programación Avanzada
- **Universidad:** Universidad de las Fuerzas Armadas ESPE

## Descripción

AgroSmart es una aplicación desarrollada con Spring Boot que permite
consultar productos agrícolas almacenados en PostgreSQL, publicar de forma
reactiva únicamente los productos comercializables y generar mensajes
publicitarios mediante LangChain4j.

El proyecto integra JPA/Hibernate, programación funcional, Project Reactor,
Spring WebFlux, LangChain4j, JUnit 5, Mockito y StepVerifier.

## Semilla personal

Los dos últimos dígitos de mi cédula son `60`.

| Parámetro | Valor |
|---|---|
| Cédula | 1726353160 |
| NN | 60 |
| Tabla PostgreSQL | `tbl_productos_base_60` |
| Puerto de la aplicación | `8160` |
| Último dígito | 0 |
| Categoría | Cacao |
| Base de datos | `agrosmart_db` |

El puerto comienza con `81` y termina con los mismos dígitos utilizados en
el nombre de la tabla.

## Tecnologías

- Java 21
- Spring Boot
- Spring WebFlux
- Project Reactor
- Spring Data JPA
- Hibernate
- PostgreSQL 17
- Docker Compose
- LangChain4j
- JUnit 5
- Mockito
- StepVerifier
- Maven

## Arquitectura

La aplicación separa la entidad de persistencia del modelo de dominio:

- `ProductoEntity`: entidad mutable utilizada por JPA/Hibernate.
- `Producto`: modelo de dominio inmutable.
- `ProductoRepository`: repositorio bloqueante de Spring Data JPA.
- `ProductoService`: flujo reactivo y aislamiento de operaciones bloqueantes.
- `AgroSmartAIService`: contrato declarativo de LangChain4j.
- `AgroSmartController`: API reactiva mediante Mono y Flux.

## Requisitos

- Java 21
- Docker Desktop
- Git
- Puerto `8160` disponible
- Puerto `5433` disponible para PostgreSQL

## Ejecución

### 1. Iniciar PostgreSQL

```powershell
docker compose up -d
