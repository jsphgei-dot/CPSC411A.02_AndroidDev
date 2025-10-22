package com.example.todotask

import android.os.Bundle
import android.os.Parcelable
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
import com.example.todotask.ui.theme.TodoTaskTheme
import kotlinx.parcelize.Parcelize
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController

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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodoScreen()
                }
            }
        }
    }
}

// =================================================================================
// 3. Stateful Parent Composable (View Logic)
// =================================================================================
@Composable
fun TodoScreen() {
    // Context for showing Toast messages
    val context = LocalContext.current

    // State for the text in the input field.
    // `rememberSaveable` ensures it survives configuration changes (e.g., rotation).
    var text by rememberSaveable { mutableStateOf("") }

    // State for the list of all To-Do items.
    // `rememberSaveable` with a custom `listSaver` is used to persist our custom TodoItem list.
    val items = rememberSaveable(
        saver = listSaver(
            save = { stateList ->
                // Don't save if it's not a mutable state list
                if (stateList.isNotEmpty()) {
                    val list = stateList.toList()
                    // Convert each item to a list of its properties
                    list.map { listOf(it.id, it.label, it.isCompleted) }
                } else {
                    emptyList()
                }
            },
            restore = { list ->
                // Restore the list from the saved properties
                list.map {
                    TodoItem(
                        id = it[0] as Int,
                        label = it[1] as String,
                        isCompleted = it[2] as Boolean
                    )
                }.toMutableStateList()
            }
        )
    ) {
        // Initial list is empty
        mutableStateListOf<TodoItem>()
    }


    // --- Event Handlers (Unidirectional Data Flow) ---

    val movieList = {

        //val trimmedText = text.trim()
        //if (trimmedText.isBlank()) {
        //    Toast.makeText(context, "Task name cannot be empty.", Toast.LENGTH_SHORT).show()
        //} else {
        //    // Generate a unique ID (simple approach for this example)
        //    val newId = (items.maxOfOrNull { it.id } ?: 0) + 1
        //    items.add(TodoItem(id = newId, label = trimmedText))
        //    text = "" // Clear the input field
        //}
    }

    val onItemCheckedChange: (TodoItem, Boolean) -> Unit = { item, isChecked ->
        val index = items.indexOf(item)
        if (index != -1) {
            items[index] = item.copy(isCompleted = isChecked)
        }
    }

    val onItemDeleted: (TodoItem) -> Unit = { item ->
        items.remove(item)
    }

    val toHome: () -> Unit = {}

    val toWatchlist: () -> Unit = {}

    val back: () -> Unit = {}

    // --- UI Layout ---

    Column(modifier = Modifier.padding(16.dp)) {
        GenerateMovieRow(
            FALSE
        )
        Spacer(modifier = Modifier.weight(1f))
        BottomBar(
            toHome = toHome,
            toWatchlist = toWatchlist,
            back = back
        )
    }

}

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
fun GenerateMovieList(
    title: String,
    items: List<TodoItem>,
    onItemCheckedChange: (TodoItem, Boolean) -> Unit,
    onItemDeleted: (TodoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(items, key = { it.id }) { item ->

                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                }
            }

    }
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
fun GenerateMovieRow(
    //like a checkmark from todoList
    onFavorited: Boolean?
) {
    var avengersEndgame = Movie(
        title = "Avengers: Endgame",
        description = "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
        image = R.drawable.avengers_endgame,
        rating = 6.7f
    )
    var captainMarvel = Movie(
        "Captain Marvel",
        "Carol Danvers becomes one of the universe's most powerful heroes when Earth is caught in the middle of a galactic war between two alien races.",
        R.drawable.captain_marvel,
        8.4f)
    val myMovieList = mutableListOf<Movie>(avengersEndgame, captainMarvel)
    val indexNum = myMovieList.indexOf(captainMarvel)
    myMovieList[indexNum].isOnWatchlist = TRUE
    val myFavoriteList = myMovieList.filter { it.isOnWatchlist }
    Column(modifier = Modifier) {
        for (i in myMovieList) {
            OutlinedImageTile(i)
        }
        for (i in myMovieList) {
            OutlinedImageTile(i)
        }
    }

}

@Composable
fun OutlinedImageTile(movieItem: Movie) {
    Column(modifier = Modifier) {
        Card(
            modifier = Modifier, // 1. Set the size of the tile
            border = BorderStroke(2.dp, Color.Gray), // 3. Add the outline here
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Optional shadow
        ) {
            Card(
                modifier = Modifier.size(140.dp), // 1. Set the size of the tile
            ) {
                Image(
                    painter = painterResource(id = movieItem.image), // 4. Your image resource
                    contentDescription = "A descriptive text for the image", // Accessibility text
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    contentScale = ContentScale.Crop, // 5. Crop image to fill the card
                )
            }

            Text(
                text = "Rating: ${movieItem.rating}",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = movieItem.title
            )
        }
    }
}
@Composable
fun BottomBar2()
{
    val navController = rememberNavController()
    Scaffold(bottomBar = { TabView(tabBarItems, navController) })
    {
        NavHost(navController = navController, startDestination = homeTab.title) {
            composable(homeTab.title) {
                Text(homeTab.title)
            }
            composable(alertsTab.title) {
                Text(alertsTab.title)
            }
            composable(settingsTab.title) {
                Text(settingsTab.title)
            }
            composable(moreTab.title) {
                MoreView()
            }
        }
    }
}

@Composable
fun TabView(x0: tabBarItems, x1: NavHostController) {
    TODO("Not yet implemented")
}

@Composable
fun BottomBar(
    toHome: () -> Unit,
    toWatchlist: () -> Unit,
    back: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = toHome) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Go Home"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = toWatchlist,
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Go To Watchlist"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = back) {
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
@Preview(showBackground = true)
@Composable
fun TodoScreenPreview() {
    TodoTaskTheme {
        TodoScreen()
    }
}