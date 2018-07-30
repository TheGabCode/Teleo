package gab.cdi.teloe

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.app_bar_home.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MandeleiTV.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MandeleiTV.newInstance] factory method to
 * create an instance of this fragment.
 */
class MandeleiTV : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private lateinit var globalInflater : LayoutInflater
    private var globalContainer : ViewGroup? = null
    private var globalSavedInstanceState : Bundle? = null
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
        globalContainer = container
        globalInflater = inflater
        globalSavedInstanceState = savedInstanceState
        return initUI(globalContainer,inflater)
    }

    private fun initUI(container : ViewGroup?, inflater: LayoutInflater) : View {
        if(container != null)  container.removeAllViewsInLayout()
        var toolbar = activity.toolbar
        var view : View? = null
        var orientation = activity.resources.configuration.orientation
        when(mParam1){
            "classic" -> {
                if(orientation == Configuration.ORIENTATION_PORTRAIT){
                    view = inflater.inflate(R.layout.fragment_mandelei_tv1980, container, false)
                    toolbar.setBackgroundColor(Color.parseColor("#282A29"))
                }
                else{
                    view = inflater.inflate(R.layout.fragment_mandelei_tv1980, container, false)
                }
            }
            "retro" -> {
                if(orientation == Configuration.ORIENTATION_PORTRAIT){
                    view = inflater.inflate(R.layout.fragment_mandelei_tv1950, container, false)
                    toolbar.setBackgroundColor(Color.parseColor("#453028"))
                    var logoContainer = toolbar.getChildAt(0) as LinearLayout
                    var logo = logoContainer.getChildAt(0) as ImageView
                    logo.visibility = View.INVISIBLE
                }
                else{
                    view = inflater.inflate(R.layout.fragment_mandelei_tv1950, container, false)
                }
            }
            "modern" -> {
                if(orientation == Configuration.ORIENTATION_PORTRAIT){
                    view = inflater.inflate(R.layout.fragment_mandelei_tvmodern, container, false)
                    var logoContainer = activity.toolbar.getChildAt(0) as LinearLayout
                    var logo = logoContainer.getChildAt(0) as ImageView
                    logo.setImageResource(R.drawable.modern_teleo_logo)
                }
                else{
                    view = inflater.inflate(R.layout.fragment_mandelei_tvmodern, container, false)
                }

            }
            "1940" -> {
                if(orientation == Configuration.ORIENTATION_PORTRAIT){
                    view = inflater.inflate(R.layout.fragment_mandelei_tv1940, container, false)
                    toolbar.setBackgroundColor(Color.parseColor("#392719"))
                    var logoContainer = toolbar.getChildAt(0) as LinearLayout
                    var logo = logoContainer.getChildAt(0) as ImageView
                    logo.visibility = View.INVISIBLE
                }
                else{
                    view = inflater.inflate(R.layout.fragment_mandelei_tv1940, container, false)
                }
            }
            "1950" -> {
                if(orientation == Configuration.ORIENTATION_PORTRAIT){
                    view = inflater.inflate(R.layout.fragment_mandelei_tv1950, container, false)
                    toolbar.setBackgroundColor(Color.parseColor("#453028"))
                    var logoContainer = toolbar.getChildAt(0) as LinearLayout
                    var logo = logoContainer.getChildAt(0) as ImageView
                    logo.visibility = View.INVISIBLE
                }
                else{
                    view = inflater.inflate(R.layout.fragment_mandelei_tv1950, container, false)
                }
            }
            "1960" -> {
                if(orientation == Configuration.ORIENTATION_PORTRAIT){
                    view = inflater.inflate(R.layout.fragment_mandelei_tv1960, container, false)
                    toolbar.setBackgroundColor(Color.parseColor("#1B1F1C"))
                }
                else{
                    view = inflater.inflate(R.layout.fragment_mandelei_tv1960, container, false)
                }
            }
            "1970" -> {
                if(orientation == Configuration.ORIENTATION_PORTRAIT){
                    view = inflater.inflate(R.layout.fragment_mandelei_tv1970, container, false)
                    toolbar.setBackgroundColor(Color.parseColor("#1B1F1C"))
                }
                else{
                    view = inflater.inflate(R.layout.fragment_mandelei_tv1970, container, false)
                }
            }
            "1980" -> {
                if(orientation == Configuration.ORIENTATION_PORTRAIT){
                    view = inflater.inflate(R.layout.fragment_mandelei_tv1980, container, false)
                    toolbar.setBackgroundColor(Color.parseColor("#282A29"))
                }
                else{
                    view = inflater.inflate(R.layout.fragment_mandelei_tv1980, container, false)
                }

            }
            "1990" -> {
                if(orientation == Configuration.ORIENTATION_PORTRAIT){
                    view = inflater.inflate(R.layout.fragment_mandelei_tv1990, container, false)
                    toolbar.setBackgroundColor(Color.parseColor("#1B1F1C"))
                }
                else{
                    view = inflater.inflate(R.layout.fragment_mandelei_tv1990, container, false)
                }
            }

            else -> view = inflater.inflate(R.layout.fragment_mandelei_tvretro, container, false)
        }
        if(orientation == Configuration.ORIENTATION_LANDSCAPE)  toolbar.visibility = View.GONE
        else toolbar.visibility = View.VISIBLE
        return view
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        var view = initUI(globalContainer,globalInflater)
        globalContainer?.addView(view)
//        toggleLayout()
    }

    private fun toggleLayout(){
        var ft = fragmentManager.beginTransaction()
        var theme : String = mParam1!!
        ft.replace(R.id.content_home,MandeleiTV.newInstance(theme,""))
        ft.commit()
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
        var toolbar = activity.toolbar
        toolbar.visibility = View.VISIBLE
        var logoContainer = activity.toolbar.getChildAt(0) as LinearLayout
        var logo = logoContainer.getChildAt(0) as ImageView
        logo.visibility = View.VISIBLE
        activity.toolbar.setBackgroundColor(Color.parseColor("#000000"))
        logo.setImageResource(R.drawable.teloe)
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
         * @return A new instance of fragment MandeleiTV.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): MandeleiTV {
            val fragment = MandeleiTV()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
