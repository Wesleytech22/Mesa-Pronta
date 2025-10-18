//package com.mesapronta.app.di
//
//import com.mesapronta.app.data.repository.MesaRepository
//import com.mesapronta.app.data.repository.ReservaRepository
//
///**
// * Module de injeção de dependência SIMPLES sem Hilt
// */
//object AppModule {
//
//    // Fornece instância do MesaRepository
//    fun provideMesaRepository(): MesaRepository {
//        return MesaRepository()
//    }
//
//    // Fornece instância do ReservaRepository
//    fun provideReservaRepository(): ReservaRepository {
//        return ReservaRepository()
//    }
//}