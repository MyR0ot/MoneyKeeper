package com.example.moneykeeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.balram.locker.utils.Locker
import com.balram.locker.view.AppLocker
import com.balram.locker.view.LockActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : LockActivity (), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bt_on_off.setOnClickListener(this)
        bt_change.setOnClickListener(this)
        bt_change.setText(R.string.change_passcode)

        updateUI()
    }

    override fun onClick(v: View) {
        if(v == bt_on_off){
            val type = if(AppLocker.getInstance().appLock.isPasscodeSet) Locker.DISABLE_PASSLOCK else Locker.CHANGE_PASSWORD
            Intent(this@MainActivity, LockActivity::class.java).also {
                it.putExtra(Locker.TYPE, type)
                startActivityForResult(it, type)
            }
        } else if(v == bt_change) {
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
            Locker.DISABLE_PASSLOCK -> { println("DISABLE PASSLOCK")}
            Locker.ENABLE_PASSLOCK, Locker.CHANGE_PASSWORD -> {
                if(requestCode == RESULT_OK){
                    Toast.makeText(this, getString(R.string.setup_passcode), Toast.LENGTH_LONG).show()
                }

            }
            else -> { // обратите внимание на блок
                print("x is neither 1 nor 2")
            }
        }

        updateUI()
    }


    private fun updateUI(): Unit {
        if(AppLocker.getInstance().appLock.isPasscodeSet){
            bt_on_off.setText(R.string.disable_passcode)
            bt_change.isEnabled = true
        } else {
            bt_on_off.setText(R.string.enable_passcode)
            bt_change.isEnabled = false
        }
    }


}
