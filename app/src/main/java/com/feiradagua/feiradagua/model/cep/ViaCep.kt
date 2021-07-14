package com.feiradagua.feiradagua.model.cep

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ViaCep(
    val bairro: String,
    val cep: String,
    val complemento: String,
    val ddd: String,
    val gia: String,
    val ibge: String,
    val localidade: String,
    val logradouro: String,
    val siafi: String,
    val uf: String
): Parcelable