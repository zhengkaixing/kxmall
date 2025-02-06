package com.kxmall.common.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class QRCodeGenerator {

    public static String generateQRCodeBase64(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    public static void main(String[] args) {
        try {
            String base64QRCode = generateQRCodeBase64("这是二维码的内容", 350, 350);
            System.out.println("二维码的Base64编码: " + base64QRCode);
        } catch (WriterException e) {
            System.out.println("无法生成二维码: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O 错误: " + e.getMessage());
        }
    }
}
