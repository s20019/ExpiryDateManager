package jp.ac.it_college.std.s20019.expirydatemanager2.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import jp.ac.it_college.std.s20019.expirydatemanager2.R
import jp.ac.it_college.std.s20019.expirydatemanager2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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
}