package com.claudiogalvaodev.mysubscribers.ui.subscriberlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.claudiogalvaodev.mysubscribers.R
import com.claudiogalvaodev.mysubscribers.data.db.AppDatabase
import com.claudiogalvaodev.mysubscribers.data.db.dao.SubscriberDAO
import com.claudiogalvaodev.mysubscribers.extension.navigateWithAnimations
import com.claudiogalvaodev.mysubscribers.repository.DatabaseDataSource
import com.claudiogalvaodev.mysubscribers.repository.SubscriberRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SubscriberListFragment : Fragment(R.layout.subscriber_list_fragment) {

    private val viewModel: SubscriberListViewModel by viewModels {
        object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val subscriberDAO: SubscriberDAO = AppDatabase.getInstance(requireContext()).subscriberDAO
                val repository: SubscriberRepository = DatabaseDataSource(subscriberDAO)
                return SubscriberListViewModel(repository) as T
            }
        }
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddSubscriber: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents(view)
        observeViewModelEvents()
        configureViewListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSubscribers()
    }

    private fun initComponents(view:View) {
        recyclerView = view.findViewById(R.id.recycler_subscribers)
        fabAddSubscriber = view.findViewById(R.id.fab_add_subscriber)
    }

    private fun observeViewModelEvents() {
        viewModel.allSubscribersEvent.observe(viewLifecycleOwner) { allSubscribers ->
            val subscriberListAdapter = SubscriberListAdapter(allSubscribers).apply {
                onItemClick = { subscriber ->
                    val directions = SubscriberListFragmentDirections
                        .actionSubscriberListFragmentToSubscriberFragment(subscriber)
                    findNavController().navigateWithAnimations(directions)
                }
            }

            recyclerView.run {
                setHasFixedSize(true)
                adapter = subscriberListAdapter
            }
        }
    }

    private fun configureViewListeners() {
        fabAddSubscriber.setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_subscriberListFragment_to_subscriberFragment)
        }
    }

}