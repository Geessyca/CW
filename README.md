<h1 align="center">
  🎓<br>Code Woman
</h1>

<h4 align="center">
  O intuito deste repositório é compartilhar o aplicativo android criado para o trabalho final da máteria de Programação para Dispositivos Móveis
</h4>

<h2 align="left">
  Telas e funcionalidades ↷ 
</h2>
<h3>Ao abrir o aplicativo<h3>

<h4>Ao abrir o aplicativo é possível dois cenários, quando logado abre a tela principal e quando não há usuário logado a tela aberta é a que redireciona para a tela de login ou de cadastro <h4>
<img src="https://github.com/Geessyca/CW/blob/main/telainical.jpg"/>

<h3>Tela de Login e Cadastro<h3>

<h4>Nessa tela é possível escolher entre criar um usuário ou logar em um já existente. Ambas possuem validação para quando o usuário estiver com o acesso incorreto ou algum erro de cadastro.<h4>
<img src="https://github.com/Geessyca/CW/blob/main/telalogin.jpg"/>

<h3>Tela Principal<h3>

<h4>Nessa tela é possível sair da conta; Publicar mensagens e curtir mensagens publicadas por você ou outras pessoas.<h4>
<img src="https://github.com/Geessyca/CW/blob/main/telaprincipal.jpg"/>
  
<h2 align="left">
  Códigho das prinipais funcionalidades ↷ 
</h2>

<h3>Banco de dados<h3>
  
```
override fun onCreate(db: SQLiteDatabase?) {
        val newtb = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_PROF + " TEXT," + KEY_UNAME + " TEXT,"
                + KEY_PSWD + " TEXT" + ")")
        db?.execSQL(newtb)
        val createCommentsTable = ("CREATE TABLE IF NOT EXISTS " + TABLE_COMMENTS + "("
                + "ROWID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$KEY_COMENTARIO TEXT, $KEY_DATA TEXT, $KEY_NAME TEXT, $KEY_CURTIDAS INT, $LIST_CURTIDAS TEXT )")

        db?.execSQL(createCommentsTable)
    }
```
<h3>Comentários<h3>
  
```
fun inserirComentario(comentario: String, data: String, name: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_COMENTARIO, comentario)
        values.put(KEY_DATA, data)
        values.put(KEY_NAME, name)

        values.put(KEY_CURTIDAS, 0)

        // Insira os dados na tabela de comentários
        db.insert(TABLE_COMMENTS, null, values)

        // Feche o banco de dados após a inserção
        db.close()
    }
    fun updateLikeStatus(commentId: Int, userName: String) {
        writableDatabase.use { db ->
            // SQL query to update the likes and liked users
            val sql = "UPDATE $TABLE_COMMENTS " +
                    "SET $KEY_CURTIDAS = $KEY_CURTIDAS + 1, " +
                    "$LIST_CURTIDAS = CASE " +
                    "WHEN $LIST_CURTIDAS IS NULL THEN '$userName' " +
                    "ELSE $LIST_CURTIDAS || ',$userName' END " +
                    "WHERE ROWID = $commentId"

            // Execute the SQL query
            db.execSQL(sql)
        }
    }

    @SuppressLint("Range")
    fun getComments(): List<Comment> {
        val comments = mutableListOf<Comment>()
        val selectQuery = "SELECT * FROM $TABLE_COMMENTS ORDER BY $KEY_DATA DESC"


        val db = this.readableDatabase

        readableDatabase.use { db ->
            db.rawQuery(selectQuery, null).use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val commentId = cursor.getInt(cursor.getColumnIndex("ROWID"))
                        val date = cursor.getString(cursor.getColumnIndex(KEY_DATA))
                        val people = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                        val message = cursor.getString(cursor.getColumnIndex(KEY_COMENTARIO))
                        val heartCount = cursor.getInt(cursor.getColumnIndex(KEY_CURTIDAS))
                        val peopleHeartIndex = cursor.getColumnIndex(LIST_CURTIDAS)
                        val peopleHeart = if (cursor.isNull(peopleHeartIndex)) "" else cursor.getString(peopleHeartIndex)

                        val comment = Comment(commentId, date, people, message, heartCount,peopleHeart)
                        comments.add(comment)
                    } while (cursor.moveToNext())
                }
            }
        }

        return comments
    }
```
<h3>Login<h3>
  
```
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
```
<h3>Cadastro<h3>
  
```
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

```




<h3>Lista de comentários<h3>
  
```
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

```

<h3>Publicar comentário<h3>
  
```
    private fun salvarNoSQLite(value: String?) {
        val comentario = commentsEditText.text.toString()
        val dataAtual = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val value = intent.getStringExtra("name").toString()
        val dbHelper = DB_class(this)
        dbHelper.inserirComentario(comentario, dataAtual, value)
        lista()
    }

```
##  👩🏻‍💻 Autora<br>
<table>
  <tr>
    <td align="center">
      <a href="https://github.com/geessyca">
        <img src="https://avatars.githubusercontent.com/u/72661229?v=4" width="100px;" alt="Icon GitHub"/><br>
        <sub>
          <b>Gessyca Moreira</b>
        </sub>
      </a>
    </td>
  </tr>
</table>
