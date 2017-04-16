package com.zgmz.ls.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.zgmz.ls.AppContext;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.DisplayMetrics;

/**
 * Various utilities shared amongst the Launcher's classes.
 */
public final class BitmapUtils {
	public static final String TAG = "BitmapUtil";

	protected static int sIconWidth = 72;
	protected static int sIconHeight = 72;

	protected static int sShotScreenWidth = 480;
	protected static int sShotScreenHeight = 800;

	// private static int sIconSize = sIconWidth * sIconHeight;

	protected static int sShotScreenSize = sShotScreenWidth * sShotScreenHeight;

	private static final float ROUND_PX = 5;

	public static void init(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);

		sShotScreenWidth = dm.widthPixels;
		sShotScreenHeight = dm.heightPixels;

		// sIconSize = sIconWidth * sIconHeight;
		sShotScreenSize = sShotScreenWidth * sShotScreenHeight;
	}

	public static Bitmap getIcon(Bitmap src) {

		return zoomBitmap(src, sIconWidth, sIconHeight);
	}

	public static Bitmap getAvatar(Bitmap src) {
		return getRoundedCornerBitmap(src);
	}

	public static Bitmap getShotScreen(Bitmap src) {
		// final int size = src.getHeight() * src.getWidth();
		// if(size > sShotScreenSize){
		// if(src.getHeight() > src.getWidth()){
		// return zoomBitmap(src, sShotScreenWidth, sShotScreenHeight);
		// }
		// else{
		// return zoomBitmap(src, sShotScreenWidth, sShotScreenHeight);
		// }
		//
		// }
		return src;

	}

	/**
	 * 图片合成
	 * 
	 * @param
	 * @return
	 */
	public static Bitmap createBitmap(Bitmap src, Bitmap watermark) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		int wh = watermark.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		// draw watermark into
		cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存储
		return newb;
	}

	/**
	 * 放大缩小图片
	 * 
	 * @param
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap src, int w, int h) {
		int width = src.getWidth();
		int height = src.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
		return newbmp;
	}

	/**
	 * 根据原图和变长绘制圆形图片
	 * 
	 * @param source
	 * @param
	 * @return
	 */
	public static Bitmap createCircleImage(Bitmap source) {
		if (source == null)
			return null;
		return createCircleImage(source, 8, 0);
	}

	/**
	 * 根据原图和变长绘制圆形图片
	 * 
	 * @param source
	 * @param
	 * @param color
	 * @return
	 */
	public static Bitmap createCircleImage(Bitmap source, int strokeWidth, int color) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		int diameter = source.getWidth() < source.getHeight() ? source.getWidth() : source.getHeight();
		int width = diameter + strokeWidth * 2;
		Bitmap target = Bitmap.createBitmap(width, width, Config.ARGB_8888);
		/**
		 * 产生一个同样大小的画布
		 */
		Canvas canvas = new Canvas(target);
		/**
		 * 首先绘制圆形
		 */

		paint.setColor(Color.WHITE);
		canvas.drawCircle(width / 2, width / 2, width / 2, paint);
		/**
		 * 使用SRC_IN
		 */
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		/**
		 * 绘制图片
		 */
		canvas.drawBitmap(source, strokeWidth, strokeWidth, paint);

		if (color == 0)
			color = 0xFFFEA248;
		paint.setColor(color);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(strokeWidth);
		canvas.drawCircle(width / 2, width / 2, width / 2, paint);
		return target;
	}

	/**
	 * 图片圆角
	 * 
	 * @param
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap src) {
		Bitmap output = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, src.getWidth(), src.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, ROUND_PX, ROUND_PX, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(src, rect, rect, paint);
		return output;
	}

	/**
	 * 获得带倒影的图片方法
	 * 
	 * @param
	 * @return
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap src) {

		final int reflectionGap = 4;
		int width = src.getWidth();
		int height = src.getHeight();
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		Bitmap reflectionImage = Bitmap.createBitmap(src, 0, height / 2, width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(src, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();

		LinearGradient shader = new LinearGradient(0, src.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * 按正方形裁切图片
	 */
	public static Bitmap crop(Bitmap bitmap, float wScale, float hScale) {
		int w = bitmap.getWidth(); // 得到图片的宽，高
		int h = bitmap.getHeight();

		int wh = (int) (w * wScale);
		int hw = (int) (h * hScale);

		int retX = (int) (w * (1 - wScale) / 2);
		int retY = (int) (h * (1 - hScale) / 2);

		// 下面这句是关键
		return Bitmap.createBitmap(bitmap, retX, retY, wh, hw, null, false);
	}

	public static byte[] getBitmapByte(Bitmap bitmap) {
		if (bitmap == null)
			return null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	public static Bitmap getBitmapFromByte(byte[] temp) {
		if (temp != null) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
			return bitmap;
		} else {
			return null;
		}
	}

	public static Bitmap getCircleBitmapFromByte(byte[] temp) {
		Bitmap bmp = null;
		Bitmap bitmap = getBitmapFromByte(temp);
		if (bitmap != null) {
			bmp = createCircleImage(bitmap);
			bitmap.recycle();
		}
		return bmp;
	}

	public static Bitmap getAssetImage(String file) {
		Bitmap image = null;
		AssetManager am = AppContext.getAppContext().getAssets();
		try {
			InputStream is = am.open(file);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}

	public static Bitmap getFileImage(String file) {
		Bitmap image = null;
		try {
			File f = new File(file);
			InputStream is = new FileInputStream(f);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}
}
