package com.john.v.toot.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import androidx.core.app.NotificationCompat.getExtras
import android.os.Bundle
import android.util.Log


/**
 * Class to keep track of the messages
 */
class SMSBroadcastReceiver : BroadcastReceiver() {



    val SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
    val TAG = "SMS Broodcast Receiver"
    override fun onReceive(context: Context?, intent: Intent?) {

        Log.e(TAG, "RECEIVED")

        if (intent?.getAction() === SMS_RECEIVED) {
            val bundle = intent.getExtras()
            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<Any>?
                val messages = arrayOfNulls<SmsMessage>(pdus!!.size)
                for (i in pdus.indices) {
                    messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                }
                if (messages.size > -1) {
                    Log.e(TAG, "Message recieved: " + messages[0]?.messageBody)
                }
            }
        }
    }

}