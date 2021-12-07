package jp.ac.it_college.std.s20019.expirydatemanager2

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class ExpiryDateManagerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .allowWritesOnUiThread(true).build()
        Realm.setDefaultConfiguration(config)
    }
}