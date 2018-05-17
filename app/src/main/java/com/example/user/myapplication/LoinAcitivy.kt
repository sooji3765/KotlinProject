package com.example.user.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.user.myapplication.model.User
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmConfiguration
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_loin_acitivy.*

class LoinAcitivy : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loin_acitivy)
        var intent = getIntent()
        var id =intent.getStringExtra("id")

       textView.setText("로그인 아이디 : ${id}")

    }
}
