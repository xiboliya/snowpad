/**
 * Copyright (C) 2013 冰原
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

package com.xiboliya.snowpad.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.JTextComponent;

import com.xiboliya.snowpad.base.BaseFileFilter;
import com.xiboliya.snowpad.common.CharEncoding;
import com.xiboliya.snowpad.common.CurrentLines;
import com.xiboliya.snowpad.common.FileExt;
import com.xiboliya.snowpad.common.SearchStyle;

/**
 * 实用工具类，包括可重用的各种属性和方法。设计为final类型，使本类不可被继承
 * 
 * @author 冰原
 * 
 */
public final class Util {
  public static final String SOFTWARE = "冰雪记事本"; // 软件名称
  public static final String VERSION = "V5.0"; // 软件版本号
  public static final String NEW_FILE_NAME = "新建"; // 新建文件的默认名称
  public static final String OS_NAME = System.getProperty("os.name", "Windows"); // 当前操作系统的名称
  public static final String FILE_SEPARATOR = System.getProperty("file.separator", "/"); // 当前操作系统的文件分隔符
  public static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n"); // 当前操作系统的行分隔符
  public static final String SETTING_XML = "snowpad.xml"; // 用来保存软件设置的配置文件名
  public static final String LOOK_AND_FEEL = "LookAndFeel"; // 用于标识当前系统可用的外观
  public static final String PARAM_SPLIT = "#"; // 用于分隔前后文本标识的字符串
  public static final String BRACKETS_LEFT = "([{<"; // 在文本域中可以进行高亮匹配的左括号
  public static final String BRACKETS_RIGHT = ")]}>"; // 在文本域中可以进行高亮匹配的右括号
  public static final String AUTO_COMPLETE_BRACKETS_LEFT = "([{<'\""; // 在文本域中可以自动完成的左符号
  public static final String AUTO_COMPLETE_BRACKETS_RIGHT = ")]}>'\""; // 在文本域中可以自动完成的右符号
  public static final String CTRL = "Ctrl"; // Ctrl键的名称
  public static final String SHIFT = "Shift"; // Shift键的名称
  public static final String ALT = "Alt"; // Alt键的名称
  public static final String COMMAND = "Cmd"; // Command键的名称
  public static final String CTRL_H = "Ctrl+H"; // 组合键Ctrl+H的字符串
  public static final String CTRL_Y = "Ctrl+Y"; // 组合键Ctrl+Y的字符串
  public static final String CTRL_Z = "Ctrl+Z"; // 组合键Ctrl+Z的字符串
  public static final String TEXT_PREFIX = "*"; // 文件文本修改的标题栏标识符
  public static final String STYLE_PREFIX = "※"; // 文件格式修改的标题栏标识符
  public static final String IDENTIFIER_CHARS = "0123456"; // 列表编号类型标识符
  public static final String SYSTEM_LOOK_AND_FEEL_CLASS_NAME = UIManager.getSystemLookAndFeelClassName(); // 当前系统默认外观的完整类名
  public static final String[] FONT_FAMILY_NAMES = java.awt.GraphicsEnvironment
      .getLocalGraphicsEnvironment().getAvailableFontFamilyNames(); // 获取系统所有字体的名称列表
  public static final String[] SHORTCUT_NAMES = new String[] {
      "新建","打开","以指定编码打开","保存","另存为","重命名","重新载入文件","删除当前文件","关闭当前","关闭其它",
      "关闭左侧","关闭右侧","关闭全部","冻结文件","打印","清空最近编辑列表","退出","撤销","重做","剪切","复制","粘贴",
      "全选","删除","拆分文件","切换为大写","切换为小写","复制当前文件名到剪贴板","复制当前文件路径到剪贴板","复制当前目录路径到剪贴板","复制所有文本到剪贴板","复写当前行","删除当前行","删除重复行","删除空行",
      "删除至行首","删除至行尾","删除至文件首","删除至文件尾","上移当前行","下移当前行","复制当前行","剪切当前行","批量切除行","批量插入行",
      "批量分割行","批量拼接行","批量合并行","逐行复写","升序排序","降序排序","反序排序","缩进","退格","清除行首空白","清除行尾空白",
      "清除行首和行尾空白","清除选区内空白","添加/取消单行注释","添加/取消区块注释","插入特殊字符","插入时间和日期","复写选区字符","反转选区字符",
      "查找","查找下一个","查找上一个","选定查找下一个","选定查找上一个","快速向下查找","快速向上查找","替换","转到","设置/取消书签","下一个书签","上一个书签","预览书签","复制书签行","剪切书签行","清除所有书签","定位匹配括号",
      "单词边界换行","字符边界换行","Windows换行符格式","Unix换行符格式","Macintosh换行符格式","默认GB18030编码格式","ANSI编码格式","UTF-8编码格式","UTF-8 No BOM编码格式","Unicode Little Endian编码格式",
      "Unicode Big Endian编码格式","代码格式化-json","代码压缩-json","列表符号与编号","字体","Tab键设置","自动完成","自动换行","自动缩进","恢复默认设置","后退","前进","显示/隐藏工具栏",
      "显示/隐藏状态栏","显示/隐藏行号栏","显示/隐藏查找结果面板","前端显示","锁定窗口","多行标签","双击关闭标签","显示/隐藏指示图标","字体放大","字体缩小",
      "字体恢复初始大小","字体颜色","背景颜色","光标颜色","选区字体颜色","选区背景颜色","匹配括号背景颜色","当前行背景颜色","全部反色","全部补色",
      "配色方案1","配色方案2","配色方案3","配色方案4","配色方案5","恢复默认配色","高亮显示格式1","高亮显示格式2","高亮显示格式3","高亮显示格式4",
      "高亮显示格式5","清除高亮格式1","清除高亮格式2","清除高亮格式3","清除高亮格式4","清除高亮格式5","清除所有高亮格式","向前切换文档","向后切换文档","前一个文档",
      "后一个文档","统计信息","窗口管理","加密","进制转换","计算器","切割文件","拼接文件","转换文件编码","转换文件换行符","单位换算","帮助主题","关于"
      }; // 快捷键的名称
  public static final String[] SHORTCUT_VALUES = new String[] {
      "Ctrl+78","Ctrl+79","","Ctrl+83","","","","","115","",
      "","","","","","","Ctrl+81","Ctrl+90","Ctrl+89","Ctrl+88","Ctrl+67","Ctrl+86",
      "Ctrl+65","127","","Ctrl+Shift+85","Ctrl+85","","","","Ctrl+Shift+65","Ctrl+68","Ctrl+Shift+68","Ctrl+Alt+Shift+68","Ctrl+Alt+69",
      "Ctrl+Alt+37","Ctrl+Alt+39","Ctrl+Alt+Shift+37","Ctrl+Alt+Shift+39","Ctrl+Shift+38","Ctrl+Shift+40","Ctrl+Shift+67","Ctrl+Shift+88","Ctrl+Shift+82","Ctrl+Shift+73",
      "Ctrl+Shift+80","Ctrl+Shift+74","Ctrl+Shift+77","Ctrl+Shift+86","Alt+38","Alt+40","Alt+47","Ctrl+Alt+84","Ctrl+Alt+Shift+84","Ctrl+Shift+83","Ctrl+Shift+69",
      "Ctrl+Shift+76","Ctrl+Shift+84","Ctrl+76","Ctrl+77","","116","Ctrl+82","Ctrl+73",
      "Ctrl+70","114","Shift+114","Ctrl+114","Ctrl+Shift+114","Ctrl+75","Ctrl+Shift+75","Ctrl+72","Ctrl+71","Ctrl+Alt+75","113","Shift+113","","","","","Ctrl+66",
      "","","","","","","","","","",
      "","","","","","","","","","","","","",
      "","","","","","","","","Ctrl+38","Ctrl+40",
      "Ctrl+47","","","","","","","","","",
      "","","","","","","","","","",
      "","","","","","","","Ctrl+Shift+87","Ctrl+87","Alt+37",
      "Alt+39","","","","","","","","","","","","112"
      }; // 快捷键的值
  public static final int DEFAULT_FRAME_WIDTH = 600; // 窗口默认宽度
  public static final int DEFAULT_FRAME_HEIGHT = 500; // 窗口默认高度
  public static final int DEFAULT_CARET_INDEX = 0; // 文本域默认插入点位置
  public static final int INPUT_HEIGHT = 22; // 单行输入框的高度
  public static final int VIEW_HEIGHT = 18; // 标签、单选按钮、复选框的高度
  public static final int BUTTON_HEIGHT = 23; // 按钮的高度
  public static final int BUFFER_LENGTH = 1024; // 缓冲区的大小
  public static final int MIN_FONT_SIZE = 8; // 字体最小值
  public static final int MAX_FONT_SIZE = 100; // 字体最大值
  public static final int MIN_TABSIZE = 1; // Tab字符最小值
  public static final int MAX_TABSIZE = 99; // Tab字符最大值
  public static final int DEFAULT_TABSIZE = 4; // Tab字符默认值
  public static final int DEFAULT_UNDO_INDEX = 0; // 撤销标识符的默认值
  public static final int DEFAULT_BACK_FORWARD_INDEX = 0; // 光标历史位置的默认值
  public static final int MSG_LINE_SIZE = 60; // 提示框中每行字符串显示的最大字数
  public static final int PATTERN_SYNTAX_ERROR_INDEX = -2; // 正则表达式语法错误的索引值
  public static final Font GLOBAL_FONT = new Font("宋体", Font.PLAIN, 12); // 全局的默认字体
  public static final Font TEXT_FONT = new Font("宋体", Font.PLAIN, 14); // 文本域的默认字体
  public static final Color COLOR_BRACKET = new Color(20, 20, 20, 35); // 在文本域中进行高亮匹配括号的背景颜色
  public static final Color COLOR_CURRENT_LINE = new Color(0, 100, 200, 25); // 在文本域中用于标识当前行的背景颜色
  public static final Color[] COLOR_STYLE_DEFAULT = new Color[] {
      (Color) UIManager.getLookAndFeelDefaults().getColor("TextArea.foreground"),
      (Color) UIManager.getLookAndFeelDefaults().getColor("TextArea.background"),
      (Color) UIManager.getLookAndFeelDefaults().getColor("TextArea.caretForeground"),
      (Color) UIManager.getLookAndFeelDefaults().getColor("TextArea.selectionForeground"),
      (Color) UIManager.getLookAndFeelDefaults().getColor("TextArea.selectionBackground"),
      COLOR_BRACKET, COLOR_CURRENT_LINE }; // 文本域默认配色方案
  public static final UIManager.LookAndFeelInfo[] LOOK_AND_FEEL_INFOS = UIManager.getInstalledLookAndFeels(); // 当前系统可用的外观信息数组
  public static final ImageIcon SW_ICON = new ImageIcon(ClassLoader.getSystemResource("res/icon.gif")); // 主程序图标

