FROM node:18-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --prod

FROM nginx:1.21-alpine
COPY --from=build /app/dist/insurance-web-app /usr/share/nginx/html
EXPOSE 4200
CMD ["nginx", "-g", "daemon off;"]
