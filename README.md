# 📚 Digital Bookstore - eBooks of Code

![Java](https://img.shields.io/badge/Java-21-007396?logo=java)
![Jakarta EE](https://img.shields.io/badge/JakartaEE-10-orange?logo=jakartaee)
![Faces](https://img.shields.io/badge/Faces-4.1-blue)
![OmniFaces](https://img.shields.io/badge/OmniFaces-4.6.2-blueviolet)
![Jakarta Persistence JPA](https://img.shields.io/badge/JPA-3.1-lightgrey)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?logo=postgresql)
![Wildfly](https://img.shields.io/badge/Wildfly-35-darkblue?logo=wildfly)
![Maven](https://img.shields.io/badge/Maven-Build+Tool-C71A36?logo=apachemaven)
![Docker Compose](https://img.shields.io/badge/Docker-darkblue?logo=docker)
![S3](https://img.shields.io/badge/AWS%20S3-LocalStack-orange?logo=amazonaws)

---

## ✨ About the Project

This project is a simulation of a digital bookstore specialized in **programming eBooks**, built with the most modern technologies of the Java ecosystem.

The application is divided into two main areas:

- **🏬 Public Storefront:** where users can view available eBooks.
- **🛠 Back Office (Administration):** where administrators can manage:
    - Authors
    - Books
    - Categories
    - Sales
    - Users
    - Reports

## 🔧 Technologies Used

- **Java 21**
- **Jakarta EE 10**
- **Jakarta Faces (JSF) 4.1**
- **JavaScript (Vanilla)**: Scripts consolidados para controle de overlay, alertas e navegação.
- **CSS3**: Uso de variáveis (Custom Properties) e estilos unificados.
- **OmniFaces**
- **JPA**
- **PostgreSQL**
- **Wildfly 35**
- **Amazon S3 (via LocalStack)**
- **Maven**
- **Docker Compose**

## 🐳 Development Environment

To facilitate local execution, we use `docker-compose` to spin up the following services:

- **PostgreSQL** (Database)
- **LocalStack** (AWS S3 simulation)

### ▶️ How to run

1. **Clone the repository**
   ```bash
   git clone https://github.com/VictorHSP/jsf-ebookofcode.git
   cd jsf-ebookofcode
   ```
2. **Start services with Docker Compose**
   ```bash
   cd docker/
   docker-compose up -d
   ```

3. **Database Management (Liquibase)**
   This project uses Liquibase for database schema versioning.
   
   To apply database changes (ensure PostgreSQL is running via Docker):
   ```bash
   mvn liquibase:update
   ```

4. **Create S3 Bucket in LocalStack**
   ```bash
   cd localstack/
   /bin/bash init.sh
   ```

5. **Configure local WildFly in a servers folder**
   ```bash
   mkdir wildfly_35/
   cd wildfly_35/
   
   echo Downloading wildfly 35.0.1.Final...
   wget -q -O wildfly-35.0.1.Final.tar.gz https://github.com/wildfly/wildfly/releases/download/35.0.1.Final/wildfly-35.0.1.Final.tar.gz
   
   echo unziping...
   tar xf wildfly-35.0.1.Final.tar.gz
   
   echo removing tar.gz...
   rm wildfly-35.0.1.Final.tar.gz
   
   echo Getting standalone.xml...
   git clone https://github.com/VictorHSP/jsf-ebookofcode.git
   cd jsf-ebookofcode/
   cd ..
   cp -r jsf-ebookofcode/standalone/standalone.xml wildfly-35.0.1.Final/standalone/configuration
   rm -rf jsf-ebookofcode/
   
   echo adding postgresql module...
   cd wildfly-35.0.1.Final/modules/system/layers/base/
   mkdir -p org/postgresql/main
   touch org/postgresql/main/module.xml
   echo '<?xml version="1.0" ?><module xmlns="urn:jboss:module:1.9" name="org.postgresql"><resources><resource-root path="postgresql-42.7.5.jar"/></resources><dependencies><module name="javax.api"/><module name="javax.transaction.api"/></dependencies></module>' >> org/postgresql/main/module.xml
   wget -q -O org/postgresql/main/postgresql-42.7.5.jar https://jdbc.postgresql.org/download/postgresql-42.7.5.jar
   echo Finished!!
   ```

6. **Disabling JASPI Security Domain**

    1. Required for `@CustomFormAuthenticationMechanismDefinition` to work. This way
    the application recognizes the defined roles `ADMIN` and `CUSTOMER` and successfully completes authentication and authorization.
   
    2. Creating a new admin user in WildFly:
    ```bash
    cd wildfly-35.0.1.Final/bin
    /bin/bash add-user.sh
    ```
     ![add-user-example.png](doc/add-user-example.png)   

    3. After creating the user, start the server and access the URL `http://localhost:9990/console/index.html`.
    ```bash
    cd wildfly-35.0.1.Final/bin
    /bin/bash standalone.sh
    ```
   Log in with your username and password configured in the previous step. Go to `configuration -> Subsystems -> Web -> Application Security Domain -> Other (View)` Click `Edit`.
   
   4. Disable the `Integrated JASPI` option
    ![integrated-jaspi-off.png](doc/integrated-jaspi-off.png)
    

## 🛠 Useful Commands

### Liquibase
Liquibase is used to manage database migrations.
- **Update database:** `mvn liquibase:update`
- **Check status:** `mvn liquibase:status`

### Docker
- **Start services:** `docker-compose up -d`
- **Stop services:** `docker-compose down`

### LocalStack (S3)
- **Initialize bucket:** `./localstack/init.sh`

## 📁 Folder Structure
```
jsf-ebookofcode/
│
├── src/
│   ├── main/
│   │   ├── java/         # Source code (JPA, Beans, Services)
│   │   ├── resources/    # Configuration files
│   │   └── webapp/       # JSF Pages
│
├── docker/
│   ├── docker-compose.yml # Docker compose with PostgreSQL and LocalStack services
├── localstack/
│   ├── init.sh            # Local S3 bucket creation
├── standalone/
│   ├── standalone.xml     # Standalone configured with datasource pointing to local DB
│
├── pom.xml
└── README.md
```
## ☁️ S3 Integration
The simulation of file uploads and downloads (such as book covers or PDFs) 
is done via **LocalStack**, which emulates AWS S3 locally.

<hr>

Developed with 💻 by @victorhsp
