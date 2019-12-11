package com.example.moneykeeper

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_create_transaction.*
import java.util.*


class CreateTransactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_transaction)

        et_calendar.text = getToday()
        et_calendar.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                et_calendar.setText("" + dayOfMonth + " " + getMonthName(monthOfYear) + ", " + year)
            }, year, month, day).also { it.show() }

        }
    }


    private fun getMonthName(id: Int): String { // TODO: локализовать

        return when (id) {
            0 -> "января"
            1 -> "февраля"
            2 -> "марта"
            3 -> "апреля"
            4 -> "мая"
            5 -> "июня"
            6 -> "июля"
            7 -> "августа"
            8 -> "сентября"
            9 -> "октября"
            10 -> "ноября"
            11 -> "декабря"

            else -> "undefined"
        }
    }

    private fun getToday(): String { // TODO: получить текущий день
        return "" + 1 + " " + getMonthName(0) + ", " + 2019
    }


    private fun initRouting(){
        btn_cancel.setOnClickListener {
            // TODO: вернуться к первому активити без PIN CODE

        }

        btn_save.setOnClickListener {
            // TODO: создать транзакцию
        }

    }
}
