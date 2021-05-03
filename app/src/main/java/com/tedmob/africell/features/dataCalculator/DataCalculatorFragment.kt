package com.tedmob.africell.features.dataCalculator


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.dto.DataCalculatorDTO
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel
import kotlinx.android.synthetic.main.fragment_data_calculator.*
import kotlinx.android.synthetic.main.row_data_calculator.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import javax.inject.Inject

class DataCalculatorFragment : BaseFragment() {
    @Inject
    lateinit var sessionRepository: SessionRepository
    var bundleSuggestion: List<DataCalculatorDTO.BundleSuggestion>? = null
    val adapter by lazy {
        DataCalculatorAdapter(mutableListOf(), hashMapOf(), object : DataCalculatorAdapter.Callback {
            override fun onItemChangedListener(seekbarValueItems: HashMap<String, Double>) {
                calcuateData(seekbarValueItems)
            }

        })
    }

    private fun calcuateData(seekbarValueItems: HashMap<String, Double>) {
        var total: Double = 0.0

        for ((key, value) in seekbarValueItems) {
            total += value
        }

        var suggestion = bundleSuggestion?.firstOrNull()
        bundleSuggestion?.forEach {
            if (total <= (it.volume?.toDoubleOrNull() ?: 0.0)) {
                suggestion = it
            } else {
                return@forEach
            }
        }

        val df = DecimalFormat("#,##0.######", DecimalFormatSymbols(Locale.ENGLISH))
        usageTxt.text = "${df.format(total)}  ${suggestion?.unit.orEmpty()}"
        validityTxt.text = suggestion?.getFormatValidity()
    }


    private val viewModel by provideViewModel<DataCalculatorViewModel> { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_data_calculator, R.layout.toolbar_default, true)
    }


    override fun configureToolbar() {
        super.configureToolbar()
        setHasOptionsMenu(true)
        actionbar?.title = getString(R.string.data_calculator)
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        bindData()

    }

    fun setupRecyclerView() {
        recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.separator)
        drawable?.let {
            dividerItemDecoration.setDrawable(it)
        }
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    private fun bindData() {
        viewModel.getDataCalculator()
        observeResourceInline(viewModel.dataCalculatorData) { dataCalc ->
           bundleSuggestion = dataCalc.bundleSuggestion?.sortedByDescending { it.volume?.toDouble() }
            val seekbarValueItems = hashMapOf<String, Double>()
            dataCalc.datacalculators?.forEach { item ->
                val min = item.minimumValue?.toDoubleOrNull()?.toInt() ?: 0
                val progress=(item.costPerUnit?.toDouble() ?: 0.0) * min
                seekbarValueItems.put(item.idDataCalculator, progress)
            }
            calcuateData(seekbarValueItems)
            adapter.setItems(dataCalc.datacalculators.orEmpty().toMutableList(),seekbarValueItems)

        }
    }


}
