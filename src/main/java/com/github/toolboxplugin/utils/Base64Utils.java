package com.github.toolboxplugin.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Utils {

    /**
     * 将图片文件转化为 byte 数组
     *
     * @param image 待处理图片文件
     * @return 图片文件转化为的byte数组
     */
    public static byte[] toBytes(File image) {
        try (FileInputStream input = new FileInputStream(image)) {
            // InputStream 的 available() 返回的值是该InputStream 在不被阻塞的情况下，一次可以读取到的数据长度。
            // byte[] imageBytes = new byte[input.available()];
            // input.read(imageBytes);
            return IOUtils.toByteArray(input);
        } catch (IOException e) {
            return null;
        }
    }

    public static String toBase64(byte[] bytes) {
        return bytesEncode2Base64(bytes);
    }

    /**
     * 将图片转化为 base64 的字符串
     *
     * @param image 待处理图片文件
     * @return 图片文件转化出来的 base64 字符串
     */
    public static String toBase64(File image) {
        return toBase64(image, false);
    }

    /**
     * 将图片转化为 base64 的字符串。如果<code>appendDataURLScheme</code>的值为true，则为图片的base64字符串拓展Data URL scheme。
     *
     * @param image               图片文件的路径
     * @param appendDataURLScheme 是否拓展 Data URL scheme 前缀
     * @return 图片文件转化为的base64字符串
     */
    public static String toBase64(File image, boolean appendDataURLScheme) {
        String imageBase64 = bytesEncode2Base64(toBytes(image));
        if (appendDataURLScheme) {
            imageBase64 = ImageDataURISchemeMapper.getScheme(image) + imageBase64;
        }
        return imageBase64;
    }

    private static String bytesEncode2Base64(byte[] bytes) {
        return new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
    }

    private static byte[] base64Decode2Bytes(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    /**
     * 将byte数组恢复为图片文件
     *
     * @param imageBytes 图片文件的 byte 数组
     * @param imagePath  恢复的图片文件的保存地址
     * @return 如果生成成功，则返回生成的文件路径，此时结果为参数的<code>imagePath</code>。否则返回 null
     */
    public static File toImage(byte[] imageBytes, File imagePath) {
        if (!imagePath.getParentFile().exists()) {
            imagePath.getParentFile().mkdirs();
        }
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imagePath))) {
            bos.write(imageBytes);
            return imagePath;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 将base64字符串恢复为图片文件
     *
     * @param imageBase64 图片文件的base64字符串
     * @param imagePath   恢复的图片文件的保存地址
     * @return 如果生成成功，则返回生成的文件路径，此时结果为参数的<code>imagePath</code>。。否则返回 null
     */
    public static File toImage(String imageBase64, File imagePath) {
        // base64 字符串中没有 ","
        int firstComma = imageBase64.indexOf(",");
        if (firstComma >= 0) {
            imageBase64 = imageBase64.substring(firstComma + 1);
        }
        return toImage(base64Decode2Bytes(imageBase64), imagePath);
    }

    /**
     * 保存 imageBase64 到指定文件中。如果<code>fileName</code>含有拓展名，则直接使用<code>fileName</code>的拓展名。
     * 否则，如果 <code>imageBase64</code> 为Data URLs，则更具前缀的来判断拓展名。如果无法判断拓展名，则使用“png”作为默认拓展名。
     *
     * @param imageBase64 图片的base64编码字符串
     * @param dir         保存图片的目录
     * @param fileName    图片的名称
     * @return 如果生成成功，则返回生成的文件路径。否则返回 null
     */
    public static File toImage(String imageBase64, File dir, String fileName) {
        File imagePath = null;
        if (fileName.indexOf(".") < 0) {
            String extension = ImageDataURISchemeMapper.getExtensionFromImageBase64(imageBase64, "png");
            imagePath = new File(dir, fileName + "." + extension);
        } else {
            imagePath = new File(dir, fileName);
        }
        return toImage(imageBase64, imagePath);
    }

    public static void main(String[] args) {
        String bas = "data:image/png;base64,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";
        String imgpath = "D:/idea-develop-project/Project_All/ToolBoxPlugin/src/main/resources/images/icons/";
        File login_qr = Base64Utils.toImage(bas, new File(imgpath), "login_qr");
        System.out.println("login_qr = " + login_qr.getPath());
    }

}