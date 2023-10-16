package com.project.soloshoppingcart.module

import com.project.soloshoppingcart.repository.DataRepository
import com.project.soloshoppingcart.repository.DataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@InstallIn(ViewModelComponent::class, SingletonComponent::class)
@Module
abstract class DataModule {

    @Binds
    abstract fun getData(repository: DataRepositoryImpl): DataRepository

}