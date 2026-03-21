# Sistema de Gestión de Tienda

Aplicación de consola desarrollada en Java que implementa un sistema de gestión
de tienda. El proyecto tiene como objetivo aprender y aplicar conceptos de 
programación orientada a objetos, persistencia de datos con MySQL y patrones 
de diseño como MVC, DAO y Factory.

## Requisitos previos

- Java 25 o superior
- IntelliJ IDEA
- MySQL Workbench
- Credenciales de acceso a la base de datos (solicitar al admin del proyecto)

## Instalación

1. Clona el repositorio:
```bash
   git clone https://github.com/jenhmy/producto3.git
```

2. Abre el proyecto en IntelliJ IDEA

3. Añade el driver MySQL al proyecto:
   - Ve a **File** → **Project Structure** → **Libraries**
   - Clic en **+** → **From Maven**
   - Escribe: `com.mysql:mysql-connector-j:8.0.33`
   - Clic en **OK** → **Apply** → **OK**
   - Revisa Project Structure para comprobar que se ha guardado correctamente.

## Configuración de la base de datos

1. Copia el archivo `.env.example` y renómbralo a `.env`
2. Rellena los valores con las credenciales que te facilitará el admin:
```
   DB_HOST=
   DB_PORT=
   DB_DATABASE=
   DB_USER=
   DB_PASSWORD=
```
3. Ya tienes acceso a la base de datos!

## Estructura del proyecto

El proyecto sigue el patrón MVC y está organizado en los siguientes paquetes:

- `controlador/`  Gestiona la lógica de negocio
- `modelo/`  Contiene las clases Articulo, Cliente y Pedido, además de las excepciones personalizadas
- `dao/`  Interfaces DAO y sus implementaciones para MySQL
- `factory/`  Factoría para instanciar los DAOs
- `util/`  Clase de conexión a la base de datos
- `vista/`  Interfaz de consola

## Patrones de diseño utilizados

- **MVC**: Separación entre vista, lógica y datos
- **DAO**: Separación de la persistencia del resto de la aplicación
- **Factory**: Instanciación de DAOs sin acoplar la implementación
- **Singleton**: Única instancia de conexión a la base de datos
