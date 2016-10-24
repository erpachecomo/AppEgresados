package mx.edu.ittepic.appegresados;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.gms.auth.api.*;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import com.github.paolorotolo.appintro.ISlidePolicy;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link Fragmento#newInstance} factory method to
 * create an instance of this fragment.

 Intent intent = new Intent(getActivity(), SecondActivity.class);
 intent.putExtra("message", data);
 startActivity(intent);
 */

public class Fragmento extends Fragment implements ISlidePolicy, GoogleApiClient.OnConnectionFailedListener {
    @Override
    public boolean isPolicyRespected() {
        boolean se_puede=true;
        String nombre = this.nombre.getText().toString();
        String correo = this.correo.getText().toString();
        if (nombre.equals("") || correo.equals("")) {
            se_puede=false;
            mensaje.setText("Campos vacios, por favor completalos.");
            mensaje.setTextColor(Color.parseColor("#F44336"));
        }else{
            mensaje.setText("Información correcta.");
            mensaje.setTextColor(Color.parseColor("#4CAF50"));
        }
        return se_puede;
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {

    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private static final int RC_SIGN_IN = 007;
    private ProgressDialog mProgressDialog;
    private Button btnSignOut, btnRevokeAccess;


    public Fragmento() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Fragmento.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragmento newInstance(int layoutResId) {
        Fragmento fragment = new Fragmento();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        fragment.setArguments(args);
        return fragment;
    }
    EditText nombre,correo;
    TextView mensaje;
    private SignInButton btnSignIn;
    private GoogleApiClient mGoogleApiClient;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID))
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);


        
//        getArguments().putString("nombre",nombre.getText().toString());
  //      getArguments().putString("correo",correo.getText().toString());

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(Fragmento.class.getSimpleName(), "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Cargando");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            mensaje.setText("Cuenta de Google Aceptada.");
            mensaje.setTextColor(Color.parseColor("#4CAF50"));
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            btnRevokeAccess.setVisibility(View.VISIBLE);
        } else {
            nombre.setText("");
            correo.setText("");
            mensaje.setText("No estas registrado, por favor ingresa los datos.");
            mensaje.setTextColor(Color.parseColor("#F44336"));
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_registro1, container, false);
        btnSignIn = (SignInButton) v.findViewById(R.id.sign_in_button);
        nombre = (EditText) v.findViewById(R.id.editText);
        correo = (EditText) v.findViewById(R.id.editText2);
        mensaje = (TextView) v.findViewById(R.id.textView);
        btnSignOut = (Button) v.findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) v.findViewById(R.id.btn_revoke_access);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        btnRevokeAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revokeAccess();
            }
        });
        return v;
    }
    private int layoutResId;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(Fragmento.class.getSimpleName(), "onConnectionFailed:" + connectionResult);
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(Fragmento.class.getSimpleName(), "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(Fragmento.class.getSimpleName(), "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            Log.e(Fragmento.class.getSimpleName(), "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

            nombre.setText(personName);
            correo.setText(email);
            nombre.setActivated(false);
            correo.setActivated(false);
            /*Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);
*/
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
            nombre.setText("");
            correo.setText("");
            nombre.setActivated(true);
            correo.setActivated(true);
        }
    }
}
