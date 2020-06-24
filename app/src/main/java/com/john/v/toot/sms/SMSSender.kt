package com.john.v.toot.sms

import android.content.Context
import android.telephony.SmsManager

class SMSSender{

    companion object{

        fun sendMessage(context:Context , message:String, phone:String){
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phone, null, message, null, null)
        }

    }


}