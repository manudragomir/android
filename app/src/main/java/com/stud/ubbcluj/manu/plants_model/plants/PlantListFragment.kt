package com.stud.ubbcluj.manu.plants_model.plants

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.stud.ubbcluj.manu.R
import com.stud.ubbcluj.manu.auth.data.AuthRepository
import com.stud.ubbcluj.manu.plants_model.model.remote.Payload
import com.stud.ubbcluj.manu.plants_model.model.remote.PlantWebSocketClient
import com.stud.ubbcluj.manu.plants_model.model.remote.SocketData
import com.stud.ubbcluj.manu.utils.Api
import com.stud.ubbcluj.manu.utils.TAG
import kotlinx.android.synthetic.main.plant_list.*
import org.java_websocket.handshake.ServerHandshake

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class PlantListFragment : Fragment() {
    private lateinit var plantListAdapter: PlantListAdapter
    private lateinit var plantListViewModel: PlantListViewModel
    private lateinit var plantWebSocketClient: PlantWebSocketClientFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG,"fragment created")
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG,"fragment on create view")
        return inflater.inflate(R.layout.plant_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "on activity created")
        if (!AuthRepository.isLoggedIn && !AuthRepository.isOffline) {
            findNavController().navigate(R.id.LoginFragment)
            return;
        }
        if(AuthRepository.isOffline){
            checkbox_is_offline.visibility = View.VISIBLE
        }
        setupPlantList()
        btn_new.setOnClickListener{
            Log.v(TAG, "adding new plant button clicked")
            findNavController().navigate(R.id.PlantEditFragment)
        }
        btn_logout.setOnClickListener{
            Log.v(TAG, "user just logged out");
            AuthRepository.logout()
            findNavController().navigate(R.id.LoginFragment)
        }

        if(!AuthRepository.isOffline){
            setupWebSockets()
        }
    }

    inner class PlantWebSocketClientFragment(address: String) : PlantWebSocketClient(address) {
        override fun onOpen(handshakedata: ServerHandshake?) {
            Log.v(TAG, "on open")
            if(Api.tokenInterceptor.token != null){
                val payload = Payload(Api.tokenInterceptor.token!!)
                send(Gson().toJson(SocketData("authorization", payload )))
            }
        }

        override fun onMessage(message: String) {
            Log.v(TAG, "On message")
            plantListViewModel.newItemIncoming(message)
        }
    }

    private fun setupWebSockets(){
        plantWebSocketClient = PlantWebSocketClientFragment("ws://192.168.1.2:3333/")
        plantWebSocketClient.connect()
    }

    private fun setupPlantList(){
        plantListAdapter = PlantListAdapter(this)
        plant_list_recycler_view.adapter = plantListAdapter
        plantListViewModel = ViewModelProvider(this, ).get(PlantListViewModel::class.java)

        plantListViewModel.plants.observe(viewLifecycleOwner, { plantsComing ->
            Log.i(TAG, "plants are coming from model in fragment")
            plantListAdapter.plants = plantsComing
        })

        plantListViewModel.loading.observe(viewLifecycleOwner, { loadingStatus ->
            Log.i(TAG, "loading plants status changed")
            if (loadingStatus){
                progress.visibility = View.VISIBLE
            }
            else{
                progress.visibility = View.GONE
            }
        })

        plantListViewModel.loadingException.observe(viewLifecycleOwner, { loadingException ->
            if (loadingException != null) {
                Log.i(TAG, "Some error ocured when loading")
                val message = "Loading exception ${loadingException.message}"
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
            }
        })

        plantListViewModel.loadItems()
    }

    override fun onDestroy() {
        Log.v(TAG, "destroyed")
        super.onDestroy()
    }
}