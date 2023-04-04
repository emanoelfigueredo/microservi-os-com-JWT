![MICROSERVIÇOS + AUTENTICAÇÃO COM TOKEN JWT](https://user-images.githubusercontent.com/121516171/229849426-ca0d2825-024f-4244-ad0d-a9ea6460220b.png)


<p align="center">
<img src="https://img.shields.io/badge/Testes-16%20sucessos%2C%200%20falhas-green?style=for-the-badge&logo=appveyor">
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white">
<a href="https://www.linkedin.com/in/emanoel-figuer%C3%AAdo-47063b215/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white"></a>
</p>

# Descrição do projeto
API REST para registro de anotações. Utilizando a arquitetura de microserviços, a aplicação se divide em quatro partes: 
- Server Registry (Spring Cloud Netflix – Eureka) 
- Gateway (Spring Cloud Gateway)
- Microserviço de autenticacao
- Microserviço de anotações

# Subir aplicação
No diretório raiz da aplicação, digite o comando:
~~~docker
git clone https://github.com/emanoelfigueredo/microservicos-com-JWT.git
docker-compose up
~~~

# Endpoints

# Funcionamento
As requisições passam através dos filtros do Gateway. Um deles permique que qualquer requisição passe para os endpoints: /identidade/registrar, /identidade/validar ou /identidade/autenticar o token JWT. No entanto, para qualquer outro endpoint, será necessário que o token venha no header. O filtro analizará a validade do token enviado no proprio gateway. Se for válido a requisição seguirá o fluxo até o endpoint.

![representacao](https://user-images.githubusercontent.com/121516171/229862718-ad7ddcc3-dbf1-47ed-9313-055eded6d17a.png)