  public static int transfer_count = 0; // 查找或替换时，启用“转义扩展”后被转义的字符个数
  public static int matcher_length = 0; // 通过正则表达式成功匹配的字符个数
  private static Matcher matcher = null; // 通过解释Pattern对指定文本执行匹配操作的引擎

  /**
   * 由于此类为工具类，故将构造方法私有化
   */
  private Util() {
  }

  /**
   * 判断给定的字符串是否为空
   * 
   * @param str
   *          待判断的字符串
   */
  public static boolean isTextEmpty(String str) {
    return (str == null || str.isEmpty());
  }

  /**
   * 为文件选择器添加预定义的文件过滤器
   * 
   * @param fileChooser
   *          要处理的文件选择器
   */
  public static void addChoosableFileFilters(JFileChooser fileChooser) {
    FileExt[] arrFileExt = FileExt.values(); // 获取包含枚举所有成员的数组
    BaseFileFilter fileFilter = null;
    BaseFileFilter defFileFilter = null; // 默认选择的文件过滤器
    for (FileExt fileExt : arrFileExt) { // 遍历枚举的所有成员
      fileFilter = new BaseFileFilter(fileExt.toString(), fileExt.getDescription());
      fileChooser.addChoosableFileFilter(fileFilter);
      if (fileExt.equals(FileExt.TXT)) {
        defFileFilter = fileFilter;
      }
    }
    if (defFileFilter != null) {
      fileChooser.setFileFilter(defFileFilter);
    }
  }

