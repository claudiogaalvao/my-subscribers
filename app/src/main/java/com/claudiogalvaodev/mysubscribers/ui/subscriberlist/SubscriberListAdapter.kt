package com.claudiogalvaodev.mysubscribers.ui.subscriberlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.claudiogalvaodev.mysubscribers.R
import com.claudiogalvaodev.mysubscribers.data.db.entity.SubscriberEntity
import org.w3c.dom.Text

class SubscriberListAdapter(
    private val subscribers: List<SubscriberEntity>
): RecyclerView.Adapter<SubscriberListAdapter.SubscriberListViewHolder>() {

    var onItemClick: ((entity: SubscriberEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriberListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.subscriber_item, parent, false)

        return SubscriberListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubscriberListViewHolder, position: Int) {
        holder.bindView(subscribers[position])
    }

    override fun getItemCount() = subscribers.size

    inner class SubscriberListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private lateinit var textViewSubscriberName: TextView
        private lateinit var textViewSubscriberEmail: TextView

        fun bindView(subscriber: SubscriberEntity) {
            initComponents()
            textViewSubscriberName.text = subscriber.name
            textViewSubscriberEmail.text = subscriber.email

            itemView.setOnClickListener {
                onItemClick?.invoke(subscriber)
            }
        }

        private fun initComponents() {
            textViewSubscriberName = itemView.findViewById(R.id.text_subscriber_name)
            textViewSubscriberEmail = itemView.findViewById(R.id.text_subscriber_email)
        }

    }

}