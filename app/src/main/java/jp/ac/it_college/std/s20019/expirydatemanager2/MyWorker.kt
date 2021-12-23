package jp.ac.it_college.std.s20019.expirydatemanager2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val name = "おしらせ"
    val id = "notifi_ch"
    val notifyDescription = "通知の詳細"

    init {
        val mChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        mChannel.apply {
            description = notifyDescription
        }
        notificationManager.createNotificationChannel(mChannel)
    }

    override fun doWork(): Result {
        val s = inputData.getString("key")
        if (s == "b30") {
            val notification = NotificationCompat.Builder(applicationContext , id).apply {
                setContentTitle(name)
                setContentText("賞味期限が残り30日の商品があります")
                setSmallIcon(android.R.drawable.ic_dialog_info)
            }
            notificationManager.notify(10,notification.build())
        }
        if (s == "OTD") {
            val notification = NotificationCompat.Builder(applicationContext, id).apply {
                setContentTitle(name)
                setContentText("賞味期限が今日までの商品があります")
                setSmallIcon(android.R.drawable.ic_dialog_info)
            }
            notificationManager.notify(20, notification.build())
        }
        return Result.success()
    }
}