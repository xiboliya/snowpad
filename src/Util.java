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

package com.xiboliya.snowpad;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.JTextComponent;

/**
 * 实用工具类，包括可重用的各种属性和方法。设计为final类型，使本类不可被继承
 * 
 * @author 冰原
 * 
 */
public final class Util {
  public static final String SOFTWARE = "冰雪记事本"; // 软件名称
  public static final String VERSION = "V4.0"; // 软件版本号
  public static final String NEW_FILE_NAME = "新建"; // 新建文件的默认名称
  public static final String HELP_TITLE = "帮助主题"; // 帮助主题界面的默认标题
  public static final String OS_NAME = System.getProperty("os.name", "Windows"); // 当前操作系统的名称
  public static final String FILE_SEPARATOR = System.getProperty(
      "file.separator", "/"); // 当前操作系统的文件分隔符
  public static final String LINE_SEPARATOR = System.getProperty(
      "line.separator", "\n"); // 当前操作系统的行分隔符
  public static final String SETTING_XML = "snowpad.xml"; // 用来保存软件设置的配置文件名
  public static final String FILE_HISTORY = "FileHistory"; // 用于标识最近编辑的文件
  public static final String LOOK_AND_FEEL = "LookAndFeel"; // 用于标识当前系统可用的外观
  public static final String PARAM_SPLIT = "#"; // 用于分隔前后文本标识的字符串
  public static final String INSERT_SPECIAL = "﹟﹠﹡＃＠＆＊※§〃№♀♂㊣㈱℡℅⊙◎〓○●△▲▽▼◇◆□■☆★◢◣◤◥ˉ￣＿﹉﹊﹍﹎﹋﹌↑↓←→↖↗↙↘∥∣／＼∕﹨╋┳┻┫┣┃━┏┓┗┛╳╱╲"; // 特殊符号
  public static final String INSERT_PUNCTUATION = "，、。．；：？！︰…‥′‵～‖ˇˉ﹐﹑﹒?﹔﹕﹖﹗｜–︱—︳︴﹏（）︵︶｛｝︷︸〔〕︹︺【】︻︼《》︽︾〈〉︿﹀「」﹁﹂『』﹃﹄﹙﹚﹛﹜﹝﹞‘’“”〝〞ˋˊ々"; // 标点符号
  public static final String INSERT_MATH = "≈≡≠＝≤≥＜＞≮≯±＋－×÷／∫∮∝∞∧∨∑∏∪∩∈∵∴∷⊥∥∠⌒≌∽√≦≧≒﹢﹣﹤﹥﹦～∟⊿㏒㏑"; // 数学符号
  public static final String INSERT_UNIT = "°′″＄￥〒￠￡％℃℉﹩﹪‰﹫㏕㎜㎝㎞㏎㎡㎎㎏㏄¤"; // 单位符号
  public static final String INSERT_DIGIT = "⒈⒉⒊⒋⒌⒍⒎⒏⒐⒑⒒⒓⒔⒕⒖⒗⒘⒙⒚⒛⑴⑵⑶⑷⑸⑹⑺⑻⑼⑽⑾⑿⒀⒁⒂⒃⒄⒅⒆⒇①②③④⑤⑥⑦⑧⑨⑩㈠㈡㈢㈣㈤㈥㈦㈧㈨㈩ⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫ"; // 数字符号
  public static final String INSERT_PINYIN = "āáǎàōóǒòēéěèīíǐìūúǔùǖǘǚǜüêɑńňǹɡ"; // 拼音符号
  public static final String BRACKETS_LEFT = "([{<"; // 在文本域中可以进行高亮匹配的左括号
  public static final String BRACKETS_RIGHT = ")]}>"; // 在文本域中可以进行高亮匹配的右括号
  public static final String CTRL = "Ctrl"; // Ctrl键的名称
  public static final String SHIFT = "Shift"; // Shift键的名称
  public static final String ALT = "Alt"; // Alt键的名称
  public static final String KEY_UNDEFINED = "[未定义]"; // 未定义按键
  public static final String CTRL_C = "Ctrl+C"; // 组合键Ctrl+C的字符串
  public static final String CTRL_H = "Ctrl+H"; // 组合键Ctrl+H的字符串
  public static final String CTRL_V = "Ctrl+V"; // 组合键Ctrl+V的字符串
  public static final String CTRL_X = "Ctrl+X"; // 组合键Ctrl+X的字符串
  public static final String CTRL_Y = "Ctrl+Y"; // 组合键Ctrl+Y的字符串
  public static final String CTRL_Z = "Ctrl+Z"; // 组合键Ctrl+Z的字符串
  public static final String TEXT_PREFIX = "*"; // 文件文本修改的标题栏标识符
  public static final String STYLE_PREFIX = "※"; // 文件格式修改的标题栏标识符
  public static final String STATE_CHARS = "Chars:"; // 状态栏显示信息-文本总字符数
  public static final String STATE_LINES = "Lines:"; // 状态栏显示信息-文本总行数
  public static final String STATE_CUR_LINE = "Ln:"; // 状态栏显示信息-光标当前行号
  public static final String STATE_CUR_COLUMN = "Col:"; // 状态栏显示信息-光标当前列号
  public static final String STATE_CUR_SELECT = "Sel:"; // 状态栏显示信息-当前选择的字符数
  public static final String STATE_LINE_STYLE = "LineStyle:"; // 状态栏显示信息-当前换行符格式
  public static final String STATE_ENCODING = "Encoding:"; // 状态栏显示信息-当前编码格式
  public static final String SIGN_CHARS_VIEW = "__________\n__________\n__________\n__________\n__________"; // 列表符号与编号窗口的预览界面的初始化字符串
  public static final String SIGN_CHARS = "﹟·※＊§¤⊙◎○●△▲▽▼◇◆□■☆★"; // 列表符号
  public static final String IDENTIFIER_CHARS = "0123"; // 列表编号类型标识符
  public static final String IDENTIFIER_TIANGAN = "甲乙丙丁戊己庚辛壬癸"; // 十天干
  public static final String IDENTIFIER_DIZHI = "子丑寅卯辰巳午未申酉戌亥"; // 十二地支
  public static final String INFO_FILE_PATH = "文件路径："; // 统计信息窗口中使用的字符串
  public static final String INFO_FILE_MODIFY_TIME = "修改时间："; // 统计信息窗口中使用的字符串
  public static final String INFO_FILE_SIZE = "文件大小："; // 统计信息窗口中使用的字符串
  public static final String INFO_DOC_CHARS = "总字数："; // 统计信息窗口中使用的字符串
  public static final String INFO_DOC_LINES = "总行数："; // 统计信息窗口中使用的字符串
  public static final String INFO_DOC_DIGITS = "数字数："; // 统计信息窗口中使用的字符串
  public static final String INFO_DOC_LETTERS = "字母数："; // 统计信息窗口中使用的字符串
  public static final String INFO_DOC_BLANKS = "空格数："; // 统计信息窗口中使用的字符串
  public static final String SYSTEM_LOOK_AND_FEEL_CLASS_NAME = UIManager
      .getSystemLookAndFeelClassName(); // 当前系统默认外观的完整类名
  public static final String[] FONT_FAMILY_NAMES = java.awt.GraphicsEnvironment
      .getLocalGraphicsEnvironment().getAvailableFontFamilyNames(); // 获取系统所有字体的名称列表
  public static final String[] FILE_ENCODINGS = new String[] { "自动检测",
      CharEncoding.ANSI.getName(), CharEncoding.UBE.getName(),
      CharEncoding.ULE.getName(), CharEncoding.UTF8.getName(),
      CharEncoding.UTF8_NO_BOM.getName(), CharEncoding.BASE.getName() }; // 选择编码格式的数组
  public static final String[] DATE_STYLES = new String[] { "yyyy-MM-dd",
      "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss:SSS", "yyyy-MM-dd KK:mm:ss",
      "yyyy-MM-dd KK:mm:ss a", "yyyy-MM-dd HH:mm:ss E",
      "yyyy-MM-dd HH:mm:ss zZ", "yyyy年MM月dd日 HH时mm分ss秒",
      "G yyyy-MM-dd HH:mm:ss E zZ", "yy-M-d H:m:s", "yyyy/MM/dd HH:mm:ss",
      "yyyy.MM.dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd", "yyyy.MM.dd",
      "yy/MM/dd", "HH:mm:ss", "KK:mm:ss a", "HH:mm:ss:SSS" }; // 时间/日期格式字符串
  public static final String[] SIGN_IDENTIFIER_NAMES = new String[] {
      "十进制数(1.2..10.11.)", "小写十六进制数(1.2..a.b.)", "大写十六进制数(1.2..A.B.)",
      "干支计数(甲子.乙丑..癸酉.甲戌.)" }; // 列表编号类型的显示名称
  public static final String[] TOOL_TOOLTIP_TEXTS = new String[] { "新建", "打开",
      "保存", "另存为", "关闭", "关闭全部", "剪切", "复制", "粘贴", "撤销", "重做", "查找", "替换",
      "字体放大", "字体缩小", "自动换行" }; // 工具栏提示信息
  public static final String[] WINDOW_MANAGE_TABLE_TITLE_TEXTS = new String[] {
      "文件名", "路径", "类型" }; // 窗口管理界面的表格标题
  public static final String[] SHORTCUT_MANAGE_TABLE_TITLE_TEXTS = new String[] {
      "功能", "快捷键" }; // 快捷键管理界面的表格标题
  public static final String[] SHORTCUT_NAMES = new String[] {
      "新建","打开","以指定编码打开","保存","另存为","重命名","重新载入文件","删除当前文件","关闭当前","关闭其它","关闭全部","清空最近编辑列表","退出","撤销","重做","剪切","复制","粘贴","全选","删除","切换为大写","切换为小写","复制当前文件名到剪贴板","复制当前文件路径到剪贴板","复制当前目录路径到剪贴板","复制所有文本到剪贴板","复写当前行","删除当前行","删除至行首","删除至行尾","删除至文件首","删除至文件尾","上移当前行","下移当前行","复制当前行","剪切当前行","批量切除行","批量插入行","批量分割行","批量合并行","逐行复写","升序排序","降序排序","缩进","退格","清除行首空白","清除行尾空白","清除行首和行尾空白","清除选区内空白","删除全文空行","删除选区空行","添加单行注释","添加区块注释","插入特殊字符","插入时间和日期","复写选区字符","反转选区字符","查找","查找下一个","查找上一个","选定查找下一个","选定查找上一个","快速向下查找","快速向上查找","替换","转到","定位匹配括号","单词边界换行","字符边界换行","Windows换行符格式","Unix/Linux换行符格式","Macintosh换行符格式","默认GB18030编码格式","ANSI编码格式","UTF-8编码格式","UTF-8 No BOM编码格式","Unicode Little Endian编码格式","Unicode Big Endian编码格式","列表符号与编号","字体","Tab键设置","自动换行","文本拖拽","自动缩进","恢复默认设置","显示/隐藏工具类","显示/隐藏状态栏","显示/隐藏行号栏","前端显示","锁定窗口","多行标签","双击关闭标签","显示/隐藏指示图标","字体放大","字体缩小","字体恢复初始大小","字体颜色","背景颜色","光标颜色","选区字体颜色","选区背景颜色","全部反色","全部补色","配色方案1","配色方案2","配色方案3","配色方案4","配色方案5","恢复默认配色","高亮显示格式1","高亮显示格式2","高亮显示格式3","高亮显示格式4","高亮显示格式5","清除高亮格式1","清除高亮格式2","清除高亮格式3","清除高亮格式4","清除高亮格式5","清除所有高亮格式","向后切换文档","向前切换文档","统计信息","窗口管理","帮助主题","关于" }; // 快捷键的名称
  public static final String[] SHORTCUT_VALUES = new String[] {
      "Ctrl+78","Ctrl+79","","Ctrl+83","","","","","115","","","","Ctrl+81","Ctrl+90","Ctrl+89","Ctrl+88","Ctrl+67","Ctrl+86","Ctrl+65","127","Ctrl+Shift+85","Ctrl+85","","","","Ctrl+Shift+65","Ctrl+68","Ctrl+Shift+68","Ctrl+Alt+37","Ctrl+Alt+39","Ctrl+Alt+Shift+37","Ctrl+Alt+Shift+39","Ctrl+Shift+38","Ctrl+Shift+40","Ctrl+Shift+67","Ctrl+Shift+88","Ctrl+Shift+82","Ctrl+Shift+73","Ctrl+Shift+80","Ctrl+Shift+77","Ctrl+Shift+86","Alt+38","Alt+40","Ctrl+Alt+84","Ctrl+Alt+Shift+84","Ctrl+Shift+83","Ctrl+Shift+69","Ctrl+Shift+76","Ctrl+Shift+84","Ctrl+Alt+65","Ctrl+Alt+83","Ctrl+76","Ctrl+77","","116","Ctrl+82","Ctrl+73","Ctrl+70","114","Shift+114","Ctrl+114","Ctrl+Shift+114","Ctrl+75","Ctrl+Shift+75","Ctrl+72","Ctrl+71","Ctrl+66","","","","","","","","","","","","","","","","","","","","","","","","","","","Ctrl+38","Ctrl+40","Ctrl+47","","","","","","","","","","","","","","","","","","","","","","","","","Ctrl+87","Ctrl+Shift+87","","","","112" }; // 快捷键的值
  public static final String[] CAN_NOT_MODIFIED_SHORTCUT_NAMES = new String[] {"剪切","复制","粘贴","全选","删除"}; // 不可修改的快捷键名称
  public static final int[] ALL_KEY_CODES = new int[] { KeyEvent.VK_0 ,KeyEvent.VK_1 ,KeyEvent.VK_2 ,KeyEvent.VK_3 ,KeyEvent.VK_4 ,KeyEvent.VK_5 ,KeyEvent.VK_6 ,KeyEvent.VK_7 ,KeyEvent.VK_8 ,KeyEvent.VK_9 ,KeyEvent.VK_A ,KeyEvent.VK_B ,KeyEvent.VK_C ,KeyEvent.VK_D ,KeyEvent.VK_E ,KeyEvent.VK_F ,KeyEvent.VK_G ,KeyEvent.VK_H ,KeyEvent.VK_I ,KeyEvent.VK_J ,KeyEvent.VK_K ,KeyEvent.VK_L ,KeyEvent.VK_M ,KeyEvent.VK_N ,KeyEvent.VK_O ,KeyEvent.VK_P ,KeyEvent.VK_Q ,KeyEvent.VK_R ,KeyEvent.VK_S ,KeyEvent.VK_T ,KeyEvent.VK_U ,KeyEvent.VK_V ,KeyEvent.VK_W ,KeyEvent.VK_X ,KeyEvent.VK_Y ,KeyEvent.VK_Z ,KeyEvent.VK_F1 ,KeyEvent.VK_F2 ,KeyEvent.VK_F3 ,KeyEvent.VK_F4 ,KeyEvent.VK_F5 ,KeyEvent.VK_F6 ,KeyEvent.VK_F7 ,KeyEvent.VK_F8 ,KeyEvent.VK_F9 ,KeyEvent.VK_F10 ,KeyEvent.VK_F11 ,KeyEvent.VK_F12 ,KeyEvent.VK_NUMPAD0 ,KeyEvent.VK_NUMPAD1 ,KeyEvent.VK_NUMPAD2 ,KeyEvent.VK_NUMPAD3 ,KeyEvent.VK_NUMPAD4 ,KeyEvent.VK_NUMPAD5 ,KeyEvent.VK_NUMPAD6 ,KeyEvent.VK_NUMPAD7 ,KeyEvent.VK_NUMPAD8 ,KeyEvent.VK_NUMPAD9 ,KeyEvent.VK_NUM_LOCK ,KeyEvent.VK_ADD ,KeyEvent.VK_SUBTRACT ,KeyEvent.VK_MULTIPLY ,KeyEvent.VK_DIVIDE ,KeyEvent.VK_DECIMAL ,KeyEvent.VK_ESCAPE ,KeyEvent.VK_TAB ,KeyEvent.VK_SPACE ,KeyEvent.VK_BACK_SPACE ,KeyEvent.VK_BACK_QUOTE ,KeyEvent.VK_SLASH ,KeyEvent.VK_BACK_SLASH ,KeyEvent.VK_OPEN_BRACKET ,KeyEvent.VK_CLOSE_BRACKET ,KeyEvent.VK_COMMA ,KeyEvent.VK_PAGE_UP ,KeyEvent.VK_PAGE_DOWN ,KeyEvent.VK_PERIOD ,KeyEvent.VK_QUOTE ,KeyEvent.VK_SEMICOLON ,KeyEvent.VK_INSERT ,KeyEvent.VK_DELETE ,KeyEvent.VK_HOME ,KeyEvent.VK_END ,KeyEvent.VK_UP ,KeyEvent.VK_DOWN ,KeyEvent.VK_LEFT ,KeyEvent.VK_RIGHT ,KeyEvent.VK_ENTER ,KeyEvent.VK_EQUALS ,KeyEvent.VK_MINUS ,KeyEvent.VK_CAPS_LOCK ,KeyEvent.VK_WINDOWS ,KeyEvent.VK_CONTEXT_MENU ,KeyEvent.VK_SCROLL_LOCK ,KeyEvent.VK_PAUSE }; // 所有可以用作快捷键的按键常量
  public static final int[] SINGLE_KEY_CODES = new int[] { KeyEvent.VK_F1 ,KeyEvent.VK_F2 ,KeyEvent.VK_F3 ,KeyEvent.VK_F4 ,KeyEvent.VK_F5 ,KeyEvent.VK_F6 ,KeyEvent.VK_F7 ,KeyEvent.VK_F8 ,KeyEvent.VK_F9 ,KeyEvent.VK_F10 ,KeyEvent.VK_F11 ,KeyEvent.VK_F12 }; // 可以单独用作快捷键的按键常量
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
  public static final int FILE_HISTORY_MAX = 10; // 最近编辑文件的最大存储个数
  public static final int DEFAULT_UNDO_INDEX = 0; // 撤销标识符的默认值
  public static final int INSERT_MAX_ROW = 10; // 插入字符界面的最大行数
  public static final int INSERT_MAX_COLUMN = 10; // 插入字符界面的最大列数
  public static final int INSERT_MAX_ELEMENT = INSERT_MAX_ROW
      * INSERT_MAX_COLUMN; // 插入字符界面的最大元素数
  public static final int SIGN_MAX_ROW = 5; // 列表符号与编号界面的最大行数
  public static final int SIGN_MAX_COLUMN = 4; // 列表符号与编号界面的最大列数
  public static final int SIGN_MAX_ELEMENT = SIGN_MAX_ROW * SIGN_MAX_COLUMN; // 列表符号与编号界面的最大元素数
  public static final int MSG_LINE_SIZE = 60; // 提示框中每行字符串显示的最大字数
  public static final int LINE_NUMBER_HEIGHT = 2000000000; // 行号组件支持的最大高度
  public static final int LINE_NUMBER_MARGIN = 5; // 行号组件的左右边距
  public static final int LINE_NUMBER_START_OFFSET = 2; // 行号组件的起始垂直偏移量，用于对齐文本域的各行
  public static final int BRACKET_COLOR_STYLE = 11; // 在文本域中进行高亮匹配括号的颜色标识值
  public static final Font GLOBAL_FONT = new Font("宋体", Font.PLAIN, 12); // 全局的默认字体
  public static final Font TEXT_FONT = new Font("宋体", Font.PLAIN, 14); // 文本域的默认字体
  public static final Font INSERT_VIEW_FONT = new Font("宋体", Font.PLAIN, 80); // 插入字符界面中预览标签的字体
  public static final Font SIGN_VIEW_FONT = new Font("宋体", Font.PLAIN, 28); // 列表符号与编号界面中预览区域的字体
  public static final Color COLOR_BRACKET = new Color(20, 20, 20, 30); // 在文本域中进行高亮匹配括号的背景颜色
  public static final Color[] COLOR_HIGHLIGHTS = new Color[] {
      new Color(255, 0, 0, 40), new Color(0, 255, 0, 40),
      new Color(0, 0, 255, 40), new Color(0, 255, 255, 40),
      new Color(255, 0, 255, 40) }; // 用于高亮显示的颜色，其中第4个参数表示透明度，数值越小越透明
  public static final Color[] COLOR_STYLE_1 = new Color[] {
      new Color(211, 215, 207), new Color(46, 52, 54),
      new Color(211, 215, 207), new Color(238, 238, 236),
      new Color(136, 138, 133) };
  public static final Color[] COLOR_STYLE_2 = new Color[] {
      new Color(240, 240, 240), new Color(0, 128, 128),
      new Color(240, 240, 240), new Color(22, 99, 88), new Color(240, 240, 240) };
  public static final Color[] COLOR_STYLE_3 = new Color[] {
      new Color(46, 52, 54), new Color(255, 251, 240), new Color(46, 52, 54),
      new Color(255, 251, 240), new Color(46, 52, 54) };
  public static final Color[] COLOR_STYLE_4 = new Color[] {
      new Color(51, 53, 49), new Color(204, 232, 207), new Color(51, 53, 49),
      new Color(204, 232, 207), new Color(0, 60, 100) };
  public static final Color[] COLOR_STYLE_5 = new Color[] {
      new Color(58, 57, 53), new Color(221, 212, 195), new Color(58, 57, 53),
      new Color(221, 212, 195), new Color(58, 57, 53) };
  public static final Color[][] COLOR_STYLES = new Color[][] { COLOR_STYLE_1,
      COLOR_STYLE_2, COLOR_STYLE_3, COLOR_STYLE_4, COLOR_STYLE_5 }; // 文本域配色方案的数组
  public static final Color[] COLOR_STYLE_DEFAULT = new Color[] {
      (Color) UIManager.getLookAndFeelDefaults()
          .getColor("TextArea.foreground"),
      (Color) UIManager.getLookAndFeelDefaults()
          .getColor("TextArea.background"),
      (Color) UIManager.getLookAndFeelDefaults().getColor(
          "TextArea.caretForeground"),
      (Color) UIManager.getLookAndFeelDefaults().getColor(
          "TextArea.selectionForeground"),
      (Color) UIManager.getLookAndFeelDefaults().getColor(
          "TextArea.selectionBackground") }; // 文本域默认配色方案
  public static final UIManager.LookAndFeelInfo[] LOOK_AND_FEEL_INFOS = UIManager
      .getInstalledLookAndFeels(); // 当前系统可用的外观信息数组
  public static final ImageIcon SW_ICON = new ImageIcon(ClassLoader
      .getSystemResource("res/icon.gif")); // 主程序图标
  public static final ImageIcon TAB_EXIST_READONLY_ICON = new ImageIcon(
      ClassLoader.getSystemResource("res/tab_exist_readonly.png")); // 只读文件图标
  public static final ImageIcon TAB_EXIST_CURRENT_ICON = new ImageIcon(
      ClassLoader.getSystemResource("res/tab_exist_current.png")); // 普通文件图标
  public static final ImageIcon TAB_NEW_FILE_ICON = new ImageIcon(ClassLoader
      .getSystemResource("res/tab_new_file.png")); // 新建文件图标
  public static final ImageIcon TAB_NOT_EXIST_ICON = new ImageIcon(ClassLoader
      .getSystemResource("res/tab_not_exist.png")); // 丢失文件图标
  public static final ImageIcon HELP_ICON = new ImageIcon(ClassLoader
      .getSystemResource("res/help.png")); // 帮助图标
  public static final ImageIcon[] TOOL_ENABLE_ICONS = new ImageIcon[] {
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_new.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_open.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_save.png")),
      new ImageIcon(ClassLoader
          .getSystemResource("res/enable/tool_save_as.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_close.png")),
      new ImageIcon(ClassLoader
          .getSystemResource("res/enable/tool_close_all.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_cut.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_copy.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_paste.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_undo.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_redo.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_find.png")),
      new ImageIcon(ClassLoader
          .getSystemResource("res/enable/tool_replace.png")),
      new ImageIcon(ClassLoader
          .getSystemResource("res/enable/tool_font_size_plus.png")),
      new ImageIcon(ClassLoader
          .getSystemResource("res/enable/tool_font_size_minus.png")),
      new ImageIcon(ClassLoader
          .getSystemResource("res/enable/tool_line_wrap.png")) }; // 工具栏可用状态的图标
  public static final ImageIcon[] TOOL_DISABLE_ICONS = new ImageIcon[] {
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_new.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_open.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_save.png")),
      new ImageIcon(ClassLoader
          .getSystemResource("res/disable/tool_save_as.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_close.png")),
      new ImageIcon(ClassLoader
          .getSystemResource("res/disable/tool_close_all.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_cut.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_copy.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_paste.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_undo.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_redo.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_find.png")),
      new ImageIcon(ClassLoader
          .getSystemResource("res/disable/tool_replace.png")),
      new ImageIcon(ClassLoader
          .getSystemResource("res/disable/tool_font_size_plus.png")),
      new ImageIcon(ClassLoader
          .getSystemResource("res/disable/tool_font_size_minus.png")),
      new ImageIcon(ClassLoader
          .getSystemResource("res/disable/tool_line_wrap.png")) }; // 工具栏禁用状态的图标

