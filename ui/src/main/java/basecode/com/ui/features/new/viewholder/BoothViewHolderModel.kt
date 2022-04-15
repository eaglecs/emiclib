package basecode.com.ui.features.new.viewholder

import basecode.com.ui.base.listview.model.ViewHolderModel

class BoothViewHolderModel(
    val id: Long,
    val libId: Long,
    val symbol: String,
    val status: Boolean,
    val maxNumber: Int,
    val codeLoc: String,
    val isBooth: Boolean,
    val bLongitude: Double,
    val bLatitude: Double,
    val distanceValue : Float,
    val distance: String = ""
) : ViewHolderModel