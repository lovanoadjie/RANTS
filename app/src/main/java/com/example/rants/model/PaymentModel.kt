package com.example.rants.model

data class PaymentRequest(
    val amount: Double
)

data class PaymentResponse(
    val status: String,
    val snap_token: String,
    val order_id: String
)
data class PaymentVerificationRequest(
    val transaction_id: String
)
data class PaymentVerificationResponse(
    val status: String,
    val message: String
)
