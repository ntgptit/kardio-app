// app/src/main/java/com/kardio/features/solutions/presentation/ui/SolutionsFragment.kt

package com.kardio.features.solutions.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kardio.R
import com.kardio.core.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SolutionsFragment : Fragment() {

    // Lấy SharedViewModel từ fragment cha (HomeFragment)
    private val sharedViewModel: SharedViewModel by viewModels(
        { requireParentFragment() }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_solutions_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Thông báo cho SharedViewModel rằng đang ở solutions
        sharedViewModel.updateSelectedTab(1)
    }
}