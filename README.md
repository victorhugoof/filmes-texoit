## Requisitos

- Java 17

## Executar os tests

- Bash
    ```shell
    ./mvnw test
    ```
- Cmd
    ```shell
    ./mvnw.cmd test
    ```

## Executar a aplicação

- Bash
    ```shell
    ./mvnw spring-boot:run
    ```
- Cmd
    ```shell
    ./mvnw.cmd spring-boot:run
    ```

## Iniciar a aplicação com CSV de filmes personalizado

- Bash
    ```shell
    ./mvnw spring-boot:run -Dspring-boot.run.arguments="--filmes-texoit.movielist-csv-file={{FILE_PATH}}"
    ```
- Cmd
    ```shell
    ./mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--filmes-texoit.movielist-csv-file={{FILE_PATH}}"
    ```
