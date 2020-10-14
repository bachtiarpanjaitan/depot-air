package com.bataxdev.waterdepot.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.bataxdev.waterdepot.data.model.ProductModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<ProductModel> products = new MutableLiveData<>();

    public HomeViewModel() {

    }


}