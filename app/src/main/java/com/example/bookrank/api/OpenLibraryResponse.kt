package com.example.bookrank.api

data class OpenLibraryResponse(
    val docs: List<BookAdapter>
)

data class BookAdapter(
    val title: String,
    val author_name: List<String>?
)