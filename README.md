# 🍽️ Mesa Pronta

**Mesa Pronta** é um aplicativo Android escrito em **Kotlin** com **Jetpack Compose** para **buscar restaurantes e reservar mesas (com horário e características da mesa)**. Este README está alinhado com a estrutura do seu código (`component`, `data.repository`, `di`, `model`, `ui`, `AuthViewModel`, `MainActivity`).

---

## Índice

- [Visão Geral](#visão-geral)  
- [Funcionalidades](#funcionalidades)  
- [Arquitetura e Estrutura de Pastas](#arquitetura-e-estrutura-de-pastas)  
- [Tecnologias](#tecnologias)  
- [Pré-requisitos](#pré-requisitos)  
- [Instalação e Execução](#instalação-e-execução)  
- [Fluxo de Uso](#fluxo-de-uso)  
- [Pontos Técnicos Importantes](#pontos-técnicos-importantes)  
- [Melhorias Futuras](#melhorias-futuras)  
- [Licença](#licença)  
- [Autor](#autor)

---

## Visão Geral

O app permite ao usuário:
- Buscar um restaurante específico;
- Visualizar detalhes do restaurante e das mesas (ex.: `TableRestaurant`, `TableBar`);
- Selecionar características de mesa (`CaracteristicaMesa`, `LocalizacaoMesa`);
- Reservar uma mesa para data/horário específicos (`Reserva`);
- Gerenciar reservas localmente via repositórios (`MesaRepository`, `ReservaRepository`).

A arquitetura está pensada para ser facilmente extensível para integração com backend no futuro.

---

## Funcionalidades

- Pesquisa de restaurantes por nome/atributo.  
- Visualização de detalhes do restaurante e tabelas disponíveis.  
- Seleção de mesa por características e localização.  
- Reserva com data e horário.  
- Histórico local de reservas.  
- Fluxo de autenticação simples controlado por `AuthViewModel`.  
- Componentes UI reutilizáveis em `component/ViewList.kt`.

---

## Arquitetura e Estrutura de Pastas

Estrutura principal (resumida):


com.mesapronta.app
├── component
│ └── ViewList.kt
├── data.repository
│ ├── MesaRepositoryInterface.kt
│ ├── MesaRepository.kt
│ └── ReservaRepository.kt
├── di
│ └── AppModule.kt
├── model
│ ├── CaracteristicaMesa.kt
│ ├── LocalizacaoMesa.kt
│ ├── Mesa.kt
│ ├── Promotion.kt
│ ├── Reserva.kt
│ ├── Restaurant.kt
│ ├── TableBar.kt
│ └── TableRestaurant.kt
├── ui
│ ├── navigation
│ ├── screen
│ ├── theme
│ └── viewmodel
├── AuthViewModel.kt
└── MainActivity.kt


**Padrões adotados:**
- MVVM (Model — View — ViewModel)  
- Repository Pattern para abstração de dados  
- DI (módulo `AppModule.kt`) — preparado para Hilt/Koin  
- UI com Jetpack Compose (componentização em `component/`)

---

## Tecnologias

- Kotlin  
- Jetpack Compose  
- Compose Navigation  
- MVVM + Repositories  
- Dependência injetável via `di/AppModule.kt` (Hilt/Koin recomendado)  
- Persistência local via repositórios (fácil migração para Room ou API)

---

## Pré-requisitos

- Android Studio (recomendado: versão recente — Arctic Fox / Chipmunk / Dolphin ou superior)  
- JDK 17+  
- Gradle 8.5+  
- Emulador com API 33+ ou dispositivo físico

---

## Instalação e Execução

1. Clone o repositório:
   ```bash
   git clone https://github.com/seuusuario/mesa-pronta.git
   cd mesa-pronta
