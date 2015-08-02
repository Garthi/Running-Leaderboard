package os.running.leaderboard.app.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import os.running.leaderboard.app.R;
import os.running.leaderboard.app.base.Database;
import os.running.leaderboard.app.base.Runtastic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 * @todo make this as activity not as fragment
 */
public class Login extends Fragment
{
    private LinearLayout mainView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.mainView = (LinearLayout)inflater.inflate(R.layout.login, container, false);
        
        this.configureView();
        
        return this.mainView;
    }
    
    private void configureView()
    {
        try {
            Button button = (Button) this.mainView.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
            EditText text = (EditText)this.mainView.findViewById(R.id.email);
            if (text == null || !isEmailValid(text.getText().toString())) {
                valid = false;
                ((TextInputLayout)this.mainView.findViewById(R.id.email_layout)).setErrorEnabled(true);
            }

            text = (EditText)this.mainView.findViewById(R.id.password);
            if (text == null || text.getText().toString().length() <= 1) {
                valid = false;
                ((TextInputLayout)this.mainView.findViewById(R.id.password_layout)).setErrorEnabled(true);
            }
            
        } catch (Exception e) {
            Log.e("app", "Login.checkLogin", e.fillInStackTrace());
        }
        
        return valid;
    }
    
    private class createLogin extends AsyncTask<String, String, String>
    {
        Boolean loginState = false;
        
        @Override
        protected String doInBackground(String... uri) {
            Log.d("app", "start login request");
            try {
                String email = ((EditText)mainView.findViewById(R.id.email)).getText().toString();
                String password = ((EditText)mainView.findViewById(R.id.password)).getText().toString();

                Runtastic runtastic = new Runtastic(getActivity());

                runtastic.seteMail(email);
                runtastic.setPassword(password);

                loginState = runtastic.login();

            } catch (Exception e) {
                Log.e("app", "Login.createLogin", e.fillInStackTrace());
            }
            
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (!loginState) {
                // TODO add login error message
                Log.d("app", "login failed");
                return;
            }
            
            // update navigation header
            Database DB = Database.getInstance(getActivity());
            
            String accountData = DB.getAccountData("firstName");
            if (accountData != null) {
                TextView text = (TextView)getActivity().findViewById(R.id.username);
                text.setText(accountData);
            }

            accountData = DB.getAccountData("avatarUrl");
            if (accountData != null) {
                CircleImageView image = (CircleImageView)getActivity().findViewById(R.id.profile_image);
                Picasso.with(getActivity()).load(accountData).placeholder(R.drawable.default_profile).into(image);
            }
            
            // TODO remove login screen
            Log.d("app", "end login");
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
