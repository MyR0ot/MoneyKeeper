package com.example.moneykeeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneykeeper.utils.DBHelper
import kotlinx.android.synthetic.main.activity_detailed.*

class DetailedActivity : AppCompatActivity() {

    internal lateinit var db: DBHelper
    private lateinit var transactions: List<Transaction>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_detailed)

        db = DBHelper(this@DetailedActivity)
        transactions = db.allTransactions
        configureRecyclerView(transactions)


        toggle.setOnCheckedChangeListener { group, checkedId ->
            run {
                if (checkedId == sw_filter_all.id) {
                    configureRecyclerView(transactions)
                } else if (checkedId == sw_filter_expense.id) {
                    configureRecyclerView(transactions.filter { it.value < 0 })
                } else {
                    configureRecyclerView(transactions.filter { it.value > 0 })
                }
            }
        }
    }


    private fun configureRecyclerView(transactions: List<Transaction>): Unit {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(this@DetailedActivity) // последовательное отображение сверху вниз
        rv_transactions.layoutManager = layoutManager
        rv_transactions.setHasFixedSize(true)
        rv_transactions.adapter = TransactionsAdapter(transactions, object : TransactionsAdapter.Callback {
            override fun onItemClicked(item: Transaction) {
                // TODO: Click by transaction
            }

            override fun onDelete(item: Transaction){
                db.deleteTransaction(item)
                this@DetailedActivity.transactions = db.allTransactions
                configureRecyclerView(this@DetailedActivity.transactions)
            }
        })
    }



}
