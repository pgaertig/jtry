# Java tryouts and experiments 

[![Maven build](https://github.com/pgaertig/jtry/actions/workflows/maven.yml/badge.svg)](https://github.com/pgaertig/jtry/actions/workflows/maven.yml)
This is my personal playground to learn old and new things with Java

Please look into subprojects for more details.

 - [jtry-ai](jtry-ai/README.md) - OpenAI and LLM usage
 - [jtry-spring](jtry-spring/README.md) - Spring Boot usage
 - [jtry-concurrency](jtry-concurrency/README.md) - Concurrency, parallelism, virtual threads usage

This playground is also itself a Multi-module Maven project example, with concepts like 
[Bill of Materials (BOM)](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html#bill-of-materials-bom-poms) and 
[Dependency Management](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html#dependency-management).

## How to build

```bash
  mvn clean install
```