package com.example.moneykeeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemahelper.utils.LocaleChecker
import com.example.moneykeeper.utils.DBHelper
import kotlinx.android.synthetic.main.activity_detailed.*
import java.util.*

class DetailedActivity : AppCompatActivity() {


    internal lateinit var db: DBHelper
    internal var transactions = listOf(
        Transaction(0, "09 декабря, 2019", "category1", 5000, "ezy 5k"),
        Transaction(0, "10 декабря, 2019", "category2", -3000, "bla bla bla"),
        Transaction(1, "11 декабря, 2019", "category2", -150, "taxi"),
        Transaction(2, "11 декабря, 2019", "category3", -22, "Маршрутка"),
        Transaction(3, "12 декабря, 2019", "category4", 1500, "Подарок")
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_detailed)

        db = DBHelper(this@DetailedActivity)
        refreshData()
    }

    private fun refreshData() {
        transactions = db.allTransactions
        configureRecyclerView()
    }


    private fun configureRecyclerView(): Unit {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(this@DetailedActivity) // последовательное отображение сверху вниз
        rv_transactions.layoutManager = layoutManager
        rv_transactions.setHasFixedSize(true)
        rv_transactions.adapter = TransactionsAdapter(transactions, object : TransactionsAdapter.Callback {
            override fun onItemClicked(item: Transaction) {
                // TODO: Click by transaction (edit/delete)
            }
        })


        rv_transactions.addItemDecoration(
            DividerItemDecoration(
                this@DetailedActivity,
                DividerItemDecoration.VERTICAL
            )
        )
    }


}
