package gab.cdi.teloe

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

/**
 * Created by Default on 23/07/2018.
 */
class TeleoNotificationAdapter(val notifs : ArrayList<TeleoNotification>, val context : Context) : RecyclerView.Adapter<TeleoNotificationAdapter.ViewHolder>(){
    var storageReference = FirebaseStorage.getInstance()
    var NEW_EPISODE : Int = 1
    var COMMENT_REPLIED : Int = 2
    override fun getItemCount(): Int {
        return notifs.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view =  LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var thisNotification = notifs[position]
        var programReference = FirebaseDatabase.getInstance().getReference("programs")
        programReference.child(thisNotification.programId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot?) {
                val program = p0?.getValue(Program::class.java)
                val programName = program?.programName!!
                val programNameBoldLength = programName.length
                val contentString = SpannableStringBuilder("")
                val notifThumbnailReference = storageReference.reference.child("programs/${program.programThumbnailUrl}")
                GlideApp
                        .with(context)
                        .load(notifThumbnailReference)
                        .apply(RequestOptions().circleCrop())
                        .into(holder.notifThumbnail)
                when(thisNotification.eventType){
                    NEW_EPISODE -> {
                        contentString.append("${programName} has a new episode.")
                        contentString.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, programNameBoldLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        holder.notifContent.text = contentString
                    }
                    COMMENT_REPLIED -> {
                        var sourceIdReference = FirebaseDatabase.getInstance().getReference("users")
                        sourceIdReference.child(thisNotification.source).child("username").addValueEventListener(object  : ValueEventListener{
                            override fun onDataChange(p0: DataSnapshot?) {
                                var sourceName = p0?.value as String
                                var sourceNameBoldLength = sourceName.length + 8
                                contentString.append("${sourceName} replied on your comment on ")
                                contentString.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, sourceNameBoldLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                var boldProgramName = SpannableStringBuilder(programName)
                                boldProgramName.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, programNameBoldLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                contentString.append(boldProgramName)
                                holder.notifContent.text = contentString
                            }

                            override fun onCancelled(p0: DatabaseError?) {

                            }
                        })
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })

    }




    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var notifThumbnail : ImageView = view.findViewById(R.id.notificationThumbnail)
        var notifContent : TextView = view.findViewById(R.id.notificationContent)

    }
}