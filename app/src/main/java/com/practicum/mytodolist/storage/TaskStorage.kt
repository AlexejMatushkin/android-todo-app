package com.practicum.mytodolist.storage

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.mytodolist.models.Task

class TaskStorage(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "todo_app"
        private const val PREFS_KEY = "todo_tasks"
    }

    // Единый экземпляр Gson для всего класса
    private val gson = Gson()

    // Единый TypeToken для избежания создания каждый раз
    private val taskListType = object : TypeToken<List<Task>>() {}.type

    // Единый экземпляр SharedPreferences
    private val prefs by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveTasks(tasks: List<Task>) {
        prefs.edit {
            val jsonString = gson.toJson(tasks) // Используем существующий gson
            putString(PREFS_KEY, jsonString)
        }
    }

    fun loadTasks(): List<Task> {
        return try {
            val jsonString = prefs.getString(PREFS_KEY, null) ?: return emptyList()
            gson.fromJson(jsonString, taskListType) ?: emptyList() // Используем существующий type
        } catch (e: Exception) {
            emptyList() // Безопасное возвращение при ошибках
        }
    }
}