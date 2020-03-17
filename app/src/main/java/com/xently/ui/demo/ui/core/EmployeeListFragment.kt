package com.xently.ui.demo.ui.core

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.xently.ui.demo.adapters.EmployeeListAdapter
import com.xently.ui.demo.data.Employee
import com.xently.ui.demo.data.Employee.Department.ACCOUNTING
import com.xently.ui.demo.viewmodels.EmployeeViewModel
import com.xently.xui.ListFragment
import com.xently.xui.utils.showSnackBar

abstract class EmployeeListFragment : ListFragment<Employee>() {

    private val listAdapter: EmployeeListAdapter by lazy {
        EmployeeListAdapter().apply {
            listItemClickListener = this@EmployeeListFragment
        }
    }

    abstract val viewModel: EmployeeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.adapter = listAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onRefreshRequested(false)
        viewModel.observableEmployeeListRefreshEvent.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            updateSwipeRefreshProgress(it.state)
        })
    }

    override fun onFabClickListener(context: Context): View.OnClickListener? {
        return View.OnClickListener {
            viewModel.employEmployee(Employee(1, "Orinda", "Harrison", 54, ACCOUNTING, 98900f))
        }
    }

    override fun onRefreshRequested(forced: Boolean) {
        viewModel.getEmployeeList()
    }

    override fun onListItemClick(model: Employee, view: View) {
        showSnackBar(requireView(), model.toString(), Snackbar.LENGTH_LONG, "Delete") {
            viewModel.fireEmployee(model)
        }
    }

    protected fun observeEmployeeList(query: String?) {
        viewModel.getObservableEmployeeList(query).observe(viewLifecycleOwner, Observer {
            onObservableListChanged(it)

            listAdapter.submitList(it)
        })
    }
}