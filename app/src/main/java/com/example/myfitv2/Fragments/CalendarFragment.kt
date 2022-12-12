package com.example.myfitv2.Fragments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitv2.Adapters.EventsAdapter
import com.example.myfitv2.DataClasses.Event
import com.example.myfitv2.DataClasses.EventHolder
import com.example.myfitv2.R
import com.example.myfitv2.databinding.CalendarDayBinding
import com.example.myfitv2.databinding.CalendarFragmentBinding
import com.example.myfitv2.databinding.CalendarHeaderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*


class CalendarFragment : BaseFragment(R.layout.calendar_fragment), HasBackButton {


    override val titleRes: Int = R.string.calendar_title

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    private val titleSameYearFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val titleFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
    private val selectionFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")
    private val events = mutableMapOf<LocalDate, List<Event>>()


    private lateinit var dbRef: DatabaseReference
    private var mAuth: FirebaseAuth? = null
    private var user : String? = null
    private var email : String? = null

    private lateinit var binding: CalendarFragmentBinding

    private val eventsAdapter = EventsAdapter {
        if (email == user ) {
            AlertDialog.Builder(requireContext()).setMessage(R.string.dialog_delete_confirmation)
                .setPositiveButton(R.string.delete) { _, _ ->
                    deleteEvent(it)
                }
                .setNegativeButton(R.string.close, null)
                .show()
        }
    }

