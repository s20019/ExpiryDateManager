package jp.ac.it_college.std.s20019.expirydatemanager2

import android.graphics.Color
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import java.util.*

class ExpiryDateAdapter(data: OrderedRealmCollection<ExpiryDate>) :
    RealmRecyclerViewAdapter<ExpiryDate, ExpiryDateAdapter.ViewHolder>(data, true) {

    private var listener: ((Long?) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    class ViewHolder(cell: View) : RecyclerView.ViewHolder(cell) {
        val date: TextView = cell.findViewById(R.id.dateText)
        val title: TextView = cell.findViewById(R.id.titleText)
        val expiredText: TextView = cell.findViewById(R.id.expiredText)
    }

    fun setOnItemClickListener(listener: (Long?) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item3_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expirydate: ExpiryDate? = getItem(position)
        holder.date.text = DateFormat.format("yyyy/MM/dd", expirydate?.date)
        holder.title.text = expirydate?.title

        // 現在の日付を取得する処理
        val cl = Calendar.getInstance()
        val today = cl.apply {
            set(Calendar.HOUR_OF_DAY , 0)
            set(Calendar.MINUTE , 0)
            set(Calendar.SECOND , 0)
            set(Calendar.MILLISECOND , 0)
        }.time

        var Cdate = cl.time
        // nullでなければCdateに、入力された日付を代入
        if (expirydate != null) Cdate = expirydate.date

        // 入力された日付が現在の日付より前だったら
        if ( Cdate <= today ) {
            holder.apply {
                expiredText.setText(R.string.expired_text)
                expiredText.setTextColor(Color.RED)
                date.setTextColor(Color.RED)
                title.setTextColor(Color.RED)
            }
        }

        holder.itemView.setOnClickListener {
            listener?.invoke(expirydate?.id)
        }
    }

    override fun getItemId(position: Int): Long {
        return super.getItem(position)?.id ?: 0
    }
}