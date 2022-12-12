package com.example.myfitv2.DataViewModels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myfitv2.DataClasses.Friend
import com.example.myfitv2.Repository.FriendsRepository

class FriendsViewModel : ViewModel() {

    private val repository : FriendsRepository
    private val _allFriends= MutableLiveData<List<Friend>>()
    val allFriends : LiveData<List<Friend>> = _allFriends


    init {
        repository = FriendsRepository().getInstance()
        repository.loadFriends(_allFriends)
    }
}