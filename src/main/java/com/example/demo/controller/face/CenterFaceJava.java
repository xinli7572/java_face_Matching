package com.example.demo.controller.face;


import ai.onnxruntime.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.FloatBuffer;
import java.util.*;



public class CenterFaceJava {

    public static OrtSession session;
    public static OrtEnvironment env;

    static {
        try {
            env = OrtEnvironment.getEnvironment();
            session = env.createSession("C:\\javaboot\\facerecognition\\native\\CenterFace.onnx", new OrtSession.SessionOptions());

        } catch (Exception e) {
            System.err.println("DLL 加载失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public  BufferedImage getFace(BufferedImage image,FaceBox faceBox) {
        try {
            BufferedImage tmp= ImagePreprocessor.cutAndSave(image,(int)faceBox.x1,(int)faceBox.y1,(int)(faceBox.x2-faceBox.x1),(int)(faceBox.x2-faceBox.x1));
            return tmp;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public  FaceBox detectFace_box(BufferedImage image) {
        try {

            int width = 640;  // 目标输入尺寸（取决于模型）
            int height = 640;
            BufferedImage resizedImage = ImagePreprocessor.resizeWithPadding(image, width, height);
            float[] inputTensor = ImagePreprocessor.bufferFromImage(resizedImage);


            // 模型输入维度： [1, 3, H, W]
            OnnxTensor input = OnnxTensor.createTensor(env, FloatBuffer.wrap(inputTensor), new long[]{1, 3, height, width});

            OrtSession.Result result = session.run(Collections.singletonMap("input.1", input));

            // 示例：读取输出结果（实际的输出名称取决于模型）
            float[][][][] heatmap = ((float[][][][]) result.get(0).getValue());
            float[][][][] scale = ((float[][][][]) result.get(1).getValue());
            float[][][][] offset = ((float[][][][]) result.get(2).getValue());
            float[][][][] landmarks = ((float[][][][]) result.get(3).getValue());

            for (Map.Entry<String, NodeInfo> entry : session.getInputInfo().entrySet()) {
                String name = entry.getKey();
                TensorInfo info = (TensorInfo) entry.getValue().getInfo();
                System.out.println("输入名: " + name);
                System.out.println("类型: " + info.type);
                System.out.println("维度: " + Arrays.toString(info.getShape()));
            }

            for (Map.Entry<String, OnnxValue> entry : result) {
                System.out.println("输出名: " + entry.getKey());
                OnnxValue value = entry.getValue();
                System.out.println("输出类型: " + value.getClass().getSimpleName());
                System.out.println("输出值类型: " + value.getValue().getClass());
            }
            // 打印 heatmap 的形状（维度）
            int dim0 = heatmap.length;          // 一般是 batch size，通常是1
            int dim1 = heatmap[0].length;       // 通道数
            int dim2 = heatmap[0][0].length;    // 高度
            int dim3 = heatmap[0][0][0].length; // 宽度
            System.out.printf("heatmap shape: [%d, %d, %d, %d]%n", dim0, dim1, dim2, dim3);

            for (int y = 0; y < Math.min(5, dim2); y++) {
                for (int x = 0; x < Math.min(5, dim3); x++) {
                    //System.out.printf("%.4f ", heatmap[0][0][y][x]);
                }
                System.out.println();
            }


            float threshold = 0.5f;
            int stride = 4;

            List<FaceBox> faces = new ArrayList<FaceBox>();

            for (int y = 0; y < 160; y++) {
                for (int x = 0; x < 160; x++) {
                    float score = heatmap[0][0][y][x];
                    if (score > threshold) {
                        // 得分足够，提取 box 信息
                        float dx = offset[0][0][y][x];
                        float dy = offset[0][1][y][x];
                        float w = (float) Math.exp(scale[0][0][y][x]) * stride;
                        float h = (float) Math.exp(scale[0][1][y][x]) * stride;

                        float cx = x * stride + dx * stride;
                        float cy = y * stride + dy * stride;

                        float x1 = cx - w / 2;
                        float y1 = cy - h / 2;
                        float x2 = cx + w / 2;
                        float y2 = cy + h / 2;

                        // landmark
                        float[] landmark = new float[10];
                        for (int i = 0; i < 10; i++) {
                            landmark[i] = landmarks[0][i][y][x] * stride + (i % 2 == 0 ? x * stride : y * stride);
                        }
                        BufferedImage tmp= ImagePreprocessor.cutAndSave(resizedImage,(int)x1,(int)y1,(int)(x2-x1),(int)(x2-x1));
                        faces.add(new FaceBox(x1, y1, x2, y2, score, landmark,tmp));
                    }
                }
            }

            for (FaceBox face : faces) {
                //System.out.println(face);
            }

            if (faces != null && faces.size() > 0) {
                return faces.get(0);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }

}

