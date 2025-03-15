package se.curtrune.lucy.screens.editable_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.curtrune.lucy.R
import se.curtrune.lucy.adapters.EditableListAdapter
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.util.Logger


class EditableListFragment : Fragment {
    private var recyclerView: RecyclerView? = null
    private var textViewHeading: TextView? = null
    private var buttonSave: Button? = null
    private var adapter: EditableListAdapter? = null
    private var viewModel: EditableListViewModel? = null

    private var parent: Item? = null

    constructor()
    constructor(parent: Item) {
        Logger.log("EditableListFragment(Item parent)", parent.heading)
        checkNotNull(parent)
        this.parent = parent
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.editable_list_fragment, container, false)
        try {
            initViews(view)
            initViewModel()
            initRecycler()
            initListeners()
        } catch (e: Exception) {
            Logger.log("EXCEPTION", e.message)
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
        return view
    }

    private fun initListeners() {
        Logger.log("...initListeners()")
        buttonSave!!.setOnClickListener { view: View? -> saveList() }
    }

    private fun initRecycler() {
        Logger.log("...initRecycler()")
        adapter =
            EditableListAdapter(viewModel!!.items.value, object : EditableListAdapter.Callback {
                override fun onNewLine(heading: String, index: Int) {
                    Logger.log("...onNewLine(String, int)", heading)
                    Logger.log("...add an empty item, after position, ", index)
                    viewModel!!.setHeading(heading, index, context)
                    viewModel!!.addEmptyItem(index + 1)
                    adapter!!.notifyItemInserted(index + 1)
                    adapter!!.setFocus(index + 1)
                }

                override fun onHeadingChanged(heading: String, position: Int) {
                    viewModel!!.setHeading(heading, position, context)
                }
            })
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.adapter = adapter
    }

    private fun initViewModel() {
        Logger.log("...initViewModel()")
        viewModel = ViewModelProvider(requireActivity()).get(
            EditableListViewModel::class.java
        )
        viewModel!!.init(parent!!)
    }

    private fun initViews(view: View) {
        Logger.log("...initViews()")
        recyclerView = view.findViewById(R.id.mainActivity_recycler)
        textViewHeading = view.findViewById(R.id.editableListViewFragment_heading)
        //textViewHeading.setText(parent!!.heading)
        buttonSave = view.findViewById(R.id.editableListViewFragment_buttonSave)
    }

    private fun saveList() {
        Logger.log("...saveList()")
        viewModel!!.saveAll()
        parentFragmentManager.popBackStackImmediate()
    }
}