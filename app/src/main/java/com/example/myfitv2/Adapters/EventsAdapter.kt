package com.example.myfitv2.Adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitv2.Fragments.layoutInflater
import com.example.myfitv2.DataClasses.Event
import com.example.myfitv2.databinding.EventItemViewBinding

class EventsAdapter(val onClick: (Event) -> Unit) : RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    val events = mutableListOf<Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder(
            EventItemViewBinding.inflate(parent.context.layoutInflater, parent, false),
        )
    }

    override fun onBindViewHolder(viewHolder: EventsViewHolder, position: Int) {
        viewHolder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    inner class EventsViewHolder(private val binding: EventItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick(events[bindingAdapterPosition])
            }
        }

        fun bind(event: Event) {binding.itemEventText.text = event.text}
    }
}