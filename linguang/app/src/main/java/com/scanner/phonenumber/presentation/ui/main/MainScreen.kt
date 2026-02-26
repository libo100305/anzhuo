package com.scanner.phonenumber.presentation.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.scanner.phonenumber.R
import com.scanner.phonenumber.presentation.viewModel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val importState by viewModel.importState.collectAsState()
    
    LaunchedEffect(importState) {
        if (importState is MainViewModel.ImportState.Success) {
            // 导入成功后3秒自动重置状态
            kotlinx.coroutines.delay(3000)
            viewModel.resetImportState()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 标题
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // 功能卡片
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                FeatureCard(
                    title = "拍照识别电话号码",
                    description = "使用相机拍摄包含电话号码的图片，自动识别并提取电话号码",
                    icon = Icons.Default.Camera,
                    onClick = { navController.navigate("camera") }
                )
            }
            
            item {
                FeatureCard(
                    title = "通讯录匹配",
                    description = "导入手机通讯录，识别出的电话号码自动匹配对应联系人",
                    icon = Icons.Default.People,
                    onClick = { viewModel.importContacts() }
                )
            }
            
            item {
                FeatureCard(
                    title = "识别历史",
                    description = "查看所有识别记录，包括识别的号码和匹配的联系人",
                    icon = Icons.Default.History,
                    onClick = { navController.navigate("history") }
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // 导入状态显示
        when (val state = importState) {
            is MainViewModel.ImportState.Idle -> {}
            is MainViewModel.ImportState.Importing -> {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
                Text(
                    text = "正在导入通讯录...",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            is MainViewModel.ImportState.Success -> {
                Snackbar(
                    modifier = Modifier.padding(top = 16.dp),
                    action = {
                        TextButton(onClick = { viewModel.resetImportState() }) {
                            Text("知道了")
                        }
                    }
                ) {
                    Text("通讯录导入成功，共导入 ${state.count} 个联系人")
                }
            }
            is MainViewModel.ImportState.Error -> {
                Snackbar(
                    modifier = Modifier.padding(top = 16.dp),
                    action = {
                        TextButton(onClick = { viewModel.resetImportState() }) {
                            Text("重试")
                        }
                    }
                ) {
                    Text("导入失败: ${state.message}")
                }
            }
        }
    }
}

@Composable
fun FeatureCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "进入"
            )
        }
    }
}