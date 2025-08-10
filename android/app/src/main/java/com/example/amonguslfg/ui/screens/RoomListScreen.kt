
package com.example.amonguslfg.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amonguslfg.model.Room
import com.example.amonguslfg.ui.theme.AmongUsLFGTheme
import com.example.amonguslfg.viewmodels.RoomListViewModel
import java.util.Date

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.amonguslfg.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomListScreen(
    onFabClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAnnouncementsClick: () -> Unit,
    onAdminClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    listViewModel: RoomListViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val rooms by listViewModel.rooms.collectAsStateWithLifecycle()
    val sortCondition by listViewModel.sortCondition.collectAsStateWithLifecycle()
    val currentUserName by listViewModel.userName.collectAsStateWithLifecycle()
    val currentUser by mainViewModel.currentUser.collectAsStateWithLifecycle()
    var showMenu by remember { mutableStateOf(false) }

    val sortOptions = listOf("すべて", "闇鍋", "光鍋", "ガチ村", "かくれんぼ", "近アモ", "撮影", "テストプレイ", "特殊プレイ", "SNR", "Nos", "Exr", "バニラ")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("募集一覧") },
                actions = {
                    if (currentUser?.is_admin == true) {
                        IconButton(onClick = onAdminClick) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = "管理者メニュー")
                        }
                    }
                    IconButton(onClick = onAnnouncementsClick) {
                        Icon(Icons.Default.Campaign, contentDescription = "お知らせ")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "設定")
                    }
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "ソート")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            sortOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        listViewModel.updateSortCondition(option)
                                        showMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(Icons.Filled.Add, contentDescription = "新しい募集を作成")
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding).padding(horizontal = 8.dp)) {
            items(rooms) { room ->
                RoomItem(
                    room = room,
                    currentUserName = currentUserName,
                    isAdmin = currentUser?.is_admin == true,
                    onEditClick = onEditClick,
                    onDeleteClick = { listViewModel.deleteRoom(it) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoomListScreenPreview() {
    AmongUsLFGTheme {
        // This preview is simplified as the main composable expects a ViewModel.
        // For a more accurate preview, you might need a fake ViewModel.
        RoomListScreen(onFabClick = {})
    }
}