  /**
   * 检测或重新设置文本域插入点
   * 
   * @param txaSource
   *          当前文本域
   * @param index
   *          待检测的插入点位置
   * @return 处理过的插入点位置
   */
  public static int checkCaretPosition(JTextArea txaSource, int index) {
    if (txaSource == null) {
      return DEFAULT_CARET_INDEX;
    }
    int totalIndex = txaSource.getText().length();
    if (index < 0) {
      index = 0;
    } else if (index > totalIndex) {
      index = totalIndex;
    }
    return index;
  }

  /**
   * 将给定字符串重新分行，以适应对话框的显示
   * 
   * @param str
   *          待处理的字符串
   * @return 处理过的字符串
   */
  public static String convertToMsg(String str) {
    String[] arrContents = str.split("\n", -1);
    StringBuilder stbContent = new StringBuilder(); // 用于存放处理后的文本
    for (int n = 0; n < arrContents.length; n++) {
      String content = "";
      if (arrContents[n].length() > MSG_LINE_SIZE) {
        int lines = arrContents[n].length() / MSG_LINE_SIZE;
        int remain = arrContents[n].length() % MSG_LINE_SIZE;
        for (int i = 0; i < lines; i++) {
          content = content + arrContents[n].substring(MSG_LINE_SIZE * i, MSG_LINE_SIZE * (i + 1)) + "\n";
        }
        if (remain > 0) {
          content += arrContents[n].substring(MSG_LINE_SIZE * lines);
        } else {
          content = content.substring(0, content.length() - 1);
        }
      } else {
        content = arrContents[n];
      }
      stbContent.append(content + "\n");
    }
    return stbContent.toString();
  }

