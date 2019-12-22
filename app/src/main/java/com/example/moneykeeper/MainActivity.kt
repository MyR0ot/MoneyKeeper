package com.example.moneykeeper

import android.app.ActionBar
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.balram.locker.utils.Locker
import com.balram.locker.view.AppLocker
import com.balram.locker.view.LockActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.ConfigurationCompat
import com.example.cinemahelper.utils.LocaleChecker
import com.example.moneykeeper.utils.DBHelper
import java.util.*


class MainActivity : LockActivity(), View.OnClickListener {

    internal lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) { // здесь точка входа в приложение
        super.onCreate(savedInstanceState)
        loadLocale() // загрузка выбранного языка (русский/ангилйский)
        window.setFlags( // убираем иконки часов, батареи, сети и т.п.
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)

        db = DBHelper(this@MainActivity) // экземпляр класса для общения с SQLite (база данных)

        configChangeLanguage() //

        btn_on_off.setOnClickListener(this)
        btn_change.setOnClickListener(this)
        btn_change.setText(R.string.change_passcode)

        l_settings.visibility = View.GONE // изначально блок настроек скрыт
        updateUI()    // перерисовка UI для корректной работы блокировщика
        loadRouting() // инициализируем роутинг для всех нажатий по различным кнопкам
        refreshData() // обновление данных
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    override fun onClick(v: View) {
        if (v == btn_on_off) {
            val type =
                if (AppLocker.getInstance().appLock.isPasscodeSet) Locker.DISABLE_PASSLOCK else Locker.ENABLE_PASSLOCK
            Intent(this@MainActivity, LockActivity::class.java).also {
                it.putExtra(Locker.TYPE, type)
                startActivityForResult(it, type)
            }
        } else if (v == btn_change) {
            Intent(this@MainActivity, LockActivity::class.java).also {
                it.putExtra(Locker.TYPE, Locker.CHANGE_PASSWORD)
                it.putExtra(Locker.MESSAGE, getString(R.string.enter_old_passcode))
                startActivityForResult(it, type)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            Locker.DISABLE_PASSLOCK -> {
                println("DISABLE PASSLOCK")
            }
            Locker.ENABLE_PASSLOCK, Locker.CHANGE_PASSWORD -> {
                if (requestCode == RESULT_OK) {
                    Toast.makeText(this, getString(R.string.setup_passcode), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        updateUI()
    }


    private fun refreshData(){
        val transactions = db.allTransactions // обращение к БД (взятие всех транзакций)
        val total = transactions.map { it.value }.sum() // итоговая сумма
        val expenditures = transactions.filter { it.value < 0 }.map { it.value }.sum() // суммарные затраты
        val revenue = transactions.filter { it.value > 0 }.map { it.value }.sum() // суммарные расходы

        tv_total.text = total.toString() + "\u20BD" // добавляем символ валюты рубля
        tv_expenditures.text = expenditures.toString() +"\u20BD"
        tv_revenue.text = revenue.toString() +"\u20BD"
    }

    private fun updateUI(): Unit {
        if (AppLocker.getInstance().appLock.isPasscodeSet) { // логика взаимодействие с PIN
            btn_on_off.setText(R.string.disable_passcode)
            btn_change.isEnabled = true
        } else {
            btn_on_off.setText(R.string.enable_passcode)
            btn_change.isEnabled = false
        }
    }

    private fun loadRouting(): Unit {

        btn_add_transaction.setOnClickListener {
            Intent(this@MainActivity, CreateTransactionActivity::class.java).also {
                startActivity(it)
            }
        }

        btn_detailed.setOnClickListener {
            Intent(this@MainActivity, DetailedActivity::class.java).also {
                startActivity(it)
            }
        }

        btn_settings.setOnClickListener {
            btn_settings.visibility = View.GONE
            expand(l_settings)
        }

        tv_settings.setOnClickListener {
            collapse(l_settings)
            btn_settings.visibility = View.VISIBLE
        }


    }

    // Код ниже отвечает за красивую анимацию скрытия и появления блока настроек (при клике на соответствующую кнопку)
    private fun expand(v: View) { // анимированное появление
        val matchParentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec((v.parent as View).width, View.MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = v.measuredHeight

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.visibility = View.VISIBLE
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.layoutParams.height = if (interpolatedTime == 1f)
                    ActionBar.LayoutParams.WRAP_CONTENT // TODO: возможно заимпорптил не тот клас, не все норм!
                else
                    (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // Expansion speed of 1dp/ms
        a.duration = (targetHeight / v.context.resources.displayMetrics.density).toInt().toLong()
        v.startAnimation(a)
    }

    private fun collapse(v: View) { // анимированное исчезновение
        val initialHeight = v.measuredHeight

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // Collapse speed of 1dp/ms
        a.duration = (initialHeight / v.context.resources.displayMetrics.density).toInt().toLong()
        v.startAnimation(a)
    }


    private fun configChangeLanguage(): Unit {
        val currentLocale = ConfigurationCompat.getLocales(resources.configuration)[0]
        if(currentLocale.language == "ru"){
            iv_language_icon.setImageResource(R.drawable.ru)  // изменение иконок флага
        } else {
            iv_language_icon.setImageResource(R.drawable.us) // изменение иконок флага
        }

        iv_language_icon.setOnClickListener{ changeLanguage() }
        btn_change_language.setOnClickListener{ changeLanguage() }
    }

    private fun changeLanguage(): Unit {
        if(LocaleChecker.isRussianLocale(this)){
            setLocale("en")
        } else setLocale("ru")
        recreate()
    }

    private fun setLocale(lang: String):Unit {
        val locale: Locale = Locale(lang)
        Locale.setDefault(locale)
        val conf = Configuration()
        conf.locale = locale
        baseContext.resources.updateConfiguration(conf, baseContext.resources.displayMetrics)
        val editor: SharedPreferences.Editor = getSharedPreferences("Settings",
            AppCompatActivity.MODE_PRIVATE
        ).edit()
        editor.putString("language", lang)
        editor.apply()
    }

    private fun loadLocale(){
        val prefs: SharedPreferences = getSharedPreferences("Settings",
            AppCompatActivity.MODE_PRIVATE
        )
        val language: String? = prefs.getString("language", "ru")
        language?.let { setLocale(it) }
    }


}
