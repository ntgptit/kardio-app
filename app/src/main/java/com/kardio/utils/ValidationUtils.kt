// utils/ValidationUtils.kt
package com.kardio.utils

import android.util.Patterns
import java.util.regex.Pattern

object ValidationUtils {

    private const val MIN_PASSWORD_LENGTH = 6
    private const val MIN_DISPLAY_NAME_LENGTH = 3

    /**
     * Kiểm tra tính hợp lệ của địa chỉ email
     */
    fun validateEmail(email: String): String? {
        if (email.isEmpty()) {
            return "Email không được để trống"
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Email không hợp lệ"
        }

        return null
    }

    /**
     * Kiểm tra tính hợp lệ của mật khẩu
     */
    fun validatePassword(password: String): String? {
        if (password.isEmpty()) {
            return "Mật khẩu không được để trống"
        }

        if (password.length < MIN_PASSWORD_LENGTH) {
            return "Mật khẩu phải có ít nhất $MIN_PASSWORD_LENGTH ký tự"
        }

        return null
    }

    /**
     * Kiểm tra xác nhận mật khẩu
     */
    fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        if (confirmPassword.isEmpty()) {
            return "Xác nhận mật khẩu không được để trống"
        }

        if (password != confirmPassword) {
            return "Mật khẩu xác nhận không khớp"
        }

        return null
    }

    /**
     * Kiểm tra tính hợp lệ của tên hiển thị
     */
    fun validateDisplayName(displayName: String): String? {
        if (displayName.isEmpty()) {
            return "Tên hiển thị không được để trống"
        }

        if (displayName.length < MIN_DISPLAY_NAME_LENGTH) {
            return "Tên hiển thị phải có ít nhất $MIN_DISPLAY_NAME_LENGTH ký tự"
        }

        return null
    }
}