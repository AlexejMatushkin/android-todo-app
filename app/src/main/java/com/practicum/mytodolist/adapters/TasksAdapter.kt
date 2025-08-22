package com.practicum.mytodolist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.mytodolist.R
import com.practicum.mytodolist.models.Task

// Создаем адаптер для RecyclerView
class TasksAdapter(
    private val tasks: MutableList<Task>,
    private val onTaskClick: (Task, Int) -> Unit,
    private val onTaskLongCLick: (Task, Int) -> Unit
) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    // ViewHolder - хрвнит ссылки на view-элементы одного элемента списка
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.taskTitle)
        val checkbox: CheckBox = itemView.findViewById(R.id.taskCheckbox)
    }

    // Создает новый ViewHolder когда это нужно
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // "Надуваем" layout для одного элемента из списка
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    // Связывает данные с ViewHolder'ом
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        // Устанавливаем заголовок задачи
        holder.titleTextView.text = task.title

        // Устанавливаем состояние checkbox'a
        holder.checkbox.isChecked = task.isCompleted

        // Обработчик изменения состояния checkbox'a
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
        }

        // Обработка обычного клика - для редактирования
        holder.itemView.setOnClickListener {
            onTaskClick(task, position)
        }

        // Обработка долгого нажатия - для удаления
        holder.itemView.setOnLongClickListener {
            onTaskLongCLick(task, position)
            true
        }
    }

    // Возвращает количество элементов в списке
    override fun getItemCount(): Int {
        return tasks.size
    }
}
