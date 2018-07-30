package gab.cdi.teloe

import android.app.*
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage

class ProgramAdapter(val programs : ArrayList<Program>, val context : Context, val programPurpose: Int) : RecyclerView.Adapter<ProgramAdapter.ViewHolder>() {
    var programPurposeAdapter = programPurpose
    var storageReference = FirebaseStorage.getInstance()
    val WHATS_ON : Int = 0
    val TRENDING : Int = 1
    val SHOWS : Int = 2
    val SCHEDULE : Int = 3


    override fun getItemCount(): Int {
        return programs.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view : View?

        view = LayoutInflater.from(context).inflate(R.layout.program_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var thisProgram = programs[position]
        when(programPurposeAdapter){
            TRENDING -> {
                holder.programNumber.text = thisProgram.programNumber.toString()
                holder.programNumber.visibility = View.VISIBLE

            }
            SHOWS -> {
                holder.programName.visibility = View.GONE
                holder.programDescription.visibility = View.GONE
            }
        }
        holder.programName.text = thisProgram.programName
        holder.programDescription.text = thisProgram.programDescription
        holder.programNumber.text = thisProgram.programNumber.toString()

        var thumbnailReference  = storageReference.reference.child("programs/${thisProgram.programThumbnailUrl}")
        Log.d("Tag ",thisProgram.programThumbnailUrl )
        GlideApp.with(context)
                .load(thumbnailReference)
                .into(holder.programThumbnail)

        holder.itemView.setOnClickListener {
            var fragment = ShowDrilldown.newInstance(thisProgram.programId,"")
            if(context is Activity){
                var ft = context.fragmentManager.beginTransaction()
                ft.replace(R.id.content_home,fragment)
                ft.commit()
            }
        }
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var programName : TextView = view.findViewById(R.id.programName)
        var programDescription : TextView = view.findViewById(R.id.programDescription)
        var programThumbnail : ImageView = view.findViewById(R.id.programThumbnail)
        var programNumber : TextView = view.findViewById(R.id.programNumber)

    }
}