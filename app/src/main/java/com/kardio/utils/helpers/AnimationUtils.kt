package com.kardio.utils.helpers

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.PathInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.kardio.R

/**
 * Utility class to handle animations across the app với tối ưu hóa dành cho Dark Mode.
 * Loại bỏ các hiệu ứng phức tạp và giảm thiểu sự tiêu thụ pin.
 */
object AnimationUtils {

    // Animation Durations - Tối ưu hóa cho mode tối
    private const val DURATION_VERY_FAST = 120  // Giảm từ 150 xuống 120ms
    private const val DURATION_FAST = 180       // Giảm từ 200 xuống 180ms
    private const val DURATION_MEDIUM = 250     // Giảm từ 300 xuống 250ms

    // Interpolator đơn giản theo chuẩn Material Design
    private val STANDARD_CURVE = FastOutSlowInInterpolator()

    /**
     * Đơn giản hóa hiệu ứng nhấn nút (nhấn/nhả) với haptic feedback nhẹ
     */
    fun applyButtonClickAnimation(view: View, context: Context) {
        val duration = DURATION_FAST / 2

        // Chỉ thực hiện animation scale down rồi trở lại, bỏ qua effect phức tạp
        view.animate()
            .scaleX(0.97f)
            .scaleY(0.97f)
            .setDuration(duration.toLong())
            .setInterpolator(STANDARD_CURVE)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(duration.toLong())
                    .setInterpolator(STANDARD_CURVE)
                    .start()
            }
            .withStartAction {
                // Sử dụng haptic nhẹ
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            }
            .start()
    }

    /**
     * Đơn giản hóa hiệu ứng fade in
     */
    fun fadeIn(view: View, context: Context, onEnd: (() -> Unit)? = null) {
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate()
            .alpha(1f)
            .setDuration(DURATION_MEDIUM.toLong())
            .setInterpolator(STANDARD_CURVE)
            .withEndAction { onEnd?.invoke() }
            .start()
    }

    /**
     * Đơn giản hóa hiệu ứng fade out
     */
    fun fadeOut(view: View, context: Context, onEnd: (() -> Unit)? = null) {
        view.animate()
            .alpha(0f)
            .setDuration(DURATION_MEDIUM.toLong())
            .setInterpolator(STANDARD_CURVE)
            .withEndAction {
                view.visibility = View.GONE
                onEnd?.invoke()
            }
            .start()
    }

    /**
     * Đơn giản hóa animation khi load danh sách
     * Thay vì animation từng item, chỉ áp dụng fade in đơn giản cho toàn bộ container
     */
    fun fadeInContainer(container: ViewGroup, context: Context) {
        fadeIn(container, context)
    }

    /**
     * Animation chuyển tab đơn giản
     */
    fun crossFade(oldView: View, newView: View, context: Context) {
        // Ẩn view cũ
        oldView.animate()
            .alpha(0f)
            .setDuration(DURATION_MEDIUM.toLong())
            .setInterpolator(STANDARD_CURVE)
            .withEndAction { oldView.visibility = View.GONE }
            .start()

        // Hiện view mới
        newView.alpha = 0f
        newView.visibility = View.VISIBLE
        newView.animate()
            .alpha(1f)
            .setDuration(DURATION_MEDIUM.toLong())
            .setInterpolator(STANDARD_CURVE)
            .start()
    }
}