package ru.rele.relayconfig;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;

import ru.rele.relayconfig.relaydata.RelayCycleData;

public class CycleNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle_name);

        final RelayCycleData cycleData = ((MainApplication)getApplication()).getCurrentCycle();

        final EditText editText = (EditText) findViewById(R.id.cycleName);
        editText.setText(cycleData.getCycleName());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // pass
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cycleData.setCycleName(charSequence.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {
                // pass
            }
        });

        final ColorPickerView colorPicker = (ColorPickerView) findViewById(R.id.cycleColorPicker);
        colorPicker.setColor(cycleData.getCycleColor(), true);
        colorPicker.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int i) {
                cycleData.setCycleColor(i);
            }
        });

        Button applyNameAndColor = (Button)findViewById(R.id.applyNameAndColor);
        applyNameAndColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CycleNameActivity.this, ManageCyclesActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
