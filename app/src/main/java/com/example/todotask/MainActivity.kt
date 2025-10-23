package com.example.todotask

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todotask.Routes.myFavoriteList
import com.example.todotask.Routes.myMovieList

// =================================================================================
// 1. Data Model
// =================================================================================
data class Movie(
    var title: String,
    var description: String,
    var image: Int,
    var rating: Float,
    var isOnWatchlist: Boolean = FALSE
)
object Routes {
    const val HOME = "home"
    const val WATCHLIST = "watchlist"
    const val MOVIE_DETAILS = "movie_details/{movieId}"

    fun movie_details(movieId: String) = "movie_details/$movieId"

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
    val myFavoriteList = myMovieList.filter { it.isOnWatchlist }
}
fun getMovie(id: String) = myMovieList.find {it.title == id}
fun getFavorite(id: String) = Routes.myFavoriteList.find {it.title == id}
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
                    MovieApp()
                }
            }
        }
    }
}
@Composable
fun MovieApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        Routes.HOME
    ) {
        //home
        composable(Routes.HOME) {
            HomeScreen(navController)
        }

        //watchlist page
        composable(Routes.WATCHLIST,
            listOf(navArgument(
                "movieId"
            ) {type = NavType.StringType}) )
        {
            Watchlist(navController)
        }
    }
}

@Composable
fun MovieDetails(id: String, navController: NavHostController) {

}

// Home Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Home Screen")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.HOME) }) {
                        Icon(Icons.Default.Home, contentDescription = "Localized description")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { navController.navigate(Routes.WATCHLIST) }) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Localized description",
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Localized description",
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LazyColumn(modifier = Modifier) {
                items(myMovieList) { movie ->
                    OutlinedImageTile(
                        movieItem = movie,
                        modifier = Modifier.padding(8.dp),
                        navController
                    )
                }
            }
        }
    }
    //////////////////////////////////////////////////////////////
    //Column (modifier = Modifier.fillMaxSize().padding(16.dp)) {
    //    Text("Home Screen")
    //    Spacer(modifier = Modifier.height(8.dp))
//
    //    courses.forEach {
    //            course ->
    //        Card (modifier = Modifier.fillMaxWidth().clickable {
    //            navController.navigate()
    //        }) {
    //            Text(course.name)
    //            Spacer(Modifier.height(8.dp))
    //        }
    //    }
//
    //}

}

// Home Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Watchlist(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Home Screen")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.HOME) }) {
                        Icon(Icons.Default.Home, contentDescription = "Localized description")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { navController.navigate(Routes.WATCHLIST) }) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Localized description",
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Localized description",
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LazyColumn(modifier = Modifier) {
                items(myFavoriteList) { movie ->
                    OutlinedImageTile(
                        movieItem = movie,
                        modifier = Modifier.padding(8.dp),
                        navController = navController
                    )
                }
            }
        }
    }
}
@Composable
fun FavoriteList(favoriteList: List<Movie>, modifier: Modifier = Modifier, navController: NavController) {

}

@Composable
fun OutlinedImageTile(movieItem: Movie, modifier: Modifier = Modifier, navController: NavController) {
    Column(modifier = Modifier) {
        Card(
            modifier = Modifier.clickable {
                navController.navigate("movie_details/${movieItem.title}")
            }, // 1. Set the size of the tile
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
fun BottomAppBarExample(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.HOME) }) {
                        Icon(Icons.Default.Home, contentDescription = "Localized description")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { navController.navigate(Routes.WATCHLIST) }) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Localized description",
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Localized description",
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Text(
            modifier = Modifier.padding(innerPadding),
            text = "Example of a scaffold with a bottom app bar."
        )
    }
}
//////////////////////////////////////////////////////////////////////
/*
@Composable
fun CourseApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        Routes.HOME
    ) {
        //home
        composable(Routes.HOME) {
            HomeScreen(navController)
        }

        //course details
        composable(Routes.COURSE,
            listOf(navArgument(
                "courseId"
            ) {type = NavType.StringType}) )
        {

                backStackEntry ->
            val id = backStackEntry.arguments?.getString("courseId") ?: ""

            CourseScreen(id, navController)


        }

        //lessons screen
        composable(Routes.LESSON,
            listOf(
                navArgument("courseId") {type = NavType.StringType},
                navArgument("lessonId") {type = NavType.IntType}
            ) )
        { backStackEntry ->
            val id = backStackEntry.arguments?.getString("courseId") ?: ""
            val lessonId = backStackEntry.arguments?.getInt("lessonId") ?: 0
            LessonScreen(id, lessonId, navController)


        }


    }
}



// Course Details Screen
@Composable
fun CourseScreen(courseId: String, navController: NavController) {
    // fetch the data
    val course = getCourse(courseId)

    if(course == null) {
        Text("Course is not found")
        return
    }

    Column (modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(course.name)
        Spacer(Modifier.height(8.dp))

        Text("Lessons:")
        Spacer(Modifier.height(8.dp))

        course.lessons.forEachIndexed {
                index, lesson ->
            Card (
                modifier = Modifier.fillMaxWidth().clickable {
                    navController.navigate((Routes.lesson(courseId, index)))
                }
            ) {
                Text("Lesson ${index + 1}:")
                Spacer(Modifier.height(8.dp))

            }
        }



    }

}

// Lessons Screen
@Composable
fun LessonScreen(courseId: String, lessonId: Int, navController: NavController) {
    // fetch the data
    val course = getCourse(courseId)

    if(course  == null) {
        Text("Lesson not found")
        return
    }

    val lesson = course.lessons[lessonId]

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        IconButton({navController.popBackStack()}) {
            Icon(Icons.Default.ArrowBack, "Back")
        }

        Text("Lesson ${lessonId + 1}:")
        Spacer(Modifier.height(8.dp))

        Text(lesson)
        Spacer(Modifier.height(8.dp))

        Row (modifier = Modifier.fillMaxWidth()) {

            if(lessonId > 0) {
                Button({
                    navController.navigate(Routes.lesson(courseId, lessonId - 1)) {
                        popUpTo(Routes.lesson(courseId, lessonId)) {inclusive = true}
                    }
                }) {
                    Text("Previous")
                }
            }

            if(lessonId < course.lessons.size - 1) {
                Button({
                    navController.navigate(Routes.lesson(courseId, lessonId + 1)){
                        popUpTo(Routes.lesson(courseId, lessonId)) {inclusive = true}
                    }
                }) {
                    Text("Next")
                }
            }


        }
    }




}

data class Course(
    val id: String,
    val name: String,
    val lessons: List<String>
)

val courses = listOf(
    Course("java", "Java Basics",
        listOf("Variables", "Functions", "Data types", "Classes", "Inheritance")
    ),
    Course("kotlin", "Kotlin Basics",
        listOf("Variables", "Functions", "Data types", "Classes", "Inheritance")
    )

)


fun getCourse(id: String) = courses.find {it.id == id}


*/