  /**
   * 获取文本域中当前选区内的所有行
   * 
   * @param txaSource
   *          特定的文本域
   * @param currentLines
   *          表示当前多行文本的属性类
   * @return 保存当前选区内所有行的字符数组
   */
  public static String[] getCurrentLinesArray(JTextArea txaSource, CurrentLines currentLines) {
    if (txaSource == null) {
      return null;
    }
    if (currentLines == null) {
      currentLines = new CurrentLines(txaSource);
    }
    int lineCount = txaSource.getLineCount();
    int startIndex = currentLines.getStartIndex();
    int endIndex = currentLines.getEndIndex();
    int endLineNum = currentLines.getEndLineNum() + 1;
    String strContent = currentLines.getStrContent();
    String strText = txaSource.getText();
    if (strContent.endsWith("\n") && (endIndex != strText.length() || (endLineNum == lineCount - 1 && strText.endsWith("\n")))) {
      endIndex--;
    }
    txaSource.select(startIndex, endIndex);
    String strSel = txaSource.getSelectedText();
    if (strSel == null) {
      strSel = "";
    }
    return strSel.split("\n", -1); // 将当前选区的文本分行处理，包括末尾的多处空行
  }

  /**
   * 格式化欲保存的文件名
   * 
   * @param strFileName
   *          文件的完整路径
   * @param fileFilter
   *          当前的文件类型过滤器
   * @param strExt
   *          欲保存文件的扩展名
   * @return 格式化后的文件
   */
  public static File checkFileName(String strFileName, BaseFileFilter fileFilter, String strExt) {
    if (isTextEmpty(strFileName) || fileFilter == null || isTextEmpty(strExt)) {
      return null;
    }
    if (fileFilter.getExt().equalsIgnoreCase(strExt)) {
      if (!strFileName.toLowerCase().endsWith(strExt.toLowerCase())) {
        strFileName += strExt;
      }
    }
    return new File(strFileName);
  }

