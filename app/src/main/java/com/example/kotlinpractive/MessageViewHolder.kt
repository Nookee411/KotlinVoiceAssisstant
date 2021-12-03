package com.example.kotlinpractive

import android.view.View
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat
import java.text.SimpleDateFormat


class MessageViewHolder(@NonNull  itemView:View): RecyclerView.ViewHolder(itemView) {
    private val messageText: TextView = itemView.findViewById(R.id.textView_message)
    private val  messageDate:TextView = itemView.findViewById(R.id.textView_time)

    fun bind(message: Message) {
        messageText.text = message.text
        val fmt: DateFormat = SimpleDateFormat("HH:mm")
        messageDate.text = fmt.format(message.date)
    }

}