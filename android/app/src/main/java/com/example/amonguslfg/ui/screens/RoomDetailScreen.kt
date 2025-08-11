package com.example.amonguslfg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.amonguslfg.viewmodels.RoomDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomDetailScreen(
    navController: NavController,
    viewModel: RoomDetailViewModel = hiltViewModel()
) {
    val room by viewModel.room.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(room?.roomCode ?: "詳細") }, // Corrected: room?.roomCode
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { paddingValues -> // Changed it to paddingValues
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) { // Apply padding to a Box
            room?.let {
                // Re-use RoomItem for consistent display
                RoomItem(
                    room = it,
                    currentUserName = "",
                    isAdmin = false,
                    onEditClick = {},
                    onDeleteClick = {},
                    onStatusChange = { _, _ -> } // Add dummy lambda for status change
                )
            } ?: run {
                // Optional: Show a loading indicator or an error message
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
