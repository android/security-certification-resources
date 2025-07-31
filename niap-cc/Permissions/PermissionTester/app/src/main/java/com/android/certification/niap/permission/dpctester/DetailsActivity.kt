package com.android.certification.niap.permission.dpctester
/*
 * Copyright (C) 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.certification.niap.permission.dpctester.data.LogBox
import com.android.certification.niap.permission.dpctester.databinding.ActivityDetailsBinding
import com.android.certification.niap.permission.dpctester.test.log.ActivityLogger
import com.android.certification.niap.permission.dpctester.test.log.Logger
import com.android.certification.niap.permission.dpctester.test.log.LoggerFactory
import java.util.ArrayList
import kotlin.random.Random


class DetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val textView: TextView = itemView.findViewById(R.id.logrowText)
}

class DetailsViewAdapter(private val list: List<LogBox>,
                      private val listener: ListListener
) : RecyclerView.Adapter<DetailsViewHolder>() {
    interface ListListener {
        fun onClickItem(tappedView: View, itemModel: LogBox)
    }
    // その名の通りViewHolderを作成。MainViewHolderの引数にinflateしたレイアウトを入れている
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        return DetailsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.d_row_item, parent, false)
        )
    }

    // ViewHolder内に表示するデータを指定。
    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        fun textView(resId:Int):TextView {
            return holder.itemView.findViewById(resId)
        }
        //
        textView(R.id.logrowLabel).text = list[position].name
        textView(R.id.logrowText).text = list[position].description

        textView(R.id.logrowIndicator).setTextColor(Color.TRANSPARENT)
        textView(R.id.logrowNextArrow).visibility = View.INVISIBLE
        if(list[position].type == "error"){
            textView(R.id.logrowIndicator).setTextColor(Color.RED)
        } else {
            textView(R.id.logrowIndicator).setTextColor(Color.LTGRAY)
        }

    }

    // 表示したいリストの数を指定
    override fun getItemCount(): Int {
        return list.size
    }
}

class DetailsActivity : AppCompatActivity(), ActivityLogger.LogListAdaptable {
    val TAG: String = DetailsActivity::class.java.simpleName

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDetailsBinding

    private val logger: Logger =
        LoggerFactory.createActivityLogger(TAG, this);

    private var recyclerView: RecyclerView? = null
    val itemList = mutableListOf<LogBox>()
    private fun generateItemList(): List<LogBox> {
        return itemList
    }
    private fun removeItemList(){
        itemList.clear();
        recyclerView?.getAdapter()?.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")

        val bundle = intent.extras
        val arraylist: ArrayList<LogBox>? =
            bundle?.getParcelableArrayList<LogBox>("logboxes")
        arraylist?.forEach{
            addLogBox(it)
        }
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.title=name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.recyclerView = findViewById(R.id.recycler_view)
        this.recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = DetailsViewAdapter(
                generateItemList(),
                object : DetailsViewAdapter.ListListener {
                    override fun onClickItem(tappedView: View, itemModel: LogBox) {
                    }
                }
            )
        }
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        this.recyclerView?.addItemDecoration(itemDecoration)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //outState.putStringArrayList("listViewData", mStatusData as ArrayList<String?>)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()

        return super.onSupportNavigateUp()
    }

    /********************************************
     * Goodies for local list view
     ********************************************/

    override fun addLogBox(logbox:LogBox){
        runOnUiThread {
            itemList.add(logbox)
            this.recyclerView?.adapter?.notifyItemInserted(itemList.size-1)
        }
    }

    override fun addLogLine(msg: String) {
        runOnUiThread {
            itemList.add(LogBox(Random.nextLong(), "normal", msg))
            this.recyclerView?.adapter?.notifyItemInserted(itemList.size-1)
        }
    }

    fun notifyUpdate() {
        runOnUiThread {
            this.recyclerView?.adapter?.notifyDataSetChanged()
        }
    }
}