package com.example.myfitv2.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitv2.Adapters.FriendsAdapter
import com.example.myfitv2.DataViewModels.FriendsViewModel
import com.example.myfitv2.R


private lateinit var friendsAdapter: FriendsAdapter
private lateinit var viewModel : FriendsViewModel
private lateinit var friendsRecyclerView: RecyclerView



class FriendsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {return inflater.inflate(R.layout.fragment_friends, container, false)}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            friendsRecyclerView = view.findViewById(R.id.RecyclerViewForFriends)
            friendsRecyclerView.layoutManager = LinearLayoutManager(context)
            friendsRecyclerView.setHasFixedSize(true)
            friendsAdapter = FriendsAdapter()

            friendsRecyclerView.adapter = friendsAdapter

            viewModel = ViewModelProvider(this)[FriendsViewModel::class.java]
            viewModel.allFriends.observe(viewLifecycleOwner) { friendsAdapter.UpdateFriendList(it) }

        } catch (e : Exception) { Log.d("Friends fragment ",e.message.toString()); }




    }
}