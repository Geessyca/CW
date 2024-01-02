package com.gm.cw

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gm.cw.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            val name = sharedPreferences.getString("username", "")
            startActivity(Intent(this, MainWindow::class.java).putExtra("name", name))
        }
        binding.btnrgs.setOnClickListener {
            val intent=Intent(this,CadastroForm::class.java)
            startActivity(intent)
        }
        binding.btnlogin.setOnClickListener {
            val intent=Intent(this,LoginForm::class.java)
            startActivity(intent)
        }
    }
}