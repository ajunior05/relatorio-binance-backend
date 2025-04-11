projeto:
  nome: Relatório Binance
  descricao: >
    Sistema completo para importar, visualizar e filtrar relatórios de transações e ordens da Binance a partir de arquivos CSV.
    Inclui autenticação com JWT, upload de arquivos, filtros por campo e integração frontend/backend.

tecnologias:
  backend:
    - Java 17
    - Spring Boot 3
    - Spring Security + JWT
    - Spring Data JPA
    - Swagger (OpenAPI)
    - Jakarta Validation
    - Multipart File Upload
    - H2 / MySQL / PostgreSQL
  frontend:
    - React 18
    - Bootstrap 5 + React Bootstrap
    - React Icons
    - Framer Motion
    - React Toastify
    - React Router DOM

estrutura:
  backend:
    pacotes:
      - controller
      - service
      - model
      - repository
      - configuration
      - security
  frontend:
    diretorios:
      - components
      - pages
      - services
      - App.jsx

funcionalidades:
  - Upload de arquivos CSV
  - Filtros por campos (OrderNo, Status, Coin, Operation, Data)
  - Autenticação JWT (Login, Cadastro)
  - Proteção de rotas no frontend
  - Documentação com Swagger
  - Paginação e animações no frontend

variaveis_ambiente:
  frontend:
    arquivo: .env
    conteudo:
      - REACT_APP_API_URL=http://localhost:8090/api
  backend:
    arquivo: application.properties
    conteudo:
      - spring.datasource.url=jdbc:h2:mem:binance
      - springdoc.swagger-ui.path=/swagger-ui.html

endpoints:
  autenticacao:
    - POST /auth/register: Cadastro de usuário
    - POST /api/usuarios/login: Login e retorno de token JWT
  transacoes:
    - POST /api/importarTransacao: Upload de transações CSV
    - GET /api/consultarTransacao:
        params: [coin, operation, data]
        descricao: Consulta com filtros
  ordens:
    - POST /api/upload: Upload de ordens CSV
    - GET /api/consultarOrdem:
        params: [orderNo, status, data]
        descricao: Consulta com filtros

swagger:
  url: http://localhost:8090/swagger-ui/index.html
  path: /swagger-ui.html

execucao_local:
  backend:
    comandos:
      - git clone https://github.com/seu-usuario/relatorio-binance-backend.git
      - cd relatorio-binance-backend
      - ./mvnw spring-boot:run
    url: http://localhost:8090
  frontend:
    comandos:
      - git clone https://github.com/seu-usuario/relatorio-binance-frontend.git
      - cd relatorio-binance-frontend
      - npm install
      - npm start
    url: http://localhost:3000

melhorias_futuras:
  - Recuperação de senha por e-mail
  - Exportação para CSV
  - Internacionalização (i18n)
  - Deploy contínuo (CI/CD)

deploy_sugestoes:
  backend:
    - Render
    - Railway
    - Fly.io
  frontend:
    - Vercel
    - Netlify

autor:
  nome: Almir

licenca: Almir
