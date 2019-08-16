package basecode.com.domain.features.mapper

import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.mapper.Mapper
import basecode.com.domain.model.network.response.InfoUserResponse
import basecode.com.domain.model.network.response.UserModel

class UserModelMapper : Mapper<InfoUserResponse, UserModel>() {
    override fun map(input: InfoUserResponse): UserModel {
        return UserModel(patronCode = input.patronCode.valueOrEmpty(),
                expiredDate = input.expiredDate.valueOrEmpty(),
                patronGroup = input.patronGroup.valueOrEmpty(),
                patronName = input.patronName.valueOrEmpty(),
                validDate = input.validDate.valueOrEmpty())
    }
}