package gab.cdi.teloe

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.app.Notification
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_notification.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NotificationFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NotificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    private var notifs : ArrayList<TeleoNotification> = ArrayList()
    private var notificationsRecyclerView : RecyclerView? = null

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
        var view = inflater.inflate(R.layout.fragment_notification, container, false)
        notificationsRecyclerView = view.findViewById(R.id.notificationRecyclerView)
        notificationsRecyclerView?.isNestedScrollingEnabled = false
        fetchNotifications(activity)
        return view
    }


    private fun fetchNotifications(activity: Activity){
        var currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        var userReferenceNotifications = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("notifications")
        userReferenceNotifications.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot?) {
                if(p0 != null){
                    notifs.clear()
                    notificationsRecyclerView?.layoutManager = LinearLayoutManager(activity)
                    notificationsRecyclerView?.adapter = TeleoNotificationAdapter(notifs,activity)
                    p0.children?.mapNotNullTo(notifs){
                        it.getValue(TeleoNotification::class.java)
                    }

                }
                else{
                    Log.d("Tag ", "No notifications")
                }
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }

//    private fun initializeDummyNotifThumbnails(view : View){
//        GlideApp.with(activity)
//                .load(R.drawable.thereset)
//                .apply(RequestOptions().circleCrop())
//                .into(view.findViewById(R.id.firstNotif))
//        GlideApp.with(activity)
//                .load(R.drawable.vlog_tv)
//                .apply(RequestOptions().circleCrop())
//                .into(view.findViewById(R.id.secondNotif))
//        GlideApp.with(activity)
//                .load(R.drawable.teloe)
//                .apply(RequestOptions().circleCrop())
//                .into(view.findViewById(R.id.thirdNotif))
//    }
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
         * @return A new instance of fragment NotificationFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): NotificationFragment {
            val fragment = NotificationFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
