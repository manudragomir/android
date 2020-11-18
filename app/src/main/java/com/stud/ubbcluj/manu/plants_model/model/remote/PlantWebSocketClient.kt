package com.stud.ubbcluj.manu.plants_model.model.remote

import android.util.Log
import com.stud.ubbcluj.manu.utils.TAG
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI


abstract class PlantWebSocketClient(address: String): WebSocketClient(URI(address)){

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
    }

    override fun onError(ex: Exception?) {
    }
}