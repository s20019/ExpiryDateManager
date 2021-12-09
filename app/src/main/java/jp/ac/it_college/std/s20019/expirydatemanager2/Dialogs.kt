package jp.ac.it_college.std.s20019.expirydatemanager2

import android.app.DatePickerDialog
import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class ConfirmDialog(private val message: String,
                    private val okLabel: String,
                    private val okSelected: () -> Unit,
                    private val cancelLabel: String,
                    private val cancelSelected: () -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage(message)
        // ダイアログで「はい」をタップした時の処理
        builder.setPositiveButton(okLabel) { dialog, which ->
            okSelected()
        }
        // ダイアログで「いいえ」をタップした時の処理
        builder.setNegativeButton(cancelLabel) { dialog, which ->
            cancelSelected()
        }
        return builder.create()
    }
}

class DateDialog(private val onSelected: (String) -> Unit) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val date = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireActivity(), this, year, month, date)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        onSelected("$year/${month + 1}/$dayOfMonth")
    }
}