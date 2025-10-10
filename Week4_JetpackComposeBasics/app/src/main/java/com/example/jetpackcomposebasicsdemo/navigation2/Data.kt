package com.example.jetpackcomposebasicsdemo.navigation2

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