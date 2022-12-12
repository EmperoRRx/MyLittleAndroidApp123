package com.example.myfitv2.Activities



import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitv2.Fragments.layoutInflater
import com.example.myfitv2.DataClasses.Animation
import com.example.myfitv2.DataClasses.CalendarHolder
import com.example.myfitv2.R
import com.example.myfitv2.databinding.CalendarViewOptionsItemAdapterBinding



val vertical = Animation(
    enter = R.anim.slide_in_up,
    exit = R.anim.fade_out,
    popEnter = R.anim.fade_in,
    popExit = R.anim.slide_out_down,
)

val horizontal = Animation(
    enter = R.anim.slide_in_right,
    exit = R.anim.slide_out_left,
    popEnter = R.anim.slide_in_left,
    popExit = R.anim.slide_out_right,
)

class CalendarViewAdapter (var calendarList: List<CalendarHolder>, val onClick: (CalendarHolder) -> Unit) :RecyclerView.Adapter<CalendarViewAdapter.HomeOptionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOptionsViewHolder {
        return HomeOptionsViewHolder(CalendarViewOptionsItemAdapterBinding.inflate(parent.context.layoutInflater,parent,false,),)
    }

    override fun onBindViewHolder(viewHolder: HomeOptionsViewHolder, position: Int) {
        viewHolder.bind(calendarList[position])
    }

    override fun getItemCount(): Int = calendarList.size

    inner class HomeOptionsViewHolder(private val binding: CalendarViewOptionsItemAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick(calendarList[bindingAdapterPosition],)
            }
        }

        fun bind(item: CalendarHolder) {
            binding.itemOptionTitle.text = item.titleRes
            binding.itemOptionSubtitle.text = item.subtitleRes
        }
    }
}