  public static int transfer_count = 0; // 查找或替换时，启用“转义扩展”后被转义的字符个数
  public static int matcher_length = 0; // 通过正则表达式成功匹配的字符个数
  private static Matcher matcher = null; // 通过解释Pattern对指定文本执行匹配操作的引擎

  /**
   * 由于此类为工具类，故将构造方法私有化
   */
  private Util() {
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
      fileFilter = new BaseFileFilter(fileExt.toString(), fileExt
          .getDescription());
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
      return Util.DEFAULT_CARET_INDEX;
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
    String arrContents[] = str.split("\n", -1);
    StringBuilder stbContent = new StringBuilder(); // 用于存放处理后的文本
    for (int n = 0; n < arrContents.length; n++) {
      String content = "";
      if (arrContents[n].length() > MSG_LINE_SIZE) {
        int lines = arrContents[n].length() / MSG_LINE_SIZE;
        int remain = arrContents[n].length() % MSG_LINE_SIZE;
        for (int i = 0; i < lines; i++) {
          content = content
              + arrContents[n].substring(MSG_LINE_SIZE * i, MSG_LINE_SIZE
                  * (i + 1)) + "\n";
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
   * @return 保存当前选区内所有行的字符数组
   */
  public static String[] getCurrentLinesArray(JTextArea txaSource) {
    if (txaSource == null) {
      return null;
    }
    int lineCount = txaSource.getLineCount();
    CurrentLines currentLines = new CurrentLines(txaSource);
    int startIndex = currentLines.getStartIndex();
    int endIndex = currentLines.getEndIndex();
    int endLineNum = currentLines.getEndLineNum() + 1;
    String strContent = currentLines.getStrContent();
    String strText = txaSource.getText();
    if (strContent.endsWith("\n")
        && (endIndex != strText.length() || (endLineNum == lineCount - 1 && strText
            .endsWith("\n")))) {
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
  public static File checkFileName(String strFileName,
      BaseFileFilter fileFilter, String strExt) {
    if (strFileName == null || strFileName.isEmpty() || fileFilter == null
        || strExt == null || strExt.isEmpty()) {
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
      boolean isFindDown, boolean isMatchCase, boolean isWrap,
      SearchStyle searchStyle) {
    if (isFindDown) {
      return findDownText(strFindText, txcSource, isMatchCase, isWrap,
          searchStyle);
    } else {
      return findUpText(strFindText, txcSource, isMatchCase, isWrap,
          searchStyle);
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
    if (strFindText == null || txcSource == null || strFindText.isEmpty()
        || txcSource.getText().isEmpty()) {
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
        JOptionPane.showMessageDialog(txcSource, "正则表达式语法错误：\n"
            + x.getMessage(), Util.SOFTWARE, JOptionPane.NO_OPTION);
        return result;
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
    if (strFindText == null || txcSource == null || strFindText.isEmpty()
        || txcSource.getText().isEmpty()) {
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
        JOptionPane.showMessageDialog(txcSource, "正则表达式语法错误：\n"
            + x.getMessage(), Util.SOFTWARE, JOptionPane.NO_OPTION);
        return result;
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
    if (shortcut.isEmpty()) {
      return value;
    }
    boolean hasCtrl = false; // 是否含有Ctrl键
    boolean hasAlt = false; // 是否含有Alt键
    boolean hasShift = false; // 是否含有Shift键
    String[] arrKeys = shortcut.split("\\+");
    for (String str : arrKeys) {
      if (CTRL.equalsIgnoreCase(str)) {
        hasCtrl = true;
      } else if (ALT.equalsIgnoreCase(str)) {
        hasAlt = true;
      } else if (SHIFT.equalsIgnoreCase(str)) {
        hasShift = true;
      } else { // 除控制键之外的按键
        String strKey = transferKeyCode(str);
        if (!strKey.isEmpty()) {
          value = strKey;
        }
      }
    }
    if (!value.isEmpty()) {
      if (hasShift) {
        value = SHIFT + "+" + value;
      }
      if (hasAlt) {
        value = ALT + "+" + value;
      }
      if (hasCtrl) {
        value = CTRL + "+" + value;
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
    if (shortcut.isEmpty()) {
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
      } else { // 除控制键之外的按键
        try {
          keyCode = Integer.parseInt(str);
        } catch (Exception x) {
        }
      }
    }
    if (keyCode >= 0) {
      keyStroke = KeyStroke.getKeyStroke(keyCode, modifiers);
    }
    return keyStroke;
  }

}
