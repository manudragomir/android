package com.stud.ubbcluj.manu.plants_model.model.remote

import com.stud.ubbcluj.manu.plants_model.model.Plant

data class Payload(val item: Any)

data class SocketData(val event: String, val payload: Payload)