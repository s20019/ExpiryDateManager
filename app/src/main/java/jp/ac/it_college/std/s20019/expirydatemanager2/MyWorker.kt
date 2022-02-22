package jp.ac.it_college.std.s20019.expirydatemanager2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.edit
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.realm.Realm
import io.realm.kotlin.where
import java.util.*

class MyWorker(private val ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
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
        val pref: SharedPreferences = ctx.getSharedPreferences("notification", Context.MODE_PRIVATE)

        val cl = Calendar.getInstance()
        val today = cl.apply {
            set(Calendar.HOUR_OF_DAY , 0)
            set(Calendar.MINUTE , 0)
            set(Calendar.SECOND , 0)
            set(Calendar.MILLISECOND , 0)
        }.time

        val db = Realm.getDefaultInstance()

        val b30 = db.where<ExpiryDate>().equalTo("before30" , today).findAll().size
        val OTD = db.where<ExpiryDate>().equalTo("date" , today).findAll().size

        Log.i("notify_start", "doWork")
        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == 10) {      // 数字の部分を、通知を出したい時間に変える
            if (!pref.getBoolean("today_notify", false)) {

                if (b30 != 0) {
                    val notification = NotificationCompat.Builder(applicationContext , id).apply {
                        setContentTitle(name)
                        setContentText("賞味期限が残り30日の商品があります")
                        setSmallIcon(android.R.drawable.ic_dialog_info)
                    }
                    notificationManager.notify(10,notification.build())
                }

                if (OTD != 0) {
                    val notification = NotificationCompat.Builder(applicationContext, id).apply {
                        setContentTitle(name)
                        setContentText("賞味期限が今日までの商品があります")
                        setSmallIcon(android.R.drawable.ic_dialog_info)
                    }
                    notificationManager.notify(20, notification.build())
                }
                pref.edit { putBoolean("today_notify", true) }
            }
        } else {
            pref.edit { putBoolean("today_notify", false) }
        }
        Log.i("notify_release", "doWork_return")
        return Result.success()
    }
}