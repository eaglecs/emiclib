package basecode.com.presentation.features.books

class BookViewModel(
        val id: Long,
        val name: String,
        var publisher: String,
        val photo: String,
        val author: String,
        var publishedYear: String
)