package com.practicum.mytodolist

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.mytodolist.adapters.TasksAdapter
import com.practicum.mytodolist.models.Task

class MainActivity : AppCompatActivity() {

    // Объявляем переменные для наших view-элементов
    private lateinit var taskInput: EditText
    private lateinit var addButton: Button
    private lateinit var recyclerView: RecyclerView

    // Список для хранения задач
    private val tasks = mutableListOf<Task>()

    // Адаптер для RecyclerView (связывает данные и view)
    private lateinit var adapter: TasksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализируем view-элементы
        initViews()

        // Настраиваем RecyclerView
        setupRecyclerView()

        // Настраиваем обработчики кликов
        setupClickListeners()
    }

    // Функция для инициализации view-элементов
    private fun initViews() {
        taskInput = findViewById(R.id.taskInput)
        addButton = findViewById(R.id.addButton)
        recyclerView = findViewById(R.id.tasksRecyclerView)
    }

    // Функция для настройки RecyclerView
    private fun setupRecyclerView() {
        // LinearLayoutManager - располагает элементы вертикально
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Создаем адаптер
        adapter = TasksAdapter(
            tasks,
            onTaskClick = { task, position ->
                showEditDialog(task, position)
            },
            onTaskLongCLick = { task, position ->
                showDeleteDialog(task, position)
            }
        )
        recyclerView.adapter = adapter
    }

    // Функция для настройки обработчиков кликов
    private fun setupClickListeners() {
        addButton.setOnClickListener {
            addNewTask()
        }

        // Обработчик нажатия Enter
        taskInput.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                addNewTask()
                return@setOnKeyListener true // Говорим, что обработали событие
            }
            return@setOnKeyListener false // Не обрабатываем другие клавиши
        }
    }

    // Функция для добавления новой задачи
    private fun addNewTask() {
        val title = taskInput.text.toString().trim()

        if (title.isNotEmpty()) {
            // Создаем новую задачу
            val newTask = Task(
                id = tasks.size + 1, // Простой способ генерации ID
                title = title
            )

            // Добавляем задачу в список
            tasks.add(newTask)

            // Уведомляем адаптер о новом элементе
            adapter.notifyItemInserted(tasks.size - 1)

            // Автоматическая прокрутка к новой задаче
            recyclerView.scrollToPosition(tasks.size - 1)

            // Очищаем поле ввода
            taskInput.text.clear()

            // Скрываем клавиатуру
            hideKeyboard()
            Toast.makeText(
                this,
                "Задача добавлена",
                Toast.LENGTH_SHORT).show()
        } else {
            // Показываем сообщение если поле пустое
            Toast.makeText(
                this,
                "Введите задачу",
                Toast.LENGTH_SHORT).show()
        }
    }

    // Функция для скрытия клавиатуры
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(taskInput.windowToken, 0)
    }

    // Функция для показа диалога подтверждения удаления
    private fun showDeleteDialog(task: Task, position: Int) {
        val builder = android.app.AlertDialog.Builder(this)

        builder.setTitle("Удалить задачу?")
        builder.setMessage("Вы уверены, что хотите удалить '${task.title}'?")

        builder.setPositiveButton("Удалить") { dialog, which ->
            if (position != -1) {
                tasks.removeAt(position)
                adapter.notifyItemRemoved(position)
                Toast.makeText(
                    this,
                    "Задача удалена",
                    Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Отмена") { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    // Функция для показа диалога редактирования задачи
    private fun showEditDialog(task: Task, position: Int) {
        val builder = android.app.AlertDialog.Builder(this)

        val editText = EditText(this).apply {
            setText(task.title)
            setSelection(task.title.length)
        }

        builder.setTitle("Редактировать задачу")
        builder.setView(editText)

        builder.setPositiveButton("Сохранить") { dialog, which ->
            val newTitle = editText.text.toString().trim()
            if (newTitle.isNotEmpty()) {
                task.title = newTitle
                adapter.notifyItemChanged(position)
                Toast.makeText(
                    this,
                    "Задача обновлена",
                    Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Отмена") { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }
}
