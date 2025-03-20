// app/src/main/java/com/kardio/core/viewmodel/SharedViewModel.kt

package com.kardio.core.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel được chia sẻ giữa các Fragment con của HomeFragment.
 * Quản lý trạng thái và dữ liệu dùng chung giữa các tab.
 */
@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    // State cho tab đã chọn
    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    // State cho dữ liệu cần chia sẻ giữa các tab
    private val _sharedData = MutableStateFlow<Any?>(null)
    val sharedData: StateFlow<Any?> = _sharedData.asStateFlow()

    /**
     * Cập nhật tab đã chọn
     */
    fun updateSelectedTab(index: Int) {
        _selectedTabIndex.value = index
    }

    /**
     * Thiết lập dữ liệu chia sẻ
     */
    fun setSharedData(data: Any?) {
        _sharedData.value = data
    }
}