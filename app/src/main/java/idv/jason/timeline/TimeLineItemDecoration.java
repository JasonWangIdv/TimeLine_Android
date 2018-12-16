package idv.jason.timeline;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class TimeLineItemDecoration extends RecyclerView.ItemDecoration {

    private final int TIME_LINE_CANVAS_WIDTH = 100;
    private final float LINE_WIDTH = 10f;
    private final float BIG_CIRCLE_RADIUS = 30f;
    private final float SMALL_CIRCLE_RADIUS = 20f;

    private final String LINE_START_COLOR = "#000000";
    private final String LINE_END_COLOR = "#FFFFFF";
    private final String BIG_CIRCLE_COLOR = "#FF0000";

    private Paint paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintBigCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintSmallCircle = new Paint(Paint.ANTI_ALIAS_FLAG);

    public TimeLineItemDecoration() { }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        for (int i = 0 ; i < parent.getChildCount() ; i++) {
            View itemView = parent.getChildAt(i);
            int itemPosition = parent.getChildAdapterPosition(itemView);
            int itemCount = parent.getAdapter().getItemCount();

            int[] lineGradientColors = getLineGradientColors(itemPosition, itemCount);
            int circleColor = getCircleColor(lineGradientColors[0], lineGradientColors[1]);

            drawLine(c, itemView, lineGradientColors[0], lineGradientColors[1]);
            drawBigCircle(c, itemView, Color.parseColor(BIG_CIRCLE_COLOR));
            drawSmallCircle(c, itemView, circleColor);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.set(TIME_LINE_CANVAS_WIDTH, 0, 0, 0);
    }

    private int[] getLineGradientColors(int itemPosition, int itemCount) {
        int startColor = Color.parseColor(LINE_START_COLOR);
        int endColor = Color.parseColor(LINE_END_COLOR);

        int startRed = Color.red(startColor);
        int startGreen = Color.green(startColor);
        int startBlue = Color.blue(startColor);

        int endRed = Color.red(endColor);
        int endGreen = Color.green(endColor);
        int endBlue = Color.blue(endColor);

        int rangeRed = endRed - startRed;
        int rangeGreen = endGreen - startGreen;
        int rangeBlue = endBlue - startBlue;

        float perRed = rangeRed * (1f / itemCount);
        float perGreen = rangeGreen * (1f / itemCount);
        float perBlue = rangeBlue * (1f / itemCount);

        int startGradientRed = (int) (startRed + (itemPosition * perRed));
        int startGradientGreen = (int) (startGreen + (itemPosition * perGreen));
        int startGradientBlue = (int) (startBlue + (itemPosition * perBlue));

        int endGradientRed = (int) (startRed + ((itemPosition + 1) * perRed));
        int endGradientGreen = (int) (startGreen + ((itemPosition + 1) * perGreen));
        int endGradientBlue = (int) (startBlue + ((itemPosition + 1) * perBlue));

        return new int[]{Color.rgb(startGradientRed, startGradientGreen, startGradientBlue),
                         Color.rgb(endGradientRed, endGradientGreen, endGradientBlue)};
    }

    private int getCircleColor(int startGradientColor, int endGradientColor) {
        int red = (Color.red(startGradientColor) + Color.red(endGradientColor)) / 2;
        int green = (Color.green(startGradientColor) + Color.green(endGradientColor)) / 2;
        int blue = (Color.blue(startGradientColor) + Color.blue(endGradientColor)) / 2;

        return Color.rgb(red, green, blue);
    }

    private void drawLine(Canvas c, View itemView, int startColor, int endColor) {
        float left = (TIME_LINE_CANVAS_WIDTH / 2) - (LINE_WIDTH / 2);
        float top = itemView.getTop();
        float right = left + LINE_WIDTH;
        float bottom = itemView.getBottom();

        LinearGradient linearGradient = new LinearGradient(0, top, 0, bottom, startColor, endColor, Shader.TileMode.MIRROR);
        paintLine.setShader(linearGradient);

        c.drawRect(left, top, right, bottom, paintLine);
    }

    private void drawBigCircle(Canvas c, View itemView, int color) {
        float cx = TIME_LINE_CANVAS_WIDTH / 2;
        float cy = (itemView.getBottom() + itemView.getTop()) / 2;

        paintBigCircle.setColor(color);

        c.drawCircle(cx, cy, BIG_CIRCLE_RADIUS, paintBigCircle);
    }

    private void drawSmallCircle(Canvas c, View itemView, int color) {
        float cx = TIME_LINE_CANVAS_WIDTH / 2;
        float cy = (itemView.getBottom() + itemView.getTop()) / 2;

        paintSmallCircle.setColor(color);

        c.drawCircle(cx, cy, SMALL_CIRCLE_RADIUS, paintSmallCircle);
    }

}
