package com.scanner.phonenumber.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.scanner.phonenumber.R
import com.scanner.phonenumber.presentation.ui.camera.CameraScreen
import com.scanner.phonenumber.presentation.ui.history.HistoryScreen
import com.scanner.phonenumber.presentation.ui.main.MainScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberScannerApp() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("main") { MainScreen(navController) }
            composable("camera") { CameraScreen(navController) }
            composable("history") { HistoryScreen() }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem(
            title = "首页",
            icon = Icons.Default.People,
            route = "main"
        ),
        BottomNavItem(
            title = "扫描",
            icon = Icons.Default.Camera,
            route = "camera"
        ),
        BottomNavItem(
            title = "历史",
            icon = Icons.Default.History,
            route = "history"
        )
    )
    
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)