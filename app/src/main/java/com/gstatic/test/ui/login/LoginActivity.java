package com.gstatic.test.ui.login;

import android.content.Intent;
import android.os.Bundle;

import com.gstatic.test.ApiManager;
import com.gstatic.test.App;
import com.gstatic.test.ui.data.DataListActivity;
import com.gstatic.test.R;
import com.gstatic.test.pojo.LoginResult;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.etLogin)
    EditText etLogin;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    ApiManager apiManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        apiManager = App.getInstance().apiManager;

        btnLogin.setOnClickListener((view)->{

          apiManager.login(String.valueOf(etLogin.getText()),String.valueOf(etPassword.getText()))
                  .subscribeOn(Schedulers.io())
                  .doOnSubscribe(d -> showLoading())
                  .doFinally(()->hideLoading())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(this::handleResults, this::handleError);
        });

    }


    private void handleResults(LoginResult loginResult) {
         if(loginResult.status.equals("ok")){
             succesLogin(loginResult.code);
         }else if(loginResult.status.equals("error")){
             Toast.makeText(this,R.string.incorrect_login_or_pass,Toast.LENGTH_LONG).show();
         }
    }

    private void succesLogin(String code) {
        Intent intent = new Intent(this, DataListActivity.class);
        intent.putExtra("code",code);
        startActivity(intent);
    }

    private void showLoading(){
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
    }


    private void hideLoading(){
        runOnUiThread(()->{
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
        });

    }
    private void handleError(Throwable t) {
        Log.d("handleError" ,t.getMessage());
        Toast.makeText(this, t.getMessage(),
                Toast.LENGTH_LONG).show();
    }
}