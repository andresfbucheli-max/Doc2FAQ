# Doc2FAQ

Doc2FAQ is a Spring Boot web application that converts documents into
structured, automatically generated FAQs.

## Project Status

🚧 Early development — foundational Spring Boot project is in place and
core features are being actively built.

## Technology Stack

| Technology | Version |
|---|---|
| Java | 17+ |
| Spring Boot | 3.2.5 |
| Maven | 3.6+ |
| Thymeleaf | (via Spring Boot) |

## Getting Started

### Prerequisites

- [Java 17](https://adoptium.net/) or later installed
- [Maven 3.6+](https://maven.apache.org/download.cgi) installed  
  *(or use the included Maven Wrapper `./mvnw`)*

### Clone the repository

```bash
git clone <repository-url>
cd doc2faq
```

### Build the project

```bash
mvn clean package
```

### Run the application

**Option 1 – Maven Spring Boot plugin (recommended for development):**

```bash
mvn spring-boot:run
```

**Option 2 – Run the packaged JAR:**

```bash
java -jar target/doc2faq-0.0.1-SNAPSHOT.jar
```

Once started, open your browser and navigate to:

```
http://localhost:8080
```

### Run the tests

```bash
mvn test
```

## Planned Features

- Document ingestion and processing
- Automatic FAQ generation
- Web-based user interface
## Project Structure

```
src/
├── main/
│   ├── java/com/doc2faq/
│   │   ├── Doc2faqApplication.java       # Application entry point
│   │   └── controller/
│   │       └── HomeController.java       # Landing page controller
│   └── resources/
│       ├── templates/
│       │   └── index.html                # Landing page (Thymeleaf)
│       ├── static/css/
│       │   └── style.css                 # Application styles
│       └── application.properties        # Application configuration
└── test/
    └── java/com/doc2faq/
        └── Doc2faqApplicationTests.java  # Spring context load test
```
