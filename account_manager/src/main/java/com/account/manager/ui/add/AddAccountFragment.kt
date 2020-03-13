package com.account.manager.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.account.manager.R

class AddAccountFragment : Fragment() {

    private lateinit var addAccountViewModel: AddAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addAccountViewModel =
            ViewModelProviders.of(this).get(AddAccountViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_add, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        addAccountViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}