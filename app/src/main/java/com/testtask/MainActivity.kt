package com.testtask

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.testtask.models.Avatars
import com.testtask.models.AvatarsAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    private val lastVisibleItemPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        online_count_tv.text="online: ${(500..1500).random()}".toUpperCase()
        recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        setlist(Avatars.generate())


        settings_btn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        start_search_btn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

    }


    fun setlist(avatars: MutableList<Avatars>) {
        var i=0
        next_profile_btn.setOnClickListener {
            i++
            recyclerview.smoothScrollToPosition(i)
        }
        chose_human_btn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("avatar",avatars[i%avatars.size])
            startActivity(intent)
        }

        val adapter =
            AvatarsAdapter(avatars) { adapterposition ->
                Log.e("N@@@", adapterposition.toString())


                val point = avatars[adapterposition]
            }



        recyclerview.adapter = adapter


    }

}
