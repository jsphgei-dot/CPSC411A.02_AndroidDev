package com.example.jetpackcomposebasicsdemo.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ToDoViewModel: ViewModel() {
    private var nextId = mutableStateOf(0)

    val todoList = mutableStateListOf<TodoItem>()
    val completedList = mutableStateListOf<TodoItem>()

    private val totalTodosCreated = nextId.value - 1;

    fun getTotalTodosCreated(): Int {
        return totalTodosCreated
    }

    fun addTodo(label: String) {
        if(label.isNotBlank() && checkDuplicate(label.trim())) {
            val newTodo = TodoItem(nextId.value++, label.trim(), false)
            todoList.add(newTodo)
        }
    }

    fun todoItemCounter(): Int {
        return todoList.size
    }

    fun completedItemCounter(): Int {
        return completedList.size
    }

    fun markComplete(item: TodoItem) {
        todoList.remove(item)
        completedList.add(item)
    }

    fun markIncomplete(item: TodoItem) {
        completedList.remove(item)
        todoList.add(item)
    }

    private fun checkDuplicate(label: String): Boolean {
        for(item in todoList) {
            if(item.label.lowercase() == label.lowercase()) {
                return false
            }
        }

        return true
    }

}


data class TodoItem(val id: Int, val label: String, var isComplete: Boolean)