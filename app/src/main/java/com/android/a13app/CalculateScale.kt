package com.android.a13app

import android.content.BroadcastReceiver
import java.time.temporal.TemporalAmount

class CalculateScale (id: String, name: String, expense: Double, difference: Double, receiver: String, amount: Double) {

    var id: String = id
        private set

    var name: String = name

    var expense: Double = expense

    var difference: Double = difference

    var receiver: String = receiver

    var amount: Double = amount
}