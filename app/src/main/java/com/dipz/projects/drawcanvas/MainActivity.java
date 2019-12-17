package com.dipsar.drawcanvas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

public class MainActivity extends AppCompatActivity {

    public static final String DRAW_MODE = "DRAW_MODE";
    public static final String ERASE_MODE = "ERASE_MODE";

    CanvasView canvas;
    String modeDrawOrErase = "";
    private int currentBackgroundColor = 0xffffffff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canvas = (CanvasView) findViewById(R.id.canvas);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnClearCanvas:
                canvas.clearCanvas();
                break;

            case R.id.btnDrawCanvas:
                modeDrawOrErase = DRAW_MODE;
                pickColor();
//                canvas.drawEraseCanvasMode(modeDrawOrErase);
                break;

            case R.id.btnEraseCanvas:
                modeDrawOrErase = ERASE_MODE;
                canvas.drawEraseCanvasMode(modeDrawOrErase);
                break;
        }
    }

    private void pickColor() {
        final Context context = MainActivity.this;
        ColorPickerDialogBuilder
                .with(context)
                .setTitle("Choose color")
                .initialColor(currentBackgroundColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
//                        canvas.setPaintFillColor(Integer.parseInt("0x" + Integer.toHexString(selectedColor)));
                        canvas.setPaintFillColor(Color.parseColor("#"+Integer.toHexString(selectedColor)));
//                        toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
//                        changeBackgroundColor(selectedColor);
                        canvas.setPaintFillColor(Color.parseColor("#"+Integer.toHexString(selectedColor)));
                        canvas.drawEraseCanvasMode(modeDrawOrErase);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }
}
