package com.example.hometask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MyChoresActivity : AppCompatActivity() {

    private lateinit var choresContainer: LinearLayout
    private lateinit var repository: ChoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_my_chores)

        choresContainer = findViewById(R.id.choresContainer)

        repository = ChoreRepository.getInstance(this)

        displayChores()
    }

    override fun onResume() {
        super.onResume()

        displayChores()
    }

    private fun displayChores() {

        lifecycleScope.launch {

            val chores = repository.getChores()

            choresContainer.removeAllViews()

            if (chores.isEmpty()) {

                val emptyText = TextView(this@MyChoresActivity)

                emptyText.text = "No chores added yet."
                emptyText.textSize = 18f

                choresContainer.addView(emptyText)

                return@launch
            }

            for (chore in chores) {

                val choreLayout = LinearLayout(this@MyChoresActivity)

                choreLayout.orientation = LinearLayout.VERTICAL
                choreLayout.setPadding(20, 20, 20, 20)

                val choreText = TextView(this@MyChoresActivity)

                val status = if (chore.completed) {
                    "Completed"
                } else {
                    "Pending"
                }

                choreText.text =
                    "${chore.name}\n" +
                            "Assigned to: ${chore.assignedTo}\n" +
                            "Due: ${chore.dueDate}\n" +
                            "Priority: ${chore.priority}\n" +
                            "Status: $status"

                choreText.textSize = 16f

                val editButton = Button(this@MyChoresActivity)
                editButton.text = "Edit"

                editButton.setOnClickListener {

                    val intent = Intent(
                        this@MyChoresActivity,
                        EditChoreActivity::class.java
                    )

                    intent.putExtra("CHORE_ID", chore.id)

                    startActivity(intent)
                }

                val completeButton = Button(this@MyChoresActivity)
                completeButton.text = "Complete"

                completeButton.setOnClickListener {

                    val scheduler = ChoreNotificationScheduler(this@MyChoresActivity)

                    scheduler.cancelNotification(chore.id)

                    repository.completeChore(chore.id)

                    displayChores()
                }

                val deleteButton = Button(this@MyChoresActivity)
                deleteButton.text = "Delete"

                deleteButton.setOnClickListener {

                    repository.deleteChore(chore)

                    displayChores()
                }

                choreLayout.addView(choreText)
                choreLayout.addView(editButton)
                choreLayout.addView(completeButton)
                choreLayout.addView(deleteButton)

                choresContainer.addView(choreLayout)
            }
        }
    }
}