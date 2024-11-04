package com.example.calllog

import android.content.Context
import android.provider.CallLog
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun CallLogList(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val callLogs = remember { getCallLogs(context) }

    LazyColumn(modifier = modifier) {
        items(callLogs) { callLog ->
            Text(text = callLog)
        }
    }
}

private fun getCallLogs(context: Context): List<String> {
    val callLogs = mutableListOf<String>()
    val cursor = context.contentResolver.query(
        CallLog.Calls.CONTENT_URI,
        null,
        null,
        null,
        CallLog.Calls.DATE + " DESC"
    )

    cursor?.use {
        val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
        val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
        val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)
        val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)

        while (it.moveToNext()) {
            val number = it.getString(numberIndex)
            val type = it.getInt(typeIndex)
            val date = it.getLong(dateIndex)
            val duration = it.getLong(durationIndex)

            val callType = when (type) {
                CallLog.Calls.INCOMING_TYPE -> "Incoming"
                CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                CallLog.Calls.MISSED_TYPE -> "Missed"
                else -> "Unknown"
            }

            callLogs.add("Number: $number, Type: $callType, Date: $date, Duration: $duration seconds")
        }
    }

    return callLogs
}