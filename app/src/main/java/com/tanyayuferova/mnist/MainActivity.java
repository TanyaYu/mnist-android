package com.tanyayuferova.mnist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.byox.drawview.views.DrawView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private DrawView drawView;
    private FloatingActionButton predictButton;
    private MnistModel model = new MnistModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = findViewById(R.id.draw_view);
        predictButton = findViewById(R.id.predict_btn);

        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPredictClick();
            }
        });
    }

    private void onPredictClick() {
        // todo get picture from drawView
        // todo predict the number
        String answer = model.predict();
        showPredictionDialog(answer);
        drawView.restartDrawing();
    }

    private void showPredictionDialog(String prediction) {
        new AlertDialog.Builder(this)
            .setMessage(prediction)
            .setTitle(R.string.prediction_dialog_title)
            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onPredictRightClick();
                }
            })
            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onPredictWrongClick();
                }
            })
            .create()
            .show();
    }

    private void onPredictRightClick() {

    }

    private void onPredictWrongClick() {

    }
}
