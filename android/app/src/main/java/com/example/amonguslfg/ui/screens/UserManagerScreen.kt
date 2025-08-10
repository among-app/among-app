
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
import androidx.navigation.NavController
import com.example.amonguslfg.model.User
import com.example.amonguslfg.model.UserUpdate
import com.example.amonguslfg.viewmodels.UserManagerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagerScreen(
    navController: NavController,
    viewModel: UserManagerViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()

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
    ) {
        LazyColumn(
            modifier = Modifier.padding(it).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users) { user ->
                UserManagementItem(user = user, onUpdate = viewModel::updateUser)
            }
        }
    }
}

@Composable
fun UserManagementItem(user: User, onUpdate: (Int, UserUpdate) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = user.username, style = MaterialTheme.typography.titleMedium)
            Text(text = "Device ID: ${user.device_id}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("認証済み")
                Switch(checked = user.is_verified, onCheckedChange = { onUpdate(user.id, UserUpdate(isVerified = it)) })
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("管理者")
                Switch(checked = user.is_admin, onCheckedChange = { onUpdate(user.id, UserUpdate(isAdmin = it)) })
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("BAN")
                Switch(checked = user.is_banned, onCheckedChange = { onUpdate(user.id, UserUpdate(isBanned = it)) })
            }
        }
    }
}
