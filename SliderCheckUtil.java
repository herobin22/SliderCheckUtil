package com.cnhis.cloudhealth.crm.common.SliderCheck;

import com.cnhis.cloudhealth.crm.common.log.ToolLogger;
import com.cnhis.cloudhealth.crm.common.log.ToolLoggerFactory;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

/**
 * 滑块验证码工具类
 * Copyright (c) 2019 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author oy
 * @since 2019/8/22 19:51
 */
public class SliderCheckUtil {

    private static final ToolLogger TOOL_LOGGER = ToolLoggerFactory.getLogger(SliderCheckUtil.class);

    /**
     * 背景图数量, 随机生成之一
     */
    private static int BG_COUNT = 3;

    /**
     * 图片素材路径
     * 滑块形状名称: slider_icon.png
     * 滑动阴影名称: slider_icon_bg.png
     */
    private static final String FILE_PATH = "static/sliderCheck";

    /**
     * 背景图片路径
     * 默认背景图片名称: slider_bg_%d.jpg
     */
    private static final String BG_FILE_PATH = FILE_PATH + "/bg";

    /**
     * 滑动容错范围, 默认:10, 表示校验X轴时左右10个单位内都算正确
     */
    private static final int SLIDER_RANGE = 10;

    static {
        URL resource = SliderCheckUtil.class.getClassLoader().getResource(BG_FILE_PATH);
        if (resource != null) {
            File file = new File(resource.getPath());
            if (file != null) {
                File[] files = file.listFiles();
                if (files != null) {
                    BG_COUNT = files.length;
                }
            }
        }
    }

    /**
     * 生成滑块对象
     * @return
     */
    public static SliderCheck build() {
        if (BG_COUNT == 0) {
            TOOL_LOGGER.error("图形滑块验证码异常: 请上传背景图");
            return null;
        }
        try {
            Random random = new Random();
            int num = random.nextInt(BG_COUNT) + 1;
            String bgPath = String.format("%s/slider_bg_%d.jpg", BG_FILE_PATH, num);
            String iconPath = String.format("%s/slider_icon.png", FILE_PATH);
            String puzzlePath = String.format("%s/slider_icon_bg.png", FILE_PATH);

            // 滑块图标
            ClassLoader classLoader = SliderCheckUtil.class.getClassLoader();
            InputStream iconStream = classLoader.getResourceAsStream(iconPath);
            if (iconStream == null) {
                TOOL_LOGGER.error("图形滑块验证码异常: 请上传滑块图标");
                return null;
            }

            // 滑块背景图标
            InputStream puzzleStream = classLoader.getResourceAsStream(puzzlePath);
            if (iconStream == null) {
                TOOL_LOGGER.error("图形滑块验证码异常: 请上传滑块背景图标");
                return null;
            }

            // 背景图
            InputStream bgStream = classLoader.getResourceAsStream(bgPath);
            if (iconStream == null) {
                TOOL_LOGGER.error("图形滑块验证码异常: 请上传背景图");
                return null;
            }

            BufferedImage iconImg = ImageIO.read(iconStream);
            int[][] blockData = getIconData(iconImg);
            int iconWidth = iconImg.getWidth();
            int iconHeight = iconImg.getHeight();
            BufferedImage puzzleImg = ImageIO.read(puzzleStream);
            BufferedImage bgImg = ImageIO.read(bgStream);

            // 四边减小滑块宽高作为滑动区域
            int bgWidth = bgImg.getWidth();
            int maxX = bgWidth - iconWidth * 2;
            int x = random.nextInt(maxX) + iconWidth;
            int bgHeight = bgImg.getHeight();
            int maxY = bgHeight - iconHeight;
            int y = random.nextInt(maxY);
            cutByTemplate(bgImg, iconImg, puzzleImg, blockData, x, y);

            String bgImgSt = getImageBASE64(bgImg, "jpg");
            String puzzleImgSt = getImageBASE64(puzzleImg, "png");

            SliderCheck sliderCheck = new SliderCheck();
            sliderCheck.setResourceImg(bgImgSt);
            sliderCheck.setResourceWidth(bgWidth);
            sliderCheck.setResourceHeight(bgHeight);
            sliderCheck.setPuzzleImg(puzzleImgSt);
            sliderCheck.setPuzzleWidth(iconWidth);
            sliderCheck.setPuzzleHeight(iconHeight);
            sliderCheck.setPuzzleXAxis(x);
            sliderCheck.setPuzzleYAxis(y);
            return sliderCheck;
        } catch (Exception e) {
            TOOL_LOGGER.error("创建图形滑块验证码异常:", e);
            return null;
        }
    }

