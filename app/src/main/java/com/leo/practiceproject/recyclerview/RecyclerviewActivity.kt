package com.leo.practiceproject.recyclerview

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leo.practiceproject.R
import com.leo.practiceproject.databinding.ActivityRecyclerviewBinding


class RecyclerviewActivity : AppCompatActivity() {
    private val TAG = "RecyclerViewExample"
    private lateinit var binding: ActivityRecyclerviewBinding
    private var feedsList: List<FeedItem> = ArrayList()
    private var mRecyclerView: RecyclerView? = null
    private var adapter: StableIdsRecyclerViewAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var isScrolling = false
    private var start = 140
    private val chatThreadScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstVisibleItem = linearLayoutManager!!.findFirstVisibleItemPosition()
                if (firstVisibleItem <= 10) {
                    if (!isScrolling) {
                        isScrolling = true
                        generateMoreItems()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.root
        binding = ActivityRecyclerviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mRecyclerView = binding.recyclerView
        linearLayoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = linearLayoutManager
        mRecyclerView!!.addOnScrollListener(chatThreadScrollListener)
        MessageGenerationTask().execute(start)
    }

    private fun generateMoreItems() {
        Toast.makeText(this@RecyclerviewActivity, "Fetching old feeds !", Toast.LENGTH_SHORT).show()
        MessageGenerationTask().execute(10.let { start -= it; start })
    }

    class StableIdsRecyclerViewAdapter(private var feedItemList: List<FeedItem>?) :
        RecyclerView.Adapter<StableIdsRecyclerViewAdapter.CustomViewHolder>() {
        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
            val view: View =
                LayoutInflater.from(viewGroup.context).inflate(R.layout.list_row, viewGroup, false)
            return CustomViewHolder(view)
        }

        override fun onBindViewHolder(customViewHolder: CustomViewHolder, i: Int) {
            val feedItem = feedItemList!![i]
            //Setting text view title
            customViewHolder.textView.text = Html.fromHtml(feedItem.getTitle())
        }

        override fun getItemId(position: Int): Long {
            val feedItem = feedItemList!![position]
            // Lets return in real stable id from here
            return feedItem.getId().toLong()
        }

        override fun getItemCount(): Int {
            return if (null != feedItemList) feedItemList!!.size else 0
        }

        fun setData(newList: ArrayList<FeedItem>?) {
            feedItemList = newList
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var textView: TextView

            init {
                textView = view.findViewById(R.id.title)
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class MessageGenerationTask :
        AsyncTask<Int?, Void?, ArrayList<FeedItem>>() {

        override fun onPreExecute() {}

        override fun doInBackground(vararg params: Int?): ArrayList<FeedItem> {
            val newList: ArrayList<FeedItem> = ArrayList()
            val start = params[0]
            if (start != null) {
                if (start < MIN || start + 10 > MAX) return newList
            }
            if (start != null) {
                for (i in start until start + 10) {
                    val item = FeedItem()
                    item.setTitle("Title $i")
                    item.setId(i)
                    newList.add(item)
                }
            }
            return newList
        }

        override fun onPostExecute(result: ArrayList<FeedItem>) {
            if (result.size > 0) {
                if (adapter == null) {
                    adapter = StableIdsRecyclerViewAdapter(result)
                    adapter?.setHasStableIds(true)
                    mRecyclerView?.setItemAnimator(null)
                    mRecyclerView?.setAdapter(adapter)
                    mRecyclerView?.scrollToPosition(result.size - 1)
                    feedsList = result
                } else {
                    val newList: ArrayList<FeedItem> = ArrayList(feedsList)
                    newList.addAll(0, result)
                    adapter?.setData(newList)
                    val newResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                        override fun getOldListSize(): Int {
                            return feedsList.size
                        }

                        override fun getNewListSize(): Int {
                            return newList.size
                        }

                        override fun areItemsTheSame(i: Int, i1: Int): Boolean {
                            return feedsList.get(i) == newList[i1]
                        }

                        override fun areContentsTheSame(i: Int, i1: Int): Boolean {
                            return true
                        }
                    })
                    Log.d(TAG, " load more applied " + feedsList.size)
                    newResult.dispatchUpdatesTo(adapter!!)
                    feedsList = newList
                    isScrolling = false
                }
            } else {
                Toast.makeText(this@RecyclerviewActivity, "No more data !", Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object {
        private const val MIN = 0
        private const val MAX = 150
    }
}