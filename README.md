
# Concurrent Web Server with IoC Framework and REST Services
This project extends an existing web server into a full-featured framework, similar to Apache, but focused on Java.
It now uses annotations to define REST services, instead of lambda functions.

* The server is capable of:

* Delivering HTML pages, CSS, JS, and PNG images.

* Providing an Inversion of Control (IoC) framework for building web applications from POJOs.

* Simplifying the development of modern and modular applications.

* Handling requests concurrently.

* Being deployed on AWS and Docker for scalability.

## Getting Started

The following instructions will allow you to run the project locally on your machine.

### Prerequisites

You need to have the following installed:

1. **Java 17** (recommended)  
   Verify your version with:

   ```
   java -version
    ```
   Example output:

    ```
    openjdk version "17.0.16" 2025-07-15 LTS
    OpenJDK Runtime Environment Microsoft-11926163 (build 17.0.16+8-LTS)
    OpenJDK 64-Bit Server VM Microsoft-11926163 (build 17.0.16+8-LTS, mixed mode, sharing)
    ```

2. **Maven 3.9.x**

   Download from Maven Official Site.
   Verify installation with:
   ```
   mvn -v
   ```

   Example output:
   ```
   Apache Maven 3.9.11 (3e54c93a704957b63ee3494413a2b544fd3d825b)
   Maven home: /usr/local/apache-maven
   Java version: 21.0.2, vendor: Oracle Corporation
   ```

3. **Git**

   To clone the repository. Verify with:
   ```
   git --version
   ```

### Installation

1. Clone the repository and navigate to the folder containing the `pom.xml` file using the following commands:

   ```sh
   git clone https://github.com/AndresSu2342/AREP_MODULARIZACION_VIRTUALIZACION_DOCKER.git
   cd AREP_MODULARIZACION_VIRTUALIZACION_DOCKER
   ```

2. Build the project:

   ```sh
   mvn clean package
   ```

   The console output should look something like this:

   ```sh
   [INFO] Building jar: C:\Users\jdavi\IdeaProjects\AREP_MODULARIZACION_VIRTUALIZACION_DOCKER\target\MicroSpringBoot-1.0-SNAPSHOT.jar
   [INFO]
   [INFO] --- dependency:3.6.1:copy-dependencies (copy-dependencies) @ MicroSpringBoot ---
   [INFO] ------------------------------------------------------------------------
   [INFO] BUILD SUCCESS
   [INFO] ------------------------------------------------------------------------
   [INFO] Total time:  17.387 s
   [INFO] Finished at: 2025-09-15T23:23:39-05:00
   [INFO] ------------------------------------------------------------------------
   ```

3. Run the application by loading the annotated POJO from the command line:

      ```sh
      java -cp target/classes co.edu.escuelaing.httpserver.HttpServer co.edu.escuelaing.controller.GreetingController
      ```
   The console should display the following message:
      ```sh
      HTTP server started on port 35000
      ```
   You can now access static resources like `index.html` or other resources stored in the `resources/static` folder.

4. Open your browser at:

    - http://localhost:35000/

   ![index.html](https://github.com/user-attachments/assets/2ad2a191-8732-45a2-8388-b9ca5c002519)

    - http://localhost:35000/greeting?name=Pedro

   ![index.html](https://github.com/user-attachments/assets/d33517b8-e58e-44ff-afb5-a2ca2d7c3554)

5. 

## Architecture

![Architecture](https://github.com/user-attachments/assets/81544fec-48ec-4086-b291-ec0127dc62f0)

Flow of Interaction

1. A user sends an HTTP request (e.g., /greeting?name=Maria).

2. HttpServer receives it.

3. If static → returns file. 
If dynamic → routes to controller using IoC framework.

4. Controller executes and response is sent back.

## Server Directory Structure

![Server Directory](https://github.com/user-attachments/assets/967dead3-53b0-4a05-b725-975fe046aa4c)

## Core Components

## 1. **HttpServer**

### **Role:**
Manages the lifecycle of the HTTP server, including handling incoming requests, storing static and dynamic REST endpoints, and routing requests to the appropriate handler.

### **Responsibilities:**
- Start and stop the server, listening on a specified port.
- Accept incoming client connections and delegate request processing.
- Store and manage static file paths for serving resources.
- Register and store **GET** and **POST** request handlers using lambda functions.
- Route incoming requests to the correct lambda function based on the HTTP method and path.

---

## 2. **HttpResponse**

### **Role:**
Handles the construction and sending of HTTP responses, including status codes, headers, and body content.

### **Responsibilities:**
- Set and manage HTTP status codes and messages.
- Store and manage HTTP headers.
- Store the response body.
- Send the complete HTTP response to the client via a `PrintWriter`.

---

## 3. **HttpRequest**

### **Role:**
Processes and extracts query parameters from an HTTP request.

### **Responsibilities:**
- Parse query parameters from a URL-encoded string.
- Provide access to query parameters via the `getQueryParam` method.
- Decode query parameters to support special characters.

---

## 4. **Annotations**

### @RestController

Mark a class as a REST component that must be loaded when the framework starts.

### @GetMapping

Expose a Java method as a REST service at the specified path

### @RequestParam

Allows you to handle query parameters and default values.

## Class Diagram

![Class Diagram](https://github.com/user-attachments/assets/d86209ff-d0ae-4745-8320-85971f709633)

## Deployment with Docker

1. Build the Docker Image:

   ```sh
   docker build -t httpserver .
   ```

2. Run the Container

   ```sh
    docker run -d -p 35000:35000 --name httpservercontainer httpserver
   ```

3. Push to Docker Hub

    ```sh
    docker tag httpserver andressu2342/httpserver
    docker push andressu234/httpserver:latest   
    ```

## Deploy on AWS

1. On EC2 instance:

   ```sh
    sudo yum install docker
    sudo service docker start
    docker run -d -p 8080:6000 your-dockerhub-user/httpserver
   ```

2. Enable security group for port 45000 and access:

   ```sh
    http://ec2-54-159-4-151.compute-1.amazonaws.com:45000/
   ```

## Deployment Video

https://pruebacorreoescuelaingeduco-my.sharepoint.com/:v:/g/personal/cesar_borray-s_mail_escuelaing_edu_co/Eanl5LLgR0dEg_I6dlT_d38BD4ExeATFbr5yfG9raoC8gg?e=LHCH5J&nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJTdHJlYW1XZWJBcHAiLCJyZWZlcnJhbFZpZXciOiJTaGFyZURpYWxvZy1MaW5rIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXcifX0%3D

## Built With

- [Maven](https://maven.apache.org/index.html) - Dependency Management
- [Git](https://git-scm.com) - Version Control System
- [Docker](https://www.docker.com/) - Containerization and deployment
- [Amazon EC2](https://aws.amazon.com/ec2/) - Virtual server hosting
- [Amazon Web Services (AWS)](https://aws.amazon.com/) - Cloud infrastructure and services

## Author

Cesar Andres Borray Suarez - [AndresSu2342](https://github.com/AndresSu2342)

### Date

Date: 15/09/2025
