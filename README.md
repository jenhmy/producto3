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

## 🚀 Instalación

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

## 🚀 Configuración de la base de datos

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

## Base de datos

La base de datos está alojada en **Aiven** (MySQL en la nube) y contiene las siguientes tablas:

- `articulos` → almacena los artículos de la tienda
- `clientes` → almacena los clientes (estándar y premium)
- `pedidos` → almacena los pedidos vinculados a clientes y artículos

Todas las operaciones DML se realizan a través de **procedimientos almacenados**
que gestionan las transacciones internamente con `START TRANSACTION`, `COMMIT`,
`ROLLBACK` y `RESIGNAL`.

## Patrones de diseño utilizados

- **MVC**: Separación entre vista, lógica y datos
- **DAO**: Separación de la persistencia del resto de la aplicación
- **Factory**: Instanciación de DAOs sin acoplar la implementación
- **Singleton**: Única instancia de conexión a la base de datos

## Nota sobre SSL

La conexión usa `sslMode=REQUIRED` que cifra la comunicación con el servidor
pero no verifica el certificado CA. Es suficiente para un entorno de desarrollo
y evita problemas de compatibilidad entre sistemas operativos.

Para un entorno de producción se recomienda usar `sslMode=VERIFY_CA`
con el certificado CA correspondiente.
