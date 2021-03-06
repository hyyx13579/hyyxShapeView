
hyyxShapeView for Android
============================

依赖
--------------------
## Android Studio/Gradle
* JitPack.io, add jitpack.io repositiory and dependency to your build.gradle:


        repositories {
             maven { url "https://jitpack.io"}
         }

        dependencies {
            compile 'com.github.hyyx13579:hyyxShapeView:v2.0'
        }
	
控件分类
--------------------
## 1.医院通用护理信息的图表功能，可以对体温，血压，血糖，脉搏，呼吸，五大类的信息进行散点图，折线图，柱形图的图表绘制。
### 使用方法

#### 示例

![pluse](https://github.com/hyyx13579/hyyxShapeView/blob/master/screens/pluse.png "脉搏")

![temp](https://github.com/hyyx13579/hyyxShapeView/blob/master/screens/temp.png "脉搏")

![bloodpresure](https://github.com/hyyx13579/hyyxShapeView/blob/master/screens/bloodpresure.png "血压")



#### 在xml文件中

      <com.example.mylibrary.widget.iHospitalShapeView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
         />

#### 在javacode中

      iHospitalShapeView iHospitalShapeView = new iHospitalShapeView(context);
      layout.addview(iHospitalShapeView);

#### 属性

      setAxisX(float maxValue, int divideSize)//设置X轴的最大值及刻度线数量（包括0坐标刻度）
      setAxisY(float maxValue, int divideSize)//设置Y轴的最大值及刻度线数量（包括0坐标刻度）
     
      setDefaultYdataInfo(int type)//设置view自带的Y轴数据,包括体温,血压,脉搏,血糖,呼吸的五种标准化数值

      switch (type) {
            case TYPE_TEMP:
                yLabels = new int[]{32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43};
                setAxisY(43, 12);
                break;

            case TYPE_BLOODPRESURE:
                yLabels = new int[]{40, 60, 80, 100, 120, 140, 160, 180, 200};
                setAxisY(200, 9);
                break;
            case TYPE_PULSE:
                yLabels = new int[]{20, 40, 60, 80, 100, 120, 140};
                setAxisY(140, 7);
                break;
            case TYPE_BLOODSUGAR:
                yLabels = new int[]{0, 3, 6, 9, 12, 15, 18, 21, 24, 27};
                setAxisY(27, 10);
                break;
            case TYPE_BREATHE:
                yLabels = new int[]{5, 10, 15, 20, 25, 30, 35, 40, 45, 50};
                setAxisY(50, 10);
                break;
        }
     
      setShapeInfo(List<NuringInfoShapeBean> nuringInfoShapeBeen, int drawShape)//设置数据，并选择画什么图形】
	    
      public NuringInfoShapeBean(String value, String time) {//NuringInfoShapeBean的构造函数
		this.value = value;//值
		this.time = time;//时间}
	    
      public NuringInfoShapeBean(String time, String hightValue, String lowValue) {
		this.time = time;
		this.hightValue = hightValue;//血压形式高压 
		this.lowValue = lowValue;//低压}
		
       //可选择的图形
	     public static final int DRAWPOINT = 100;//折线图
	     public static final int DRAWColumn = 99;//柱状图
     
	     setBloodpresureShape(boolean isSinleling, boolean isPoint)//设置趋势图单条线或者双条线，可设置折线趋势图或散点趋势图

	     setRedLine(float reddataOne, float reddataTwo)//设置标准值的区域（带渐变效果），如果有一项为0，则变为红色标注线

	     isDrawMarkX(boolean isDrawMarkXLine, boolean isDottedX)//设置x轴各点延长线，线可设置实线和虚线         
	     isDrawMarkY(boolean isDrawMarkYLine, boolean isDottedY)//设置y轴各点延长线，线可设置实线和虚线
     
     
         
## 2.空心饼状图，可点击，低于6个数据有指示线，高于6个数据没有指示线
#### 示例

![pluse](https://github.com/hyyx13579/hyyxShapeView/blob/master/screens/hollowPie.png "空心饼状图")

```
 设置部分属性
 List<PieData> data = new ArrayList<>();
  for (int i = 1; i <= 5; i++) {
    int r = (new Random().nextInt(100) + 10) * i;
    int g = (new Random().nextInt(100) + 10) * 3 * i;
    int b = (new Random().nextInt(100) + 10) * 2 * i;
    int color = Color.rgb(r, g, b);
    if (Math.abs(r - g) > 10 && Math.abs(r - b) > 10 && Math.abs(b - g) > 10) {
       data.add(new PieData(i * 10, color));
          }
        }

          hollowPie.setPieDate(data);
          hollowPie.setCenterTitle("总记");
          hollowPie.startDraw();//可选择是否开启动画效果
```

## 3.普通折线图

