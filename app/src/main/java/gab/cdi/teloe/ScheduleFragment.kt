package gab.cdi.teloe

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat


class ScheduleFragment : Fragment() {
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null
    private var weeklySchedule : ArrayList<ArrayList<Program>> = ArrayList(7) //arraylist of arraylist of programs
    private var weeklyRecyclerView : ArrayList<RecyclerView> = ArrayList(7)
    private var days : HashMap<Int,String> = hashMapOf(0 to "monday",1 to "tuesday",2 to "wednesday",3 to "thursday", 4 to "friday", 5 to "thursday", 6 to "friday")
    private var arrayPrograms : ArrayList<Program> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)
        initializeArrayLists(view)
        populateWeeklySchedule()
        return view
    }

    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    private fun initializeArrayLists(view : View){
        Log.d("Tag ",weeklySchedule.size.toString())
        for(i in 0..6) { weeklySchedule.add(ArrayList()) }
        weeklyRecyclerView.add(view.findViewById(R.id.mondayShowsRecyclerView))
        weeklyRecyclerView.add(view.findViewById(R.id.tuesdayShowsRecyclerView))
        weeklyRecyclerView.add(view.findViewById(R.id.wednesdayShowsRecyclerView))
        weeklyRecyclerView.add(view.findViewById(R.id.thursdayShowsRecyclerView))
        weeklyRecyclerView.add(view.findViewById(R.id.fridayShowsRecyclerView))
        weeklyRecyclerView.add(view.findViewById(R.id.saturdayShowsRecyclerView))
        weeklyRecyclerView.add(view.findViewById(R.id.sundayShowsRecyclerView))

    }
    private fun populateWeeklySchedule(){
        val programsReference = FirebaseDatabase.getInstance().getReference("programs")
        programsReference.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot?) {
                arrayPrograms.clear()
                for(count in 0..6){
                    weeklySchedule[count].clear()
                    weeklyRecyclerView[count].layoutManager = GridLayoutManager(activity,2)
                    weeklyRecyclerView[count].adapter = ScheduleAdapter(weeklySchedule[count],activity)
                    weeklyRecyclerView[count].isNestedScrollingEnabled = false
                }
                p0?.children?.mapNotNullTo(arrayPrograms){
                    it.getValue(Program::class.java)
                }
                val timeFormatA = SimpleDateFormat("hh:mm a")
                val timeFormat = SimpleDateFormat("HH:MM")


                arrayPrograms.sortBy({ timeFormat.format(timeFormatA.parse(it.programAirTime))})

                for(k in 0..6){
                    for(i in 0 until arrayPrograms.size){
                        if(arrayPrograms[i].daysAired[days[k]] == true){
                            weeklySchedule[k].add(arrayPrograms[i])
                        }

                    }
                }
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScheduleFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): ScheduleFragment {
            val fragment = ScheduleFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
