package com.gm.cw

import CommentAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gm.cw.databinding.ActivityMainWindowBinding
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.HttpsURLConnection

class MainWindow : AppCompatActivity() {
    private lateinit var bind: ActivityMainWindowBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var commentsEditText: EditText
    private lateinit var publishButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind= ActivityMainWindowBinding.inflate(layoutInflater)
        setContentView(bind.root)
        sharedPreferences = getSharedPreferences("LOGON", Context.MODE_PRIVATE)
        var value=intent.getStringExtra("name")
        bind.uname.text=value
        bind.logout.setOnClickListener {
            removeStoredPreferences()
            startActivity(Intent(this, LoginForm::class.java))
        }
        commentsEditText = findViewById(R.id.comments)

        publishButton = findViewById(R.id.btnPublib)
        publishButton.setOnClickListener {
            salvarNoSQLite(value)
        }
        lista()

    }
    private fun removeStoredPreferences() {
        if (::sharedPreferences.isInitialized) {
            val editor = sharedPreferences.edit()
            editor.remove("isLoggedIn")
            editor.remove("username")
            editor.apply()
        }
    }

    private fun lista(){
        val downloader = Runnable {
            val dbHelper = DB_class(this)
            val comments = dbHelper.getComments()
            val lastTenComments = if (comments.size > 10) comments.subList(comments.size - 10, comments.size) else comments
            val name = intent.getStringExtra("name").toString()
            val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            val likeClickListener = object : CommentAdapter.OnLikeClickListener {
                override fun onLikeClick(commentId: Int) {
                    dbHelper.updateLikeStatus(commentId, name)
                    lista()
                }

            }

            val commentAdapter = CommentAdapter(lastTenComments, likeClickListener, name)
            recyclerView.adapter = commentAdapter
        }

        downloader.run()
    }

    private fun salvarNoSQLite(value: String?) {
        val comentario = commentsEditText.text.toString()
        val dataAtual = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val value = intent.getStringExtra("name").toString()
        val dbHelper = DB_class(this)
        dbHelper.inserirComentario(comentario, dataAtual, value)
        lista()
    }
}