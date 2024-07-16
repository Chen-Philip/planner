package com.example.planner.di_module

import com.example.planner.data.repository.user_repository.UserRepository
import com.example.planner.data.repository.user_repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds abstract fun provideUserRepository(impl: UserRepositoryImpl): UserRepository
}