package com.example.demo.controller.face;


import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ImagePreprocessor {

    public static BufferedImage base64ToBufferedImage(String base64String) throws IOException {
        // 去掉 data:image/png;base64, 前缀
        String base64Image = base64String.split(",")[1];

        // 解码为 byte[]
        byte[] imageBytes = Base64Utils.decodeFromString(base64Image);

        // 转为 BufferedImage
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = ImageIO.read(bis);
        bis.close();

        return bufferedImage;
    }


    public static float[] bufferFromImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        float[] buffer = new float[3 * height * width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                int idx = y * width + x;
                buffer[idx] = r;                         // Red channel
                buffer[height * width + idx] = g;        // Green channel
                buffer[2 * height * width + idx] = b;    // Blue channel
            }
        }

        return buffer;
    }

    public static BufferedImage resizeWithPadding(BufferedImage inputImage, int targetWidth, int targetHeight) {
        int originalWidth = inputImage.getWidth();
        int originalHeight = inputImage.getHeight();

        // 计算缩放比例
        float ratio = Math.min((float) targetWidth / originalWidth, (float) targetHeight / originalHeight);
        int scaledWidth = Math.round(originalWidth * ratio);
        int scaledHeight = Math.round(originalHeight * ratio);

        // 缩放图像
        Image scaledImage = inputImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

        // 创建带黑色背景的目标图像
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = outputImage.createGraphics();

        // 填充黑色背景
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, targetWidth, targetHeight);

        // 计算居中位置
        int x = (targetWidth - scaledWidth) / 2;
        int y = (targetHeight - scaledHeight) / 2;

        // 画缩放后的图像到居中位置
        g2d.drawImage(scaledImage, x, y, null);
        g2d.dispose();

        return outputImage;
    }

    //=========================================================

    public static BufferedImage resizeWithPadding2(BufferedImage input, int w, int h) {
        float ratio = Math.min((float)w / input.getWidth(), (float)h / input.getHeight());
        int sw = Math.round(input.getWidth() * ratio);
        int sh = Math.round(input.getHeight() * ratio);
        Image tmp = input.getScaledInstance(sw, sh, Image.SCALE_SMOOTH);
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = output.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);
        g.drawImage(tmp, (w - sw) / 2, (h - sh) / 2, null);
        g.dispose();
        return output;
    }

    public static float[] bufferFromImage(BufferedImage img, boolean normalize) {
        int w = img.getWidth(), h = img.getHeight();
        float[] buf = new float[3 * w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int idx = y * w + x;
                buf[idx] = normalize ? r / 255f : r;
                buf[w * h + idx] = normalize ? g / 255f : g;
                buf[2 * w * h + idx] = normalize ? b / 255f : b;
            }
        }
        return buf;
    }

    //==========================================
    public static BufferedImage cutAndSave(BufferedImage original, int x, int y, int width, int height) throws IOException {

        // 防止超出原图范围
        int maxX = Math.min(x + width, original.getWidth());
        int maxY = Math.min(y + height, original.getHeight());
        x = Math.max(0, x);
        y = Math.max(0, y);
        width = maxX - x;
        height = maxY - y;

        BufferedImage cropped = original.getSubimage(x, y, width, height);
        return cropped;
    }


    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();

        // 设置平滑缩放
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g.dispose();

        return resizedImage;
    }

    public static float[] l2Normalize(float[] vector) {
        float sum = 0f;
        for (float v : vector) {
            sum += v * v;
        }
        float norm = (float) Math.sqrt(sum);

        if (norm > 0) {
            float[] result = new float[vector.length];
            for (int i = 0; i < vector.length; i++) {
                result[i] = vector[i] / norm;
            }
            return result;
        } else {
            return vector;  // 避免除以0
        }
    }

    public static float cosineSimilarity(float[] vec1, float[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("Vectors must be the same length");
        }

        float dot = 0f;
        float normA = 0f;
        float normB = 0f;

        for (int i = 0; i < vec1.length; i++) {
            dot += vec1[i] * vec2[i];
            normA += vec1[i] * vec1[i];
            normB += vec2[i] * vec2[i];
        }

        if (normA == 0f || normB == 0f) {
            return 0f;  // 避免除以0
        }

        return dot / ((float) Math.sqrt(normA) * (float) Math.sqrt(normB));
    }

}
