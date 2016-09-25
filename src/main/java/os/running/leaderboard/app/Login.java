package os.running.leaderboard.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import os.running.leaderboard.app.base.Runtastic;
import os.running.leaderboard.app.main.presenter.MainPresenter;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public class Login extends AppCompatActivity
{
    private AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        this.activity = this;
        
        configureView();
    }

    private void configureView()
    {
        try {
            Button button = (Button)this.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (checkLogin()) {
                        new createLogin().execute();
                    }
                }
            });
        } catch (Exception e) {
            Log.e("app", "Login.configureView", e.fillInStackTrace());
        }
    }
    
    private Boolean checkLogin()
    {
        Boolean valid = true;
        
        try {
            EditText text = (EditText)activity.findViewById(R.id.email);
            if (text == null || !isEmailValid(text.getText().toString())) {
                valid = false;
                ((TextInputLayout)activity.findViewById(R.id.email_layout)).setError(
                        getResources().getString(R.string.login_error_email)
                );
            }

            text = (EditText)activity.findViewById(R.id.password);
            if (text == null || text.getText().toString().length() <= 1) {
                valid = false;
                ((TextInputLayout)activity.findViewById(R.id.password_layout)).setError(
                        getResources().getString(R.string.login_error_password)
                );
            }
            
        } catch (Exception e) {
            Log.e("app", "Login.checkLogin", e.fillInStackTrace());
        }
        
        return valid;
    }
    
    private class createLogin extends AsyncTask<String, String, String>
    {
        private Boolean loginState = false;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = ProgressDialog.show(activity, "Loading", "Please wait...", true);
        }

        @Override
        protected String doInBackground(String... uri)
        {
            Log.d("app", "start login request");
            try {
                String email = ((EditText)activity.findViewById(R.id.email)).getText().toString();
                String password = ((EditText)activity.findViewById(R.id.password)).getText().toString();

                Runtastic runtastic = new Runtastic(activity);

                runtastic.seteMail(email);
                runtastic.setPassword(password);

                loginState = runtastic.login();

            } catch (Exception e) {
                Log.e("app", "Login.createLogin", e.fillInStackTrace());
            }
            
            return "";
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            dialog.dismiss();
            
            if (!loginState) {
                // TODO add login error message
                Log.d("app", "login failed");
                return;
            }
            
            // remove login screen
            Log.d("app", "end login");
            MainPresenter mainPresenter = MainPresenter.getInstance();
            mainPresenter.createMessage(R.string.login_success);
            
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    
    /**
     * method is used for checking valid email id format.
     *
     * @param email the validate email address
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email)
    {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        
        return isValid;
    }
}
