package com.tanyayuferova.mnist;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.byox.drawview.views.DrawView;
import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import static com.byox.drawview.enums.DrawingCapture.BITMAP;

public class MainActivity extends AppCompatActivity {

    private DrawView drawView;
    private TextView phoneNumberView;
    private PromptsView promptsView;
    private Toolbar toolbar;
    private MnistClassifier model;

    private PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    private AsYouTypeFormatter formatter;
    private String phoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        formatter = phoneUtil.getAsYouTypeFormatter(getString(R.string.region_code));
        model = new MnistClassifier(this);
        model.initialize();

        phoneNumberView = findViewById(R.id.phone_number_view);
        toolbar = findViewById(R.id.toolbar);
        promptsView = findViewById(R.id.prompts);
        toolbar = findViewById(R.id.toolbar);
        drawView = findViewById(R.id.draw_view);

        drawView.setOnDrawViewListener(new EmptyOnDrawViewListener() {
            @Override
            public void onEndDrawing() {
                classifyDrawing();
            }
        });
        promptsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceLastDigit(((TextView) view).getText().toString());
            }
        });
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_clear) {
                    clear();
                    return true;
                }
                return false;
            }
        });
        clear();
    }

    private void classifyDrawing() {
        Bitmap bitmap = (Bitmap) drawView.createCapture(BITMAP)[0];
        String[] prediction = model.classify(bitmap);
        phoneNumber += prediction[0];
        promptsView.updatePrompts(prediction);
        updatePhoneNumber();
        drawView.restartDrawing();
    }

    private void clear() {
        phoneNumber = "";
        updatePhoneNumber();
        phoneNumberView.setText(R.string.phone_number_stub);
        promptsView.updatePrompts(new String[0]);
    }

    private void replaceLastDigit(String newDigit) {
        phoneNumber = phoneNumber.substring(0, phoneNumber.length() - 1);
        phoneNumber += newDigit;
        updatePhoneNumber();
    }

    private void updatePhoneNumber() {
        formatter.clear();
        String formatted = "";
        for (char c : phoneNumber.toCharArray()) {
            formatted = formatter.inputDigit(c);
        }
        phoneNumberView.setText(formatted);
    }

    @Override
    protected void onDestroy() {
        model.close();
        super.onDestroy();
    }
}
