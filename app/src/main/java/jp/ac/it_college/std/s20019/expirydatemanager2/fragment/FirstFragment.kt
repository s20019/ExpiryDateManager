package jp.ac.it_college.std.s20019.expirydatemanager2.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import jp.ac.it_college.std.s20019.expirydatemanager2.ExpiryDate
import jp.ac.it_college.std.s20019.expirydatemanager2.ExpiryDateAdapter
import jp.ac.it_college.std.s20019.expirydatemanager2.activity.MainActivity
import jp.ac.it_college.std.s20019.expirydatemanager2.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.list.layoutManager = LinearLayoutManager(context)
        val expirydates = realm.where<ExpiryDate>().findAll().sort("date", Sort.ASCENDING)  //  最後のsortで、ListViewを日付の昇順で並び替え
        val adapter = ExpiryDateAdapter(expirydates)
        binding.list.adapter = adapter

        adapter.setOnItemClickListener { id ->
            id?.let {
                val action = FirstFragmentDirections.actionToExpirydateEditFragment(it)
                findNavController().navigate(action)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setFabVisible(View.VISIBLE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}