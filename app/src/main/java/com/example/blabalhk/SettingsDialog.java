package com.example.blabalhk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

public class SettingsDialog extends DialogFragment {

//    AudioManager audioManager = getApplicationContext().getSystemService(AudioManager.class);
    private String[] catNamesArray;
    private int items;
    private MainActivity t;

    public SettingsDialog(String[] catNamesArray, int items, MainActivity context){
        this.catNamesArray = catNamesArray;
        this.items = items;
        t = context;
    }
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

//        final String[] catNamesArray = {"Васька", "Рыжик", "Мурзик"};


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


//
            builder.setTitle("Выберите .....")
                    // добавляем переключатели
                    .setSingleChoiceItems(catNamesArray, items,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int item) {
                                    Toast.makeText(
                                            getActivity(),
                                            "Любимое имя кота: "
                                                    + catNamesArray[item],
                                            Toast.LENGTH_SHORT).show();
                                    items = item;

                                }
                            })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK, so save the mSelectedItems results somewhere
                            // or return them to the component that opened the dialog
                            System.out.println(items);
                            t.setResult_devise(items);

                        }
                    })
                    .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            return builder.create();
         }
}