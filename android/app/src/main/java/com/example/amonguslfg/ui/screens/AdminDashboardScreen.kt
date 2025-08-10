
package com.example.amonguslfg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("管理者ダッシュボード") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = { navController.navigate("announcementManager") }, modifier = Modifier.fillMaxWidth()) {
                Text("お知らせの管理")
            }
            Button(onClick = { navController.navigate("userManager") }, modifier = Modifier.fillMaxWidth()) {
                Text("ユーザー管理")
            }
            Button(onClick = { navController.navigate("createRoom?isSpecial=true") }, modifier = Modifier.fillMaxWidth()) {
                Text("特殊投稿を作成")
            }
            // Add other admin functions here
        }
    }
}
