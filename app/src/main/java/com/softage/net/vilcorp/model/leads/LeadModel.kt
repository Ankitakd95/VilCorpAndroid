package com.softage.net.vilcorp.model.leads

data class LeadModel(
    val clientName: String,
    val clientAddress: String,
    val managerName: String,
    val managerPhone: String,
    val spocName: String,
    val spocPhone: String,
    val pjpCategory: String,
    val simCount: String?,
    val targetDeliveryDate: String,
    val leadStatus: String,
    val leadId: Int
)
