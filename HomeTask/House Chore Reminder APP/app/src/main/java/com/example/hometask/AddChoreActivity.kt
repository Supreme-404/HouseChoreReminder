package com.example.hometask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Calendar

class AddChoreActivity : AppCompatActivity() {

    private lateinit var repository: ChoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_chore)

        repository = ChoreRepository.getInstance(this)

        val choreName = findViewById<EditText>(R.id.edtChoreName)
        val description = findViewById<EditText>(R.id.edtDescription)
        val assignedTo = findViewById<EditText>(R.id.edtAssignedTo)
        val dueDate = findViewById<EditText>(R.id.edtDueDate)

        val priority = findViewById<Spinner>(R.id.spinnerPriority)

        val saveButton = findViewById<Button>(R.id.btnSaveChore)
        val cancelButton = findViewById<Button>(R.id.btnCancel)

        val priorities = arrayOf(
            "Low",
            "Medium",
            "High"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            priorities
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        priority.adapter = adapter

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

        saveButton.setOnClickListener {

            val name = choreName.text.toString().trim()
            val desc = description.text.toString().trim()
            val person = assignedTo.text.toString().trim()
            val date = dueDate.text.toString().trim()
            val selectedPriority = priority.selectedItem.toString()

            if (name.isEmpty()) {

                Toast.makeText(
                    this,
                    "Please enter a chore name",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            lifecycleScope.launch {

                val choreId = repository.addChore(
                    name = name,
                    description = desc,
                    assignedTo = person,
                    dueDate = date,
                    priority = selectedPriority
                )

                val scheduler = ChoreNotificationScheduler(this@AddChoreActivity)

                scheduler.scheduleNotification(
                    choreId = choreId.toInt(),
                    choreName = name,
                    dueDate = date
                )

                Toast.makeText(
                    this@AddChoreActivity,
                    "Chore saved successfully",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }
}