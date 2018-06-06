package com.example.user.myapplication

import android.content.Intent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.user.myapplication.model.User
import com.jakewharton.rxbinding2.InitialValueObservable
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.realm.Realm

import kotlinx.android.synthetic.main.activity_join.*
import org.jetbrains.anko.Android
import org.jetbrains.anko.toast
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.NoSuchElementException


class JoinActivity : AppCompatActivity() {

    //이메일 정규식
    val emailContent ="[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\\\w+\\\\.)+\\\\w+\$"

    //패스워드 정규식
    val passContent ="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$"

    lateinit var realm:Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        button.isEnabled = false

        realm= Realm.getDefaultInstance()


        id_input.setError("오류")
        // 길이
        val lengthGreaterThan5 = ObservableTransformer<String,String> { obserable ->

            obserable.flatMap {
                io.reactivex.Observable.just(it).map{ it.trim() }
                        .filter { it.length > 5 }
                        .singleOrError()
                        .onErrorResumeNext {
                            if (it is NoSuchElementException) {
                                Single.error(Exception("5자 이상 입력해주세요"))
                            } else {
                                Single.error(it)
                            }
                        }
                        .toObservable()
            }
        }

        var realmcheck = realm.where(User::class.java)

        // 아이디
        val verifyIdPattern = ObservableTransformer<String,String>{ observable ->
            observable.flatMap {
                io.reactivex.Observable.just(it).map { it.trim() }
                        .filter { it -> it.equals(realmcheck.equalTo("id",it.toString()).findAll().get(0)!!.id.toString())  }
                        .singleOrError()
                        .onErrorResumeNext {
                            if (it is NoSuchElementException) {
                                Single.error(Exception("존재하는 아이디 입니다."))
                            } else {
                                Single.error(it)
                            }
                        }
                        .toObservable()
            }
        }

        // 이메일
        val verifyEmailPattern = ObservableTransformer<String,String>{ observable ->
            observable.flatMap {
                io.reactivex.Observable.just(it).map { it.trim() }
                        .filter { Pattern.matches(emailContent,it) }
                        .singleOrError()
                        .onErrorResumeNext {
                            if (it is NoSuchElementException) {
                                Single.error(Exception("이일 메형식이 아닙니다."))
                            } else {
                                Single.error(it)
                            }
                        }
                        .toObservable()
            }
        }

        //비밀번호 패턴
        val passwordPattern = ObservableTransformer<String,String> { observable ->
            observable.flatMap {
                io.reactivex.Observable.just(it).map{ it.trim() }
                        .filter { Pattern.matches(passContent, it) }
                        .singleOrError()
                        .onErrorResumeNext {
                            if (it is NoSuchElementException) {
                                Single.error(Exception("비밀번호 형식에 맞게 입력해주세요"))
                            } else {
                                Single.error(it)
                            }
                        }
                        .toObservable()
            }
        }

        // 비밀번호 비교
        val verifyPass2Pattern = ObservableTransformer<String,String>{ observable ->
            observable.flatMap {
                io.reactivex.Observable.just(it).map { it.trim() }
                        .filter { it -> it.equals(pass1_input.text.toString()) }
                        .singleOrError()
                        .onErrorResumeNext {
                            if (it is NoSuchElementException) {
                                Single.error(Exception("비밀번호를 동일하게 입력해주세요."))
                            } else {
                                Single.error(it)
                            }
                        }
                        .toObservable()
            }
        }

        val emailObservable = RxTextView.textChanges(email_input)
                .map {
                    it -> Patterns.EMAIL_ADDRESS.matcher(it).matches()
                }


        val passObservable  = RxTextView.textChanges(pass1_input)
                .map {
                    it -> Pattern.matches(passContent,it)
                }


         RxTextView.afterTextChangeEvents(id_input)
                .skipInitialValue()
                .map {
                    email_input_layout.error = null
                    it.view().text.toString() }
                .debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .compose(verifyEmailPattern)
                .compose(errorMessae{
                    email_input_layout.error = it.message
                })
                .subscribe(
                        {Log.i("onNext "," $it looks good")
                        },{Log.i("onError"," ${it.message}") }
                    ,{ Log.i("onComplete","error")}
                )


          RxTextView.afterTextChangeEvents(id_input)
                .skipInitialValue()
                .map {
                    id_input_layout.error = null
                    it.toString() }
                 .debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .compose(lengthGreaterThan5)
                .compose(verifyIdPattern)
                .compose(errorMessae {
                    id_input_layout.error = it.message
                })
                .subscribe(
                        {Log.i("onNext "," $it looks good")
                        },{Log.i("onError"," ${it.message}") }
                        ,{ Log.i("onComplete","error")}
                )


        // 에러표시  변경

        RxTextView.afterTextChangeEvents(pass1_input)
                .skipInitialValue()
                .map { it.view().text.toString() }
              .debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .compose(passwordPattern)
                .compose(errorMessae {
                  pass1_input_layout.error = it.message
                })
                .subscribe()


        RxTextView.afterTextChangeEvents(pass2_input)
                .skipInitialValue()
                .map {
                    pass2_input_layout.error = null
                    it.view().text.toString() }
                .debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .compose(verifyPass2Pattern)
                .compose(errorMessae {
            pass2_input_layout.error = it.message
        })
                .subscribe(
                        {Log.i("onNext "," $it looks good")
                        },{Log.i("onError"," ${it.message}") }
                        ,{ Log.i("onComplete","error")}
                )


        val signButton:Observable<Boolean> = Observable.combineLatest(emailObservable,passObservable, BiFunction { t1, t2 ->  t1 && t2} )


        signButton.distinctUntilChanged()
                .subscribe{ enable -> button.isEnabled = enable}

        button.setOnClickListener {

            var intent = Intent(this, MainActivity::class.java)

            realm.beginTransaction()

            var nextNum: Long = realm.where(User::class.java).count() + 1
            var user = realm.createObject(User::class.java, nextNum)

            user.email = email_input.text.toString().trim()
            user.id = id_input.text.toString().trim()
            user.password = pass1_input.text.toString().trim()

            Log.v("user infomation", "${user.email}")
            realm.commitTransaction()

            toast("회원가입이 완료되었습니다.")

            startActivity(intent)
            finish()
        }



    }

    private inline fun errorMessae(crossinline onError: (ex: Throwable) -> Unit): ObservableTransformer<String, String> = ObservableTransformer { observable ->
                observable.retryWhen { errors ->
                        errors.flatMap {
                                onError(it)
                                io.reactivex.Observable.just("")
                            }
                    }
            }


    @Override
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}

