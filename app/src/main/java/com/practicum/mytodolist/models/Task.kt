package com.practicum.mytodolist.models

// Data class - специальный класс для хранени данных
data class Task(
    val id: Int,       // Уникальный идентификатор
    var title: String, // Название задачи
    var isCompleted: Boolean = false, // Выполнена ли задача
    val createdAt: Long = System.currentTimeMillis() // Время создания
)
