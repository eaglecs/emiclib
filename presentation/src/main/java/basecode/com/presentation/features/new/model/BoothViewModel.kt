package basecode.com.presentation.features.new.model

class BoothViewModel(
    val id: Long,
    val libId: Long,
    val symbol: String,
    val status: Boolean,
    val maxNumber: Int,
    val codeLoc: String,
    val isBooth: Boolean,
    val bLongitude: Double,
    val bLatitude: Double
)