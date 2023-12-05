package tn.gsi.studybee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FileAdapter(private val dataList:ArrayList<FileClass>): RecyclerView.Adapter<FileAdapter.ViewHolderClass>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView =  LayoutInflater.from(parent.context).inflate(R.layout.file_layout , parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

       val currentItem = dataList [position]
       holder.rvImage.setImageResource(currentItem.dataImage)
        holder.rvTitle.text = currentItem.dataTitle
    }

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val rvImage :ImageView = itemView.findViewById(R.id.fileimage)
        val rvTitle : TextView = itemView.findViewById(R.id.filetitle)

    }
}