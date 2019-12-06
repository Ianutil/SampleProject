/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  Pullable
 * Created by  ianchang on 2018-05-04 16:04:57
 * Last modify date   2018-05-04 14:45:36
 */

package com.ian.widget.view;

public interface Pullable
{
	/**
	 * 判断是否可以下拉，如果不需要下拉功能可以直接return false
	 * 
	 * @return true如果可以下拉否则返回false
	 */
	boolean canPullDown();

	/**
	 * 判断是否可以上拉，如果不需要上拉功能可以直接return false
	 * 
	 * @return true如果可以上拉否则返回false
	 */
	boolean canPullUp();
}
