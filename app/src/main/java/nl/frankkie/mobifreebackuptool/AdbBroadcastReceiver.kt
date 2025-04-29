package nl.frankkie.mobifreebackuptool

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat

class AdbBroadcastReceiver : BroadcastReceiver() {

    private val TAG = "MFBT"


    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive: $context $intent")
        if (context == null || intent == null) {
            return
        }

        if (intent.action.equals(ACTION_GET_PACKAGES)) {

            return
        }

        if (intent.action.equals(ACTION_SET_PROGRESS)) {
            val progress = intent.getIntExtra("progress", 0)
            Log.v(TAG, "onReceive: progress $progress")
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // Show notification with progress
                // TODO: Show notification with progress
            }
            return
        }

    }

    companion object {
        const val ACTION_GET_PACKAGES = "nl.frankkie.mobifreebackuptool.GET_PACKAGES"
        const val ACTION_SET_PROGRESS = "nl.frankkie.mobifreebackuptool.SET_PROGRESS"
    }
}