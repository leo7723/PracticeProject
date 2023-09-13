package com.leo.practiceproject.recyclerview

class FeedItem {
    private var id = 0
    private var title: String? = null

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String?) {
        this.title = title
    }

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }
}