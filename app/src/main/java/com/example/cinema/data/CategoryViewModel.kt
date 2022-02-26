package com.example.cinema.data;

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinema.domain.Category;
import com.example.cinema.service.RetrofitMovie
import com.example.cinema.service.ServiceCategory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class CategoryViewModel: ViewModel() {
    private val _categoryList = mutableStateListOf<Category>()
    var errorMessage: String by mutableStateOf("")
    val categoryList: List<Category>
    get() = _categoryList

    fun getCategoryList(context: Context) {
        val retrofit: Retrofit? = RetrofitMovie.getMovieRetrofit(context)
        val serviceCategory: ServiceCategory = retrofit!!.create(ServiceCategory::class.java)
        val call: Call<List<Category>> = serviceCategory.getCategories()

        viewModelScope.launch {
            try {
                call.enqueue(object : Callback<List<Category>> {
                    override fun onResponse(call: Call<List<Category>>, uneReponse: Response<List<Category>>) {
                        if (uneReponse.isSuccessful) {
                            if (uneReponse.body() != null) {
                                var categories = uneReponse.body();
                                _categoryList.clear()
                                if (categories != null) {
                                    _categoryList.addAll(categories)
                                }
                            }
                        } else {
                            errorMessage = "Erreur d'appel!";
                        }
                    }
                    override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                        errorMessage = "Erreur rencontrée";
                        call.cancel()
                    }
                })
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }
}
