package gab.cdi.teloe

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_region.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RegionFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RegionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegionFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var regionHashMapClicked : HashMap<Int,Boolean> = hashMapOf<Int,Boolean>(0 to false, 1 to false, 2 to false,3 to false,4 to false)
    private var regionHashMapDeselect : HashMap<Int,Boolean> = hashMapOf<Int,Boolean>(0 to false, 1 to false, 2 to false,3 to false,4 to false)
    private lateinit var regionContainer : LinearLayout
    private lateinit var userSettings : SharedPreferences
    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
        userSettings  = this.activity.getSharedPreferences("User Settings",0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_region, container, false)
        regionContainer = view.findViewById(R.id.regionContainer)
        getUserRegionSettings()
        setRegionOnClickListeners(view)
        return view
    }

    private fun setRegionOnClickListeners(view : View){
        val regionContainer : LinearLayout = view.findViewById(R.id.regionContainer)
        val childCount = regionContainer.childCount
        for(count in 0..childCount){
            if(regionContainer.getChildAt(count) is LinearLayout){
                var ll = regionContainer.getChildAt(count) as LinearLayout
                ll.setOnClickListener{ toggleSelected(count, regionContainer)
                    var changeUserSettings : SharedPreferences.Editor = this.activity.getSharedPreferences("User Settings",0).edit()
                    changeUserSettings.putInt("preferedRegion",count)
                    savePreferedRegion(count)
                    changeUserSettings.apply()
                    getUserRegionSettings()
                }
            }
        }
    }

    private fun getUserRegionSettings(){
        var preferedRegion : Int = userSettings.getInt("preferedRegion",0)
        toggleSelected(preferedRegion,regionContainer)
    }

    private fun toggleSelected(count : Int, regionContainer : LinearLayout){
        regionHashMapClicked.putAll(regionHashMapDeselect)
        regionHashMapClicked.put(count,true)
        val hashSize = regionHashMapClicked.size
        for(incount in 0..(hashSize-1)){
            if(regionHashMapClicked.get(incount) == false){ //unhighlight
                val deselectLL = regionContainer.getChildAt(incount) as LinearLayout
                val deselectInnerLL = deselectLL.getChildAt(0) as LinearLayout
                val hideSubregions = deselectLL.getChildAt(1) as LinearLayout
                hideSubregions.visibility = View.GONE
                val deselectTextView = deselectInnerLL.getChildAt(0) as TextView
                deselectTextView.setTextColor(Color.parseColor("#F2F2F2"))
                deselectLL.setBackgroundColor(Color.parseColor("#4A4C49"))
                regionHashMapClicked.put(incount,false)
            }
            else{ //highlight
                val selectLL = regionContainer.getChildAt(incount) as LinearLayout
                val selectInnerLL = selectLL.getChildAt(0) as LinearLayout
                val showSubregions = selectLL.getChildAt(1) as LinearLayout
                showSubregions.visibility = View.VISIBLE
                val selectTextView = selectInnerLL.getChildAt(0) as TextView
                selectTextView.setTextColor(Color.parseColor("#232323"))
                selectLL.setBackgroundColor(Color.parseColor("#C8C5BF"))
            }
        }
    }

    private fun savePreferedRegion(region : Int){
        var id = FirebaseAuth.getInstance().currentUser?.uid
        if(id != null){
            var usersReference = FirebaseDatabase.getInstance().getReference("users").child(id).child("userSettings").child("preferedRegion")
            usersReference.setValue(region)
        }

    }


    // TODO: Rename method, update argument and hook method into UI event
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
         * @return A new instance of fragment RegionFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): RegionFragment {
            val fragment = RegionFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
