package com.reactivecommit.tree

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import android.widget.Toast
import com.reactivecommit.tree.ui.RootActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.properties.Delegates

class NetworkMonitor (private val application: Application): KoinComponent {

    private val repository: RootRepository by inject()

    fun startNetworkCallback() {
        val cm: ConnectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()

        cm.registerNetworkCallback(
            builder.build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    NetworkState.isNetworkConnected = true
                    GlobalScope.launch(Dispatchers.IO) {
                        val houses = repository.findAllHouses().size
                        withContext(Dispatchers.Main) {
                            if(houses == 0){
                                application.startActivity(Intent(application.applicationContext, RootActivity::class.java).apply {
                                    setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                })
                            }
                        }
                    }
                }

                override fun onLost(network: Network) {
                    NetworkState.isNetworkConnected = false
                    Toast.makeText(application, "Offline mode", Toast.LENGTH_LONG).show()
                }
            })
    }

    fun stopNetworkCallback() {
        val cm: ConnectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
    }
}

object NetworkState {
    var isNetworkConnected: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
        Log.i("Network connectivity", "$newValue")
    }
}