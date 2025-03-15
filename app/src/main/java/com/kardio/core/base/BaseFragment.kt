// File: app/src/main/java/com/kardio/core/base/BaseFragment.kt
package com.kardio.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Base Fragment class that provides common functionality and setup for all Fragments.
 * Includes support for ViewBinding and collecting StateFlow.
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    /**
     * Abstract method to provide ViewBinding instance.
     * Must be implemented by child classes.
     */
    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeData()
    }

    /**
     * Setup views and UI components.
     * Should be implemented by child fragments.
     */
    open fun setupViews() {}

    /**
     * Observe ViewModel data.
     * Should be implemented by child fragments.
     */
    open fun observeData() {}

    /**
     * Helper function to collect StateFlow values in a lifecycle-aware manner.
     */
    protected fun <T> Flow<T>.collectWithLifecycle(action: suspend (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                this@collectWithLifecycle.collectLatest(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}