package com.gabez.todo_list_simple.di

import com.gabez.todo_list_simple.app.fragments.addItemView.AddItemViewModel
import com.gabez.todo_list_simple.app.fragments.editItemView.EditItemViewModel
import com.gabez.todo_list_simple.app.fragments.listView.ListViewModel
import com.gabez.todo_list_simple.data.AppRepositoryImpl
import com.gabez.todo_list_simple.data.dataSources.*
import com.gabez.todo_list_simple.data.firestore.RemoteDatabase
import com.gabez.todo_list_simple.data.firestore.RemoteDatabaseImpl
import com.gabez.todo_list_simple.data.firestore.RemoteDatabaseItemsProvider
import com.gabez.todo_list_simple.data.pagination.PagingDatasource
import com.gabez.todo_list_simple.data.pagination.PagingLastIndexProvider
import com.gabez.todo_list_simple.domain.repository.AppRepository
import com.gabez.todo_list_simple.domain.usecases.AddItemUsecase
import com.gabez.todo_list_simple.domain.usecases.DeleteItemUsecase
import com.gabez.todo_list_simple.domain.usecases.EditItemUsecase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val appModule = module {
    viewModel { AddItemViewModel(get(), get()) }
    viewModel { ListViewModel(get(), get(), get(), get()) }
    viewModel { EditItemViewModel(get(), get()) }

    single { AddItemUsecase(get()) }
    single { DeleteItemUsecase(get()) }
    single { EditItemUsecase(get()) }

    single { AppRepositoryImpl(get()) as AppRepository }
    single { RemoteDatasourceImpl(get()) as RemoteDatasource }
    single { RemoteDatabaseImpl(get()) as RemoteDatabase }
    single { PagingDatasource() }

    single { PagingDatasource() as PagingLastIndexProvider }
    single { RemoteDatabaseImpl(get()) as RemoteDatabaseItemsProvider }

    single { RemoteDataChangeProvider() }
}