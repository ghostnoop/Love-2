package com.testtask.models

import java.io.Serializable

class Avatars(var url: String):Serializable {
    override fun toString(): String {
        return url
    }


    companion object{
        open fun generate(): MutableList<Avatars> {
            val temp:MutableList<Avatars> = mutableListOf()
            temp.add(Avatars("https://randomuser.me/api/portraits/women/23.jpg"))
            temp.add(Avatars("https://randomuser.me/api/portraits/women/33.jpg"))
            temp.add(Avatars("https://randomuser.me/api/portraits/women/43.jpg"))
            temp.add(Avatars("https://randomuser.me/api/portraits/women/53.jpg"))
            temp.add(Avatars("https://randomuser.me/api/portraits/women/63.jpg"))
            return temp
        }
    }
}