# --- Estágio 1: Build ---
# Altere a versão do Node.js para 20
FROM node:20-alpine AS build

WORKDIR /app

# Copia package.json e package-lock.json e instala as dependências.
COPY package*.json ./
RUN npm install

# Copia o resto do código fonte.
COPY . .

# Executa o build de produção do Vue.
RUN npm run build

# --- Estágio 2: Serve ---
# ... O resto do arquivo não muda
FROM nginx:1.25-alpine

COPY --from=build /app/dist /usr/share/nginx/html

RUN rm /etc/nginx/conf.d/default.conf

COPY nginx.conf /etc/nginx/conf.d

EXPOSE 80

ENTRYPOINT ["nginx", "-g", "daemon off;"]