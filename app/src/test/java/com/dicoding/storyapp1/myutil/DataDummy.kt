package com.dicoding.storyapp1.myutil

import com.dicoding.storyapp1.data.Stories

object DataDummy {
    fun generateDummyResponse(): List<Stories> {
        val items: MutableList<Stories> = arrayListOf()
        for (i in 0..100) {
            val quote = Stories(
                id = "626262",
                name = "adudu",
                description = "Tim bubadibako",
                photoUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fid.pinterest.com%2Fpin%2F712624341040766050%2F&psig=AOvVaw2P_RQ1lZgGcZzv4HtwP4AW&ust=1698803985334000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCMjx-JOYn4IDFQAAAAAdAAAAABAd",
                createdAt = "januari",
                lat = 78.6,
                lon = 56.8
            )
            items.add(quote)
        }
        return items
    }
}