package jp.ac.it_college.std.s20019.expirydatemanager2.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.work.Data
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import io.realm.Realm
import io.realm.kotlin.where
import jp.ac.it_college.std.s20019.expirydatemanager2.ExpiryDate
import jp.ac.it_college.std.s20019.expirydatemanager2.MyWorker
import jp.ac.it_college.std.s20019.expirydatemanager2.R
import jp.ac.it_college.std.s20019.expirydatemanager2.databinding.ActivityMainBinding
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val manager = WorkManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // 常にダークテーマをOFFにする処理
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)

        val naviController = findNavController(R.id.nav_host_fragment_content_main)
        // 画面上部のアクションバーに、戻るボタンを追加する処理
        setupActionBarWithNavController(naviController)

        etc()

        binding.fab.setOnClickListener { view ->
            naviController.navigate(R.id.action_to_expirydateEditFragment)
        }
    }

    // 前の画面に遷移する処理
    override fun onSupportNavigateUp() =
        findNavController(R.id.nav_host_fragment_content_main).navigateUp()

    // ExpiryDateEditFragmentに遷移した後、fabボタンを非表示にする時に使う処理
    fun setFabVisible(visibility: Int) {
        binding.fab.visibility = visibility
    }

    private fun notification(s: String) {
        val data = Data.Builder().apply{
            putString("key" , s)
        }.build()

        val saveRequest = PeriodicWorkRequestBuilder<MyWorker>(
            1, TimeUnit.DAYS,
                15, TimeUnit.MINUTES
        )
            .setInputData(data)
            .build()
        manager.enqueue(saveRequest)
    }

    private fun etc(){
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

        if (b30 != 0) { notification("b30") }
        if (OTD != 0) { notification("OTD") }
    }
}