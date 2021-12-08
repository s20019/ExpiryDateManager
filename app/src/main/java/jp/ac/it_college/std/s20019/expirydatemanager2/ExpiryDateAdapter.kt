package jp.ac.it_college.std.s20019.expirydatemanager2

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class ExpiryDateAdapter(data: OrderedRealmCollection<ExpiryDate>) :
    RealmRecyclerViewAdapter<ExpiryDate, ExpiryDateAdapter.ViewHolder>(data, true) {

    private var listener: ((Long?) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    class ViewHolder(cell: View) : RecyclerView.ViewHolder(cell) {
        val date: TextView = cell.findViewById(android.R.id.text1)
        val title: TextView = cell.findViewById(android.R.id.text2)
    }

    fun setOnItemClickListener(listener: (Long?) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expirydate: ExpiryDate? = getItem(position)
        holder.date.text = DateFormat.format("yyyy/MM/dd", expirydate?.date)
        holder.title.text = expirydate?.title
        holder.itemView.setOnClickListener {
            listener?.invoke(expirydate?.id)
        }
    }

    override fun getItemId(position: Int): Long {
        return super.getItem(position)?.id ?: 0
    }
}