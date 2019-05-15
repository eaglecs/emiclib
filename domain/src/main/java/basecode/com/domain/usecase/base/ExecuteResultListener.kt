package basecode.com.domain.usecase.base

class ExecuteResultListener<SuccessOutput, FailOutput> : ResultListener<SuccessOutput, FailOutput> {
    var isSuccess = false
    var successOutput: SuccessOutput? = null
    var failOutput: FailOutput? = null
    override fun success(data: SuccessOutput) {
        isSuccess = true
        this.successOutput = successOutput
    }

    override fun fail(error: FailOutput) {
        isSuccess = false
        this.failOutput = failOutput
    }

    override fun done() {

    }
}