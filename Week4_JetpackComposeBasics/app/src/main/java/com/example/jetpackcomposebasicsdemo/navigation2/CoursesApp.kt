// Define the package for this file, which helps organize the code within the project.
package com.example.jetpackcomposebasicsdemo.navigation2

// Import necessary libraries from Jetpack Compose for UI elements and navigation.
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

/**
 * The main composable function that sets up the application's entire navigation graph.
 * This acts as the central router for the app's screens.
 */
@Composable
fun CourseApp() {
    // Creates and remembers a NavController. This controller manages the app's screen stack
    // and allows you to navigate between composables.
    val navController = rememberNavController()

    // NavHost is the container that displays the current screen based on the route.
    NavHost(
        navController = navController,
        startDestination = Routes.HOME // Sets the home screen as the default starting point.
    ) {
        // Defines the route for the "home" screen.
        composable(Routes.HOME) {
            // When the app navigates to Routes.HOME, the HomeScreen composable is displayed.
            HomeScreen(navController)
        }

        // Defines the route for the "course details" screen.
        composable(
            route = Routes.COURSE, // The route pattern, e.g., "course/{courseId}".
            // Specifies that this route expects an argument named "courseId".
            arguments = listOf(navArgument("courseId") {
                type = NavType.StringType // The argument must be a String.
            })
        ) { backStackEntry -> // The lambda gives access to the backStackEntry, which holds the arguments.
            // Safely extracts the "courseId" from the arguments. If it's missing, it defaults to an empty string.
            val id = backStackEntry.arguments?.getString("courseId") ?: ""
            // Renders the CourseScreen, passing the retrieved ID and the NavController.
            CourseScreen(id, navController)
        }

        // Defines the route for the "lessons" screen.
        composable(
            route = Routes.LESSON, // The route pattern, e.g., "course/{courseId}/lesson/{lessonId}".
            // Specifies that this route expects two arguments.
            arguments = listOf(
                navArgument("courseId") { type = NavType.StringType },
                navArgument("lessonId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            // Safely extracts both arguments, providing default values to prevent crashes.
            val id = backStackEntry.arguments?.getString("courseId") ?: ""
            val lessonId = backStackEntry.arguments?.getInt("lessonId") ?: 0
            // Renders the LessonScreen with the extracted data.
            LessonScreen(id, lessonId, navController)
        }
    }
}

/**
 * The composable for the Home Screen. It displays a list of all available courses.
 */
@Composable
fun HomeScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Home Screen")
        Spacer(modifier = Modifier.height(8.dp))

        // Iterates through a predefined list of courses.
        courses.forEach { course ->
            // Displays each course in a Card that can be clicked.
            Card(modifier = Modifier.fillMaxWidth().clickable {
                // When clicked, navigate to the specific course's detail screen
                // by passing its ID in the route.
                navController.navigate(Routes.course(course.id))
            }) {
                Text(course.name, modifier = Modifier.padding(16.dp))
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

/**
 * The composable for the Course Details Screen. It displays the lessons for a specific course.
 */
@Composable
fun CourseScreen(courseId: String, navController: NavController) {
    // Fetches the specific course's data using the provided courseId.
    val course = getCourse(courseId)

    // A safety check: if the courseId is invalid, display an error message and stop.
    if (course == null) {
        Text("Course is not found")
        return
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(course.name)
        Spacer(Modifier.height(8.dp))

        // Iterates through the lessons of the fetched course.
        course.lessons.forEachIndexed { index, lesson ->
            // Displays each lesson in a clickable Card.
            Card(modifier = Modifier.fillMaxWidth().clickable {
                // When a lesson is clicked, navigate to the LessonScreen,
                // passing both the courseId and the lesson's index (as lessonId).
                navController.navigate(Routes.lesson(courseId, index))
            }) {
                Text("${lesson}", modifier = Modifier.padding(16.dp))
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

/**
 * The composable for the Lesson Screen. Displays the content of a single lesson.
 */
@Composable
fun LessonScreen(courseId: String, lessonId: Int, navController: NavController) {
    // Fetches the course data to access the specific lesson.
    val course = getCourse(courseId)

    // Safety check for invalid course or lesson IDs.
    if (course == null || lessonId < 0 || lessonId >= course.lessons.size) {
        Text("Movie not found")
        return
    }

    // Retrieves the specific lesson's content from the course data.
    val lesson = course.lessons[lessonId]

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // A back button that navigates to the previous screen in the back stack.
        IconButton({ navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBack, "Back")
        }

        Text("Movie ${lessonId + 1}:")
        Spacer(Modifier.height(8.dp))
        Text(lesson)
        Spacer(Modifier.height(8.dp))

        // A Row to hold the "Previous" and "Next" buttons.
        Row(modifier = Modifier.fillMaxWidth()) {
            // The "Previous" button is only shown if it's not the first lesson.
            if (lessonId > 0) {
                Button({
                    // Navigates to the previous lesson.
                    navController.navigate(Routes.lesson(courseId, lessonId - 1)) {
                        // This removes the current lesson screen from the back stack,
                        // creating a cleaner navigation history (i.e., back goes to the course list,
                        // not the previous lesson).
                        popUpTo(Routes.lesson(courseId, lessonId)) { inclusive = true }
                    }
                }) {
                    Text("Previous")
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Fills the space between buttons.

            // The "Next" button is only shown if it's not the last lesson.
            if (lessonId < course.lessons.size - 1) {
                Button({
                    // Navigates to the next lesson.
                    navController.navigate(Routes.lesson(courseId, lessonId + 1)) {
                        // Same popUpTo logic to remove the current screen from the back stack.
                        popUpTo(Routes.lesson(courseId, lessonId)) { inclusive = true }
                    }
                }) {
                    Text("Next")
                }
            }
        }
    }
}