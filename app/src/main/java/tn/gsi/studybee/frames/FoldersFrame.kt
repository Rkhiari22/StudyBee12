package tn.gsi.studybee.frames

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tn.gsi.studybee.adapter.FolderAdapter
import tn.gsi.studybee.R

class FoldersFrame : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {



        val view = inflater?.inflate(R.layout.folders, container, false)

        val folderRecyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)
        folderRecyclerView?.adapter = FolderAdapter()

        val layoutManager = LinearLayoutManager(activity)
        folderRecyclerView?.layoutManager = layoutManager




        return view
    }
}