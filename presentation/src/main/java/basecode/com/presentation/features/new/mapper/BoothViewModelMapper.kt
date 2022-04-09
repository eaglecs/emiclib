package basecode.com.presentation.features.new.mapper

import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.extention.valueOrFalse
import basecode.com.domain.mapper.Mapper
import basecode.com.domain.model.network.response.BoothsResponse
import basecode.com.presentation.features.new.model.BoothViewModel

class BoothViewModelMapper : Mapper<BoothsResponse, List<BoothViewModel>>() {
    override fun map(input: BoothsResponse): List<BoothViewModel> {
        val lstBooth = mutableListOf<BoothViewModel>()
        input.forEach { booth ->
            lstBooth.add(BoothViewModel(id = booth.id.valueOrZero(),
            libId = booth.libId.valueOrZero(), bLatitude = booth.bLatitude.valueOrZero(),
            bLongitude = booth.bLongitude.valueOrZero(), status = booth.status.valueOrFalse(),
            codeLoc = booth.codeLoc.valueOrEmpty(), isBooth = booth.isBooth.valueOrFalse(),
            maxNumber = booth.maxNumber.valueOrZero(), symbol = booth.symbol.valueOrEmpty()))
        }

        return lstBooth
    }
}