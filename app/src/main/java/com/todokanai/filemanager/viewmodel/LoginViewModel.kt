package com.todokanai.filemanager.viewmodel

import androidx.lifecycle.viewModelScope
import com.todokanai.filemanager.data.dataclass.ServerHolderItem
import com.todokanai.filemanager.repository.DataStoreRepository
import com.todokanai.filemanager.viewmodel.logics.LoginViewModelLogics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val dsRepo: DataStoreRepository) : LoginViewModelLogics() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()
    init{
        viewModelScope.launch {
            serverListFlow.collect{
                _uiState.update{ currentState ->
                    currentState.copy(serverList = it)
                }
            }
        }
    }

    val tempList = listOf(
        ServerHolderItem("1"),
        ServerHolderItem("2"),
        ServerHolderItem("3")
    )

    override val serverListFlow: Flow<List<ServerHolderItem>>
        get() = flowOf(tempList)

    fun onServerClick(server:ServerHolderItem){

    }


}

data class LoginUiState(
    val serverList: List<ServerHolderItem> = emptyList()
)