package com.example.amonguslfg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.amonguslfg.model.Announcement
import com.example.amonguslfg.viewmodels.AnnouncementManagerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementManagerScreen(
    navController: NavController,
    viewModel: AnnouncementManagerViewModel = hiltViewModel()
) {
    val announcements by viewModel.announcements.collectAsState()
    var showDialog by remember { mutableStateOf<Announcement?>(null) }
    var isCreatingNew by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("お知らせの管理") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { isCreatingNew = true }) {
                Icon(Icons.Default.Add, contentDescription = "新規作成")
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(announcements) {
                AnnouncementManagementItem(
                    announcement = it,
                    onEditClick = { showDialog = it },
                    onDeleteClick = { viewModel.deleteAnnouncement(it.id) }
                )
            }
        }
    }

    if (isCreatingNew) {
        AnnouncementEditDialog(
            onDismiss = { isCreatingNew = false },
            onConfirm = { title, content ->
                viewModel.createAnnouncement(title, content)
                isCreatingNew = false
            }
        )
    }

    showDialog?.let { announcement ->
        AnnouncementEditDialog(
            announcement = announcement,
            onDismiss = { showDialog = null },
            onConfirm = { title, content ->
                viewModel.updateAnnouncement(announcement.id, title, content)
                showDialog = null
            }
        )
    }
}

@Composable
fun AnnouncementManagementItem(
    announcement: Announcement,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = announcement.title, style = MaterialTheme.typography.titleMedium)
                Text(text = announcement.content, style = MaterialTheme.typography.bodySmall)
            }
            Row {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "編集")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "削除")
                }
            }
        }
    }
}

@Composable
fun AnnouncementEditDialog(
    announcement: Announcement? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(announcement?.title ?: "") }
    var content by remember { mutableStateOf(announcement?.content ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (announcement == null) "お知らせを作成" else "お知らせを編集") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("タイトル") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("内容") })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(title, content) }) {
                Text(if (announcement == null) "作成" else "更新")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("キャンセル")
            }
        }
    )
}
