package mx.edu.ittepic.appegresados;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class Slide extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setWizardMode(true);
        // Instead of fragments, you can also use our default slide
        setSlideOverAnimation();
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(Fragmento.newInstance(R.layout.fragment_registro1));
        addSlide(AppIntroFragment.newInstance("¡Indica que te gusta!", "Selecciona los intereses que quieras para recibir noticias relacionadas.", R.drawable.ic_favorite_border_white_48dp, Color.parseColor("#388E3C")));
        addSlide(AppIntroFragment.newInstance("Presume lo mejor de ti", "¡Que no se te escape nada!, presume tus aptitudes para que empresas y colaboradores puedan contactarte.", R.drawable.ic_work_white_48dp, Color.parseColor("#795548")));
        addSlide(AppIntroFragment.newInstance("Completa tu perfil", "Comparte tu correo, tu profesión y más, para contactar y conocer profesionales como tu.", R.drawable.ic_assignment_ind_white_48dp, Color.parseColor("#D32F2F")));
        addSlide(AppIntroFragment.newInstance("Listo", "Ya casi estas registrado, solo falta que verifiques tu correo. Bienvenido al mundo profesional.", R.drawable.ic_mail_outline_white_48dp, Color.parseColor("#607D8B")));

        // OPTIONAL METHODS
        // Override bar/separator color.
        //setBarColor(Color.parseColor("#e67668"));
        //setSeparatorColor(Color.parseColor("#df4a1c"));

        // Hide Skip/Done button.
        showSkipButton(false);
        //setProgressButtonEnabled(true);


        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent i = new Intent(Slide.this, Principal.class);
        startActivity(i);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
                super.onSlideChanged(oldFragment, newFragment);
    }


}
