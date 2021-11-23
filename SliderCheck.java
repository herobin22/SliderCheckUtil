package com.cnhis.cloudhealth.crm.common.SliderCheck;

/**
 * @Date 2020/3/24 10:45
 * @Author xx
 * @ClassName SliderCheck
 **/
public class SliderCheck {

    // 原图 base64
    private String resourceImg;
    private Integer resourceWidth;
    private Integer resourceHeight;

    // 拼图 base64
    private String puzzleImg;
    private Integer puzzleWidth;
    private Integer puzzleHeight;

    // 坐标
    private Integer puzzleXAxis;
    private Integer puzzleYAxis;

    public String getResourceImg() {
        return resourceImg;
    }

    public void setResourceImg(String resourceImg) {
        this.resourceImg = resourceImg;
    }

    public Integer getResourceWidth() {
        return resourceWidth;
    }

    public void setResourceWidth(Integer resourceWidth) {
        this.resourceWidth = resourceWidth;
    }

    public Integer getResourceHeight() {
        return resourceHeight;
    }

    public void setResourceHeight(Integer resourceHeight) {
        this.resourceHeight = resourceHeight;
    }

    public String getPuzzleImg() {
        return puzzleImg;
    }

    public void setPuzzleImg(String puzzleImg) {
        this.puzzleImg = puzzleImg;
    }

    public Integer getPuzzleWidth() {
        return puzzleWidth;
    }

    public void setPuzzleWidth(Integer puzzleWidth) {
        this.puzzleWidth = puzzleWidth;
    }

    public Integer getPuzzleHeight() {
        return puzzleHeight;
    }

    public void setPuzzleHeight(Integer puzzleHeight) {
        this.puzzleHeight = puzzleHeight;
    }

    public Integer getPuzzleXAxis() {
        return puzzleXAxis;
    }

    public void setPuzzleXAxis(Integer puzzleXAxis) {
        this.puzzleXAxis = puzzleXAxis;
    }

    public Integer getPuzzleYAxis() {
        return puzzleYAxis;
    }

    public void setPuzzleYAxis(Integer puzzleYAxis) {
        this.puzzleYAxis = puzzleYAxis;
    }
}
