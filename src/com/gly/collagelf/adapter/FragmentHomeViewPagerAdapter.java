package com.gly.collagelf.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
/**
 * HomeFragment中的ViewPager的适配器，用来滚动图片
 * @author 高留洋
 *
 */
public class FragmentHomeViewPagerAdapter extends PagerAdapter {

	private List<View> views;

	public FragmentHomeViewPagerAdapter(List<View> views) {
		super();
		this.views = views;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager)container).removeView(views.get(position % views.size()));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ViewPager parent = (ViewPager) views.get(position % views.size()).getParent();
		 if (parent != null) {
		    parent.removeAllViews();
		 } 
		((ViewPager)container).addView(views.get(position % views.size()), 0);
        return views.get(position % views.size());
	}
}
