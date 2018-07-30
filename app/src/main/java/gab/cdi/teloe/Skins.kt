package gab.cdi.teloe

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.R.attr.duration




/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Skins.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Skins.newInstance] factory method to
 * create an instance of this fragment.
 */
class Skins : Fragment() {
    private var skinsHashMapClicked : HashMap<Int,Boolean> = hashMapOf<Int,Boolean>(0 to false, 1 to false, 2 to false)
    private var skinsHashMapDeselect : HashMap<Int,Boolean> = hashMapOf<Int,Boolean>(0 to false, 1 to false, 2 to false)
    private lateinit var userSettings : SharedPreferences
    private lateinit var skinsContainer : LinearLayout
    private lateinit var customToastRoom : LinearLayout
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

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
        var view = inflater.inflate(R.layout.fragment_skins, container, false)
        skinsContainer = view.findViewById(R.id.skinsContainer)
        customToastRoom = view.findViewById(R.id.customToastRoom)
        getUserThemeSettings(view,inflater)
        setSkinsOnClickListeners(view,inflater)

        return view
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

    private fun setSkinsOnClickListeners(view : View, inflater : LayoutInflater){
        var skinsContainer : LinearLayout = view.findViewById(R.id.skinsContainer)
        val childCount = skinsContainer.childCount
        Log.d("Tag ","$childCount")
        for(count in 0..(childCount-1)){
            var ll = skinsContainer.getChildAt(count) as LinearLayout
            ll.setOnClickListener{ toggleSelected(count,skinsContainer, view, inflater, true)
                var changeUserSettings : SharedPreferences.Editor = this.activity.getSharedPreferences("User Settings",0).edit()
                changeUserSettings.putInt("themeUsed",count)
                saveThemeUsed(count)
                changeUserSettings.apply()
                getUserThemeSettings(view,inflater)
            }
        }
    }

    private fun toggleSelected(count : Int, skinsContainer : LinearLayout, view : View, inflater: LayoutInflater, switched :  Boolean){
        skinsHashMapClicked.putAll(skinsHashMapDeselect)
        skinsHashMapClicked.put(count,true)
        val hashSize = skinsHashMapClicked.size
        for(incount in 0..(hashSize-1)){
            if(skinsHashMapClicked.get(incount) == false){ //unhighlight
                val deselectLL = skinsContainer.getChildAt(incount) as LinearLayout
                val deselectText = deselectLL.getChildAt(1) as TextView
                deselectLL.setBackgroundColor(Color.parseColor("#333333"))
                deselectText.setTextColor(Color.parseColor("#F2F2F2"))
            }
            else{ //highlight
                val selectLL = skinsContainer.getChildAt(incount) as LinearLayout
                val selectText = selectLL.getChildAt(1) as TextView
                selectLL.setBackgroundColor(Color.parseColor("#C8C5BF"))
                selectText.setTextColor(Color.parseColor("#232323"))
                if(switched == true){
                    var toastOffset = intArrayOf(0,0)
                    customToastRoom.getLocationOnScreen(toastOffset)
                    Log.d("Tag ", "${toastOffset[0]} ${toastOffset[1]}")
                    toastOffset[1] -= 50
                    displayCustomToast(inflater,toastOffset, "You've switched to ${selectText.text}!")
                }
            }

        }
    }

    fun displayCustomToast(inflater : LayoutInflater, toastOffset : IntArray, message : String){
        var customToast = inflater.inflate(R.layout.teleo_toast,null)
        var customToastText = customToast.findViewById<TextView>(R.id.toast_message)
        customToastText.text = message
        var toast = Toast(activity.applicationContext)
        toast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL,toastOffset[0],toastOffset[1])
        toast.duration = Toast.LENGTH_SHORT
        toast.view = customToast
        toast.show()
    }

    private fun getUserThemeSettings(view : View, inflater : LayoutInflater){
        var themeUsed = userSettings.getInt("themeUsed",0)
        toggleSelected(themeUsed,skinsContainer,view,inflater,false)
    }

    private fun saveThemeUsed(theme : Int){
        var id = FirebaseAuth.getInstance().currentUser?.uid
        if(id != null){
            var userReference = FirebaseDatabase.getInstance().getReference("users").child(id).child("userSettings").child("themeUsed")
            userReference.setValue(theme)
        }

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
         * @return A new instance of fragment Skins.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): Skins {
            val fragment = Skins()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
