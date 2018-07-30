package gab.cdi.teloe

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage

class ScheduleAdapter (val programs : ArrayList<Program>, val context : Context) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>(){
    var storageReference = FirebaseStorage.getInstance()
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val thisProgram : Program = programs[position]

        holder.scheduleTextView.text = thisProgram.programAirTime

        val scheduleReference = storageReference.reference.child("programs/${thisProgram.programThumbnailUrl}")
        GlideApp
                .with(context)
                .load(scheduleReference)
                .into(holder.scheduleThumbnail)
    }

    override fun getItemCount(): Int {
        return programs.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val view = LayoutInflater.from(context).inflate(R.layout.schedule_item,parent,false)
        return ViewHolder(view)
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var scheduleTextView : TextView = view.findViewById(R.id.scheduleTime)
        var scheduleThumbnail : ImageView = view.findViewById(R.id.scheduleThumbnail)
    }


}