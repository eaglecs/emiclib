package basecode.com.ui.features.new.mapper

import basecode.com.domain.mapper.Mapper
import basecode.com.presentation.features.new.model.BoothViewModel
import basecode.com.ui.features.new.viewholder.BoothViewHolderModel
import basecode.com.ui.util.LocationUtil
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class BoothViewHolderModelMapper :
    Mapper<BoothViewHolderModelMapper.Input, List<BoothViewHolderModel>>(), KoinComponent {
    private val locationUtil by inject<LocationUtil>()
    override fun map(input: Input): List<BoothViewHolderModel> {
        val lstBooth = mutableListOf<BoothViewHolderModel>()
        input.booths.forEach { booth ->
            val distance = locationUtil.getDistance(
                fromLat = input.lat,
                fromLng = input.lng,
                toLat = booth.bLatitude,
                toLng = booth.bLongitude
            )
            lstBooth.add(BoothViewHolderModel(
                id = booth.id,
                libId = booth.libId,
                bLatitude = booth.bLatitude,
                bLongitude = booth.bLongitude,
                symbol = booth.symbol,
                codeLoc = booth.codeLoc,
                isBooth = booth.isBooth,
                status = booth.status,
                maxNumber = booth.maxNumber,
                distance = distance,
                distanceValue = locationUtil.getDistanceValue(
                    fromLat = input.lat,
                    fromLng = input.lng,
                    toLat = booth.bLatitude,
                    toLng = booth.bLongitude
            )))
        }

        return lstBooth
    }

    class Input(val lat: Double, val lng: Double, val booths: List<BoothViewModel>)
}