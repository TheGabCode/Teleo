package gab.cdi.teloe

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.teleo_drawer.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    lateinit var whatsOn : TextView
    lateinit var toastLocation : LinearLayout
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

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


        var view = inflater.inflate(R.layout.fragment_home3, container, false)

        whatsOn = view.findViewById(R.id.whatsOnLayout)
        whatsOn.setOnClickListener {
            _ ->
                var fragment = WhatsOn()
                var ft = fragmentManager.beginTransaction()
                ft.replace(R.id.content_home,fragment)
                ft.commit()

        }
        toastLocation = view.findViewById(R.id.customToastRoom)
        var toastOffset = intArrayOf(0,0)
        toastLocation.getLocationOnScreen(toastOffset)

        var viewTreeObserver = toastLocation.viewTreeObserver
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                toastLocation.viewTreeObserver.removeGlobalOnLayoutListener(this)
                toastOffset[0] = toastLocation.x.toInt()
                toastOffset[1] = toastLocation.y.toInt()

            }
        })
        if(activity.intent.getSerializableExtra("fromSignup") == true){
            displayCustomToast(inflater,view,toastOffset)
            activity.intent.putExtra("fromSignup",false)
        }
        return view
    }

    fun displayCustomToast(inflater : LayoutInflater, view : View, toastOffset : IntArray){
        Log.d("Tag ", "${toastOffset[0]} ${toastOffset[1]}")
        var customToast = inflater.inflate(R.layout.teleo_toast,null)
        var customToastText = customToast.findViewById<TextView>(R.id.toast_message)
        customToastText.text = "Sign-up success!"
        var toast = Toast(activity.applicationContext)
        toast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL,toastOffset[0],190)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = customToast
        toast.show()
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

        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */

        fun newInstance(param1: String, param2: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
