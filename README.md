🍽️ Mesa Pronta

Mesa Pronta é um aplicativo Android desenvolvido em Kotlin com Jetpack Compose, projetado para facilitar a busca e reserva de mesas em restaurantes específicos, com escolha de horário, mesa e características do ambiente.

Com uma arquitetura moderna, baseada em MVVM e injeção de dependência (Hilt/Koin), o projeto visa oferecer uma experiência fluida, modular e escalável, adequada tanto para protótipos quanto para evolução com backend real.

🧭 Sumário

Visão Geral

Funcionalidades

Arquitetura e Estrutura de Pastas

Tecnologias Utilizadas

Configuração do Ambiente

Execução do Projeto

Fluxo de Uso do App

Possíveis Melhorias Futuras

Licença

🔍 Visão Geral

O Mesa Pronta resolve um problema comum: encontrar restaurantes e reservar mesas com facilidade, permitindo ao usuário escolher:

o restaurante desejado,

o tipo e localização da mesa,

o horário disponível para reserva.

O sistema é totalmente local, armazenando informações de forma simples via repositories internos. Contudo, a arquitetura foi desenhada para suportar uma futura integração com APIs externas.

✨ Funcionalidades

🔎 Busca de Restaurantes — encontre rapidamente estabelecimentos específicos.

🪑 Reserva de Mesas — selecione mesa, características e horário.

📍 Visualização de Localização e Detalhes — veja informações sobre o ambiente e o restaurante.

🧾 Histórico de Reservas — acompanhe reservas anteriores.

🔐 Autenticação Simples (AuthViewModel) — controle de acesso básico integrado à UI.

🎨 Interface Declarativa com Jetpack Compose — moderna, leve e responsiva.

🧱 Arquitetura e Estrutura de Pastas

O projeto segue o padrão MVVM, com camadas bem definidas de responsabilidade:

📦 com.mesapronta.app
│
├── 📁 component
│   └── ViewList.kt
│       → Componentes reutilizáveis de UI (listas, cards, etc.)
│
├── 📁 data.repository
│   ├── MesaRepository.kt
│   ├── MesaRepositoryInterface.kt
│   └── ReservaRepository.kt
│       → Implementações responsáveis por gerenciar dados de mesa e reserva.
│
├── 📁 di
│   └── AppModule.kt
│       → Módulo de injeção de dependências (ex: repositórios, viewmodels).
│
├── 📁 model
│   ├── CaracteristicaMesa.kt
│   ├── LocalizacaoMesa.kt
│   ├── Mesa.kt
│   ├── Promotion.kt
│   ├── Reserva.kt
│   ├── Restaurant.kt
│   ├── TableBar.kt
│   └── TableRestaurant.kt
│       → Modelos de domínio representando entidades da aplicação.
│
├── 📁 ui
│   ├── 📁 navigation
│   ├── 📁 screen
│   ├── 📁 theme
│   └── 📁 viewmodel
│       → Camada de apresentação (Compose) e gerenciamento de estado.
│
├── AuthViewModel.kt
│   → Lida com autenticação e controle de sessão.
│
└── MainActivity.kt
    → Ponto de entrada da aplicação (Compose setup e Navegação principal).

🧩 Padrões adotados

MVVM (Model-View-ViewModel): separação clara entre lógica de negócios, dados e UI.

Repository Pattern: abstrai a origem dos dados (facilita migração para API futuramente).

Dependency Injection (DI): promovido pelo módulo AppModule.kt.

Composable UI: telas e componentes reutilizáveis em Jetpack Compose.
