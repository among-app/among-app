package com.example.amonguslfg.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.amonguslfg.viewmodels.MainViewModel
import com.example.amonguslfg.viewmodels.RoomListViewModel
import com.example.amonguslfg.viewmodels.SortFilters

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
    val filters by listViewModel.sortFilters.collectAsStateWithLifecycle()
    val currentUserName by listViewModel.userName.collectAsStateWithLifecycle()
    val currentUser by mainViewModel.currentUser.collectAsStateWithLifecycle()
    var showFilterDialog by remember { mutableStateOf(false) }

    if (showFilterDialog) {
        FilterDialog(
            currentFilters = filters,
            onDismiss = { showFilterDialog = false },
            onApply = { newFilters ->
                listViewModel.updateSortFilters(newFilters)
                showFilterDialog = false
            }
        )
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                listViewModel.fetchRooms()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("募集一覧") },
                actions = {
                    if (currentUser?.isAdmin == true) { // Corrected: is_admin to isAdmin
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
                }
            )
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                FloatingActionButton(
                    onClick = { showFilterDialog = true },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Default.FilterList, contentDescription = "フィルター")
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (filters.regulations.isNotEmpty() || filters.mods.isNotEmpty()) {
                    FloatingActionButton(
                        onClick = { listViewModel.clearSortFilters() },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.FilterListOff, contentDescription = "フィルターをクリア")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(onClick = onFabClick) {
                    Icon(Icons.Default.Add, contentDescription = "新しい募集を作成")
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding).padding(horizontal = 8.dp)) {
            items(rooms) { room ->
                RoomItem(
                    room = room,
                    currentUserName = currentUserName,
                    isAdmin = currentUser?.isAdmin == true, // Corrected: is_admin to isAdmin
                    onEditClick = onEditClick,
                    onDeleteClick = { listViewModel.deleteRoom(room.id) }
                )
            }
        }
    }
}

@Composable
fun FilterDialog(
    currentFilters: SortFilters,
    onDismiss: () -> Unit,
    onApply: (SortFilters) -> Unit
) {
    var selectedRegulations by remember { mutableStateOf(currentFilters.regulations) }
    var selectedMods by remember { mutableStateOf(currentFilters.mods) }

    val regulationOptions = listOf("闇鍋", "光鍋", "ガチ村", "かくれんぼ", "近アモ", "撮影", "テストプレイ", "特殊プレイ", "そのほか")
    val modOptions = listOf("SNR", "Nos", "Exr", "バニラ")

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text("フィルター", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                Text("レギュレーション", style = MaterialTheme.typography.titleMedium)
                regulationOptions.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { 
                                selectedRegulations = if (selectedRegulations.contains(option)) {
                                    selectedRegulations - option
                                } else {
                                    selectedRegulations + option
                                }
                            }
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(checked = selectedRegulations.contains(option), onCheckedChange = null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(option)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("MOD", style = MaterialTheme.typography.titleMedium)
                modOptions.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { 
                                selectedMods = if (selectedMods.contains(option)) {
                                    selectedMods - option
                                } else {
                                    selectedMods + option
                                }
                            }
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(checked = selectedMods.contains(option), onCheckedChange = null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(option)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(onClick = onDismiss) {
                        Text("キャンセル")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onApply(SortFilters(regulations = selectedRegulations, mods = selectedMods)) }) {
                        Text("適用")
                    }
                }
            }
        }
    }
}