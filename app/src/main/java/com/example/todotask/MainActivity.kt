package com.example.todotask

import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign

import com.example.todotask.ui.theme.TodoTaskTheme
import kotlinx.coroutines.android.awaitFrame
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoTaskTheme {
                MovieApp()
            }
        }
    }
}

@Composable
fun MovieApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "mainFlow"
    ) {

        composable("movieDetails/{movieID}") { backStackEntry ->
            val mainFlowEntry = remember(backStackEntry) {
                navController.getBackStackEntry("mainFlow")
            }
            val movieIndex = backStackEntry.arguments?.getString("movieID")?.toIntOrNull()
            val movieViewModel: MovieViewModel = viewModel(mainFlowEntry)

            if (movieIndex != null) {
                DetailsScreen(navController, movieViewModel, movieIndex)
            } else {
                // toast an error maybe
            }

        }



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
}*/

@Composable
fun HomeScreen(             // the home screen of the app
    navController: NavController,
    movieViewModel: MovieViewModel,
) {

    Scaffold(       // Top and bottom bar setup
        bottomBar = { BottomBar(navController) },
        topBar = {Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) ,
            horizontalArrangement = Arrangement.Center

        ) {
            Text("Home",
                fontSize = 36.sp)

        }
        }

        //Page Content
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SetTiles(navController, movieViewModel)
        }
    }

}

@Composable
fun WatchListScreen(                        // Watchlist screen, same as homescreen just with the viewmodel filtered
    navController: NavController,         // to elements with the onWatchList attribute set to true
    movieViewModel: MovieViewModel
) {

    Scaffold(       // Top and bottom bar setup
        bottomBar = { BottomBar(navController) },
        topBar = {Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) ,
            horizontalArrangement = Arrangement.Center

        ) {
            Text("Watch List",
                fontSize = 36.sp)

        }
        }

        //Page Content
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SetTiles(navController, movieViewModel, true)
        }
    }


}

@Composable
fun DetailsScreen(
    navController   : NavController,
    movieViewModel  : MovieViewModel,
    movieIndex      : Int
) {
    val movieList               =   movieViewModel.movieList
    val currentMovie : Movie    =   movieList[movieIndex]
    Scaffold(       // Top and bottom bar setup
        bottomBar = { BottomBar(navController) },
        topBar = {Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) ,
            horizontalArrangement = Arrangement.Center

        ) {
            Text(currentMovie.title,
                fontSize = 36.sp)

        }
        }

        //Page Content
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = movieViewModel.movieList[movieIndex].image),
                    contentDescription = "A descriptive text for the image",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
                Text("Rating: ${currentMovie.rating.coerceIn(0,5)}/5")
                Text("Description: ${currentMovie.desc}",
                fontSize = 20.sp,
                textAlign = TextAlign.Left
                )
            }

            // watchlisted checkbox
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Watch Listed")
                Checkbox(
                    checked = currentMovie.isOnWatchList,
                    onCheckedChange = { isChecked ->
                        movieViewModel.updateMovie(currentMovie, movieIndex, isChecked)
                    }
                )
            }
        }


    }
}




@Composable
fun SetTiles(   // sets the tiles
    navController: NavController,
    movieViewModel: MovieViewModel,
    watchList     : Boolean? = null
) {
    val movies = if (watchList == true) {
        movieViewModel.movieList.filter { it.isOnWatchList }
    } else
        movieViewModel.movieList



    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 👈 2 columns
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(movies.size) { index ->
            val currentMovie = movies[index]
            OutlinedImageTile(navController, movieViewModel, currentMovie.movieID)
        }
    }
}

@Composable
fun OutlinedImageTile(
    navController   : NavController,
    movieViewModel  : MovieViewModel,
    movieID           : Int
) {
    Card(
        modifier = Modifier
            .clickable {
                navController.navigate("movieDetails/$movieID") // pass index
    },
        border = BorderStroke(2.dp, Color.Gray), // 3. Add the outline here
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Optional shadow
    ) {
        Card(
            modifier = Modifier.size(140.dp), // 1. Set the size of the tile
        ) {
            var myImage: Int = movieViewModel.movieList[movieID].image
            Image(
                painter = painterResource(id = myImage), // 4. Your image resource
                contentDescription = "A descriptive text for the image", // Accessibility text
                modifier = Modifier.size(140.dp), // 2. Set the size of the image
                contentScale = ContentScale.FillBounds
            )
        }
        Text(
            text = "Rating: ${movieViewModel.movieList[movieID].rating}",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = movieViewModel.movieList[movieID].title
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
        //OutlinedImageTile()
    }
}

@Composable         // creates the bottom bar of buttons for navigating the app
fun BottomBar(
    navController: NavController
) {
    val currentBackStackEntry = navController.currentBackStackEntry
    val currentRoute = currentBackStackEntry?.destination?.route
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {navController.navigate("home") {
            launchSingleTop = true
            restoreState = true
            popUpTo("mainFlow") { inclusive = false }
        }   }
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Go Home"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { navController.navigate("watchList") {
                launchSingleTop = true
                restoreState = true
                popUpTo("mainFlow") { inclusive = false }
            } },
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Go To Watchlist"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            if (currentRoute == "home") {
            } else if(currentRoute == "watchList") {
                navController.navigate("home") {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo("mainFlow") { inclusive = false }
                }
            }
            else {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                }
            }}) {
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