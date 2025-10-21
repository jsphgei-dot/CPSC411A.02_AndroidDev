package com.example.todotask

import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.remember
import androidx.navigation.navigation
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.todotask.ui.theme.TodoTaskTheme
import kotlinx.parcelize.Parcelize


// =================================================================================
// 1. Data Model
// =================================================================================

/**
 * Represents a single To-Do item.
 * @param id A unique identifier for the item.
 * @param label The text content of the task.
 * @param isCompleted Whether the task is marked as complete.
 *
 * It's Parcelable to be easily saved and restored by `rememberSaveable`.
 */
@Parcelize
data class TodoItem(
    val id: Int,
    val label: String,
    val isCompleted: Boolean = false
) : Parcelable


// =================================================================================
// 2. Main Activity
// =================================================================================
@Composable
fun MovieApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "mainFlow"
    ) {
        // wip figuring how to pass an identifier for the movie when we navigate to details
        //composable("movieDetails/{movieIndex}") { backStackEntry ->
        //    val movieId = backStackEntry.arguments?.getString("movieName") ?: return@composable
        //    DetailsScreen(movieName)
        //}



        navigation(startDestination = "home", "mainFlow"){
            composable("home") { backStackEntry ->
                val mainFlowEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("mainFlow")
                }

                val movieViewModel: MovieViewModel = viewModel(mainFlowEntry)


                HomeScreen(navController, movieViewModel)
            }

            composable("watchList") {backStackEntry ->
                val mainFlowEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("mainFlow")
                }
                val movieViewModel: MovieViewModel = viewModel(mainFlowEntry)


                WatchListScreen(navController, movieViewModel)
            }

        }

    }

}



// =================================================================================
// 3. Stateful Parent Composable (View Logic)
// =================================================================================
//@Composable

/*      i think we dont need this anymore - chris
fun TodoScreen() {
    // Context for showing Toast messages
    val context = LocalContext.current

    // State for the text in the input field.
    // `rememberSaveable` ensures it survives configuration changes (e.g., rotation).
    var text by rememberSaveable { mutableStateOf("") }


    val toHome: () -> Unit = {}

    val toWatchlist: () -> Unit = {}

    val back: () -> Unit = {}

    // --- UI Layout ---

    Column(modifier = Modifier.padding(16.dp)) {
        //MovieList(
            //title:
        //)
        Spacer(modifier = Modifier.weight(1f))
        BottomBar(
            toHome = toHome,
            toWatchlist = toWatchlist,
            back = back
        )
    }
}
*/
// =================================================================================
// 4. Stateless Child Composables (UI Components)
// =================================================================================

/**
 * A stateless composable for the text input and "Add" button.
 * State is hoisted to the parent (`TodoScreen`).
 */


/**
 * A stateless composable that displays a section header and a list of To-Do items.
 */
@Composable
fun MovieListSection(
    title: String,
    items: List<TodoItem>,
    onItemCheckedChange: (TodoItem, Boolean) -> Unit,
    onItemDeleted: (TodoItem) -> Unit,
    modifier: Modifier = Modifier,
    movieViewModel: MovieViewModel
) {
    Column(modifier = modifier) {

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(items, key = { it.id }) { item ->
                    TodoItemRow(
                        item = item,
                        onCheckedChange = { isChecked -> onItemCheckedChange(item, isChecked) },
                        onDelete = { onItemDeleted(item) }
                    )
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                }
            }

    }
}

@Composable
fun HomeScreen(             // the home screen of the app
    navController: NavController,
    movieViewModel: MovieViewModel,
) {

    BottomBar(navController)    // creates the navigation bar bar


    // content
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("you are on the home screen")


    }

}

@Composable
fun WatchListScreen(                        // Watchlist screen, same as homescreen just with the viewmodel filtered
    navController: NavController,         // to elements with the onWatchList attribute set to true
    movieViewModel: MovieViewModel
) {

    BottomBar(navController)    // creates the navigation bar bar


    // content
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("you are on the watchlist screen")


    }

}

@Composable
fun DetailsScreen(
    navController: NavController,
    movieViewModel: MovieViewModel
) {

}

/**
 * A stateless composable for displaying a single row in the To-Do list.
 */
@Composable
fun TodoItemRow(
    item: TodoItem,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isCompleted,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = item.label,
            fontSize = 18.sp,
            textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
            color = if (item.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(0.dp))
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete task"
            )
        }
    }
}

@Composable
fun OutlinedImageTile() {
    Card(
        modifier = Modifier.size(150.dp), // 1. Set the size of the tile
        shape = RoundedCornerShape(16.dp), // 2. Define the shape with rounded corners
        border = BorderStroke(2.dp, Color.Gray), // 3. Add the outline here
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Optional shadow
    ) {
        Image(
            painter = painterResource(id = R.drawable.captain_marvel), // 4. Your image resource
            contentDescription = "A descriptive text for the image", // Accessibility text
            contentScale = ContentScale.FillHeight, // 5. Crop image to fill the card
            modifier = Modifier.fillMaxSize() // Make image fill the whole card
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OutlinedImageTilePreview() {
    // We use a Box to center the tile for the preview
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        OutlinedImageTile()
    }
}

@Composable         // creates the bottom bar of buttons for navigating the app
fun BottomBar(
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.popBackStack("home", inclusive = false) }) { // use pop backstack since home is the start page
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Go Home"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { navController.navigate("watchList") },
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Go To Watchlist"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { navController.popBackStack() }) {        // go back 1 page in the stack
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Go Back"
            )
        }
    }
}

// =================================================================================
// 5. Preview
// =================================================================================
/*
@Preview(showBackground = true)
@Composable
fun TodoScreenPreview() {
    TodoTaskTheme {
        TodoScreen()
    }
}*/