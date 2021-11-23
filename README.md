# SliderCheckUtil--图片滑块验证码
### 背景
在PC和App中, 常用图片滑块来做辅助验证, 如下图所示:![滑块验证码.png](https://upload-images.jianshu.io/upload_images/1646270-29a28514f2364a50.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 分析
##### 图片素材
- 背景图片 
- 滑块形状
- 滑块阴影
##### 后台
  **随机生成滑动的位置, 得到X,Y轴坐标(以左上角为原点), 根据图片素材与坐标**
(1)用滑块阴影覆盖实际滑块位置, 生成实际背景图片
(2)将实际滑块位置图形与滑块形状重叠, 生成实际滑块形状图片
**返回数据给前台**
(1)实际背景图片以及其尺寸
(2)实际滑块形状图片以及其尺寸
(3)Y轴坐标, X轴用于校验 (后台校验则不返回, 由前台校验可返回)
##### 前台
显示背景图片, 根据Y轴坐标, 放置滑块位置, 添加滑动事件, 将滑块实际滑动的X轴距离进行校验

###Java工具类使用
- 滑块配置
    1.将图片素材放到项目静态文件夹下, 如图:![图片素材.png](https://upload-images.jianshu.io/upload_images/1646270-bfaa5456650e2a95.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

  2.配置工具类中图片素材的数量与路径
```
public class SliderCheckUtil {
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
}
```
- 滑块对象字段说明
```
public class SliderCheck {
    // 原图(实际背景图片) base64
    private String resourceImg;
    // 原图宽度
    private Integer resourceWidth;
    // 原图调度
    private Integer resourceHeight;

    // 拼图(实际滑块形状) base64
    private String puzzleImg;
    // 拼图宽度
    private Integer puzzleWidth;
    // 拼图调度
    private Integer puzzleHeight;

    // X轴坐标
    private Integer puzzleXAxis;
    // Y轴坐标
    private Integer puzzleYAxis;
}
```
- 调用工具类, 将滑块对象返回给前台
```
SliderCheck sliderCheck = SliderCheckUtil.build();
```

- 校验, 根据X轴坐标与滑动距离比较
```
// 可配置 SLIDER_RANGE 容错值, 默认为10
boolean isSuccess = SliderCheckUtil.verifySlider(sliderCheck, distance);
```

### 备注
- Demo提供两套切图, 分别为PC端和App端使用, 如尺寸不合适请自行切图
1. PC端图片尺寸 
  (1)背景图: 450x300px
  (2)滑块形状与阴影: 88x88px
2. App端图片尺寸(App请注意px与pt的转换)
  (1)背景图: 300x200px
  (2)滑块形状与阴影: 88x88px
- 校验方式只根据X轴距离, 比较简单, 可自行增加校验方式提高安全性, 如:  
(1)校验次数
(2)超时失败
(2)X轴加Y轴
(3)加密如:MD5

> 如果还有不懂的问题, 或者出现其它bug
请查看Demo: [Demo](https://github.com/herobin22/SliderCheckUtil)
简书地址:https://www.jianshu.com/p/1c208774e937
或者给我留言, 喜欢的话, 就给作者一个star
