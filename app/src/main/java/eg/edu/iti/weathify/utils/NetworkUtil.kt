package eg.edu.iti.weathify.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

object NetworkUtil {
    fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected == true
    }
}

class NetworkMonitor(
    private val onNetworkAvailable: () -> Unit,
    private val onNetworkLost: () -> Unit
) : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        onNetworkAvailable.invoke()
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        onNetworkLost.invoke()
    }

    fun registerNetworkCallbacks(context: Context){
        val nm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        nm.registerNetworkCallback(
            NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build(),
            this
        )
    }

    fun unregisterNetworkCallbacks(context: Context){
        val nm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        nm.unregisterNetworkCallback(this)
    }
}