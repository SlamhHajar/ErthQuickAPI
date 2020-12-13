package com.hajarslamah.erthquickapi

import com.google.gson.annotations.SerializedName

class ErthResponse()
{
 @SerializedName("features")
 lateinit var erthItems: List<ErthData>

}