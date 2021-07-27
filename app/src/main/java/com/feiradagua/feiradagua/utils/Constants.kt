package com.feiradagua.feiradagua.utils

import android.Manifest

class Constants {
    object Login {
        const val RC_SIGN_IN = 999
    }

    object Firebase {
        const val USER_COLLECTION = "Users"
        const val REGISTERED_COLLECTION = "Registered"
        const val ORDERS_COLLECTION = "Orders"
        const val PRODUCTS_COLLECTION = "Products"
    }

    object BuscaCepAPI {
        const val VIA_CEP_BASE_URL = "https://viacep.com.br/"
    }

    object Intents {
        const val POSITION = "Position"
        const val EXTRA_INFOS = "ExtraInfos"
        const val ORDER_DETAILS = "OrderDetails"
        const val PRODUCT_UPDATE = "ProductUpdate"
        const val PRODUCT_INFO = "ProductInfo"
        const val PRODUCT_ID = "ProductId"
        const val PRODUCER_ID = "ProducerId"
        const val CART_INFO = "CartInfo"
    }

    object CameraX {
        const val TAG = "CameraXBasic"
        const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val REQUEST_CODE_PERMISSIONS = 10
        const val PHOTO_URI = "photoUri"
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}