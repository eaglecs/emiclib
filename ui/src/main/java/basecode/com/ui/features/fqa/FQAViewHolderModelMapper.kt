package basecode.com.ui.features.fqa

import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.mapper.Mapper
import basecode.com.domain.model.network.response.QAResponse

class FQAViewHolderModelMapper : Mapper<QAResponse, FQAViewHolderModel>() {
    override fun map(input: QAResponse): FQAViewHolderModel {
        return FQAViewHolderModel(id = input.id.valueOrZero(),
                anwser = input.answer.valueOrEmpty(),
                question = input.question.valueOrEmpty())
    }
}