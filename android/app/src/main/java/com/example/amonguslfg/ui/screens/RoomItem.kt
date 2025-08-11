package com.example.amonguslfg.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.amonguslfg.model.Room

@Composable
fun RoomItem(
    room: Room,
    currentUserName: String,
    isAdmin: Boolean,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardModifier = if (room.isSpecial) {
        modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(2.dp, Color.Yellow, MaterialTheme.shapes.medium)
    } else {
        modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    }

    Card(
        modifier = cardModifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "部屋主: ${room.owner.username}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (isAdmin || room.owner.username == currentUserName && currentUserName.isNotBlank()) {
                    Row {
                        IconButton(onClick = { onEditClick(room.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "編集")
                        }
                        IconButton(onClick = { onDeleteClick(room.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "削除")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "ルームコード: ${room.roomCode}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "人数: ${room.currentPlayers} / ${room.maxPlayers}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "レギュレーション: ${room.regulation}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "MOD: ${room.modName}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "サーバー: ${room.serverName}", style = MaterialTheme.typography.bodyMedium)
            room.description?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "備考: $it", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}