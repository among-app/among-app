package com.example.amonguslfg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.amonguslfg.viewmodels.CreateRoomUiState
import com.example.amonguslfg.viewmodels.CreateRoomViewModel
import kotlin.math.roundToInt

import androidx.compose.foundation.clickable
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoomScreen(
    navController: NavController,
    viewModel: CreateRoomViewModel = hiltViewModel()
) {
    val roomCode by viewModel.roomCode.collectAsStateWithLifecycle()
    val currentPlayers by viewModel.currentPlayers.collectAsStateWithLifecycle()
    val maxPlayers by viewModel.maxPlayers.collectAsStateWithLifecycle()
    val regulations by viewModel.regulations.collectAsStateWithLifecycle()
    val mod by viewModel.mod.collectAsStateWithLifecycle()
    val modOtherText by viewModel.modOtherText.collectAsStateWithLifecycle()
    val server by viewModel.server.collectAsStateWithLifecycle()
    val serverOtherText by viewModel.serverOtherText.collectAsStateWithLifecycle()
    val description by viewModel.description.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isSpecial by viewModel.isSpecial.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    var showRegulationDialog by remember { mutableStateOf(false) }

    val title = if (isSpecial) "特殊投稿を作成" else "募集を作成"

    val regulationOptions = listOf("闇鍋", "光鍋", "ガチ村", "かくれんぼ", "近アモ", "撮影", "テストプレイ", "特殊プレイ", "そのほか")
    val mods = listOf("SNR", "Nos", "Exr", "バニラ", "その他")
    val servers = listOf("アジア", "SNRTOKYO", "NOS", "その他")

    if (showRegulationDialog) {
        MultiSelectDialog(
            title = "レギュレーションを選択",
            options = regulationOptions,
            selectedOptions = regulations,
            onSelectionChanged = { viewModel.onRegulationSelected(it) },
            onDismissRequest = { showRegulationDialog = false }
        )
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is CreateRoomUiState.Success -> {
                navController.popBackStack()
            }
            is CreateRoomUiState.Error -> {
                snackbarHostState.showSnackbar(message = state.message)
                viewModel.resetUiState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()){
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(value = roomCode, onValueChange = { viewModel.onRoomCodeChange(it) }, label = { Text("ルームコード (6桁の英字)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(value = currentPlayers, onValueChange = { viewModel.currentPlayers.value = it }, label = { Text("現在人数") }, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("/", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = maxPlayers.roundToInt().toString(), style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
                }
                Slider(value = maxPlayers, onValueChange = { viewModel.maxPlayers.value = it }, valueRange = 2f..15f, steps = 12)
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = if (regulations.isEmpty()) "" else regulations.joinToString(", "),
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth().clickable { showRegulationDialog = true },
                    readOnly = true,
                    label = { Text("レギュレーション (複数選択可)") },
                    enabled = false, // To make it non-focusable and rely on clickable
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
                DropdownMenu(label = "MOD", options = mods, selectedOption = mod, onOptionSelected = { viewModel.mod.value = it })
                if (mod == "その他") {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = modOtherText, onValueChange = { viewModel.modOtherText.value = it }, label = { Text("MOD名を入力") }, modifier = Modifier.fillMaxWidth())
                }
                Spacer(modifier = Modifier.height(16.dp))
                DropdownMenu(label = "サーバー", options = servers, selectedOption = server, onOptionSelected = { viewModel.server.value = it })
                if (server == "その他") {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = serverOtherText, onValueChange = { viewModel.serverOtherText.value = it }, label = { Text("サーバー名を入力") }, modifier = Modifier.fillMaxWidth())
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = description, onValueChange = { viewModel.description.value = it }, label = { Text("備考") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = { viewModel.createRoom() }, modifier = Modifier.fillMaxWidth(), enabled = uiState != CreateRoomUiState.Loading) {
                    Text("募集する")
                }
            }
            if (uiState == CreateRoomUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun MultiSelectDialog(
    title: String,
    options: List<String>,
    selectedOptions: List<String>,
    onSelectionChanged: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = title, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectionChanged(option) }
                            .padding(vertical = 8.dp)
                    ) {
                        Checkbox(checked = selectedOptions.contains(option), onCheckedChange = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onDismissRequest, modifier = Modifier.align(Alignment.End)) {
                    Text("閉じる")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {}, 
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}