package com.example.jetpackcomposebasicsdemo.navigation2

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

// Home Screen
@Composable
fun HomeScreen(navController: NavController) {

    Column (modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Home Screen")
        Spacer(modifier = Modifier.height(8.dp))

        courses.forEach {
            course ->
            Card (modifier = Modifier.fillMaxWidth().clickable {
                navController.navigate((Routes.course(course.id)))
            }) {
                Text(course.name)
                Spacer(Modifier.height(8.dp))
            }
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


















