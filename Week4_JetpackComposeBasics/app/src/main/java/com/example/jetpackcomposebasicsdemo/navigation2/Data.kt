package com.example.jetpackcomposebasicsdemo.navigation2

data class Course(
    val id: String,
    val name: String,
    val lessons: List<String>,
    val imageLink: List<String>
)

val courses = listOf(
    Course("2019", "2019 Movies Top 5",
            listOf("Avengers: Endgame", "The Lion King", "Toy Story 4", "Frozen II", "Captain Marvel"),
        listOf("https://m.media-amazon.com/images/M/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_SY139_CR1,0,92,139_.jpg",
            "https://m.media-amazon.com/images/M/MV5BMjIwMjE1Nzc4NV5BMl5BanBnXkFtZTgwNDg4OTA1NzM@._V1_SY139_CR1,0,92,139_.jpg",
            "https://m.media-amazon.com/images/M/MV5BMTYzMDM4NzkxOV5BMl5BanBnXkFtZTgwNzM1Mzg2NzM@._V1_SY139_CR1,0,92,139_.jpg",
            "https://m.media-amazon.com/images/M/MV5BZTE1YjlmZjctNjIwNi00NDQ0LTlmMzgtYWZkY2RkZTMwNTdmXkEyXkFqcGc@._V1_SY139_CR1,0,92,139_.jpg",
            "https://m.media-amazon.com/images/M/MV5BZDI1NGU2ODAtNzBiNy00MWY5LWIyMGEtZjUxZjUwZmZiNjBlXkEyXkFqcGc@._V1_SY139_CR1,0,92,139_.jpg"
            )
        ),
    Course("2018", "2018 Movies Top 5",
        listOf("Black Panther", "Avengers: Infinity War", "Incredibles 2", "Jurassic World: Fallen Kingdom", "Deadpool 2"),
        listOf("https://m.media-amazon.com/images/M/MV5BMTg1MTY2MjYzNV5BMl5BanBnXkFtZTgwMTc4NTMwNDI@._V1_SY139_CR1,0,92,139_.jpg",
            "https://m.media-amazon.com/images/M/MV5BMjMxNjY2MDU1OV5BMl5BanBnXkFtZTgwNzY1MTUwNTM@._V1_SY139_CR1,0,92,139_.jpg",
            "https://m.media-amazon.com/images/M/MV5BMTEzNzY0OTg0NTdeQTJeQWpwZ15BbWU4MDU3OTg3MjUz._V1_SY139_CR1,0,92,139_.jpg",
            "https://m.media-amazon.com/images/M/MV5BNzIxMjYwNDEwN15BMl5BanBnXkFtZTgwMzk5MDI3NTM@._V1_SY139_CR1,0,92,139_.jpg",
            "https://m.media-amazon.com/images/M/MV5BNGY3N2ZhYmMtYTlmYi00ZWIzLWJiZWMtMjgxMjljYTk3MDAwXkEyXkFqcGc@._V1_SY139_CR1,0,92,139_.jpg")
    )

)


fun getCourse(id: String) = courses.find {it.id == id}