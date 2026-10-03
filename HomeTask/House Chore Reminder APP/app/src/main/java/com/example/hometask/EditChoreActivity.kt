package com.example.hometask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Calendar

class EditChoreActivity : AppCompatActivity() {

    private lateinit var repository: ChoreRepository
    private var choreId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_chore)

        repository = ChoreRepository.getInstance(this)

        val choreName = findViewById<EditText>(R.id.edtEditName)
        val description = findViewById<EditText>(R.id.edtEditDescription)
        val assignedTo = findViewById<EditText>(R.id.edtEditAssignedTo)
        val dueDate = findViewById<EditText>(R.id.edtEditDueDate)

        val updateButton = findViewById<Button>(R.id.btnUpdateChore)
        val deleteButton = findViewById<Button>(R.id.btnDeleteChore)

        choreId = intent.getIntExtra("CHORE_ID", -1)

        lifecycleScope.launch {

            val chores = repository.getChores()

            val chore = chores.find { it.id == choreId }

            if (chore != null) {

                choreName.setText(chore.name)
                description.setText(chore.description)
                assignedTo.setText(chore.assignedTo)
                dueDate.setText(chore.dueDate)

                updateButton.setOnClickListener {

                    val updatedChore = chore.copy(
                        name = choreName.text.toString().trim(),
                        description = description.text.toString().trim(),
                        assignedTo = assignedTo.text.toString().trim(),
                        dueDate = dueDate.text.toString().trim()
                    )

                    val scheduler = ChoreNotificationScheduler(this@EditChoreActivity)

                    scheduler.cancelNotification(chore.id)

                    repository.updateChore(updatedChore)

                    scheduler.scheduleNotification(
                        choreId = chore.id,
                        choreName = updatedChore.name,
                        dueDate = updatedChore.dueDate
                    )

                    finish()
                }

                deleteButton.setOnClickListener {

                    val scheduler = ChoreNotificationScheduler(this@EditChoreActivity)

                    scheduler.cancelNotification(chore.id)

                    repository.deleteChore(chore)

                    finish()
                }
            }
        }

        dueDate.setOnClickListener {

            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->

                    val selectedDate =
                        "$selectedDay/${selectedMonth + 1}/$selectedYear"

                    TimePickerDialog(
                        this,
                        { _, selectedHour, selectedMinute ->

                            val formattedTime =
                                String.format(
                                    "%02d:%02d",
                                    selectedHour,
                                    selectedMinute
                                )

                            dueDate.setText(
                                "$selectedDate $formattedTime"
                            )
                        },
                        hour,
                        minute,
                        true
                    ).show()

                },
                year,
                month,
                day
            ).show()
        }
    }
}