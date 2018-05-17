package com.example.user.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.user.myapplication.model.User
import io.realm.Realm
import io.realm.RealmConfiguration

import kotlinx.android.synthetic.main.activity_join.*
import java.util.regex.Pattern

class JoinActivity : AppCompatActivity() {

    //이메일 정규식
    private val emailContent ="[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\\\w+\\\\.)+\\\\w+\$"

    //패스워드 정규식
    private val passContent ="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$"

    lateinit var realm:Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        Realm.init(this)
        var config = RealmConfiguration.Builder()
                .name("users.realm")
                .build()
        realm= Realm.getInstance(config)

        checkUser()

        button.setOnClickListener {

            var intent = Intent(this,MainActivity::class.java)

            realm.beginTransaction()

            var nextNum : Long = realm.where(User::class.java).count() +1
            var user = realm.createObject(User::class.java,nextNum)
            user.email = email_edit.text.toString().trim()
            user.id = id_edit.text.toString().trim()
            user.password1 = pw1_edit.text.toString().trim()
            user.password2 = pw2_edit.text.toString().trim()
            Log.v("user infomation","${user.email}")
            realm.commitTransaction()

            startActivity(intent)

        }

    }

    private fun checkUser(){

        id_edit.afterTextChanged({

            if (it.isEmpty() || it.length < 8) {
                Toast.makeText(this,getString(R.string.id_content), Toast.LENGTH_LONG).show()
                button.visibility = View.GONE
            }else{
                button.visibility= View.VISIBLE
            }
        })

        email_edit.afterTextChanged {
            if(it.isEmpty()||!Pattern.matches(emailContent,it)){
                Toast.makeText(this,getString(R.string.email_content),Toast.LENGTH_SHORT).show()
                button.visibility = View.GONE
            }else{
                button.visibility= View.VISIBLE
            }
        }

        pw1_edit.afterTextChanged {
            if (it.isEmpty()||!Pattern.matches(passContent,it)){
                Toast.makeText(this,getString(R.string.pass_content),Toast.LENGTH_SHORT).show()
                button.visibility = View.GONE
            }else{
                button.visibility= View.VISIBLE
            }
        }

        pw2_edit.afterTextChanged {
            if (it.isEmpty()||pw1_edit.text.toString().trim()!=it){
                Toast.makeText(this,getString(R.string.pass2_content),Toast.LENGTH_SHORT).show()
                button.visibility = View.GONE
            }else{
                button.visibility= View.VISIBLE
            }
        }

    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }

    @Override
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}