  /**
   * 修改整个程序的默认字体
   */
  public static void setDefaultFont() {
    FontUIResource fontRes = new FontUIResource(GLOBAL_FONT);
    Enumeration<Object> keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
      Object key = keys.nextElement();
      Object value = UIManager.get(key);
      if (value instanceof FontUIResource) {
        UIManager.put(key, fontRes);
      }
    }
  }

  /**
   * 在文本组件中查找字符串
   * 
   * @param strFindText
   *          查找的字符串
   * @param txcSource
   *          文本组件
   * @param isFindDown
   *          是否向下查找
   * @param isMatchCase
   *          是否区分大小写
   * @param isWrap
   *          是否循环查找
   * @param searchStyle
   *          搜索模式
   * @return 查找的字符串位于文本组件中的索引
   */
  public static int findText(String strFindText, JTextComponent txcSource,
    boolean isFindDown, boolean isMatchCase, boolean isWrap, SearchStyle searchStyle) {
    if (isFindDown) {
      return findDownText(strFindText, txcSource, isMatchCase, isWrap, searchStyle);
    } else {
      return findUpText(strFindText, txcSource, isMatchCase, isWrap, searchStyle);
    }
  }

  /**
   * 在文本组件中向下查找字符串
   * 
   * @param strFindText
   *          查找的字符串
   * @param txcSource
   *          文本组件
   * @param isMatchCase
   *          是否区分大小写
   * @param isWrap
   *          是否循环查找
   * @param searchStyle
   *          搜索模式
   * @return 查找的字符串位于文本组件中的索引
   */
  private static int findDownText(String strFindText, JTextComponent txcSource,
      boolean isMatchCase, boolean isWrap, SearchStyle searchStyle) {
    if (isTextEmpty(strFindText) || txcSource == null || isTextEmpty(txcSource.getText())) {
      return -1;
    }
    if (searchStyle == SearchStyle.TRANSFER) {
      int len1 = strFindText.length();
      strFindText = transfer(strFindText);
      int len2 = strFindText.length();
      transfer_count = len1 - len2;
    }
    int result = -1;
    String strSourceAll = txcSource.getText();
    if (!isMatchCase) {
      if (searchStyle == SearchStyle.PATTERN) {
        strFindText = "(?i)" + strFindText; // 正则表达式中，可用(?i)打开不区分大小写的属性
      } else {
        strFindText = strFindText.toLowerCase();
      }
      strSourceAll = strSourceAll.toLowerCase();
    }
    int caretPos = txcSource.getCaretPosition();
    String strSource = strSourceAll.substring(caretPos);
    if (searchStyle == SearchStyle.PATTERN) {
      try {
        matcher = Pattern.compile(strFindText).matcher(strSource);
      } catch (PatternSyntaxException x) {
        // x.printStackTrace();
        return PATTERN_SYNTAX_ERROR_INDEX;
      }
      matcher_length = 0;
      if (matcher.find()) {
        result = caretPos + matcher.start();
        matcher_length = matcher.end() - matcher.start();
      } else {
        if (isWrap) {
          matcher = Pattern.compile(strFindText).matcher(strSourceAll);
          if (matcher.find()) {
            result = matcher.start();
            matcher_length = matcher.end() - matcher.start();
          }
        }
      }
    } else {
      int index = strSource.indexOf(strFindText);
      if (index >= 0) {
        result = caretPos + index;
      } else {
        if (isWrap) {
          result = strSourceAll.indexOf(strFindText);
        }
      }
    }
    return result;
  }

  /**
   * 在文本组件中向上查找字符串
   * 
   * @param strFindText
   *          查找的字符串
   * @param txcSource
   *          文本组件
   * @param isMatchCase
   *          是否区分大小写
   * @param isWrap
   *          是否循环查找
   * @param searchStyle
   *          搜索模式
   * @return 查找的字符串位于文本组件中的索引
   */
  private static int findUpText(String strFindText, JTextComponent txcSource,
      boolean isMatchCase, boolean isWrap, SearchStyle searchStyle) {
    if (isTextEmpty(strFindText) || txcSource == null || isTextEmpty(txcSource.getText())) {
      return -1;
    }
    if (searchStyle == SearchStyle.TRANSFER) {
      int len1 = strFindText.length();
      strFindText = transfer(strFindText);
      int len2 = strFindText.length();
      transfer_count = len1 - len2;
    }
    int result = -1;
    int caretPos = txcSource.getCaretPosition();
    String strSel = txcSource.getSelectedText();
    if (strSel != null) {
      if (!isMatchCase) {
        if (searchStyle == SearchStyle.PATTERN) {
          if (strSel.matches("(?i)" + strFindText)) { // 正则表达式中，可用(?i)打开不区分大小写的属性
            caretPos -= strSel.length();
          }
        } else if (strSel.equalsIgnoreCase(strFindText)) {
          caretPos -= strSel.length();
        }
      } else {
        if (searchStyle == SearchStyle.PATTERN) {
          if (strSel.matches(strFindText)) {
            caretPos -= strSel.length();
          }
        } else if (strSel.equals(strFindText)) {
          caretPos -= strSel.length();
        }
      }
    }
    String strSourceAll = txcSource.getText();
    if (!isMatchCase) {
      if (searchStyle == SearchStyle.PATTERN) {
        strFindText = "(?i)" + strFindText; // 正则表达式中，可用(?i)打开不区分大小写的属性
      } else {
        strFindText = strFindText.toLowerCase();
      }
      strSourceAll = strSourceAll.toLowerCase();
    }
    String strSource = strSourceAll.substring(0, caretPos);
    if (searchStyle == SearchStyle.PATTERN) {
      try {
        matcher = Pattern.compile(strFindText).matcher(strSource);
      } catch (PatternSyntaxException x) {
        // x.printStackTrace();
        return PATTERN_SYNTAX_ERROR_INDEX;
      }
      matcher_length = 0;
      while (matcher.find()) {
        result = matcher.start();
        matcher_length = matcher.end() - matcher.start();
      }
      if (result < 0 && isWrap) {
        matcher = Pattern.compile(strFindText).matcher(strSourceAll);
        while (matcher.find()) {
          result = matcher.start();
          matcher_length = matcher.end() - matcher.start();
        }
      }
    } else {
      result = strSource.lastIndexOf(strFindText);
      if (result < 0 && isWrap) {
        result = strSourceAll.lastIndexOf(strFindText);
      }
    }
    return result;
  }

  /**
   * 以给定的起始索引在文本组件中查找字符串
   * 
   * @param strFindText
   *          查找的字符串
   * @param txcSource
   *          文本组件
   * @param caretPos
   *          指定的起始索引
   * @param isMatchCase
   *          是否区分大小写
   * @param searchStyle
   *          搜索模式
   * @return 查找的字符串位于文本组件中的索引
   */
  public static int findText(String strFindText, JTextComponent txcSource, int caretPos,
      boolean isMatchCase, SearchStyle searchStyle) {
    if (isTextEmpty(strFindText) || txcSource == null || isTextEmpty(txcSource.getText())) {
      return -1;
    }
    if (searchStyle == SearchStyle.TRANSFER) {
      int len1 = strFindText.length();
      strFindText = transfer(strFindText);
      int len2 = strFindText.length();
      transfer_count = len1 - len2;
    }
    int result = -1;
    String strSourceAll = txcSource.getText();
    if (!isMatchCase) {
      if (searchStyle == SearchStyle.PATTERN) {
        strFindText = "(?i)" + strFindText; // 正则表达式中，可用(?i)打开不区分大小写的属性
      } else {
        strFindText = strFindText.toLowerCase();
      }
      strSourceAll = strSourceAll.toLowerCase();
    }
    String strSource = strSourceAll.substring(caretPos);
    if (searchStyle == SearchStyle.PATTERN) {
      try {
        matcher = Pattern.compile(strFindText).matcher(strSource);
      } catch (PatternSyntaxException x) {
        // x.printStackTrace();
        return PATTERN_SYNTAX_ERROR_INDEX;
      }
      matcher_length = 0;
      if (matcher.find()) {
        result = caretPos + matcher.start();
        matcher_length = matcher.end() - matcher.start();
      }
    } else {
      int index = strSource.indexOf(strFindText);
      if (index >= 0) {
        result = caretPos + index;
      }
    }
    return result;
  }

  /**
   * 根据文件开头的BOM（如果存在的话），判断文件的编码格式。 文本文件有各种不同的编码格式，如果判断有误，则会导致显示或保存错误。
   * 为了标识文件的编码格式，便于编辑和保存，则在文件开头加入了BOM，用以标识编码格式。 UTF-8格式：0xef 0xbb 0xbf， Unicode
   * Little Endian格式：0xff 0xfe， Unicode Big Endian格式：0xfe
   * 0xff。而ANSI格式是没有BOM的。另有一种不含BOM的UTF-8格式的文件，则不易与ANSI相区分，因此需要进一步检测。
   * 
   * @param file
   *          待判断的文件
   */
  public static CharEncoding checkFileEncoding(File file) {
    FileInputStream fileInputStream = null;
    int[] charArr = new int[3];
    try {
      fileInputStream = new FileInputStream(file);
      for (int i = 0; i < charArr.length; i++) {
        charArr[i] = fileInputStream.read();
      }
    } catch (Exception x) {
      // x.printStackTrace();
    } finally {
      try {
        fileInputStream.close();
      } catch (IOException x) {
        // x.printStackTrace();
      }
    }
    if (charArr[0] == 0xff && charArr[1] == 0xfe) {
      return CharEncoding.ULE;
    } else if (charArr[0] == 0xfe && charArr[1] == 0xff) {
      return CharEncoding.UBE;
    } else if (charArr[0] == 0xef && charArr[1] == 0xbb && charArr[2] == 0xbf) {
      return CharEncoding.UTF8;
    } else {
      if (isUTF8NoBom(file)) {
        return CharEncoding.UTF8_NO_BOM;
      } else {
        return CharEncoding.BASE;
      }
    }
  }

  /**
   * 判断文件是否为UTF8无BOM格式。
   * UTF-8的编码规则很简单，只有2条：
   * 1.对于单字节的字符，字节的第一位设为0，后面7位为这个符号的unicode码。因此对于英语字母，UTF-8编码和ASCII码是相同的。
   * 2.对于n字节的字符(n>1 && n<=6)，第一个字节的前n位都为1，第n+1位为0，后面字节的前两位都为10。剩下的其他位，即为此字符的unicode码。
   * 
   * @param file
   *          待判断的文件
   * @return 是否为UTF8无BOM格式，是UTF8无BOM格式返回true，反之返回false
   */
  private static boolean isUTF8NoBom(File file) {
    FileInputStream fileInputStream = null;
    int maxLength = 1024 * 1024;
    byte[] rawtext = new byte[maxLength];
    try {
      fileInputStream = new FileInputStream(file);
      fileInputStream.read(rawtext);
    } catch (Exception x) {
      // x.printStackTrace();
    } finally {
      try {
        fileInputStream.close();
      } catch (IOException x) {
        // x.printStackTrace();
        return false;
      }
    }
    int score = 0;
    int goodbytes = 0;
    int asciibytes = 0;
    int rawtextlen = rawtext.length;
    for (int i = 0; i < rawtextlen; i++) {
      if ((rawtext[i] & (byte) 0x7F) == rawtext[i]) { // 单字节字符
        asciibytes++;
      } else if (-64 <= rawtext[i] && rawtext[i] <= -33
        && i + 1 < rawtextlen
        && -128 <= rawtext[i + 1]
        && rawtext[i + 1] <= -65) { // 双字节字符
        goodbytes += 2;
        i++;
      } else if (-32 <= rawtext[i] && rawtext[i] <= -17
        && i + 2 < rawtextlen
        && -128 <= rawtext[i + 1] && rawtext[i + 1] <= -65
        && -128 <= rawtext[i + 2] && rawtext[i + 2] <= -65) { // 三字节字符
        goodbytes += 3;
        i += 2;
      } else if (-16 <= rawtext[i] && rawtext[i] <= -9
        && i + 3 < rawtextlen
        && -128 <= rawtext[i + 1] && rawtext[i + 1] <= -65
        && -128 <= rawtext[i + 2] && rawtext[i + 2] <= -65
        && -128 <= rawtext[i + 3] && rawtext[i + 3] <= -65) { // 四字节字符
        goodbytes += 4;
        i += 3;
      }
    }
    if (asciibytes == rawtextlen) {
      return false;
    }
    score = 100 * goodbytes / (rawtextlen - asciibytes);
    // If not above 98, reduce to zero to prevent coincidental matches
    // Allows for some (few) bad formed sequences
    if (score > 98) {
      return true;
    } else if (score > 95 && goodbytes > 30) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 删除目录下的所有文件
   * 
   * @param file
   *          目录文件
   */
  public static void deleteAllFiles(File file) {
    if (!file.exists()) {
      return;
    }
    if (file.isFile()) {
      file.delete();
      return;
    }
    File[] files = file.listFiles();
    for (int i = 0; i < files.length; i++) {
      deleteAllFiles(files[i]);
    }
  }

  /**
   * 将给定的字符串进行转义替换，即将字符串中的\n替换为换行符，\t替换为tab字符
   * 
   * @param strSource
   *          处理的字符串
   * @return 替换后的字符串
   */
  public static String transfer(String strSource) {
    return strSource.replace("\\n", "\n").replace("\\t", "\t"); // 将字符串中的\n替换为换行符，\t替换为tab字符
  }

  /**
   * 将给定的快捷键组合的常量字符串形式转换为快捷键描述
   * 
   * @param shortcut
   *          表示快捷键组合的常量字符串
   * @return 表示快捷键描述的字符串
   */
  public static String transferShortcut(String shortcut) {
    String value = "";
    if (isTextEmpty(shortcut)) {
      return value;
    }
    boolean hasCtrl = false; // 是否含有Ctrl键
    boolean hasAlt = false; // 是否含有Alt键
    boolean hasShift = false; // 是否含有Shift键
    boolean hasCommand = false; // 是否含有Shift键
    String[] arrKeys = shortcut.split("\\+");
    for (String str : arrKeys) {
      if (CTRL.equalsIgnoreCase(str)) {
        hasCtrl = true;
      } else if (ALT.equalsIgnoreCase(str)) {
        hasAlt = true;
      } else if (SHIFT.equalsIgnoreCase(str)) {
        hasShift = true;
      } else if (COMMAND.equalsIgnoreCase(str)) {
        hasCommand = true;
      } else { // 除控制键之外的按键
        String strKey = transferKeyCode(str);
        if (!isTextEmpty(strKey)) {
          value = strKey;
        }
      }
    }
    if (!isTextEmpty(value)) {
      if (hasShift) {
        value = SHIFT + "+" + value;
      }
      if (hasAlt) {
        value = ALT + "+" + value;
      }
      if (hasCtrl) {
        value = CTRL + "+" + value;
      }
      if (hasCommand) {
        value = COMMAND + "+" + value;
      }
    }
    return value;
  }

  /**
   * 将给定的字符串形式的按键常量转换为按键描述
   * 
   * @param strKeyCode
   *          表示按键常量的字符串
   * @return 表示按键描述的字符串
   */
  public static String transferKeyCode(String strKeyCode) {
    String strKey = "";
    try {
      strKey = KeyEvent.getKeyText(Integer.parseInt(strKeyCode));
    } catch (Exception x) {
      // x.printStackTrace();
      return "";
    }
    return strKey;
  }

  /**
   * 将给定的快捷键组合的常量字符串形式转换为可用于快捷键设置的对象
   * 
   * @param shortcut
   *          表示快捷键组合的常量字符串
   * @return 表示可用于快捷键设置的对象
   */
  public static KeyStroke transferKeyStroke(String shortcut) {
    KeyStroke keyStroke = null;
    if (isTextEmpty(shortcut)) {
      return keyStroke;
    }
    int modifiers = 0; // 控制键的扩展修饰符常量，0表示没有控制键
    int keyCode = -1; // 除控制键之外的按键常量
    String[] arrKeys = shortcut.split("\\+");
    for (String str : arrKeys) {
      if (CTRL.equalsIgnoreCase(str)) {
        modifiers += InputEvent.CTRL_DOWN_MASK;
      } else if (ALT.equalsIgnoreCase(str)) {
        modifiers += InputEvent.ALT_DOWN_MASK;
      } else if (SHIFT.equalsIgnoreCase(str)) {
        modifiers += InputEvent.SHIFT_DOWN_MASK;
      } else if (COMMAND.equalsIgnoreCase(str)) {
        modifiers += InputEvent.META_DOWN_MASK;
      } else { // 除控制键之外的按键
        try {
          keyCode = Integer.parseInt(str);
        } catch (Exception x) {
          // x.printStackTrace();
        }
      }
    }
    if (keyCode >= 0) {
      keyStroke = KeyStroke.getKeyStroke(keyCode, modifiers);
    }
    return keyStroke;
  }

  /**
   * 检测文件以及所在的目录是否存在
   * 
   * @param file
   *          被检测的文件
   * @return 被检测文件是否存在，如果存在返回true，反之则为false
   */
  public static boolean checkFile(File file) {
    File fileParent = new File(file.getParent()); // 获取文件的父目录
    if (!fileParent.exists()) {
      fileParent.mkdirs(); // 如果父目录不存在，则创建之
    }
    return file.exists();
  }
}