// ui/components/inputs/QlzTextFieldExtensions.kt
package com.kardio.ui.components.inputs

/**
 * Lấy nội dung text của QlzTextField
 */
fun QlzTextField.getText(): String {
    return this.getEditText().text.toString()
}

/**
 * Đặt nội dung cho QlzTextField
 */
fun QlzTextField.setText(text: String) {
    this.getEditText().setText(text)
}