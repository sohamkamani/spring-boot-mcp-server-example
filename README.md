# Java MCP Server Example

This repository contains an example of a Java server implementing the Model Context Protocol (MCP). It includes a basic shopping list example.

## Project Structure

- `src/main/java`: Contains the main Java source code.
- `src/main/resources`: Contains application resources like properties files.
- `model-context-protocol/weather/starter-stdio-server`: Seems to be related to a weather MCP server example.

## Getting Started

Open your terminal in the project's root directory and run:

```bash
./mvnw spring-boot:run
```
(Or `mvn spring-boot:run` if you don't have the Maven wrapper, or use your IDE's run command).

You should see some Spring Boot startup logs (which will be minimal on the console due to our `application.properties` settings). If there are no major errors, your MCP server is running and waiting for an MCP client to connect via STDIO.
