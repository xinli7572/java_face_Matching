package com.example.demo.controller.face;


import ai.onnxruntime.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.Map;

public class MobileFaceNetJava {
    public static OrtSession session;
    public static OrtEnvironment env;

    static {
        try {
            env = OrtEnvironment.getEnvironment();
            session = env.createSession("C:\\javaboot\\facerecognition\\native\\modelnew.onnx", new OrtSession.SessionOptions());

        } catch (Exception e) {
            System.err.println("DLL 加载失败: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public float[] recognition(BufferedImage image) throws Exception {

        // 打印模型输入输出信息
        for (Map.Entry<String, NodeInfo> entry : session.getInputInfo().entrySet()) {
            System.out.printf("Input Name: %s, Shape: %s%n", entry.getKey(),
                    java.util.Arrays.toString(((TensorInfo) entry.getValue().getInfo()).getShape()));
        }

        for (Map.Entry<String, NodeInfo> entry : session.getOutputInfo().entrySet()) {
            System.out.printf("Output Name: %s, Shape: %s%n", entry.getKey(),
                    java.util.Arrays.toString(((TensorInfo) entry.getValue().getInfo()).getShape()));
        }

        int targetSize = 112;  // 根据 MobileFaceNet 模型输入尺寸
        BufferedImage resized = ImagePreprocessor.resizeImage(image, targetSize, targetSize);
        float[] inputData = ImagePreprocessor.bufferFromImage(resized, false); // 不归一化 RGB

        // 创建输入 tensor
        FloatBuffer fb = FloatBuffer.wrap(inputData);
        long[] shape = new long[]{1, 3, targetSize, targetSize};
        OnnxTensor inputTensor = OnnxTensor.createTensor(env, fb, shape);

        // 推理运行
        try {
            OrtSession.Result res = session.run(Collections.singletonMap(session.getInputNames().iterator().next(), inputTensor));
            OnnxValue out = res.get(0);  // 或按名称获取
            float[][] embedding = (float[][]) ((OnnxTensor) out).getValue();

            // 输出人脸特征向量示例
            System.out.println("Embedding size: " + embedding[0].length);
            for (int i = 0; i < embedding[0].length; i++) {
                //System.out.printf("%.4f ", embedding[0][i]);
            }

            return embedding[0];
        } catch (Exception e) {

        }
        return null;
    }


}
