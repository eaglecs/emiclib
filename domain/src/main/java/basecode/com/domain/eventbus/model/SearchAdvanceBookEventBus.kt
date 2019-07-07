package basecode.com.domain.eventbus.model

class SearchAdvanceBookEventBus(val categoryId: Int,
                                val title: String,
                                val author: String,
                                val keyword: String,
                                val language: String)