package com.glauco.weatherapp.ui.nav

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.glauco.weatherapp.viewmodel.MainViewModel

@Composable
fun BottomNavBar (viewModel: MainViewModel, items: List<BottomNavItem>) { // CORRIGIDO: Recebe ViewModel
    NavigationBar(
        contentColor = Color.Black
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title, fontSize = 12.sp) },
                alwaysShowLabel = true,
                // PRÁTICA 08/09: Usa viewModel.page para definir se está selecionado
                selected = viewModel.page == item.route,
                onClick = {
                    // PRÁTICA 08/09: Define a rota no ViewModel. A navegação real é feita no LaunchedEffect da MainActivity.
                    viewModel.page = item.route
                }
            )
        }
    }
}