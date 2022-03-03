package com.example.cinema.ui.bottomnav

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.example.cinema.R


sealed class BottomNavItem(var title:String, var icon:Int, var screen_route:String){
    object Movies : BottomNavItem("Films", R.drawable.ic_movie,"movies")
    object Characters: BottomNavItem("Acteurs",R.drawable.ic_actor,"actors")
}

