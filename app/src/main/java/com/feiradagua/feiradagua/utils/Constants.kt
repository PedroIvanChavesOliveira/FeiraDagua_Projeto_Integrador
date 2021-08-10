package com.feiradagua.feiradagua.utils

import android.Manifest
import com.feiradagua.feiradagua.BuildConfig

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

    object  SharedPreferences {
        const val SHAREDPREFERENCES_TUTORIAL_NAME_FIRST = "ShowTutorial1"
        const val SHAREDPREFERENCES_TUTORIAL_NAME_SECOND = "ShowTutorial2"
        const val SHAREDPREFERENCES_TUTORIAL_NAME_THIRD = "ShowTutorial3"
        const val SHAREDPREFERENCES_TUTORIAL_NAME_FORTH = "ShowTutorial4"
        const val SHAREDPREFERENCES_TUTORIAL_NAME_FIFTH= "ShowTutorial5"
        const val SHAREDPREFERENCES_TUTORIAL_NAME_SIXTH = "ShowTutorial6"
        const val SHAREDPREFERENCES_TUTORIAL_NAME_FIRST_PRODUCER = "ShowTutorial1Producer"
        const val SHAREDPREFERENCES_TUTORIAL_NAME_SECOND_PRODUCER = "ShowTutorial2Producer"
        const val SHAREDPREFERENCES_TUTORIAL_NAME_THIRD_PRODUCER = "ShowTutorial3Producer"
        const val SHAREDPREFERENCES_TUTORIAL_NAME_FORTH_PRODUCER = "ShowTutorial4Producer"
        const val SHAREDPREFERENCES_TUTORIAL_NAME_FIFTH_PRODUCER = "ShowTutorial5Producer"
        const val SHAREDPREFERENCES_TUTORIAL_NAME_SIXTH_PRODUCER = "ShowTutorial6Producer"
        const val SHAREDPREFERENCES_TUTORIAL_KEY = "show"
    }

    object  Notification {
        const val BASE_URL = "https://fcm.googleapis.com"
        const val SERVER_KEY = BuildConfig.NOTIFICATION_SERVER_KEY
        const val CONTENT_TYPE = "application/json"
        const val CHANNEL_ID = "my_channel"
        const val TOPIC = "/topics/Topic"
    }

    object BuscaCepAPI {
        const val VIA_CEP_BASE_URL = "https://viacep.com.br/"
    }

    object Intents {
        const val POSITION = "Position"
        const val POSITION_SPLASH = "PositionSplash"
        const val EXTRA_INFOS = "ExtraInfos"
        const val ORDER_DETAILS = "OrderDetails"
        const val PRODUCT_UPDATE = "ProductUpdate"
        const val PRODUCT_INFO = "ProductInfo"
        const val PRODUCT_ID = "ProductId"
        const val PRODUCER_ID = "ProducerId"
        const val CART_INFO = "CartInfo"
        const val TUTORIAL = "Tutorial"
    }

    object VoiceRecognation {
        const val REQUEST_CODE = 999
    }

    object CameraX {
        const val TAG = "CameraXBasic"
        const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val REQUEST_CODE_PERMISSIONS = 10
        const val PHOTO_URI = "photoUri"
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}