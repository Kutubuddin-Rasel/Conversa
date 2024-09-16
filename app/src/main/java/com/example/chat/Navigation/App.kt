package com.example.chat.Navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.chat.screens.ChatScreen
import com.example.chat.screens.Chats
import com.example.chat.screens.SingIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chat.R
import com.example.chat.Routes
import com.example.chat.data.bottombar
import com.example.chat.screens.LogIn
import com.example.chat.screens.Profile
import com.example.chat.screens.SearchUser
import com.example.chat.screens.Username

@Composable
fun App() {
    val navController = rememberNavController()
    var showBottomBar by remember { mutableStateOf(true) }
    val navItemList = listOf(
        bottombar(Routes.chats, "Chats", R.drawable.chats),
        bottombar(Routes.searchUser, "People", R.drawable.people),
        bottombar(Routes.profile, "Profile", R.drawable.profile)
    )
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController, navItemList = navItemList)
            }
        }
    ) {
        NavHost(navController, startDestination = Routes.login, modifier = Modifier.padding(it)) {
            composable(Routes.login) {
                LogIn(navController, Routes.login)
            }
            composable(Routes.signIn) {
                SingIn(navController, Routes.signIn)
            }
            composable(Routes.usernameUi) {
                Username(navController)
            }
            composable(Routes.searchUser) {
                SearchUser(navController)
            }
            composable(Routes.chats) {
                Chats(navController)
            }
            composable(Routes.profile) {
                Profile(navController)
            }
            composable("${Routes.chatscreen}/{username}/{userid}", arguments = listOf(
                navArgument("username") { type = NavType.StringType },
                navArgument("userid") { type = NavType.StringType }
            )) { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username")
                val userid = backStackEntry.arguments?.getString("userid")
                if (username != null && userid != null) {
                    // Hide bottom bar when in chat screen
                    showBottomBar = false
                    ChatScreen(username, userid, navController)
                }
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            showBottomBar = when (destination.route) {
                Routes.login,
                Routes.signIn,
                Routes.usernameUi,
                Routes.chatscreen -> false // Hide bottom bar on these screens
                else -> true // Show bottom bar on other screens
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController, navItemList: List<bottombar>) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    NavigationBar(
        containerColor = Color(16, 29, 77),
        modifier = Modifier.fillMaxWidth()
    ) {
        navItemList.forEach { bottombar ->
            NavigationBarItem(
                selected = currentRoute == bottombar.route,
                onClick = {
                    navController.navigate(bottombar.route){
                        launchSingleTop = true
                    }
                },
                icon = {
                    Image(
                        painter = painterResource(bottombar.image),
                        "", modifier = Modifier.size(40.dp),
                        colorFilter = ColorFilter.tint(if (navController.currentBackStackEntryAsState().value?.destination?.route == bottombar.route) Color.White else Color.Black)
                    )
                },
                label = { Text(bottombar.screenName, fontSize = 15.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White, // Color of the selected icon
                    unselectedIconColor = Color.Gray, // Color of the unselected icon
                    selectedTextColor = Color.White, // Color of the selected label text
                    unselectedTextColor = Color.Gray, // Color of the unselected label text
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}