    /**
     * 校验滑块
     * @param sliderCheck 滑块对象
     * @param distance 滑动距离
     * @return
     */
    public static boolean verifySlider(SliderCheck sliderCheck, Integer distance) {
        return sliderCheck != null && verifySlider(sliderCheck.getPuzzleXAxis(), distance);
    }

    /**
     * 校验滑块
     * @param puzzleXAxis 滑块对象X轴
     * @param distance 滑动距离
     * @return
     */
    public static boolean verifySlider(Integer puzzleXAxis, Integer distance) {
        return puzzleXAxis != null
                && distance != null
                && distance > 0
                && distance > puzzleXAxis - SLIDER_RANGE && distance < puzzleXAxis + SLIDER_RANGE;
    }

    /**
     * 转换图片Base64
     * @param image
     * @param formatName
     * @return
     * @throws IOException
     */
    public static String getImageBASE64(BufferedImage image, String formatName) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, formatName, out);
        byte[] b = out.toByteArray();
        return new String(Base64.encodeBase64(b));//生成base64编码
    }

    /**
     * 根据png图生成图片轮廓
     * @param icon
     * @return
     * @throws Exception
     */
    private static int[][] getIconData(BufferedImage icon) throws Exception {
        int[][] data = new int[icon.getWidth()][icon.getHeight()];
        for (int i = 0; i < icon.getWidth(); i++) {
            for (int j = 0; j < icon.getHeight(); j++) {
                int rgb = icon.getRGB(i, j);
                data[i][j] = rgb == 0 ? 0 : 1;
            }
        }
        return data;
    }

    /**
     *
     * @Createdate: 2019年1月24日上午10:51:30
     * @Title: cutByTemplate
     * @Description: 生成小图片、给大图片添加阴影
     * @author mzl
     * @param bgImg
     * @param puzzleImg
     * @param blockData
     * @param x
     * @param y void
     * @throws
     */
    private static void cutByTemplate(BufferedImage bgImg, BufferedImage iconImage, BufferedImage puzzleImg, int[][] blockData, int x, int y) {
        for (int i = 0; i < blockData.length; i++) {
            int[] data = blockData[i];
            for (int j = 0; j < data.length; j++) {
                int rgb = data[j];
                // 原图中对应位置变色处理
                int rgb_ori = bgImg.getRGB(x + i, y + j);

                if (rgb == 1) {
                    // 抠图上复制对应颜色值
                    puzzleImg.setRGB(i, j, rgb_ori);
                    // 原图对应位置颜色变化(原图通过绘制的方式进行图层叠加)
                    // bgImg.setRGB(x + i, y + j, rgb_ori & 0x363636);
                }else{
                    // 这里把背景设为透明(背景图自带阴影, 这里不作处理)
                    // puzzleImg.setRGB(i, j, rgb_ori & 0xffffff);
                }
            }
        }

        // 绘制背景图
        Graphics2D g2d = bgImg.createGraphics();
        int imgWidth = iconImage.getWidth();
        int imgHeight = iconImage.getHeight();
        // 在图形和图像中实现混合和透明效果
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1f));
        // 绘制
        g2d.drawImage(iconImage, x, y, imgWidth, imgHeight, null);
        // 释放图形上下文使用的系统资源
        g2d.dispose();
    }

}