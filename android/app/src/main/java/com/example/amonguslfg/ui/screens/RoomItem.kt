
package com.example.amonguslfg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.amonguslfg.model.Room
import com.example.amonguslfg.ui.theme.AmongUsLFGTheme
import java.util.Date

import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Color

@Composable
fun RoomItem(
    room: Room,
    currentUserName: String,
    isAdmin: Boolean,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardModifier = if (room.is_special) {
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
            Text(text = "ルームコード: ${room.room_code}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "人数: ${room.current_players} / ${room.max_players}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "レギュレーション: ${room.regulation}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "MOD: ${room.mod_name}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "サーバー: ${room.server_name}", style = MaterialTheme.typography.bodyMedium)
            room.description?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "備考: $it", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoomItemPreview() {
    AmongUsLFGTheme {
        val sampleRoom = Room(
            id = 1,
            ownerName = "プレビューユーザー",
            roomCode = "PREVIW",
            currentPlayers = 5,
            maxPlayers = 10,
            regulation = "ガチ村",
            modName = "SNR",
            serverName = "アジア",
            description = "これはプレビュー用の募集です。気軽に参加してください。",
            createdAt = Date()
        )
        RoomItem(room = sampleRoom, modifier = Modifier.padding(8.dp))
    }
}
