package com.example.amonguslfg.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.amonguslfg.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "roomList") {
        composable("roomList") {
            RoomListScreen(
                onFabClick = { navController.navigate("createRoom?isSpecial=false") },
                onSettingsClick = { navController.navigate("settings") },
                onAnnouncementsClick = { navController.navigate("announcements") },
                onAdminClick = { navController.navigate("admin") },
                onEditClick = { roomId -> navController.navigate("editRoom/$roomId") }
            )
        }
        composable(
            route = "createRoom?isSpecial={isSpecial}",
            arguments = listOf(navArgument("isSpecial") {
                type = NavType.BoolType
                defaultValue = false
            })
        ) {
            CreateRoomScreen(navController = navController)
        }
        composable("settings") {
            SettingsScreen(navController = navController)
        }
        composable("announcements") {
            AnnouncementsScreen(navController = navController)
        }
        composable("admin") {
            AdminDashboardScreen(navController = navController)
        }
        composable("announcementManager") {
            AnnouncementManagerScreen(navController = navController)
        }
        composable("userManager") {
            UserManagerScreen(navController = navController)
        }
        composable(
            route = "editRoom/{roomId}",
            arguments = listOf(navArgument("roomId") { type = NavType.IntType })
        ) {
            EditRoomScreen(navController = navController)
        }
    }
}