// File: app/src/main/java/com/kardio/core/base/BaseActivity.kt
package com.kardio.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Base Activity class that provides common functionality and setup for all Activities.
 * Includes support for ViewBinding and collecting StateFlow.
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    /**
     * Abstract method to provide ViewBinding instance.
     * Must be implemented by child classes.
     */
    abstract fun getViewBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(binding.root)

        setupViews()
        observeData()
    }

    /**
     * Setup views and UI components.
     * Should be implemented by child activities.
     */
    open fun setupViews() {}

    /**
     * Observe ViewModel data.
     * Should be implemented by child activities.
     */
    open fun observeData() {}

    /**
     * Helper function to collect StateFlow values in a lifecycle-aware manner.
     */
    protected fun <T> Flow<T>.collectWithLifecycle(action: suspend (T) -> Unit) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                this@collectWithLifecycle.collectLatest(action)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}