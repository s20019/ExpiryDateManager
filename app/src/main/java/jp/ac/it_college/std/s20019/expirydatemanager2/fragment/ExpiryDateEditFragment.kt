package jp.ac.it_college.std.s20019.expirydatemanager2.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.format.DateFormat
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import jp.ac.it_college.std.s20019.expirydatemanager2.ConfirmDialog
import jp.ac.it_college.std.s20019.expirydatemanager2.DateDialog
import jp.ac.it_college.std.s20019.expirydatemanager2.ExpiryDate
import jp.ac.it_college.std.s20019.expirydatemanager2.activity.MainActivity
import jp.ac.it_college.std.s20019.expirydatemanager2.databinding.FragmentExpiryDateEditBinding
import java.lang.IllegalArgumentException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ExpiryDateEditFragment : Fragment() {

    private var _binding: FragmentExpiryDateEditBinding? = null
    private val binding get() = _binding!!
    private val args: ExpiryDateEditFragmentArgs by navArgs()
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentExpiryDateEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // IDが登録済みの場合は編集画面（削除ボタンを表示）
        if (args.expirydateId != -1L) {
            binding.linkText.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            binding.linkText.setOnClickListener {
                val uri = Uri.parse("https://cookpad.com/search/")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }

            val expirydate = realm.where<ExpiryDate>().equalTo("id", args.expirydateId).findFirst()
            binding.dateEdit.setText(DateFormat.format("yyyy/MM/dd", expirydate?.date))
            binding.titleEdit.setText(expirydate?.title)
            binding.detailEdit.setText(expirydate?.detail)
            binding.deleteButton.visibility = View.VISIBLE
        }
        // そうでない場合は新規登録（削除ボタンを非表示）
        else {
            binding.deleteButton.visibility = View.INVISIBLE
        }

        (activity as? MainActivity)?.setFabVisible(View.INVISIBLE)

        // 保存ボタンをタップした時の処理
        binding.saveButton.setOnClickListener {
            val dialog = ConfirmDialog("保存しますか？", "保存", { saveExpiryDate(it) }, "キャンセル",
                { Snackbar.make(it, "キャンセルしました", Snackbar.LENGTH_SHORT).show() }
            )
            dialog.show(parentFragmentManager, "save_dialog")
        }

        // 削除ボタンをタップした時の処理
        binding.deleteButton.setOnClickListener {
            val dialog = ConfirmDialog("削除しますか？", "削除", { deleteExpiryDate(it) }, "キャンセル",
                { Snackbar.make(it, "キャンセルしました", Snackbar.LENGTH_SHORT).show() }
            )
            dialog.show(parentFragmentManager, "delete_dialog")
        }

        // 日付選択ボタンをタップした時の処理
        binding.dateButton.setOnClickListener {
            DateDialog { date ->
                binding.dateEdit.setText(date)
            }.show(parentFragmentManager, "date_dialog")
        }
    }

    // 保存処理
    private fun saveExpiryDate(view: View) {
        when (args.expirydateId) {
            // 新規登録画面
            -1L -> {
                realm.executeTransaction { db: Realm ->
                    val maxId = db.where<ExpiryDate>().max("id")
                    val nextId = (maxId?.toLong() ?: 0L) + 1L
                    val expirydate = db.createObject<ExpiryDate>(nextId)
                    val date = "${binding.dateEdit.text}".toDate()

                    if (date != null) expirydate.date = date
                    expirydate.title = binding.titleEdit.text.toString()
                    expirydate.detail = binding.detailEdit.text.toString()
                }
                Snackbar.make(view, "追加しました", Snackbar.LENGTH_SHORT)
                    .setAction("戻る") { findNavController().popBackStack() }
                    .setActionTextColor(Color.YELLOW)
                    .show()
            }
            // 編集画面
            else -> {
                realm.executeTransaction { db: Realm ->
                    val expirydate = db.where<ExpiryDate>().equalTo("id", args.expirydateId).findFirst()
                    val date = "${binding.dateEdit.text}".toDate()
                    if (date != null) expirydate?.date = date
                    expirydate?.title = binding.titleEdit.text.toString()
                    expirydate?.detail = binding.detailEdit.text.toString()
                }
                Snackbar.make(view, "修正しました", Snackbar.LENGTH_SHORT)
                    .setAction("戻る") { findNavController().popBackStack() }
                    .setActionTextColor(Color.YELLOW)
                    .show()
            }
        }
    }

    // 削除処理
    private fun deleteExpiryDate(view: View) {
        realm.executeTransaction { db: Realm ->
            db.where<ExpiryDate>().equalTo("id", args.expirydateId)
                ?.findFirst()
                ?.deleteFromRealm()
        }
        Snackbar.make(view, "削除しました", Snackbar.LENGTH_SHORT)
            .setActionTextColor(Color.YELLOW)
            .show()

        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun String.toDate(pattern: String = "yyyy/MM/dd"): Date? {
        return try {
            SimpleDateFormat(pattern, Locale.ROOT).parse(this)
        } catch (e: IllegalArgumentException) {
            return null
        } catch (e: ParseException) {
            return null
        }
    }
}