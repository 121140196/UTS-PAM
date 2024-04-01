package com.example.utspam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class Home : Fragment() {

    private lateinit var searchList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private var userList = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val searchView: SearchView = view.findViewById(R.id.searchView)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = UserAdapter(userList)
        recyclerView.adapter = adapter

        searchList = ArrayList()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchQuery ->
                    search(searchQuery)
                }
                return true
            }
        })

        val retrofit = Retrofit.Builder()
            .baseUrl("https://reqres.in/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getUsers()

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(
                call: Call<ApiResponse>,
                response: Response<ApiResponse>
            ) {
                if (response.isSuccessful) {
                    val userListResponse = response.body()?.data ?: emptyList()
                    userList.clear()
                    userList.addAll(userListResponse)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(
                call: Call<ApiResponse>,
                t: Throwable
            ) {}
        })
        return view
    }

    private fun search(query: String) {
        searchList.clear()
        val searchText = query.toLowerCase(Locale.getDefault())
        if (searchText.isNotEmpty()) {
            userList.forEach { user ->
                if (user.first_name.toLowerCase(Locale.getDefault()).contains(searchText)) {
                    searchList.add(user)
                }
            }
        } else {
            searchList.addAll(userList)
        }
        adapter.setData(searchList)
    }
}