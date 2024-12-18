package com.example.rants.model

data class PaymentRequest(val name: String, val email: String, val amount: Int)
data class PaymentResponse(val status: String, val token: String)

interface ApiService {

}
