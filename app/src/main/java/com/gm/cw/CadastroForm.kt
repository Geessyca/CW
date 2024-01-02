package com.gm.cw

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.gm.cw.databinding.ActivityCadastroFormBinding

class CadastroForm : AppCompatActivity() {
    private lateinit var binding : ActivityCadastroFormBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCadastroFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("LOGON", Context.MODE_PRIVATE)
        var dbhelp=DB_class(applicationContext)
        var db=dbhelp.writableDatabase
        binding.btnrgs.setOnClickListener {
            var name=binding.ed1.text.toString()
            var username=binding.ed2.text.toString()
            var password=binding.ed3.text.toString()
            if(name.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                var data = ContentValues()
                data.put("profissao", binding.ed1.text.toString())
                data.put("username", binding.ed2.text.toString())
                data.put("pswd", binding.ed3.text.toString())
                var rs:Long = db.insert("user", null, data)
                if(!rs.equals(-1)) {
                    var ad = AlertDialog.Builder(this)
                    ad.setTitle("Sucesso")
                    ad.setMessage("Conta Registrada")
                    ad.setPositiveButton("Ok", null)
                    ad.show()
                    binding.ed1.text.clear()
                    binding.ed2.text.clear()
                    binding.ed3.text.clear()
                    val query="SELECT * FROM user WHERE username='"+username+"' AND pswd='"+password+"'"
                    val lg=db.rawQuery(query,null)
                    if(lg.moveToFirst()){
                        lg.close()
                        if (::sharedPreferences.isInitialized) {
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("isLoggedIn", true)
                            editor.putString("username", username)
                            editor.apply()
                        }
                        startActivity(Intent(this,MainWindow::class.java).putExtra("name",username))
                    }
                }else{
                    var ad = AlertDialog.Builder(this)
                    ad.setTitle("Erro")
                    ad.setMessage("Conta não registrada")
                    ad.setPositiveButton("Ok", null)
                    ad.show()
                    binding.ed1.text.clear()
                    binding.ed2.text.clear()
                    binding.ed3.text.clear()
                }
            }else{
                Toast.makeText(this,"Todos os campos requeridos",Toast.LENGTH_SHORT).show()
            }
        }
        binding.loginLink.setOnClickListener {
            val intent=Intent(this,LoginForm::class.java)
            startActivity(intent)
        }
    }
}