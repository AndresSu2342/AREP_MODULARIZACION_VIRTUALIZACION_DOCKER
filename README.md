
# Web Framework with IoC, Reflection and Annotations
This project implements a micro framework inspired by Spring Boot.  
It allows you to set up a lightweight web server with support for **static files** and **dynamic REST services** defined using **annotations** (`@RestController`, `@GetMapping`, `@RequestParam`).

The objective of the lab is to understand the use of **reflection in Java** to discover components and expose REST services without having to register them manually.

## Getting Started

The following instructions will allow you to run the project locally on your machine.

### Prerequisites

You need to have the following installed:

1. **Java 21** (recommended)  
   Verify your version with:

   ```
   java -version
    ```
   Example output:

    ```
    java version "21.0.2" 2024-01-16 LTS
    Java(TM) SE Runtime Environment (build 21.0.2+13-LTS-58)
    Java HotSpot(TM) 64-Bit Server VM (build 21.0.2+13-LTS-58, mixed mode, sharing)
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
   git clone https://github.com/AndresSu2342/AREP_SERVIDORES_METAPROTOCOLS_PATRON_IOC_REFLEXION.git
   cd AREP_SERVIDORES_METAPROTOCOLS_PATRON_IOC_REFLEXION
   ```

2. Build the project:

   ```sh
   mvn clean package
   ```

   The console output should look something like this:

   ```sh
   [INFO] Building jar: C:\Users\jdavi\IdeaProjects\AREP_SERVIDORES_METAPROTOCOLS_PATRON_IOC_REFLEXION\target\MicroSpringBoot-1.0-SNAPSHOT.jar
   [INFO] BUILD SUCCESS
   [INFO] ------------------------------------------------------------------------
   [INFO] Total time:  3.098 s
   [INFO] Finished at: 2025-09-01T19:47:54-05:00
   [INFO] ------------------------------------------------------------------------
   ```

3. Run the application by loading the annotated POJO from the command line:

      ```sh
      java -cp target/classes co.edu.escuelaing.microspringboot.MicroSpringBoot co.edu.escuelaing.microspringboot.GreetingController
      ```
   The console should display the following message:
      ```sh
      Ready to receive ...
      ```
   You can now access static resources like `index.html` or other resources stored in the `resources/static` folder.

4. Open your browser at:

    - http://localhost:35000/greeting 

    ![index.html](https://github.com/user-attachments/assets/63311f67-3db3-4907-a1b2-f591d66b7613)

    - http://localhost:35000/greeting?name=Pedro

    ![index.html](https://github.com/user-attachments/assets/d33517b8-e58e-44ff-afb5-a2ca2d7c3554)

5. 

## Architecture

![Architecture](https://github.com/user-attachments/assets/084fa004-77f2-4b05-92ff-72896095ac05)

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

## Built With

[Maven](https://maven.apache.org/index.html) - Dependency Management

[Git](https://git-scm.com) - Version Control System

## Author

Cesar Andres Borray Suarez - [AndresSu2342](https://github.com/AndresSu2342)

### Date

Date: 01/09/2025
