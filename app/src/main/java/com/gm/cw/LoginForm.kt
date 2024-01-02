package com.gm.cw
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.gm.cw.databinding.ActivityLoginFormBinding
class LoginForm : AppCompatActivity() {
    private lateinit var bind : ActivityLoginFormBinding
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind= ActivityLoginFormBinding.inflate(layoutInflater)
        setContentView(bind.root)
        sharedPreferences = getSharedPreferences("LOGON", Context.MODE_PRIVATE)
        var dbhelp=DB_class(applicationContext)
        var db=dbhelp.readableDatabase
        bind.btnlogin.setOnClickListener {
            var username=bind.logtxt.text.toString();
            var password=bind.ed3.text.toString()
            val query="SELECT * FROM user WHERE username='"+username+"' AND pswd='"+password+"'"
            val rs=db.rawQuery(query,null)
            if(rs.moveToFirst()){
                val name=rs.getString(rs.getColumnIndex("username"))
                rs.close()
                if (::sharedPreferences.isInitialized) {
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLoggedIn", true)
                    editor.putString("username", name)
                    editor.apply()
                }

                startActivity(Intent(this, MainWindow::class.java).putExtra("name", name))
                finish()
            }
            else{
                var ad = AlertDialog.Builder(this)
                ad.setTitle("Erro")
                ad.setMessage("Username ou senha incorreta!")
                ad.setPositiveButton("Ok", null)
                ad.show()
            }
        }
        bind.regisLink.setOnClickListener {
            val intent= Intent(this,CadastroForm::class.java)
            startActivity(intent)
        }
    }
}