// File: app/src/main/java/com/kardio/features/library/presentation/ui/StudySetsFragment.kt
package com.kardio.features.library.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kardio.R
import com.kardio.core.base.BaseFragment
import com.kardio.databinding.FragmentStudySetsBinding
import com.kardio.features.home.presentation.ui.HomeFragmentDirections
import com.kardio.features.library.domain.model.StudySet
import com.kardio.features.library.presentation.adapter.StudySetAdapter
import com.kardio.features.library.presentation.viewmodel.StudySetsViewModel
import com.kardio.ui.components.feedback.QlzSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StudySetsFragment : BaseFragment<FragmentStudySetsBinding>() {

    private val viewModel: StudySetsViewModel by viewModels()
    private lateinit var studySetAdapter: StudySetAdapter

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentStudySetsBinding {
        return FragmentStudySetsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFilterSpinner()
        initRecyclerView()
        setupSwipeToRefresh()
    }

    private fun initFilterSpinner() {
        val filterOptions = resources.getStringArray(R.array.study_set_filter_options)
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            filterOptions
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spinnerFilter.adapter = spinnerAdapter
        binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedFilter = parent?.getItemAtPosition(position) as String
                viewModel.applyFilter(selectedFilter)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun initRecyclerView() {
        studySetAdapter = StudySetAdapter(
            onItemClick = { studySet ->
                navigateToStudySetDetail(studySet)
            }
        )

        binding.recyclerViewStudySets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = studySetAdapter
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshStudySets()
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    updateUi(state)
                }
            }
        }
    }

    private fun updateUi(state: StudySetsViewModel.StudySetsUiState) {
        // Update loading state
        binding.swipeRefreshLayout.isRefreshing = state.isLoading

        // Update views based on content availability
        binding.recyclerViewStudySets.isVisible = state.studySets.isNotEmpty()
        binding.layoutEmptyState.root.isVisible = state.studySets.isEmpty() && !state.isLoading
        binding.layoutErrorState.root.isVisible = state.error != null

        if (state.error != null) {
            binding.layoutErrorState.textViewErrorMessage.text = state.error
            QlzSnackbar.showError(requireContext(), state.error)
            viewModel.errorShown()
        }

        // Update adapter data
        studySetAdapter.submitList(state.studySets)
    }

    private fun navigateToStudySetDetail(studySet: StudySet) {
        // Example navigation - adjust according to your nav graph
         findNavController().navigate(
             R.id.action_libraryFragment_to_moduleDetailFragment,
             bundleOf("moduleId" to studySet.id)
         )
    }
}