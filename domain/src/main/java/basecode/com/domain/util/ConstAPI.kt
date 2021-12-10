package basecode.com.domain.util

object ConstAPI {
    enum class DocType(val value: Int){
        Book(0), Image(1), Video(2),SpeakBook(3),Thesis(-1),Ebook(4)
    }
}