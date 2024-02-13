# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
## Chess Server Design 
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkwBzKBACu2GAGI0wKgE8YAJRRakEsFEFIIaYwHcAFkjAdEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAej0VKAAdNABvLMpTAFsUABoYXCl3aBlKlBLgJAQAX0wKcNgQsLZxKKhbe18oAAoiqFKKquUJWqh6mEbmhABKDtZ2GB6BIVFxKSitFDAAVWzx7Kn13ZExSQlt0PUosgBRABk3uCSYCamYAAzXQlP7ZTC3fYPbY9Tp9FBRNB6BAIDbULY7eRQw4wECDQQoc6US7FYBlSrVOZ1G5Y+5SJ5qBRRACSADl3lZfv8ydNKfNFssWjA2Ul4mDaHCMaFIXSJFE8SgCcI9GBPCTJjyaXtZQyXsL2W9OeKNeSYMAVZ4khAANbofWis0WiG0g6PQKwzb9R2qq22tBo+Ew0JwyLey029AByhB+DIdBgKIAJgADMm8vlzT6I2h2ugZJodPpDEZoLxjjAPhA7G4jF4fH440Fg6xQ3FEqkMiopC40Onuaa+XV2iHus30V6EFWkGh1VMKbN+etJeIGTLXUcTkSxv2UFq7q7dUzyJ9vlyrjygSDjTBndqDx7xwjjQGpTA1w95fjfMrVbPNbf92hHo9TZDkz1JU1M3DP17TFKCAOxN0H3hKIoN9SNl1Ud0xxoKBUItdD-RHGN-HjJNU3TNDs1zNB820XQDGMHQUDtSstH0Zha28XxMFIptelbPgTySN40nSLsJB7PIqL9KNR16dh8KzWTMNXF0PzDQiEJ1YCjyEr4RM07Mb3felsIUr0U2TF8V3MkdyOs4jzL4hMYCszA8wLBji0GGQK2GGAAHEeUeLj614xtmGQ1tAreDt0i0HlpII7M5IZTClOgjDPSw6V1JxGTI1Mt1niPd4vh+IyYOBCBQXg4qYwysMYBKCAZBQVFVMxO8NI+ALgrKCRRj3RDD1eE9KoAKkvWqYESsptPvEImvmyQbKw6K8LmkK0ucyKjh2zz6KLYxsD0KBsAQVQ4C-VQBtUMKeJc9KWy2ttkjE1bkuU9B01W1keWHV6XsfT9FV8UZVqq9BKn+-8urynqCpS2SGt08aKt+KHCrcGq6pR9BpDR5DFOhtxWvazqcrUpG5VxW77shnkAbKEadNKlkDSNOGyhgWDtoW4mcNJ1bmT4dbGteg6yjF3aehc6WUFlo7C0YoxzA6yd3BgAApCBpyCi8jAUBBQGtCKAiinDW1iU54q+0wCd7AoXLgCBJygWGeTFoGuhBlCYAAK31mdsadyp8ld93oC9mW+FaJdqe6wDkZ+-00Y549MYF1QcZm-G05M-KkOFr0cYl5PEMV2WM8ZDHTxzsX85zxagJJr1RfFhG32LsGCViU2NbGKOPdjpW+DZ+9M9Aw1fhHmPG74fnVqJ4vJdBtzUwrzaHLl0IFc36yVe8pinEsRBFVgYBsEuwhnFcDxuIbS3-cE4TRIydRdtLp8+A6k5iTqETo+GmKc6Zf1rnqfScU3jGkqDjMeN5W5mXbk+dyXUd6Hz3rGS2u9PJAA