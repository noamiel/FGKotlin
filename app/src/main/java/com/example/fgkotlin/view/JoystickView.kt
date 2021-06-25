package com.example.fgkotlin.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import com.example.fgkotlin.model.updateable
import kotlin.math.sqrt


/*
This class includes the joystick view and implements surfaceView, surfaceHolder.callback and onTouchListetner
 */
public class JoystickView : SurfaceView, SurfaceHolder.Callback, View.OnTouchListener {
    lateinit var joystickCallback: JoystickListener
    lateinit var m: updateable
    // a flag for the the first time of showing joystick in screen
    var first: Boolean = true

    public interface JoystickListener {
        abstract fun onJoystickMoved(xPercent: Double, yPercent: Double, source: Int)

    }

    // width of joystick
    var w: Float = 0.0F
    // height of joystick
    var h: Float = 0.0F
    var centerX: Float = 0.0F
    var centerY: Float = 0.0F
    var baseRadius: Float = 0.0F
    var hatRadius: Float = 0.0F


    public constructor(context: Context, m: updateable) : super(context) {
        holder.addCallback(this)
        setOnTouchListener(this)
        this.m = m
        if (context is JoystickListener) {
            joystickCallback = context
        }
    }

    public constructor(context: Context, attributes: AttributeSet, m: updateable) : super(
        context,
        attributes,
    )
     {
        holder.addCallback(this)
        setOnTouchListener(this)
        if (context is JoystickListener) {
            joystickCallback = context
        }
    }

    public constructor(
        context: Context,
        attributes: AttributeSet,
        style: Int,
        m: updateable
    ) : super(context, attributes, style) {
        holder.addCallback(this)
        setOnTouchListener(this)
        if (context is JoystickListener) {
            joystickCallback = context
        }
    }


    /*
    This func draws the joystick
     */
    private fun drawJoystick(newX: Float, newY: Float) {
        // not first time of showing joystick on screen
        if (!first) {
            var aileron: Double = (2 * newX.toDouble()) / w - 1
            var elevator: Double = (2 * newY.toDouble() / h) - 1
            m.update("aileron", aileron)
            m.update("elevator", elevator)
        } else { //first time showing joystick
            first = false
        }
        var myCanves: Canvas = this.holder.lockCanvas()
        if (myCanves == null) return;
        myCanves.drawColor(Color.WHITE);
        var colors = Paint()
        colors.setColor(Color.BLACK)
        myCanves.drawCircle(centerX, centerY, baseRadius, colors) //the joystick base
        colors.setColor(Color.GRAY)
        myCanves.drawCircle(newX, newY, hatRadius, colors) //the joystick hat
        this.holder.unlockCanvasAndPost(myCanves)

    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        setupDimensions()
        drawJoystick(centerX, centerY)

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {


    }

    /*
    This func handles what happens after user touches joystick
     */
    override fun onTouch(v: View?, e: MotionEvent?): Boolean {
        var code: Int = e!!.getAction() and MotionEvent.ACTION_MASK
        if ((code == MotionEvent.ACTION_POINTER_UP) || (code == MotionEvent.ACTION_UP) || (code == MotionEvent.ACTION_CANCEL)) {
            if (v != null) {
                if (v.equals(this)) {
                    if (e != null) {
                        if (e.getAction() == 1) {
                            var a = (e.getX() - centerX).toDouble()
                            var b = (e.getY() - centerY).toDouble()
                            var displacement: Double = sqrt(Math.pow(a, 2.0) + Math.pow(b, 2.0))
                            if (displacement < this.baseRadius) {
                                //in bounds
                                drawJoystick(e.getX(), e.getY());
                                joystickCallback.onJoystickMoved(
                                    ((e.getX() - centerX) / baseRadius).toDouble(),
                                    ((e.getY() - centerY) / baseRadius).toDouble(),
                                    getId()
                                );
                            }
                            // out of bounds
                            else {
                                var ratio: Double = baseRadius / displacement;
                                var constrainedX: Double = centerX + (e.getX() - centerX) * ratio;
                                var constrainedY: Double = centerY + (e.getY() - centerY) * ratio;
                                drawJoystick(constrainedX.toFloat(), constrainedY.toFloat());
                                joystickCallback.onJoystickMoved(
                                    (constrainedX - centerX) / baseRadius,
                                    ((constrainedY - centerY) / baseRadius),
                                    getId()
                                )
                            }
                            return true
                        } else {
                            drawJoystick(centerX, centerY)
                            joystickCallback.onJoystickMoved(0.0, 0.0, getId())
                        }
                    }
                    return true
                }
                return true
            }
            return true
        }
        return true
    }


    /*
    This func sets upp dimensions in the beginning of the program
     */
    private fun setupDimensions() {
        w = width.toFloat()
        h = height.toFloat()
        centerX = width.toFloat() / 2
        centerY = height.toFloat() / 2
        baseRadius = Math.min(width, height).toFloat() / 3
        hatRadius = Math.min(width, height).toFloat() / 6
        hatRadius = (hatRadius * 0.85).toFloat()
    }
}