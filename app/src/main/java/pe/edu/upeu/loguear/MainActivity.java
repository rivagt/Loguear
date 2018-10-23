package pe.edu.upeu.loguear;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.appevents.AppEventsLogger;


public class MainActivity extends AppCompatActivity {

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Iniciamos SDK de Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        //Establecemos devoluciones de llamadas
        callbackManager = CallbackManager.Factory.create();
        info = (TextView) findViewById(R.id.info);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        //Registramos las devoluciones de llamadas
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                info.setText(
                        "User ID: " + loginResult.getAccessToken().getUserId()+ "n" + "Auth Token: " + loginResult.getAccessToken().getToken());
                makeToast(loginResult.getAccessToken().toString());
            }

            @Override
            public void onCancel() {
                String cancelMessage = "Login Cancelado.";
                info.setText(cancelMessage);
                makeToast(cancelMessage);

            }

            @Override
            public void onError(FacebookException error) {
                String errorMessage = "Login error.";
                info.setText(errorMessage);
                makeToast(errorMessage);
            }
        });

        if (isLoggedIn()) {
            info.setText("User ID: " + AccessToken.getCurrentAccessToken().getUserId());

        }

    }
    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return (accessToken != null) && (!accessToken.isExpired());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //los logs de 'instalar' y 'aplicación activa' App Eventos.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Logs de'app desactivada' App Eventos.
        AppEventsLogger.deactivateApp(this);
    }

}
