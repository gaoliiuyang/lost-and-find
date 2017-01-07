package com.gly.collagelf.utils;


import com.gly.collagelf.baseactivity.BaseActivity;

import android.content.Intent;
import android.net.Uri;

/**
 * 跳转相册裁剪图片的工具类
 * @author 高留洋
 * 需要传入上下文和图片保存的路径
 */
public class CropPictureUtil {

	public static void cropPicture(BaseActivity context,Uri uri){
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		//类型
		intent.setType("image/*");
		// 剪切
		intent.putExtra("crop", "true");
		// 剪切比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 剪切的像素点
		intent.putExtra("outputX", 450);
		intent.putExtra("outputY", 450);

		intent.putExtra("scale", true);

		intent.putExtra("return-data", false);
		// 存放的位置
		intent.putExtra("output", uri);
		context.startActivityForResult(intent, 200);
	}
}
