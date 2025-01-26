package se.curtrune.lucy.screens.economy

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.economy.classes.Transaction
import se.curtrune.lucy.activities.economy.workers.TransactionWorker
import se.curtrune.lucy.adapters.TransactionAdapter
import se.curtrune.lucy.util.Logger
import java.time.LocalDate

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionFragment : Fragment() {
    private var adapter: TransactionAdapter? = null
    private var currentTransaction: Transaction? = null
    private var editTextDescription: EditText? = null
    private var editTextAmount: EditText? = null
    private var textViewDate: TextView? = null
    //private var recycler: RecyclerView? = null
    private var date = LocalDate.now()
    private var composeView: ComposeView? = null
    private val account: String? = null
    private val type: Transaction.Type? = null
    private var transactions: MutableList<Transaction?>? = null

    // TODO: Rename and change types of parameters

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Logger.log("TransactionFragment.onCreate(LayoutInflater, ViewGroup, Bundle) kotlin")
        val view = inflater.inflate(R.layout.transactions_fragment, container, false)
        //setHasOptionsMenu(true)
        initDefaults()
        initComponents(view)
        initListeners()
        initContent()
        transactions = TransactionWorker.selectTransactions(context)
        //initRecycler(transactions)
        return view
    }

    private val transaction: Transaction
        private get() {
            Logger.log("...getTransaction()")
            currentTransaction = Transaction()
            currentTransaction!!.description = editTextDescription!!.text.toString()
            currentTransaction!!.amount = editTextAmount!!.text.toString().toFloat()
            currentTransaction!!.date = date
            return currentTransaction as Transaction
        }

    private fun initComponents(view: View) {
        Logger.log("...initComponents()")
        editTextDescription = view.findViewById(R.id.transactionActivity_goods)
        editTextAmount = view.findViewById(R.id.economyActivity_amount)
        textViewDate = view.findViewById(R.id.transactionFragment_date)
        textViewDate!!.text = LocalDate.now().toString()
        /*        spinnerAccounts = findViewById(R.id.economyActivity_accounts);
        spinnerTypes = findViewById(R.id.economyActivity_types);*/
        composeView = view.findViewById(R.id.transactionFragment_composeView)
    }
    private fun initContent(){
        println("...initContent()")
        composeView!!.setContent {
            MaterialTheme() {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(text = "hello kotlin")
                    Text(text = "red text", color = Color.Red)
                }
            }
        }
    }

    private fun initDefaults() {
        Logger.log("...initDefaults()")
        date = LocalDate.now()
    }

    private fun initListeners() {
        Logger.log("...initListeners()")
        textViewDate!!.setOnClickListener { view: View? -> showDatePickerDialog() }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Logger.log("...onOptionsItemSelected(MenuItem)", item.title.toString())
        if (item.itemId == R.id.economyActivity_save) {
            saveTransaction()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun resetUserInterface() {
        Logger.log("...resetUserInterface()")
        date = LocalDate.now()
        textViewDate!!.text = date.toString()
        editTextAmount!!.setText("")
        editTextDescription!!.setText("")
    }

    private fun saveTransaction() {
        Logger.log("...saveTransaction()")
        if (!validateInput()) {
            return
        }
        currentTransaction = transaction
        currentTransaction = TransactionWorker.insert(currentTransaction, context)
        transactions!!.add(currentTransaction)
        adapter!!.notifyDataSetChanged()
        resetUserInterface()
    }

    private fun setUserInterface(transaction: Transaction) {
        Logger.log("...setUserInterface(Transaction)")
        editTextAmount!!.setText(transaction.amount.toString())
        editTextDescription!!.setText(transaction.description)
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(requireContext())
        datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
            Logger.log("...onDateSet(DatePicker, year,month, dayOfMonth)")
            date = LocalDate.of(year, month + 1, dayOfMonth)
            textViewDate!!.text = date.toString()
        }
        datePickerDialog.show()
    }

    private fun validateInput(): Boolean {
        if (editTextDescription!!.text.toString().isEmpty()) {
            Toast.makeText(context, "missing description", Toast.LENGTH_LONG).show()
            return false
        }
        if (editTextAmount!!.text.toString().isEmpty()) {
            Toast.makeText(context, "missing amount", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    companion object {
        var VERBOSE = true

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TransactionFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): TransactionFragment {
            val fragment = TransactionFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}