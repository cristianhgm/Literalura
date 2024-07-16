# Literalura

## Descripción

Literalura es una aplicación basada en Spring Boot que permite buscar y gestionar libros y autores a partir de una API externa. La aplicación incluye funcionalidades para buscar libros por título, listar libros registrados, listar autores registrados, buscar autores vivos en un determinado año y listar libros por idioma.

## Requisitos

- Java 17
- Maven 3.6.0+
- PostgreSQL

## Estructura del Proyecto

El proyecto está organizado en los siguientes paquetes:

- `com.literalura.principal`: Contiene la clase principal `Principal` que gestiona el menú y las opciones disponibles para el usuario.
- `com.literalura.dtos`: Contiene los DTOs (`Datos`, `DatosAutor`, `DatosLibros`) utilizados para mapear los datos obtenidos de la API.
- `com.literalura.models`: Contiene las entidades `Autor` y `Libro`.
- `com.literalura.repository`: Contiene los repositorios `AutorRepository` y `LibroRepository` para interactuar con la base de datos.
- `com.literalura.service`: Contiene los servicios `ConsumoAPI` y `ConvierteDatos` para consumir la API externa y convertir los datos a objetos de la aplicación.

## Instalación y Ejecución

1. **Clonar el repositorio**

    ```bash
    git clone <URL_DEL_REPOSITORIO>
    cd literalura
    ```

2. **Configurar la base de datos**

    Crear una base de datos PostgreSQL y actualizar las credenciales en el archivo `application.properties`:

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/nombre_de_tu_base_de_datos
    spring.datasource.username=tu_usuario
    spring.datasource.password=tu_contraseña
    spring.jpa.hibernate.ddl-auto=update
    ```

3. **Construir y ejecutar la aplicación**

    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

## Uso

Al ejecutar la aplicación, se mostrará un menú con las siguientes opciones:

1. **Buscar por título**: Permite buscar un libro por su título y mostrar información relevante del mismo.
2. **Listar libros registrados**: Muestra una lista de todos los libros registrados en la base de datos.
3. **Listar autores registrados**: Muestra una lista de todos los autores registrados en la base de datos.
4. **Listar autores vivos en un determinado año**: Permite buscar y listar los autores que estaban vivos en un año específico.
5. **Listar libros por idioma**: Permite filtrar y listar los libros registrados por idioma.
6. **Salir**: Finaliza la ejecución de la aplicación.

## Dependencias

El proyecto utiliza las siguientes dependencias:

- `spring-boot-starter-data-jpa`: Starter para usar Spring Data JPA con Hibernate.
- `spring-boot-starter-test`: Starter para pruebas unitarias y de integración usando Spring Boot.
- `jackson-databind`: Biblioteca para trabajar con JSON.
- `postgresql`: Controlador JDBC para PostgreSQL.
