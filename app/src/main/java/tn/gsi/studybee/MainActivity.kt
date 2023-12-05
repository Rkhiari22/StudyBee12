package tn.gsi.studybee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<FileClass>
    lateinit var imageList: Array<Int>
    lateinit var titleList: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageList = arrayOf(
            R.drawable.pdf,
            R.drawable.pdf ,
            R.drawable.pdf)

        titleList = arrayOf(
            "File 1",
            "File 2",
            "File 3"
        )

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf<FileClass>()
        getData()
    }

    private fun getData() {
        for (i in imageList.indices){
            val dataClass = FileClass(imageList[i], titleList[i])
            dataList.add(dataClass)
        }
        recyclerView.adapter = FileAdapter(dataList)


    }
}