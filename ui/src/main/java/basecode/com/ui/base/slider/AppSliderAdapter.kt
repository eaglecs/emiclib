package basecode.com.ui.base.slider

import ss.com.bannerslider.adapters.SliderAdapter
import ss.com.bannerslider.viewholder.ImageSlideViewHolder

class AppSliderAdapter(private val lstData: MutableList<String>) : SliderAdapter() {
    override fun getItemCount(): Int = lstData.size

    override fun onBindImageSlide(position: Int, viewHolder: ImageSlideViewHolder) {
        val url = lstData[position]
        viewHolder.bindImageSlide(url)
    }
}