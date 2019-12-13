package com.example.moneykeeper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionsAdapter(var items: List<Transaction>, val callback: Callback):RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = TransactionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.transaction_list_item, parent, false))

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size


    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val date = itemView.findViewById<TextView>(R.id.tv_date)
        private val category = itemView.findViewById<TextView>(R.id.tv_category)
        private val value = itemView.findViewById<TextView>(R.id.tv_value)
        private val note = itemView.findViewById<TextView>(R.id.tv_note)


        fun bind(transaction: Transaction) {
            date.text = transaction.date
            category.text = transaction.category
            value.text = transaction.value.toString()
            note.text = transaction.note


            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) callback.onItemClicked(transaction)
            }
        }

    }

    interface Callback {
        fun onItemClicked(item: Transaction)
    }


    fun updateList(newList: List<Transaction>): Unit {
        items = newList
        notifyDataSetChanged()
    }


}