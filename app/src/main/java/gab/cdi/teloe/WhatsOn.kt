package gab.cdi.teloe

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class WhatsOn : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    private var programs : ArrayList<Program> = ArrayList()
    private var programRecyclerView : RecyclerView? = null
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
        val view = inflater.inflate(R.layout.fragment_whats_on2, container, false)
        programRecyclerView = view.findViewById(R.id.whatsOnProgramRecyclerView)

//        programRecyclerView?.layoutManager = LinearLayoutManager(activity)
//        programRecyclerView?.adapter = ProgramAdapter(programs,activity,0)
        initializeWhatsOnPrograms(activity)
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

    private fun  initializeWhatsOnPrograms(activity: Activity){
//        var programsReference = FirebaseDatabase.getInstance().getReference("programs")
//        programsReference.setValue(null)
//        programsReference.push().setValue(Program("Vlog TV","vlog_tv.png","Your favorite vloggers are now on TV! (MORE)","7:00AM",-1))
//        programsReference.push().setValue(Program("Tripinas","tripinas.png","Trip around Pinas! (MORE)","3:00PM",-1))
//        programsReference.push().setValue(Program("The Reset - Spoilers","thereset.png","The Reset PH gang talks about Marvel: Infinity (MORE)","3:00PM",-1))
        val programsReference = FirebaseDatabase.getInstance().getReference("programs")
        programsReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot?) {
                programs.clear()
                programRecyclerView?.layoutManager = LinearLayoutManager(activity)
                programRecyclerView?.adapter = ProgramAdapter(programs,activity,0)
                programRecyclerView?.isNestedScrollingEnabled = false
                p0?.children?.mapNotNullTo(programs){
                    it.getValue<Program>(Program::class.java)
                }
            }
            override fun onCancelled(p0: DatabaseError?) {}
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
         * @return A new instance of fragment WhatsOn.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): WhatsOn {
            val fragment = WhatsOn()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
