package com.gstatic.test.ui.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gstatic.test.ApiManager;
import com.gstatic.test.App;
import com.gstatic.test.pojo.LocationData;
import com.gstatic.test.pojo.LocationDataResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyViewModel extends ViewModel {
 OnErrorListener onErrorListener;
   ApiManager apiManager = App.getInstance().apiManager;
   String code;
   List<LocationData> dataList;
   MutableLiveData<List<LocationData>> data;

   public LiveData<List<LocationData>> getData() {
       if (data == null&&dataList==null) {
           dataList=new ArrayList<>();
           data = new MutableLiveData<>();
           loadData(1);
       }
       data.setValue(dataList);
       return data;
   }
 
   public void loadData(int page) {
     apiManager.loadLocationData(code,page)
             .subscribeOn(Schedulers.computation())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(this::handleResults, error->handleError(error,page));;
   }

    private void handleError(Throwable throwable,Integer page) {
       if(throwable.getMessage().contains("Unterminated object"))
           loadData(page+1);
        else {
          if(onErrorListener!=null)onErrorListener.onError(throwable.getMessage());
       }


    }

    private void handleResults(LocationDataResult locationDataResultList) {
       dataList.addAll(locationDataResultList.locationData);
       data.setValue(locationDataResultList.locationData);//
        //  data.getValue().addAll(locationDataResultList.locationData);
       // data.postValue(data.getValue());
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setOnError(OnErrorListener onError) {
        this.onErrorListener = onError;
    }

    interface OnErrorListener {
      void onError(String string);
    };
}