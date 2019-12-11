package com.example.moneykeeper

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.balram.locker.utils.Locker
import com.balram.locker.view.AppLocker
import com.balram.locker.view.LockActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : LockActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        btn_on_off.setOnClickListener(this) // TODO: kill this
        btn_change.setOnClickListener(this) // TODO: kill this
        btn_change.setText(R.string.change_passcode)    // TODO: kill this


        updateUI()
        loadRouting()
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


    private fun updateUI(): Unit {
        if(AppLocker.getInstance().appLock.isPasscodeSet){
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

    }


}
