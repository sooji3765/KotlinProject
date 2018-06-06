package com.example.user.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.user.myapplication.model.User
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    lateinit var realm:Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        realm = Realm.getDefaultInstance()

        button2.setOnClickListener {

            if (dbCheck()==true){
                var intent= Intent(this,FirstActivity::class.java)
                intent.putExtra("id",editText.text.toString().trim())
                startActivity(intent)
            }
        }
        button3.setOnClickListener {

            var intent1 = Intent(this,JoinActivity::class.java)

            startActivity(intent1)
        }
    }
    private fun dbCheck() :Boolean{
        var id = editText.text.toString().trim()
        var pass = editText2.text.toString().trim()


        val query = realm.where(User::class.java)
        val user= query.equalTo("id",id).findAll()

        // 결과가 없으면
        if (user.size ==0 ){
            toast("일치하는 정보 없음")
            return false
        }else{
            if(user.get(0)!!.password.toString()==pass){
                   return true
            }else{
                toast("잘못된 비밀번호 입니다.")
            }
        }
        return false
    }

}