    private val inputDialog by lazy {
        val editText = AppCompatEditText(requireContext())
        val layout = FrameLayout(requireContext()).apply {
            // Setting the padding on the EditText only pads the input area
            // not the entire EditText so we wrap it in a FrameLayout.
            val padding = dpToPx(20, requireContext())
            setPadding(padding, padding, padding, padding)
            addView(editText, FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        }
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.input_dialog_title))
            .setView(layout)
            .setPositiveButton(R.string.save) { _, _ ->
                saveEvent(editText.text.toString())
                // Prepare EditText for reuse.
                editText.setText("")
            }
            .setNegativeButton(R.string.close, null)
            .create()
            .apply {
                setOnShowListener {
                    // Show the keyboard
                    editText.requestFocus()
                    context.inputMethodManager
                        .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                }
                setOnDismissListener {
                    // Hide the keyboard
                    context.inputMethodManager
                        .toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addStatusBarColorUpdate(R.color.statusbar_color)

        email = arguments?.get("email").toString().replace(".", "")
        mAuth = FirebaseAuth.getInstance()
        user = mAuth?.currentUser?.email.toString().replace(".", "")
        dbRef = FirebaseDatabase.getInstance("https://myfitv3-760b1-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(email!!).child("events")





        binding = CalendarFragmentBinding.bind(view)
        binding.exThreeRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = eventsAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }

        binding.exThreeCalendar.monthScrollListener = {
            activityToolbar.title = if (it.yearMonth.year == today.year) {
                titleSameYearFormatter.format(it.yearMonth)
            } else {
                titleFormatter.format(it.yearMonth)
            }
            // Select the first day of the visible month.
            selectDate(it.yearMonth.atDay(1))
        }

        val daysOfWeek = daysOfWeek()
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(50)
        val endMonth = currentMonth.plusMonths(50)
        updateEventList()
        configureBinders(daysOfWeek)
        binding.exThreeCalendar.apply {
            setup(startMonth, endMonth, daysOfWeek.first())
            scrollToMonth(currentMonth)
        }

        if (savedInstanceState == null) {
            // Show today's events initially.
            binding.exThreeCalendar.post { selectDate(today) }
        }
        binding.exThreeAddButton.setOnClickListener { inputDialog.show() }

        if (email != user ) {binding.exThreeAddButton.makeInVisible()}




    }

    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.exThreeCalendar.notifyDateChanged(it) }
            binding.exThreeCalendar.notifyDateChanged(date)
            updateAdapterForDate(date)
        }
    }

    private fun saveEvent(text: String) {
        if (text.isBlank()) {
            Toast.makeText(requireContext(), R.string.empty_input_text, Toast.LENGTH_LONG)
                .show()
        } else {
            selectedDate?.let {
                events[it] = events[it].orEmpty().plus(Event(UUID.randomUUID().toString(), text, it))
                updateAdapterForDate(it)
                addEventToDatabase(Event(UUID.randomUUID().toString(), text, it))
            }
        }
    }

    private fun deleteEvent(event: Event) {
        val date = event.date
        if (date != null) {
            events[date] = events[date].orEmpty().minus(event)
            updateAdapterForDate(date)
            deleteEventFromDatabase(event)
        }
    }

    private fun updateAdapterForDate(date: LocalDate) {
        eventsAdapter.apply {
            events.clear()
            events.addAll(this@CalendarFragment.events[date].orEmpty())
            notifyDataSetChanged()
        }
        binding.exThreeSelectedDateText.text = selectionFormatter.format(date)
    }

    private fun updateEventList() {
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val event = snapshot.getValue(EventHolder::class.java)
                    events[LocalDate.parse(event?.date)] = events[LocalDate.parse(event?.date)].orEmpty().plus(Event(event?.id, event?.text,LocalDate.parse(event?.date)))
                    selectDate(LocalDate.parse(event?.date))
                    updateAdapterForDate(LocalDate.parse(event?.date))
                }
                selectDate(LocalDate.now())
                updateAdapterForDate(LocalDate.now())
            }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    private fun addEventToDatabase(event: Event) {
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map: MutableMap<String, Any> = HashMap()
                map["id"] = event.id.toString()
                map["text"] = event.text.toString()
                map["date"] = event.date.toString()
                dbRef.ref.child(event.id.toString()).updateChildren(map)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun deleteEventFromDatabase(event : Event) {
        dbRef.child(event.id.toString()).removeValue()
    }


    override fun onStart() {
        super.onStart()
        activityToolbar.setBackgroundColor(
            requireContext().getColorCompat(R.color.toolbar_color),
        )
    }

    override fun onStop() {
        super.onStop()
        activityToolbar.setBackgroundColor(
            requireContext().getColorCompat(R.color.colorPrimary),
        )
    }



    private fun configureBinders(daysOfWeek: List<DayOfWeek>) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = CalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        selectDate(day.date)
                    }
                }
            }
        }
        binding.exThreeCalendar.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val textView = container.binding.exThreeDayText
                val dotView = container.binding.exThreeDotView

                textView.text = data.date.dayOfMonth.toString()

                if (data.position == DayPosition.MonthDate) {
                    textView.makeVisible()
                    when (data.date) {
                        today -> {
                            textView.setTextColorRes(R.color.this_white)
                            textView.setBackgroundResource(R.drawable.today_bg)
                            dotView.makeInVisible()
                        }
                        selectedDate -> {
                            textView.setTextColorRes(R.color.this_blue)
                            textView.setBackgroundResource(R.drawable.selected_bg)
                            dotView.makeInVisible()
                        }
                        else -> {
                            textView.setTextColorRes(R.color.this_black)
                            textView.background = null
                            dotView.isVisible = events[data.date].orEmpty().isNotEmpty()
                        }
                    }
                } else {
                    textView.makeInVisible()
                    dotView.makeInVisible()
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = CalendarHeaderBinding.bind(view).legendLayout.root
        }
        binding.exThreeCalendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    // Setup each header day text if we have not done that already.
                    if (container.legendLayout.tag == null) {
                        container.legendLayout.tag = data.yearMonth
                        container.legendLayout.children.map { it as TextView }
                            .forEachIndexed { index, tv ->
                                tv.text = daysOfWeek[index].name.first().toString()
                                tv.setTextColorRes(R.color.this_black)
                            }
                    }
                }
            }
    }


    private fun showNotification (notification : String) { Toast.makeText(context,notification, Toast.LENGTH_SHORT).show() }

}
