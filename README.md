# Proyecto Base Implementando Clean Architecture

## Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyectos y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por �ltimo el inicio y configuraci�n de la aplicaci�n.

Lee el artículo [Clean Architecture � Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el m�dulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio mediante modelos y entidades del dominio.

## Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de aplicaci�n y reacciona a las invocaciones desde el m�dulo de entry points, orquestando los flujos hacia el modulo de entities.

## Infrastructure

### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no est�n arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
gen�ricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patr�n de dise�o [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.

## Application

Este módulo es el más externo de la arquitectura, es el encargado de ensamblar los distintos m�dulos, resolver las dependencias y crear los beans de los casos de use (UseCases) de forma autom�tica, inyectando en �stos instancias concretas de las dependencias declaradas. Adem�s inicia la aplicaci�n (es el �nico m�dulo del proyecto donde encontraremos la funci�n �public static void main(String[] args)�.

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**

# Endpoints

## POST /api/franchise

### Descripción

Endpoint que permite la creación de una nueva franquicia.

### Ejemplo de consumo (cURL):
``` 
curl --location 'http://localhost:8081/api/franchise' \
--header 'Content-Type: application/json' \
--data '{
    "name" : "Franqui2"
}'
```
### Response 200:
``` 
{
    "franchiseId": "2",
    "name": "Franqui"
}
```

### Response 422:
``` 
{
    "code": "E422-003",
    "message": "Franchise name must not be empty"
}
```

## POST /api/branch

### Descripción

Endpoint que permite la creación de una nueva sucursal.

### Ejemplo de consumo (cURL):
``` 
curl --location 'http://localhost:8081/api/branch' \
--header 'Content-Type: application/json' \
--data '{
    "name" : "branch1",
    "franchiseId" : 2
}'
```
### Response 200:
``` 
{
    "branchId": 4,
    "franchiseId": 2,
    "name": "branch22"
}
```

### Response 400:
``` 
{
    "code": "400",
    "message": "Bad Request Error"
}
```

### Response 422:
``` 
{
    "code": "E422-000",
    "message": "Franchise does not exist"
}
```

## POST /api/product

### Descripción

Endpoint que permite la creación de un nuevo producto .

### Ejemplo de consumo (cURL):
``` 
curl --location 'http://localhost:8081/api/product' \
--header 'Content-Type: application/json' \
--data '{
    "name" : "product121",
    "branchId" : 4,
    "stock": 1700
}'
```
### Response 200:
``` 
{
    "productId": 9,
    "branchId": 4,
    "name": "product121",
    "stock": 1700
}
```

### Response 400:
``` 
{
    "code": "400",
    "message": "Bad Request Error"
}
```

### Response 422:
``` 
{
    "code": "E422-001",
    "message": "Branch does not exist"
}
```

## DELETE /api/product

### Descripción

Endpoint que permite la eliminación de un nuevo producto .

### Ejemplo de consumo (cURL):
``` 
curl --location --request DELETE 'http://localhost:8081/api/product?id=1'
```

### Response 200:
``` 
{
    "code": "200",
    "message": "Product deleted successfully"
}
```

### Response 400:
``` 
{
    "code": "400",
    "message": "Bad Request Error"
}
```

### Response 422:
``` 
{
    "code": "E422-002",
    "message": "Product does not exist"
}
```

## PATCH /api/product/stock

### Descripción

Endpoint que permite el cambio del valor del stock de un nuevo producto .

### Ejemplo de consumo (cURL):
``` 
curl --location --request PATCH 'http://localhost:8081/api/product/stock?id=2&stock=30'
```

### Response 200:
``` 
{
    "productId": 3,
    "branchId": 1,
    "name": "product1",
    "stock": 30
}
```

### Response 400:
``` 
{
    "code": "400",
    "message": "Bad Request Error"
}
```

### Response 422:
``` 
{
    "code": "E422-002",
    "message": "Product does not exist"
}
```

## PATCH /api/franchise/name

### Descripción

Endpoint que permite el cambio del valor del nombre de una franquicia existente .

### Ejemplo de consumo (cURL):
``` 
curl --location --request PATCH 'http://localhost:8081/api/franchise/name?id=3&name=franc' 
```

### Response 200:
``` 
{
    "franchiseId": 3,
    "name": "franc"
}
```

### Response 400:
``` 
{
    "code": "400",
    "message": "Bad Request Error"
}
```

### Response 422:
``` 
{
    "code": "E422-000",
    "message": "Franchise does not exist"
}
```

## PATCH /api/branch/name

### Descripción

Endpoint que permite el cambio del valor del nombre de una sucursal existente .

### Ejemplo de consumo (cURL):
``` 
curl --location --request PATCH 'http://localhost:8081/api/branch/name?id=1&name=branch2' 
```

### Response 200:
``` 
{
    "branchId": 1,
    "franchiseId": 2,
    "name": "branch2"
}
```

### Response 400:
``` 
{
    "code": "400",
    "message": "Bad Request Error"
}
```

### Response 422:
``` 
{
    "code": "E422-001",
    "message": "Branch does not exist"
}
```

## PATCH /api/product/name

### Descripción

Endpoint que permite el cambio del valor del nombre de un producto existente .

### Ejemplo de consumo (cURL):
``` 
curl --location --request PATCH 'http://localhost:8081/api/product/name?id=2&name=product2' 
```

### Response 200:
``` 
{
    "branchId": 1,
    "productId": 2,
    "name": "product2"
}
```

### Response 400:
``` 
{
    "code": "400",
    "message": "Bad Request Error"
}
```

### Response 422:
``` 
{
    "code": "E422-002",
    "message": "Product does not exist"
}
```

## GET /api/franchise/branches/products/max-stock

### Descripción

Endpoint que permite retornar los productos con mayor valor de stock por sucursal para una franquicia específica

### Ejemplo de consumo (cURL):
``` 
curl --location 'http://localhost:8081/api/franchise/branches/products/max-stock?franchise_id=1' 
```

### Response 200:
``` 
{
    "franchiseId": 1,
    "name": "Franuqss",
    "branches": [
        {
            "branchId": 1,
            "name": "branch11",
            "maxStockProduct": {
                "productId": 2,
                "branchId": 1,
                "name": "product112",
                "stock": 200
            }
        },
        {
            "branchId": 2,
            "name": "branch12",
            "maxStockProduct": {
                "productId": 7,
                "branchId": 2,
                "name": "product121",
                "stock": 1300
            }
        }
    ]
}
```

### Response 400:
``` 
{
    "code": "400",
    "message": "Bad Request Error"
}
```

### Response 422:
``` 
{
    "code": "E422-000",
    "message": "Franchise does not exist"
}
```

# Ejecución local

## 1. Requisitos 

- Java JRE (Java 17 o superior).
- Gradle version 8.8 o superior.
- Docker (para ejecutar la versión dockerizada de postgres)

## 2. Configurar base de datos

### 2.1 Ejecutar la versión dockerizada de postgresql:

```
docker run --name postgresql-container -p 5432:5432 -e POSTGRES_PASSWORD=somePassword -d postgres
```

### 2.2 Ejecutar el script de creación de tablas

```
CREATE TABLE public.franchises (
franchise_id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
"name" varchar NULL
);

CREATE TABLE public.branches (
branch_id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
franchise_id int4 NOT NULL,
"name" varchar NULL
);

CREATE TABLE public.products (
product_id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
branch_id int4 NOT NULL,
"name" varchar NULL,
stock int4 NOT NULL DEFAULT 0
);
```
## 3. Configurar la variable de entorno local

Configurar SPRING_PROFILES_ACTIVE=localo agregar las propiedades de los parametros de bse de datos (application-local.yaml) en application.yaml

## 4. Ejecutar el proyecto de forma local
``` 
./gradlew clean && bootRun
```
