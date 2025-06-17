<h1>🩺 Clínica API - Backend</h1>

<p>Este é o back-end do sistema de agendamento de consultas médicas, construído com <strong>Spring Boot</strong> e <strong>MySQL</strong>.</p>

<h2>🚀 Funcionalidades</h2>
<ul>
  <li>CRUD de usuários (médicos e pacientes)</li>
  <li>Cadastro e autenticação</li>
  <li>Gerenciamento de consultas</li>
  <li>Cadastro de disponibilidade de médicos</li>
  <li>Integração com front-end em React</li>
</ul>

<h2>🌍 Deploy</h2>
<p>A API está hospedada no Azure:</p>
<pre><code>https://projeto-clinica-cscsgyg9gkd4chbx.brazilsouth-01.azurewebsites.net</code></pre>

<h2>📦 Tecnologias</h2>
<ul>
  <li>Java 17</li>
  <li>Spring Boot</li>
  <li>Spring Data JPA</li>
  <li>MySQL</li>
  <li>Deploy: Azure App Service</li>
</ul>

<h2>🔗 Endpoints Principais</h2>
<ul>
  <li><code>GET /usuarios</code></li>
  <li><code>POST /usuarios</code></li>
  <li><code>POST /login</code></li>
  <li><code>GET /consultas</code></li>
  <li><code>POST /consultas</code></li>
  <li><code>PUT /consultas/{id}/aceitar</code></li>
  <li><code>GET /consultas/profissional/{id}</code></li>
</ul>

<h2>📁 Estrutura</h2>
<ul>
  <li><code>controller/</code>: Controladores REST</li>
  <li><code>service/</code>: Regras de negócio</li>
  <li><code>repository/</code>: Integração com JPA e banco</li>
  <li><code>dto/</code>: Objetos de transferência</li>
  <li><code>entities/</code>: Entidades JPA</li>
</ul>
