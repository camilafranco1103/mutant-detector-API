# MutantDetectorAPI

Este proyecto es una API para la detección de mutantes mediante el análisis de secuencias de ADN.
La API ofrece la capacidad de analizar si una secuencia de ADN pertenece a un mutante o a un humano, así como proporcionar estadísticas de las verificaciones realizadas.

## Descripción General

La API permite enviar secuencias de ADN en formato JSON y determinar si corresponden a un mutante. Si la secuencia contiene dos o más secuencias consecutivas de cuatro letras iguales en cualquier dirección (horizontal, vertical o diagonal), será identificada como mutante.

### Endpoints

- **POST /mutant**: Verifica si una secuencia de ADN corresponde a un mutante.
- **GET /mutant/stats**: Devuelve las estadísticas de las verificaciones de ADN realizadas.

## Acceso a la API

La API está desplegada en Render y se puede acceder mediante los siguientes endpoints:

### Base URL

https://mutant-detector-api.onrender.com

### POST /mutant

Verifica si una secuencia de ADN corresponde a un mutante. Recordemos que se debe de utilizar alguna aplicación como POSTMAN o curl.

#### Ejemplo de solicitud

``sh
POST https://mutant-detector-api.onrender.com/mutant
{
  "dna": [
    "ATGCGA",
    "CAGTGC",
    "TTATGT",
    "AGAAGG",
    "CCCCTA",
    "TCACTG"
  ]
}
Respuesta exitosa (mutante):
HTTP Status: 200 OK

{
  "message": "Es mutante"
}
Respuesta en caso de que no sea mutante:
HTTP Status: 403 Forbidden

{
  "message": "No es mutante"
}
GET /mutant/stats
Devuelve estadísticas de las verificaciones de ADN.

Ejemplo de solicitud
GET https://mutant-detector-api.onrender.com/mutant/stats
Respuesta exitosa:
{
  "count_mutant_dna": 40,
  "count_human_dna": 100,
  "ratio": 0.4
}

Estructura del Proyecto y Manejo de Datos
El proyecto utiliza Spring Boot para construir una API REST y emplea H2 como base de datos en disco para almacenar los resultados de las verificaciones de ADN. A continuación, se detalla cómo se procesan y manejan los datos en cada parte del proyecto:

a. Recepción y Validación de Datos (Controlador y DTO)
Cuando un cliente realiza una solicitud POST al endpoint /mutant con una secuencia de ADN, la secuencia es enviada en formato JSON. La clase MutantController es la encargada de recibir esta secuencia.

La secuencia de ADN se mapea a la clase DTO DnaRequest, que encapsula el arreglo de cadenas (secuencia de ADN) en un formato manejable por la API. El objeto DnaRequest actúa como un contenedor para los datos que son enviados a la API.

Ejemplo de secuencia recibida:

{
  "dna": [
    "ATGCGA",
    "CAGTGC",
    "TTATGT",
    "AGAAGG",
    "CCCCTA",
    "TCACTG"
  ]
}
b. Validaciones de la Secuencia de ADN
La validación de la secuencia se lleva a cabo en la clase de servicio MutantDetector. Este componente realiza las siguientes operaciones:

Validación del Tamaño de la Matriz: La secuencia de ADN debe ser de tamaño NxN. Esto se verifica antes de realizar cualquier otra operación. Si la matriz no cumple con esta condición, se lanza una excepción personalizada IllegalArgumentException que devuelve un error al cliente.
Validación de los Caracteres Permitidos: Se verifica que cada cadena en la matriz solo contenga los caracteres A, T, C y G. Si se encuentran caracteres inválidos, se lanza otra excepción.
c. Lógica de Verificación de Mutantes (Método isMutant)
La clase MutantDetector implementa la lógica principal para determinar si una secuencia corresponde a un mutante. Las reglas son las siguientes:

Se consideran mutantes aquellos ADN que contienen dos o más secuencias consecutivas de cuatro letras iguales en cualquier dirección (horizontal, vertical o diagonal).

El método isMutant() toma la matriz de caracteres generada y verifica secuencias de cuatro letras consecutivas en:

Horizontal: Recorre cada fila.
Vertical: Recorre cada columna.
Diagonal Principal: De la esquina superior izquierda a la esquina inferior derecha.
Diagonal Secundaria: De la esquina superior derecha a la esquina inferior izquierda.
Si se encuentran dos o más secuencias, se determina que es mutante. Este método devuelve true si se cumple la condición, y false en caso contrario.

d. Persistencia de Datos (Base de Datos H2)
Una vez validada la secuencia de ADN y determinado si es mutante o no, se almacena en la base de datos H2. La clase DnaRecord representa la entidad que se guarda en la base de datos y contiene:

ID: Un identificador único autogenerado.
DNA: La secuencia de ADN, almacenada como un String (la secuencia se convierte en una cadena larga).
isMutant: Un booleano que indica si la secuencia pertenece a un mutante (true) o no (false).
La persistencia se maneja a través de Spring Data JPA, y el repositorio DnaRecordRepository se utiliza para insertar los registros en la base de datos.

ID	DNA	isMutant
1	ATGCGA,CAGTGC,TTATGT,AGAAGG,CCCCTA,TCACTG	true
2	AATCGG,CGTCAA,TATGCT,GTCAAA,CGTCCA,TGTGAA	false
e. Estadísticas del ADN (/mutant/stats)
El endpoint /mutant/stats devuelve las estadísticas acumuladas de las verificaciones de ADN:

count_mutant_dna: El número total de secuencias de ADN que son mutantes.
count_human_dna: El número total de secuencias de ADN que no son mutantes (humanas).
ratio: La relación entre mutantes y humanos, calculada como count_mutant_dna / count_human_dna.
La clase de servicio MutantDetector utiliza el repositorio para contar cuántos mutantes y cuántos humanos hay en la base de datos, y luego calcula el ratio.

Ejemplo de Respuesta para /mutant/stats:

{
  "count_mutant_dna": 40,
  "count_human_dna": 100,
  "ratio": 0.4
}
Ejecución de Pruebas
Las pruebas se ejecutan localmente. Para clonar el repositorio y ejecutar los tests de manera local, sigue estos pasos:

Abre una terminal (o el terminal de tu IDE como IntelliJ).

Navega al directorio donde quieras clonar el proyecto, por ejemplo, si quieres clonarlo en tu carpeta Proyectos, puedes usar:


cd /ruta/a/tu/carpeta/Proyectos
Clona el repositorio con el siguiente comando:

git clone https://github.com/tu-usuario/MutantDetectorAPI.git
Esto creará una carpeta llamada MutantDetectorAPI dentro del directorio donde te encuentres.

Resumen:
Si tu carpeta de trabajo está en C:\Proyectos, puedes hacer:


cd C:\Proyectos
git clone https://github.com/tu-usuario/MutantDetectorAPI.git
Esto descargará el proyecto completo en la carpeta C:\Proyectos\MutantDetectorAPI.

Luego de clonar, puedes moverte al directorio del proyecto con:

cd MutantDetectorAPI
Desde ahí podrás ejecutar los comandos de Gradle, abrir el proyecto en un IDE, o realizar otras acciones.

Ejecuta los tests:

./gradlew test
Y así obtendrás una URL donde podrás ver los resultados:


file:///C:/Users/camil/mutant-detector/build/reports/tests/test/index.html
