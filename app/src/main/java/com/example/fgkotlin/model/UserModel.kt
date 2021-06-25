package com.example.fgkotlin.model

import android.util.Log
import android.widget.SeekBar
import com.example.fgkotlin.viewModel.MainActivity

/*
This class is an hepler class for model class
 */
class UserModel(rudder: SeekBar, throttle: SeekBar, m: Model, ma: MainActivity) {
    lateinit var ip: String
    lateinit var port: String
    lateinit var m: Model
    lateinit var ma: MainActivity

    // initaliaztion
    init {
        this.m = m
        this.ma = ma
        rudder.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar, progress: Int,
                fromUser: Boolean
            ) {

                m.update("rudder", ((progress).toDouble() / 50) - 1) // converts rudder to value
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {


            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {


            }
        })

        throttle.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar, progress: Int,
                fromUser: Boolean
            ) {


                m.update("throttle", ((progress).toDouble() / 100)) // converts throttle to value
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {


            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {


            }
        })

    }

    /*
    This func handles the user's click on connect button
     */
    fun handleClick() {

        try {
            m.connect(ip, port)
            ma.connectClicked()
        } catch (e: Exception) {
            ma.connectFailed() // this func opens dialog


        }

    }
}




