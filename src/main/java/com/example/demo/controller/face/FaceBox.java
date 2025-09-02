package com.example.demo.controller.face;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class FaceBox {
    public float x1, y1, x2, y2; // 边界框坐标
    public float score;         // 人脸得分
    public float[] landmarks;   // 关键点（长度应为10，表示5个点：x1, y1, ..., x5, y5）
    public BufferedImage resizedImage;

    public FaceBox(float x1, float y1, float x2, float y2, float score, float[] landmarks,BufferedImage resizedImage) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.score = score;
        this.landmarks = landmarks;
        this.resizedImage = resizedImage;
    }

    @Override
    public String toString() {
        return "FaceBox{" +
                "x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                ", score=" + score +
                ", landmarks=" + Arrays.toString(landmarks) +
                '}';
    }
}

