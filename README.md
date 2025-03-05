# Projeto de Desenvolvimento de API com Spring Boot 3.2.0 e Java 21

Este é um projeto desenvolvido durante o curso de Desenvolvimento de API's.
O curso aborda os fundamentos e práticas essenciais para criar API's RESTful utilizando o Spring Boot 3.2.0 e a linguagem Java 21.

## API de Gerenciamento de Pessoas e Livros

A API de Gerenciamento de Pessoas e Livros é uma solução robusta e flexível para manipular
informações de pessoas e livros em um banco de dados. Desenvolvida utilizando tecnologias
modernas como Spring Boot, Java e RESTful, ela oferece uma variedade de endpoints para
realizar operações como buscar, criar, atualizar e excluir registros de pessoas e livros de forma
eficiente e segura.

Além disso, a API conta com um sistema de autenticação de usuários para garantir a segurança
dos dados manipulados. Com endpoints dedicados para cada funcionalidade, é fácil integrar esta
API em diferentes sistemas e aplicações, proporcionando uma experiência de desenvolvimento 
simplificada e escalável.

## Habilidades exercitadas

- Desenvolvimento de API's RESTful do zero absoluto
- Boas práticas na criação de API's
- Conceitos teóricos fundamentais do REST
- Utilização do Postman para testar requisições RESTful
- Modelagem de API's com base no modelo de maturidade RESTful
- Implementação dos principais verbos do REST (GET, POST, PUT, DELETE)
- Manipulação de parâmetros via query, path, header e body
- Paginação de API's
- Content Negotiation e Media Types
- Upload e download de arquivos
- Utilização prática do padrão HATEOAS
- Documentação de API com Swagger (Open API)
- Conceitos básicos de SQL e migrações com Flyway
- Autenticação REST e segurança com JWT
- Testes unitários e de integração com JUnit 5, Mockito, REST Assured e Testcontainers
- Integração e Deploy Contínuos com o Github e Github Actions
- Consumo de API's de terceiros
- Conceitos básicos de Docker e Docker Compose

### Requisitos
Certifique-se de ter o Java, Maven e Docker instalados em sua máquina.

### Executando a Aplicação

<details>
<summary>Aplicação</summary>
<br>

1. Clone o projeto.
   ```bash
     git@github.com:Phyllipesa/rest-with-spring-boot-and-java-udm.git
   ```

2. Entre no diretório.
   ```bash
     cd rest-with-spring-boot-and-java-udm/application
   ```

3. Compile o projeto.
   ```bash
     mvn package -DskipTests
   ```

4. Volte para o diretório anterior.
   ```bash
     cd ..
   ```

5. Execute o comando.
   ```bash
     docker compose up -d --build
   ```

6. A aplicação será iniciada no endereço 'http://localhost:80'.


7. Acesse a documentação da API no swagger.
   ```bash
     http://localhost/swagger-ui/index.html
   ```

PS: Você pode utilizar as configurações de ENV e Collections fornecidas na pasta "docs" com o Postman ou outra ferramenta similar.
</details>

<details>
<summary>Testes</summary>
<br>

1. Abra o arquivo __application.yml__ do diretório a seguir:

   ```bash
     application/src/test/resources
   ```

2. Altere o conteúdo do arquivo application.yml que se encontra no diretório __/src/main/resources__ para:

     ```bash
       spring:
         profiles:
           active: test
     ```

3. Crie uma nova pasta em um diretório a sua escolha, ela servirá para armazenar os arquivos dos endpoints de upload e download.


4. Abra o arquivo __application-test.yml__ que está no diretório __application/src/test/resources__ e substitua o valor da váriavel __upload-dir__ pelo caminho absoluto do diretório de armazenamento criado.

   ```bash
     file:
       upload-dir: /exempo/de/caminho
   ```

5. Certifique-se de estar no diretório __application__ e use o comando

   ```bash
     mvn test
   ```
</details>

### Tecnologias

![YAML](https://img.shields.io/badge/yaml-%23ffffff.svg?style=for-the-badge&logo=yaml&logoColor=151515)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![JUnit5rest](https://img.shields.io/badge/JUnit5-25A162.svg?style=for-the-badge&logo=JUnit5&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
