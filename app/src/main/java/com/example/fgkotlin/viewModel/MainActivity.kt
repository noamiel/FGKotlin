package com.example.fgkotlin.viewModel


import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.fgkotlin.R
import com.example.fgkotlin.model.UserModel
import com.example.fgkotlin.databinding.ActivityMainBinding
import com.example.fgkotlin.model.Model
import com.example.fgkotlin.view.JoystickView

/*
This class is the main class that runs all program
 */
class MainActivity : AppCompatActivity(), View.OnTouchListener, JoystickView.JoystickListener {
    lateinit var mainBinding: ActivityMainBinding
    lateinit var customSurfaceView: JoystickView
    lateinit var rudder: SeekBar
    lateinit var throttle: SeekBar
    // flag for checking if user did connect the simulator or not
    var connectWasClicked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var m: Model = Model()
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        // disables seek bars in case that the user did not connect the simulator
        rudder = findViewById<SeekBar>(R.id.rudder)
        rudder.setEnabled(false)
        throttle = findViewById<SeekBar>(R.id.throttle)
        throttle.setEnabled(false)
        customSurfaceView = JoystickView(this, m)
        customSurfaceView.setOnTouchListener(this)
        var canvasLayout = findViewById<ConstraintLayout>(R.id.joystick)
        canvasLayout.addView(customSurfaceView)

        var userModel = UserModel(rudder, throttle, m, this)
        userModel.ip = "Enter the IP"
        userModel.port = "Enter the port"
        mainBinding.userModel = userModel
    }

    override fun onTouch(v: View?, e: MotionEvent?): Boolean {
        if (connectWasClicked) {
            customSurfaceView.onTouch(v, e)
        }
        return true;
    }

    override fun onJoystickMoved(xPercent: Double, yPercent: Double, source: Int) {
    }

    /*
    In case that user connected, this func makes the seek bars enabled
     */
    fun connectClicked() {
        throttle.setEnabled(true)
        rudder.setEnabled(true)
        connectWasClicked = true
    }

    /*
    In case that user did not cinnect, we upload an error msg
     */
    fun connectFailed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("IP or port are invalid")
            .setPositiveButton("OK") { dialogInterface, which ->
                Toast.makeText(this.applicationContext, "clicked yes", Toast.LENGTH_LONG)
            }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()


    }

}