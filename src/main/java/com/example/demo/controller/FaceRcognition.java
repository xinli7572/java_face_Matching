package com.example.demo.controller;


import com.example.demo.controller.face.CenterFaceJava;
import com.example.demo.controller.face.FaceBox;
import com.example.demo.controller.face.ImagePreprocessor;
import com.example.demo.controller.face.MobileFaceNetJava;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;


@Controller
public class FaceRcognition {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Hello, Spring Boot with Thymeleaf!");
        return "index";
    }


    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestBody ImageRequest imageRequest) {

        try {

            String action = imageRequest.getAction();

            System.out.println("action = " + action);
            System.out.println("action = " + action);
            System.out.println("action = " + action);
            System.out.println("action = " + action);

            String pic_name = "1.jpg";
            if (action.equals("face_1")) {
                pic_name = "1.jpg";
            }
            if (action.equals("face_2")) {
                pic_name = "2.jpg";
            }
            if (action.equals("face_3")) {
                pic_name = "3.jpg";
            }
            if (action.equals("face_4")) {
                pic_name = "4.png";
            }

            BufferedImage face = ImagePreprocessor.base64ToBufferedImage(imageRequest.getImage());

            File file_2 = new File("C:\\javaboot\\facerecognition\\native\\pic\\" + pic_name);
            BufferedImage image_2 = ImageIO.read(file_2);

            FaceBox face_box_1 = new CenterFaceJava().detectFace_box(face);
            FaceBox face_box_2 = new CenterFaceJava().detectFace_box(image_2);

            float[] recognition_1 = new MobileFaceNetJava().recognition(face_box_1.resizedImage);
            float[] recognition_2 = new MobileFaceNetJava().recognition(face_box_2.resizedImage);

            ImagePreprocessor imagePreprocessor = new ImagePreprocessor();

            float cos = imagePreprocessor.cosineSimilarity(imagePreprocessor.l2Normalize(recognition_1), imagePreprocessor.l2Normalize(recognition_2));

            System.out.println();
            System.out.println("cos = " + cos);
            System.out.println();

            String match = String.valueOf(cos);
            if (cos > 0.6) {
                match = match + "  MATCH";
            } else {
                match = match + "  NO MATCH";
            }


            //  Base64
            ByteArrayOutputStream baos_1 = new ByteArrayOutputStream();
            ImageIO.write(face_box_1.resizedImage, "png", baos_1);
            String base64Image_1 = Base64Utils.encodeToString(baos_1.toByteArray());

            ByteArrayOutputStream baos_2 = new ByteArrayOutputStream();
            ImageIO.write(face_box_2.resizedImage, "png", baos_2);
            String base64Image_2 = Base64Utils.encodeToString(baos_2.toByteArray());

            ByteArrayOutputStream baos_3 = new ByteArrayOutputStream();
            ImageIO.write(image_2, "png", baos_3);
            String base64Image_3 = Base64Utils.encodeToString(baos_3.toByteArray());


            if (face_box_1 != null) {

                JSONObject box2 = new JSONObject();
                box2.put("x", face_box_1.x1 - 135);
                box2.put("y", face_box_1.y1 - 185);
                box2.put("width", face_box_1.x2 - face_box_1.x1 - 60);
                box2.put("height", face_box_1.x2 - face_box_1.x1- 30);
                box2.put("image_1", "data:image/png;base64," + base64Image_1);
                box2.put("image_2", "data:image/png;base64," + base64Image_2);
                box2.put("image_3", "data:image/png;base64," + base64Image_3);
                box2.put("cos", match);

                // 构建 boxes 数组
                JSONArray boxes = new JSONArray();
                boxes.put(box2);

                // 总体 JSON 对象
                JSONObject result = new JSONObject();
                result.put("boxes", boxes);

                //System.out.println(result.toString());

                return ResponseEntity.ok(result.toString());

            }

        } catch (Exception e) {

        }
        JSONObject result = new JSONObject();
        result.put("boxes", "fail");
        return ResponseEntity.ok(result.toString());
    }

    // 定义一个内嵌的类来接收前端传来的图像数据
    public static class ImageRequest {
        private String image; // Base64编码的图像数据
        private String action;

        // getter 和 setter
        public String getImage() {
            return image;
        }

        public String getAction() {
            return action;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }


}
