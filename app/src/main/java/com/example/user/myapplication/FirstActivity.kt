package com.example.user.myapplication

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.user.myapplication.model.Memo
import com.example.user.myapplication.model.User
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_first.*
import kotlinx.android.synthetic.main.app_bar_first.*
import kotlinx.android.synthetic.main.content_first.*
import kotlinx.android.synthetic.main.dialog_layout.view.*
import kotlinx.android.synthetic.main.nav_header_first.*
import kotlinx.android.synthetic.main.nav_header_first.view.*
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FirstActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        setSupportActionBar(toolbar)

        var intent = getIntent()
        var id =intent.getStringExtra("id")

        var shf : SharedPreferences = getSharedPreferences("todo",0)
        var edit : SharedPreferences.Editor = shf.edit()

        var realm = Realm.getDefaultInstance()

        val user = realm.where(User::class.java).equalTo("id",id).findAll()



        // 고쳐야지
        //var memoData: ArrayList<Memo>

        //memoData.content = shf.getString("content")
        //memoData.num = shf.getString("num")
        nav_view.setNavigationItemSelectedListener(this)

        val nav_header_view : View = nav_view.getHeaderView(0)

        nav_header_view.textView_id.setText("${user.get(0)!!.id.toString()}")
        nav_header_view.textView_Email.setText("${user.get(0)!!.email.toString()}")

        //var memoRecycleAdapter = MemoRecycleAdapter(this, memoData){
         //   memo -> toast("${memo.content}")
        //}


        //recycleView.adapter = memoRecycleAdapter

        val layoutManager = LinearLayoutManager(this)
        recycleView.layoutManager = layoutManager
        recycleView.setHasFixedSize(true)



        fab.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.dialog_layout,null)
            val dialogText =  dialogView.dialogEt

            builder.setView(dialogView)
                    .setPositiveButton("저장"){  dialogInterface ,i ->
                        edit.putString("content", dialogText.text.toString())
                        val mNow = System.currentTimeMillis()
                        val mDate = Date(mNow)
                        val mFormat = SimpleDateFormat("yyyy-MM-dd")
                        var count = (shf.all.size+1).toString()
                        edit.putString("date",mFormat.format(mDate).toString())
                        edit.putString("num",count)
                        edit.commit()
                    }
                    .setNegativeButton("취소"){
                        dialogInterface, i ->

                    }
                    .show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.first, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_event -> {
                // Handle the camera action

            }

            
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
