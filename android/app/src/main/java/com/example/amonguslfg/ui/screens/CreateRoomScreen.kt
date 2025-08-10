
package com.example.amonguslfg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.amonguslfg.viewmodels.CreateRoomViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoomScreen(
    navController: NavController,
    viewModel: CreateRoomViewModel = hiltViewModel()
) {
    val roomCode by viewModel.roomCode.collectAsState()
    val currentPlayers by viewModel.currentPlayers.collectAsState()
    val maxPlayers by viewModel.maxPlayers.collectAsState()
    val regulation by viewModel.regulation.collectAsState()
    val mod by viewModel.mod.collectAsState()
    val server by viewModel.server.collectAsState()
    val description by viewModel.description.collectAsState()
    val creationSuccess by viewModel.creationSuccess.collectAsState()
    val isSpecial by viewModel.isSpecial.collectAsState()

    val title = if (isSpecial) "特殊投稿を作成" else "募集を作成"

    val regulations = listOf("闇鍋", "光鍋", "ガチ村", "かくれんぼ", "近アモ", "撮影", "テストプレイ", "特殊プレイ", "そのほか")
    val mods = listOf("SNR", "Nos", "Exr", "バニラ", "その他")
    val servers = listOf("アジア", "SNRTOKYO", "NOS", "その他")

    LaunchedEffect(creationSuccess) {
        if (creationSuccess) {
            navController.popBackStack()
        }
    }

    Scaffold(
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
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(value = roomCode, onValueChange = { viewModel.roomCode.value = it }, label = { Text("ルームコード (6桁の英字)") }, modifier = Modifier.fillMaxWidth())
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
            DropdownMenu(label = "レギュレーション", options = regulations, selectedOption = regulation, onOptionSelected = { viewModel.regulation.value = it })
            Spacer(modifier = Modifier.height(16.dp))
            DropdownMenu(label = "MOD", options = mods, selectedOption = mod, onOptionSelected = { viewModel.mod.value = it })
            Spacer(modifier = Modifier.height(16.dp))
            DropdownMenu(label = "サーバー", options = servers, selectedOption = server, onOptionSelected = { viewModel.server.value = it })
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = description, onValueChange = { viewModel.description.value = it }, label = { Text("備考") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { viewModel.createRoom() }, modifier = Modifier.fillMaxWidth()) {
                Text("募集する")
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
