package com.example.kotlinpractive

import android.os.Bundle
import android.os.PersistableBundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import java.util.function.Consumer


class MainActivity : AppCompatActivity() {
    private lateinit var buttonSend:Button
    private lateinit var questionText:EditText
    private lateinit var chatMessageList:RecyclerView
    private lateinit var textToSpeech:TextToSpeech

    private lateinit var messageListAdapter:MessageListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState!=null) {
            Log.d(TAG, "onCreate: $savedInstanceState")
            restoreDialog(savedInstanceState.getStringArray("messageArray"))
        }

        setContentView(R.layout.activity_main)
        buttonSend = findViewById(R.id.button_send)
        questionText = findViewById(R.id.question_field)
        chatMessageList = findViewById(R.id.chatMessageList)
        messageListAdapter = MessageListAdapter()

        chatMessageList.layoutManager=LinearLayoutManager(this)
        chatMessageList.adapter = messageListAdapter
        textToSpeech = TextToSpeech(applicationContext,
            TextToSpeech.OnInitListener { status ->
                if(status!=TextToSpeech.ERROR){
                    textToSpeech.language= Locale("ru")
                }
            })

        buttonSend.setOnClickListener { onSend(); }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        restoreDialog(savedInstanceState.getStringArray("messageArray"))
    }

    private fun restoreDialog(textArray:Array<String>?){
        Log.d(TAG, "restoreDialog: $textArray")
    }

    private fun onSend(){
        val text = questionText.text.toString()
        messageListAdapter.messageList.add(Message(text, true))
        messageListAdapter.notifyDataSetChanged()
        AI.getAnswer(text) { response ->
            messageListAdapter.messageList.add(Message(response, false))
            textToSpeech.speak(response, TextToSpeech.QUEUE_FLUSH, null, null)
            questionText.text.clear()
            messageListAdapter.notifyDataSetChanged()
            chatMessageList.scrollToPosition(messageListAdapter.messageList.size - 1)
        }


    }


}