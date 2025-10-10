package com.example.jetpackcomposebasicsdemo.assignment1solution

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// TODO DEMO

data class TodoItem(val id: Int, val label: String, var isComplete: Boolean)

@Composable
fun ToDoItemDisplay(item: TodoItem,
                    onCheckedChange: (Boolean) -> Unit = {},
                    onClosed: () -> Unit = {}
) {
    Row (modifier = Modifier.fillMaxWidth().border(2.dp, Color.Cyan).padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(item.label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )

        Checkbox(checked = item.isComplete, onCheckedChange = onCheckedChange)


        // need a close icon
        IconButton (onClick = onClosed) {
            Icon(painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel), contentDescription = "Close", modifier = Modifier.size(30.dp))

        }


    }
}

@Composable
fun ToDo(modifier: Modifier) {
    val todoList = rememberSaveable { mutableStateListOf<TodoItem>() }
    var itemId by rememberSaveable { mutableStateOf(0) }
    val completedList = rememberSaveable { mutableStateListOf<TodoItem>() }

    Column (
        modifier = modifier

    ) {
        Text("TODO List", modifier = Modifier.padding(10.dp),
            fontSize = 30.sp,
            style = MaterialTheme.typography.headlineLarge
        )

        TodoHeader({
                label -> todoList.add(createTodoItem(label, false))
        })

        if (todoList.isNotEmpty()){
            Text("Items", modifier = Modifier.padding(10.dp), fontSize = 25.sp, style = MaterialTheme.typography.headlineMedium)

            Column (modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for(item in todoList) {
                    ToDoItemDisplay(item, {
                        item.isComplete = true
                        todoList.remove(item)
                        completedList.add(item)
                    },
                        {
                            todoList.remove(item)
                        }
                    )
                }
            }
        }


        if(completedList.isNotEmpty()) {
            Column (modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Completed Items", modifier = Modifier.padding(10.dp), fontSize = 25.sp, style = MaterialTheme.typography.headlineMedium)
                for(item in completedList) {
                    ToDoItemDisplay(item, {
                        item.isComplete = false
                        completedList.remove(item)
                        todoList.add(item)
                    },
                        {
                            completedList.remove(item)
                        }

                    )
                }
            }
        }


    }
}

@Composable
fun TodoHeader(
    onAdd: (String) -> Unit
) {
    var label by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    Row (modifier = Modifier.padding(10.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = label,
            onValueChange = {label = it},
            placeholder = {Text("Enter the task name")},
            singleLine = true
        )

        Button(onClick = {
            if(label.isNotBlank()){
                onAdd(label.trim())
                label = ""
            }else{
                Toast.makeText(context, "Please enter a task", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Add")
        }
    }
}

fun createTodoItem(label: String, isComplete: Boolean): TodoItem {
    val item: TodoItem = TodoItem(1, label, isComplete)
    return item
}



