/**
 * Copyright (C) 2014 冰原
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xiboliya.snowpad;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 软件参数配置类
 * 
 * @author 冰原
 * 
 */
public class Setting {
  // 文本域的相关设置
  public boolean isLineWrap = true; // 是否自动换行
  public boolean isWrapStyleWord = true; // 是否单词边界换行
  public boolean textDrag = false; // 是否可拖拽文本
  public boolean autoIndent = false; // 是否可自动缩进
  public boolean tabReplaceBySpace = false; // 是否以空格代替Tab键
  public boolean autoComplete = false; // 是否自动完成
  public int tabSize = Util.DEFAULT_TABSIZE; // Tab键所占字符数
  public Font font = Util.TEXT_FONT; // 文本域字体
  public Color[] colorStyle = null; // 配色方案
  // 查找/替换的相关设置
  public boolean matchCase = true; // 是否区分大小写
  public boolean isWrap = false; // 是否循环查找
  public boolean findDown = true; // 是否向下查找
  public SearchStyle searchStyle = SearchStyle.DEFAULT; // 搜索模式
  // 显示的相关设置
  public boolean viewToolBar = true; // 是否显示工具栏
  public boolean viewStateBar = true; // 是否显示状态栏
  public boolean viewLineNumber = false; // 是否显示行号栏
  public boolean viewSearchResult = false; // 是否显示查找结果面板
  public boolean viewAlwaysOnTop = false; // 是否前端显示
  public boolean viewLockResizable = false; // 是否锁定窗口
  public boolean viewTabPolicy = true; // 是否显示多行标签
  public boolean viewClickToClose = true; // 是否双击关闭标签
  public boolean viewTabIcon = true; // 是否显示指示图标
  public int viewLookAndFeel = -1; // 当前外观的索引号
  // 所有已打开的文件
  public LinkedList<String> fileHistoryList = new LinkedList<String>(); // 存放所有已打开的文件名的链表
  // 所有快捷键的设置
  public HashMap<String, String> shortcutMap = new HashMap<String, String>(); // 存放所有快捷键的设置的哈希表
}
