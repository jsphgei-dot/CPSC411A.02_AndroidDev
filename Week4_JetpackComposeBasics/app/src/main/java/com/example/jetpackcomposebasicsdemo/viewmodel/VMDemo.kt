package com.example.jetpackcomposebasicsdemo.viewmodel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel


class CounterViewModel: ViewModel() {
    var counter by mutableStateOf(0)
        private set

    fun increment() {
        counter++
    }

    fun decrement() {
        counter--
    }
}


@Composable
fun CountScreen(viewModel: CounterViewModel = viewModel()) {
    Column {
        Text(("Count: ${viewModel.counter}"))

        Row {
            Button(onClick = {viewModel.increment()}) {
                Text("Increment")
            }

            Button(onClick = {viewModel.decrement()}) {
                Text("Decrement")
            }


        }
    }
}


