package com.iftm.livros.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.iftm.livros.presentation.books.form.BookFormScreen
import com.iftm.livros.presentation.books.list.BookListScreen
import com.iftm.livros.presentation.books.trash.TrashScreen
import kotlin.reflect.KClass

private data class BottomDestination(
    val label: String,
    val route: Any,
    val routeClass: KClass<*>,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

private val bottomDestinations = listOf(
    BottomDestination(
        label = "Biblioteca",
        route = BookListRoute,
        routeClass = BookListRoute::class,
        selectedIcon = Icons.Filled.LocalLibrary,
        unselectedIcon = Icons.Outlined.LocalLibrary,
    ),
    BottomDestination(
        label = "Novo Livro",
        route = BookFormRoute(),
        routeClass = BookFormRoute::class,
        selectedIcon = Icons.Filled.AddCircle,
        unselectedIcon = Icons.Outlined.AddCircleOutline,
    ),
    BottomDestination(
        label = "Lixeira",
        route = TrashRoute,
        routeClass = TrashRoute::class,
        selectedIcon = Icons.Filled.Delete,
        unselectedIcon = Icons.Outlined.Delete,
    ),
)

@Composable
fun BookApp() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = { BookBottomBar(navController, currentDestination) },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BookListRoute,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<BookListRoute> {
                BookListScreen(
                    snackbarHostState = snackbarHostState,
                    onEditBook = { id -> navController.navigate(BookFormRoute(id)) },
                )
            }
            composable<BookFormRoute> { entry ->
                val route = entry.toRoute<BookFormRoute>()
                BookFormScreen(
                    snackbarHostState = snackbarHostState,
                    isEditing = route.bookId != 0L,
                    onSaved = {
                        navController.navigate(BookListRoute) {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onBack = { navController.popBackStack() },
                )
            }
            composable<TrashRoute> {
                TrashScreen(snackbarHostState = snackbarHostState)
            }
        }
    }
}

@Composable
private fun BookBottomBar(
    navController: NavHostController,
    currentDestination: NavDestination?,
) {
    NavigationBar {
        bottomDestinations.forEach { destination ->
            val selected = currentDestination?.hierarchy?.any { it.hasRoute(destination.routeClass) } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = destination.route !is BookFormRoute
                        }
                        launchSingleTop = true
                        restoreState = destination.route !is BookFormRoute
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                        contentDescription = destination.label,
                    )
                },
                label = { Text(destination.label) },
            )
        }
    }
}
