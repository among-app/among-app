package com.example.amonguslfg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.amonguslfg.model.User
import com.example.amonguslfg.model.UserUpdate
import com.example.amonguslfg.viewmodels.UserManagerViewModel

import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.ui.input.nestedscroll.nestedScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagerScreen(
    navController: NavController,
    viewModel: UserManagerViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsStateWithLifecycle()
    val updatingUserIds by viewModel.updatingUserIds.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    val pullToRefreshState = rememberPullToRefreshState()
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.fetchUsers()
        }
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            pullToRefreshState.startRefresh()
        } else {
            pullToRefreshState.endRefresh()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ユーザー管理") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(users, key = { it.id }) { user ->
                    UserManagementItem(
                        user = user,
                        isUpdating = user.id in updatingUserIds,
                        onUpdate = viewModel::updateUser
                    )
                }
            }
            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullToRefreshState,
            )
        }
    }
}

@Composable
fun UserManagementItem(
    user: User,
    isUpdating: Boolean,
    onUpdate: (Int, UserUpdate) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Box(contentAlignment = Alignment.Center) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = user.username, style = MaterialTheme.typography.titleMedium)
                Text(text = "Device ID: ${user.deviceId}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("認証済み")
                    Switch(
                        checked = user.isVerified,
                        onCheckedChange = { onUpdate(user.id, UserUpdate(isVerified = it)) },
                        enabled = !isUpdating
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("管理者")
                    Switch(
                        checked = user.isAdmin,
                        onCheckedChange = { onUpdate(user.id, UserUpdate(isAdmin = it)) },
                        enabled = !isUpdating
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("BAN")
                    Switch(
                        checked = user.isBanned,
                        onCheckedChange = { onUpdate(user.id, UserUpdate(isBanned = it)) },
                        enabled = !isUpdating
                    )
                }
            }
            if (isUpdating) {
                CircularProgressIndicator()
            }
        }
    }
}