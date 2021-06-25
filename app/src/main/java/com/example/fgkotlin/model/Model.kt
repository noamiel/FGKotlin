package com.example.fgkotlin.model

import java.io.IOException
import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/*
This class is the MODEL class according to MVVM architecture
 */
class Model : updateable {
    lateinit var fg: Socket
    lateinit var out: PrintWriter
    var aileron: Double = 0.0
    var elevator: Double = 0.0
    var rudder: Double = 0.0
    var throttle: Double = 0.0
    private lateinit var executor: Executor

    /*
    This func updates values of the flight gear simulator
     */
    override fun update(attri: String, value: Double) {
        if (attri.equals("throttle")) {

            this.throttle = value;
        } else {
            if (attri.equals("rudder")) {

                this.rudder = value;
            } else if (attri.equals("aileron")) {

                this.aileron = value;
            } else if (attri.equals("elevator")) {

                this.elevator = value;
            }
        }
        val task = Runnable {

            out!!.print("set /controls/flight/aileron $aileron\r\n")
            out!!.print("set /controls/flight/elevator $elevator\r\n")
            out!!.print("set /controls/flight/rudder $rudder\r\n")
            out!!.print("set /controls/engines/current-engine/throttle $throttle\r\n")
            out!!.flush()
        }
        task.run()

    }

    /*
    This func connects the flight gear simulator to our app
     */
    @Throws(IOException::class, java.lang.NumberFormatException::class)
    fun connect(ip: String, port: String) {
        try {
            fg = Socket(ip, port.toInt())
            out = PrintWriter(fg.getOutputStream())
        } catch (e: Exception) {
            throw e
        }
        this.executor = Executors.newSingleThreadExecutor()
    }
}

