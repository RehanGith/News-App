package com.example.newsapp.model

import java.io.Serializable


data class Source(
    val id: String? = "",
    val name: String?
) : Serializable