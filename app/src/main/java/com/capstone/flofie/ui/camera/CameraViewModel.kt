package com.capstone.flofie.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.flofie.model.Resultbox
import java.io.File

class CameraViewModel : ViewModel() {

    var getFile : File? = null

//    var getFile : MutableLiveData<File?>? = null

    var result : Resultbox? = null
}