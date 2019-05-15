package basecode.com.domain.usecase.base

interface ResultListener<in Result, in Error> {
    fun success(data: Result)
    fun fail(error: Error)
    fun done()
}