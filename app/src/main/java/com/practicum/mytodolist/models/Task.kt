package com.practicum.mytodolist.models

enum class TaskCategory {
    WORK, PERSONAL, SHOPPING, HEALTH, NONE
}

// Data class - специальный класс для хранени данных
data class Task(
    val id: Long = System.currentTimeMillis(),       // Уникальный идентификатор
    var title: String, // Название задачи
    var isCompleted: Boolean = false, // Выполнена ли задача
    val createdAt: Long = System.currentTimeMillis(), // Время создания
    val category: TaskCategory = TaskCategory.NONE
)
