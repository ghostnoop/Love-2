package com.testtask.models

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.testtask.R
import com.testtask.app.inflate
import kotlinx.android.synthetic.main.avatar_view.view.*


class AvatarsAdapter(
    private val avatars: MutableList<Avatars>,
    private val listener: (Int) -> Unit
) : RecyclerView.Adapter<AvatarsAdapter.MyViewHolder>() {
    companion object{
        fun getlist(): MutableList<ColorDrawable> {
            var bbs = mutableListOf<ColorDrawable>()
            bbs.add(ColorDrawable(Color.BLACK))
            bbs.add(ColorDrawable(Color.YELLOW))
            bbs.add(ColorDrawable(Color.GREEN))
            bbs.add(ColorDrawable(Color.BLUE))
            bbs.add(ColorDrawable(Color.MAGENTA))
            bbs.add(ColorDrawable(Color.CYAN))
            bbs.add(ColorDrawable(Color.DKGRAY))
            return bbs
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            inflate(
                parent.context,
                R.layout.avatar_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(avatars[position%avatars.size], listener)
    }

    override fun getItemCount(): Int {
        return Integer.MAX_VALUE
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(avatar: Avatars, listener: (Int) -> Unit) = with(itemView) {
            Log.e("N!!",avatar.url)
            Glide.with(this).load(avatar.url).error(ColorDrawable(Color.RED)).placeholder(R.drawable.ic_account_circle_black_24dp).circleCrop().into(imageView)
//            val temp = getlist()
//            Glide.with(this).load(temp.get((0..temp.size-1).random())).error(ColorDrawable(Color.RED)).placeholder(
//                R.drawable.ic_account_circle_black_24dp
//            ).circleCrop().into(imageView)

            itemView.setOnClickListener { listener(adapterPosition) }
//            View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY
        }

    }


}