package com.silencedut.particlesystemsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.silencedut.particlesystem.AnimatorManger;
import com.silencedut.particlesystem.ParticleSystem;
import com.silencedut.particlesystem.particledecorations.ScaleDecoration;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        findViewById(R.id.emitter).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new ParticleSystem(MainActivity.this, 50, R.mipmap.star_pink, 1000)
//                        .setSpeedRange(0.1f, 0.25f)
//                        .setRotationSpeed(120)
//                        .addModifier(new ScaleDecoration(0.2f, 2f, 0, 1000))
//                        .oneShot(v, 50);
//            }
//        });
        AnimatorManger animatorManger = new AnimatorManger(this,findViewById(R.id.emitter));
        animatorManger.startAnimator();
    }
}
