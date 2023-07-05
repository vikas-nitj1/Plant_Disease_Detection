package com.example.plantdiseasedetection;

import androidx.appcompat.app.AppCompatActivity;

public class IMAGE extends AppCompatActivity {
    private String class_name;
    private String Confidence;
    private String error;

    public IMAGE(String class_name , String confidence){
        this.class_name = class_name;
        this.Confidence = confidence;
    }
    public IMAGE(String error){
        this.error = error;
    }
    public String getClass_name() {
        return class_name;
    }

    public String getConfidence() {
        return Confidence;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "IMAGE{" +
                "class_name='" + class_name + '\'' +
                ", Confidence='" + Confidence + '\'' +
                '}';
    }
}
