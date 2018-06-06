package com.example.user.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class SlashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            //2초간 대기
            Thread.sleep(2000);

        }catch (e:InterruptedException){
            e.printStackTrace()
        }

        startActivity(Intent(this,MainActivity::class.java))
        finish()


    }
}