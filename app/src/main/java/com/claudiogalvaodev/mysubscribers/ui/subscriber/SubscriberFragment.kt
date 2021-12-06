package com.claudiogalvaodev.mysubscribers.ui.subscriber

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.claudiogalvaodev.mysubscribers.R
import com.claudiogalvaodev.mysubscribers.data.db.AppDatabase
import com.claudiogalvaodev.mysubscribers.data.db.dao.SubscriberDAO
import com.claudiogalvaodev.mysubscribers.extension.hideKeyboard
import com.claudiogalvaodev.mysubscribers.repository.DatabaseDataSource
import com.claudiogalvaodev.mysubscribers.repository.SubscriberRepository
import com.google.android.material.textfield.TextInputEditText

class SubscriberFragment : Fragment(R.layout.subscriber_fragment) {

    private val viewModel: SubscriberViewModel by viewModels {
        object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val subscriberDAO: SubscriberDAO = AppDatabase.getInstance(requireContext()).subscriberDAO
                val repository: SubscriberRepository = DatabaseDataSource(subscriberDAO)
                return SubscriberViewModel(repository) as T
            }
        }
    }
    private lateinit var buttonAdd: Button
    private lateinit var inputName: TextInputEditText
    private lateinit var inputEmail: TextInputEditText
    private val args: SubscriberFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewReferences(view)

        args.subscriber?.let { subscriber ->
            buttonAdd.text = getString(R.string.subscriber_button_update)
            inputName.setText(subscriber.name)
            inputEmail.setText(subscriber.email)
        }

        observeEvents()
        setListeners()
    }

    private fun setViewReferences(view: View) {
        buttonAdd = view.findViewById(R.id.button_add)
        inputName = view.findViewById(R.id.input_name)
        inputEmail = view.findViewById(R.id.input_email)
    }

    private fun observeEvents() {
        viewModel.subscriberStateEventData.observe(viewLifecycleOwner) { subscriberState ->
            when(subscriberState) {
                is SubscriberViewModel.SubscriberState.Inserted -> {
                    findNavController().popBackStack()
                    hideKeyboard()
                }
                is SubscriberViewModel.SubscriberState.Updated -> {
                    findNavController().popBackStack()
                    hideKeyboard()
                }
            }
        }

        viewModel.messageEventData.observe(viewLifecycleOwner) { stringResId ->
            Toast.makeText(context, stringResId, Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetFields() {
        inputName.text?.clear()
        inputEmail.text?.clear()
        inputName.requestFocus()
    }

    private fun hideKeyboard() {
        val parentActivity = requireActivity()
        if(parentActivity is AppCompatActivity) {
            parentActivity.hideKeyboard()
        }
    }

    private fun setListeners() {
        buttonAdd.setOnClickListener {
            val name = inputName.text.toString()
            val email = inputEmail.text.toString()

            if(name.isNotEmpty() && email.isNotEmpty()) {
                viewModel.addOrUpdateSubscriber(args.subscriber?.id ?: 0, name, email)
            }
        }
    }

}