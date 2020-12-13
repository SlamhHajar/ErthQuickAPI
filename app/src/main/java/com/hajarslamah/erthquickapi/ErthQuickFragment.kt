package com.hajarslamah.erthquickapi

import android.content.Context
import android.content.Intent
import android.icu.text.DecimalFormat
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

private const val TAG = "Erth"
class ErthQuickFragment : Fragment() {

    private lateinit var erthQuickRecyclerView: RecyclerView
    private lateinit var erthQuickViewModel: ErthQuickViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//            //    val ERthLiveData: LiveData<String> = ErthQuickFetcher().fetchContents()
//      val erthLiveData: LiveData<List<ErthData>> = ErthQuickFetcher().fetchContents()
//
//        erthLiveData.observe(
//            this,
//            Observer {
//            Log.d(TAG, "Response received: $it")            })

        erthQuickViewModel =
            ViewModelProviders.of(this).get(ErthQuickViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_erth_quick, container, false)
        erthQuickRecyclerView = view.findViewById(R.id.erth_recycler_view)
        erthQuickRecyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        erthQuickViewModel.erthItemsLiveData.observe(
            viewLifecycleOwner,
            Observer { erthItems ->
                Log.d(TAG, "Have erth items from ViewModel $erthItems")
                // Eventually, update data backing the recycler view
                erthQuickRecyclerView.adapter = ERthAdapter(erthItems)
            })
    }
    private class ErthHolder(itemTextView: View)
        : RecyclerView.ViewHolder(itemTextView) ,View.OnClickListener {
        var cordnate:List<Double> = emptyList()
        val placeTextView= itemTextView.findViewById(R.id.place) as TextView
        val countryTextView= itemTextView.findViewById(R.id.country) as TextView
        val magTextView= itemTextView.findViewById(R.id.mag) as TextView
        val dateTextView= itemTextView.findViewById(R.id.date) as TextView
        val timeTextView= itemTextView.findViewById(R.id.time) as TextView
        init {
            itemTextView.setOnClickListener(this)
        }
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(erthdate: ErthData){
            var date=milliscondToDate(erthdate.properties.time)
            var time=milliscondToTime(erthdate.properties.time)
          var date1=getDate(erthdate.properties.time, "MMM DD, yyyy")
            dateTextView.setText(date1)
            timeTextView.setText(time)
            val string = erthdate.properties.place
            val parts = string.split("of".toRegex()).toTypedArray()
            val address = parts[0] // 2km NNW
            val country = parts[1] // Palomar Observatory
            placeTextView.setText(country)
           countryTextView.setText(address)
            var mag = erthdate.properties.mag

            magTextView.setText(formatMag(mag))
                when{
                 mag in 2.0..3.9-> magTextView.setBackgroundResource(R.drawable.round_shape)
                       mag in  4.0..4.9-> magTextView.setBackgroundResource(R.drawable.round_yellow_shape)
                        mag in  5.0..5.9-> magTextView.setBackgroundResource(R.drawable.round_orange_shape)
                    mag in 6.0..10.0-> magTextView.setBackgroundResource(R.drawable.round_red_shape)
                 else ->magTextView.setBackgroundResource(R.drawable.round_shape)
                }
             cordnate=erthdate.geometry.coordinates}

        override fun onClick(p0: View?) {
            var longitude= cordnate[0]
            var  latitude=cordnate[1]
            val mapIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse("geo:$longitude,$latitude")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            requireActvity.startActivity(mapIntent)
           // mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          //  if (mapIntent.resolveActivity(packageManager) != null) {
             //   startActivity(mapIntent)
         //   }

        }


        @RequiresApi(Build.VERSION_CODES.N)
        private fun formatMag(mag: Double): String? {
            val magFormat = DecimalFormat("0.0")
            return magFormat.format(mag)
        }
        @RequiresApi(Build.VERSION_CODES.N)
        fun milliscondToDate(Datemilli: Long) : String {
            val timeInMilliseconds = Datemilli
            val dateObject = Date(timeInMilliseconds)
            val dateFormatter = SimpleDateFormat("MM d, yyyy")
            val dateToDisplay: String = dateFormatter.format(dateObject)
            return dateToDisplay
        }
        @RequiresApi(Build.VERSION_CODES.N)
        fun milliscondToTime(Datemilli: Long) : String {
            val timeInMilliseconds = Datemilli
            val dateObject = Date(timeInMilliseconds)
            val timeFormatter = SimpleDateFormat("hh:mm a")
            val timeToDisplay: String = timeFormatter.format(dateObject)
            return timeToDisplay
        }


        @RequiresApi(Build.VERSION_CODES.N)
        fun getDate(milliSeconds: Long, dateFormat: String?): String? {
            // Create a DateFormatter object for displaying date in specified format.
            val formatter = SimpleDateFormat(dateFormat)
           // Create a calendar object that will convert the date and time value in milliseconds to date.
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = milliSeconds
            return formatter.format(calendar.time)
        }


    }
    private class ERthAdapter(private val erthItems: List<ErthData>)
        : RecyclerView.Adapter<ErthHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ErthHolder {
         val v=   LayoutInflater.from(parent.context).inflate(
             R.layout.erthquick_details,
             parent,
             false
         )
            return ErthHolder(v)
        }
        override fun getItemCount(): Int = erthItems.size
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onBindViewHolder(holder: ErthHolder, position: Int) {
            val erthItems = erthItems[position]
            holder.bind(erthItems)
        }
    }

companion object {

        @JvmStatic
        fun newInstance() =
            ErthQuickFragment()
}}