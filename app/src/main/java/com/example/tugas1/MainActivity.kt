package com.example.tugas1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tugas1.ui.LoginScreen
import com.example.tugas1.ui.RegisterScreen
import com.example.tugas1.ui.pages.*
import com.example.tugas1.ui.viewmodel.ChatViewModel
import com.example.tugas1.viewmodel.AuthViewModel
import com.example.tugas1.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val productViewModel: ProductViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val chatViewModel: ChatViewModel = viewModel() // singgle ViewModel untuk chat
    val isAuthenticated by authViewModel.authState.collectAsState()

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = if (isAuthenticated) "main_graph" else "auth_graph"
        ) {

            // ---------------- AUTH GRAPH ----------------
            navigation(startDestination = "login", route = "auth_graph") {
                composable("login") { LoginScreen(navController, authViewModel) }
                composable("register") { RegisterScreen(navController, authViewModel) }
            }

            // ---------------- MAIN GRAPH ----------------
            navigation(startDestination = "dashboard", route = "main_graph") {
                composable("dashboard") { DashboardScreen(navController, productViewModel) }
                composable("cart") { CartScreen(navController, productViewModel) }
                composable("wishlist") { WishlistScreen(navController, productViewModel) }
                composable("profile") { ProfileScreen(navController, authViewModel) }
                composable("notification") { NotificationScreen(navController) }
                composable("chat") { ChatScreen(navController, productViewModel) }
                composable("checkout") { CheckoutScreen(navController, productViewModel) }

                // CHAT DETAIL — atur agar menerima encoded imageUrl (decode di screen)
                composable(
                    route = "chat_detail/{shopName}/{imageUrl}",
                    arguments = listOf(
                        navArgument("shopName") { type = NavType.StringType },
                        navArgument("imageUrl") { type = NavType.StringType }
                    )
                ) { entry ->
                    val shopName = entry.arguments?.getString("shopName") ?: ""
                    val imageUrl = entry.arguments?.getString("imageUrl") ?: ""
                    // berikan viewModel ke composable
                    ChatDetailScreen(
                        shopName = shopName,
                        imageUrl = imageUrl,
                        navController = navController,
                        viewModel = chatViewModel
                    )
                }
            }
        }
    }
}
