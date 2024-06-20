package com.example.lokatravel.data.response

import com.google.gson.annotations.SerializedName

data class JakartaResponse(
	@field:SerializedName("JakartaResponse")
	val jakartaResponse: List<JakartaResponseItem>? = null
)

data class JakartaResponseItem(
	@field:SerializedName("Category")
	val category: String? = null,

	@field:SerializedName("Place_Id")
	val placeId: Int? = null,

	@field:SerializedName("Place_Name")
	val placeName: String? = null,

	@field:SerializedName("Rating")
	val rating: Float? = null, // Rating might be float based on JSON

	@field:SerializedName("Long")
	val long: Double? = null, // Use Double for latitude and longitude

	@field:SerializedName("City")
	val city: String? = null,

	@field:SerializedName("Lat")
	val lat: Double? = null // Use Double for latitude and longitude
)
