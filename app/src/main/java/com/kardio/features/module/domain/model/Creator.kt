// features/module/domain/model/Creator.kt
package com.kardio.features.module.domain.model

data class Creator(
    val id: String,
    val name: String,
    val avatar: String? = null,
    val isPremium: Boolean = false
)