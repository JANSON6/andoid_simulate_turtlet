package com.janeaston.drawbitmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CanvasView extends View {
    private RectF robotRect;
    private Paint robotPaint;
    private Paint arrowPaint;
    private Paint trailPaint; // 轨迹画笔
    private float robotX;
    private float robotY;
    private float robotWidth = 100;
    private float robotHeight = 100;
    private float robotAngle = 0;

    // 存储轨迹点
    private List<Float> trailPoints = new ArrayList<>();
    private static final int TRAIL_MAX_POINTS = 1000; // 最多存储500个点

    public CanvasView(Context context) {
        super(context);
        init();
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 机器人矩形画笔
        robotPaint = new Paint();
        robotPaint.setColor(Color.RED);
        robotPaint.setStyle(Paint.Style.FILL);
        robotPaint.setAntiAlias(true);

        // 箭头画笔
        arrowPaint = new Paint();
        arrowPaint.setColor(Color.GREEN);
        arrowPaint.setStyle(Paint.Style.FILL);
        arrowPaint.setStrokeWidth(3f);
        arrowPaint.setAntiAlias(true);

        // 轨迹画笔
        trailPaint = new Paint();
        trailPaint.setColor(Color.argb(30,  0, 0, 255)); // 半透明蓝色
        trailPaint.setStyle(Paint.Style.STROKE);
        trailPaint.setStrokeWidth(8f);
        trailPaint.setAntiAlias(true);
        trailPaint.setStrokeCap(Paint.Cap.ROUND);
        trailPaint.setStrokeJoin(Paint.Join.ROUND);

        robotRect = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w,  h, oldw, oldh);
        robotX = w / 2f;
        robotY = h / 2f;
        updateRect();

        // 清除旧轨迹
        trailPoints.clear();
        // 添加初始位置
        trailPoints.add(robotX);
        trailPoints.add(robotY);
    }

    public void updateRobotPose(float dx, float dy, float dyaw) {
        // 保存当前位置
        float prevX = robotX;
        float prevY = robotY;

        // 更新角度
        robotAngle += dyaw;

        // 计算位移
        float cos = (float) Math.cos(robotAngle);
        float sin = (float) Math.sin(robotAngle);
        float globalDx = dx * cos - dy * sin;
        float globalDy = dx * sin + dy * cos;

        robotX += globalDx;
        robotY += globalDy;

        // 边界检查
        robotX = Math.max(robotWidth/2,  Math.min(getWidth()  - robotWidth/2, robotX));
        robotY = Math.max(robotHeight/2,  Math.min(getHeight()  - robotHeight/2, robotY));

        // 如果位置有变化，则添加新的轨迹点
        if (robotX != prevX || robotY != prevY) {
            trailPoints.add(robotX);
            trailPoints.add(robotY);

            // 控制轨迹点的数量，避免内存占用过大
            if (trailPoints.size()  > TRAIL_MAX_POINTS) {
                trailPoints.remove(0);  // 移除x
                trailPoints.remove(0);  // 移除y
            }
        }

        invalidate();
    }

    private void updateRect() {
        float halfWidth = robotWidth / 2;
        float halfHeight = robotHeight / 2;
        robotRect.set(-halfWidth,  -halfHeight, halfWidth, halfHeight);
    }

    private void drawArrow(Canvas canvas) {
        float arrowLength = robotWidth * 0.6f;
        float arrowWidth = robotWidth * 0.3f;

        Path arrowPath = new Path();
        arrowPath.moveTo(arrowLength/2,  0);
        arrowPath.lineTo(-arrowLength/2,  -arrowWidth/2);
        arrowPath.lineTo(-arrowLength/2*0.8f,  0);
        arrowPath.lineTo(-arrowLength/2,  arrowWidth/2);
        arrowPath.close();

        canvas.drawPath(arrowPath,  arrowPaint);
    }

    private void drawTrail(Canvas canvas) {
        if (trailPoints.size()  < 4) return; // 至少需要2个点才能画线

        // 创建轨迹路径
        Path trailPath = new Path();
        trailPath.moveTo(trailPoints.get(0),  trailPoints.get(1));

        // 连接所有轨迹点
        for (int i = 2; i < trailPoints.size();  i += 2) {
            trailPath.lineTo(trailPoints.get(i),  trailPoints.get(i  + 1));
        }

        canvas.drawPath(trailPath,  trailPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 先绘制轨迹(在最底层)
        drawTrail(canvas);

        // 再绘制机器人
        canvas.save();
        canvas.translate(robotX,  robotY);
        canvas.rotate((float)  Math.toDegrees(robotAngle));
        canvas.drawRect(robotRect,  robotPaint);
        drawArrow(canvas);
        canvas.restore();
    }

    // 清空轨迹的方法
    public void clearTrail() {
        trailPoints.clear();
        // 添加当前位置
        trailPoints.add(robotX);
        trailPoints.add(robotY);
        invalidate();
    }
}
