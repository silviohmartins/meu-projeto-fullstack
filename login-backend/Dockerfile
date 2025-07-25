# --- Estágio 1: Build ---
# Usamos uma imagem oficial do Maven com Java 17 para compilar nosso projeto.
FROM maven:3.8.5-openjdk-17 AS build

# Define o diretório de trabalho dentro do contêiner.
WORKDIR /app

# Copia apenas o pom.xml primeiro para aproveitar o cache do Docker.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o resto do código fonte.
COPY src ./src

# Executa o build do Maven para gerar o arquivo .jar.
# -DskipTests pula a execução dos testes durante o build do Docker.
RUN mvn package -DskipTests

# --- Estágio 2: Run ---
# Usamos uma imagem leve, apenas com o Java Runtime, para rodar a aplicação.
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copia o arquivo .jar gerado no estágio de build para o nosso contêiner final.
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080 para que possamos acessá-la de fora do contêiner.
EXPOSE 8080

# Comando que será executado quando o contêiner iniciar.
ENTRYPOINT ["java", "-jar", "app.jar"]