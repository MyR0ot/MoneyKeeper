package com.example.moneykeeper

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.moneykeeper.utils.DBHelper
import kotlinx.android.synthetic.main.activity_create_transaction.*
import kotlinx.android.synthetic.main.transaction_list_item.*
import java.util.*


class CreateTransactionActivity : AppCompatActivity() {


    internal lateinit var db: DBHelper
    private val categories = arrayOf("другие", "еда", "транспорт", "здоровье", "путешествия", "подарки", "отдых", "face palm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_create_transaction)

        db = DBHelper(this@CreateTransactionActivity)
        initRouting()
        initCalendar()
        initSpinner()
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

    private fun getToday(): String {
        val date = Date()
        return "" + date.date + " " + getMonthName(date.month ) + ", " + (date.year + 1900)
    }

    private fun initRouting(){
        btn_cancel.setOnClickListener {
            // TODO: вернуться к первому активити без PIN CODE
            finish()
        }

        btn_save.setOnClickListener {

            val id = db.getNextId()
            val date = et_calendar.text.toString()
            val category = sp_category_chooser.selectedItem.toString()
            val note = et_note.text.toString()
            var value = et_value.text.toString().toInt()
            if(sw_create_expense.isActivated) value = -value;
            val transaction = Transaction(id, date, category, value, note)

            db.addTransaction(transaction)
            // TODO: вернуться к первому активити без PIN CODE
            finish()
        }

    }

    private fun initSpinner() {
        val adapter = ArrayAdapter<String>(
            this@CreateTransactionActivity,
            android.R.layout.simple_spinner_item,
            categories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_category_chooser.adapter = adapter
        sp_category_chooser.setSelection(0) // default: 'all' genres display
        sp_category_chooser.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View,
                position: Int, id: Long
            ) { }

            override fun onNothingSelected(arg0: AdapterView<*>) {
                sp_category_chooser.setSelection(0)
            }
        }
    }

    private fun initCalendar() {
        et_calendar.text = getToday()
        et_calendar.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                et_calendar.text = "" + dayOfMonth + " " + getMonthName(monthOfYear) + ", " + year
            }, year, month, day).also { it.show() }
        }
    }
}
