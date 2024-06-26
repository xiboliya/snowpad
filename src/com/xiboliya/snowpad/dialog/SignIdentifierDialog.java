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

package com.xiboliya.snowpad.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextArea;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.common.CurrentLines;
import com.xiboliya.snowpad.util.Util;

/**
 * "列表符号与编号"对话框
 * 
 * @author 冰原
 * 
 */
public class SignIdentifierDialog extends BaseDialog implements ActionListener,
    FocusListener, ChangeListener, DocumentListener {
  private static final long serialVersionUID = 1L;
  private static final String IDENTIFIER_CHARS = "0123456789"; // 列表编号类型标识符
  private static final String SIGN_CHARS = "﹟·※＊§¤⊙◎○●△▲▽▼◇◆□■☆★"; // 列表符号
  private static final String SIGN_CHARS_VIEW = "__________\n__________\n__________"; // 预览界面的初始化字符串
  private static final String IDENTIFIER_TIANGAN = "甲乙丙丁戊己庚辛壬癸"; // 十天干
  private static final String IDENTIFIER_DIZHI = "子丑寅卯辰巳午未申酉戌亥"; // 十二地支
  private static final String IDENTIFIER_QIYAO = "日月火水木金土"; // 七曜
  private static final String[] SIGN_IDENTIFIER_NAMES = new String[] { "半角数字格式", "全角数字格式", "简体汉字格式", "繁体汉字格式", "小写字母格式", "大写字母格式", "天干格式", "地支格式", "干支格式", "七曜格式" }; // 列表编号类型的显示名称
  private static final String HALF_WIDTH_NUMBERS = "0123456789"; // 半角数字
  private static final char[] FULL_WIDTH_NUMBERS = new char[] { '０', '１', '２', '３', '４', '５', '６', '７', '８', '９' }; // 全角数字
  private static final String[] SIMPLIFIED_CHINESE_NUMBERS = new String[] { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" }; // 简体数字
  private static final String[] SIMPLIFIED_CHINESE_UNITS = new String[] { "", "十", "百", "千", "万", "十", "百", "千", "亿", "十" }; // 简体数字单位
  private static final String[] TRADITIONAL_CHINESE_NUMBERS = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" }; // 繁体数字
  private static final String[] TRADITIONAL_CHINESE_UNITS = new String[] { "", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾" }; // 繁体数字单位
  private static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz"; // 小写英文字母
  private static final Font SIGN_VIEW_FONT = new Font("宋体", Font.PLAIN, 28); // 列表符号与编号界面中预览区域的字体
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JPanel pnlLeft = new JPanel(new BorderLayout());
  private JPanel pnlRight = new JPanel(null);
  private JTabbedPane tpnMain = new JTabbedPane();
  private GridLayout gridLayout = new GridLayout(5, 4, 5, 5);
  private GridLayout specialGridLayout = new GridLayout(5, 2, 5, 5);
  private BaseButton btnOk = new BaseButton("确定");
  private BaseButton btnCancel = new BaseButton("取消");
  private JLabel lblStart = new JLabel("起始编号：");
  private BaseTextField txtStart = new BaseTextField(true, "\\d*"); // 限制用户只能输入数字
  private JLabel lblModifier = new JLabel("修饰字符：");
  private BaseTextField txtModifier = new BaseTextField(".", true, ".{0,2}"); // 限制用户输入的字符数量不能超过2个
  private JCheckBox chkSkipEmptyLine = new JCheckBox("跳过空行(S)", false);
  private JTextArea txaView = new JTextArea();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private EtchedBorder etchedBorder = new EtchedBorder();
  private MouseAdapter mouseAdapter = null;
  private String strSignIdentifier = ""; // 当前选中的符号或编号字符串
  private String strModifierSign = "."; // 符号的修饰字符
  private String strModifierIdentifier = ""; // 编号的修饰字符

  /**
   * 构造方法
   * 
   * @param owner 用于显示该对话框的父组件
   * @param modal 是否为模式对话框
   * @param txaSource 针对操作的文本域
   */
  public SignIdentifierDialog(JFrame owner, boolean modal, BaseTextArea txaSource) {
    super(owner, modal, txaSource);
    this.init();
    this.setMnemonic();
    this.addListeners();
    this.setSize(330, 310);
    this.fillTabbedPane();
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("列表符号与编号");
    this.pnlMain.setLayout(null);
    this.pnlLeft.setBounds(0, 0, 200, 270);
    this.pnlRight.setBounds(200, 0, 120, 300);
    this.pnlMain.add(this.pnlLeft);
    this.pnlMain.add(this.pnlRight);
    this.pnlLeft.add(this.tpnMain, BorderLayout.CENTER);
    this.btnOk.setBounds(15, 25, 90, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(15, 60, 90, Util.BUTTON_HEIGHT);
    this.lblStart.setBounds(6, 100, 70, Util.VIEW_HEIGHT);
    this.txtStart.setBounds(76, 100, 40, Util.INPUT_HEIGHT);
    this.lblModifier.setBounds(6, 135, 70, Util.VIEW_HEIGHT);
    this.txtModifier.setBounds(76, 135, 40, Util.INPUT_HEIGHT);
    this.chkSkipEmptyLine.setBounds(6, 170, 110, Util.VIEW_HEIGHT);
    this.txaView.setBounds(12, 205, 98, 65);
    this.txaView.setBorder(new EtchedBorder());
    this.txaView.setOpaque(true);
    this.txaView.setEditable(false);
    this.txaView.setFocusable(false);
    this.pnlRight.add(this.btnOk);
    this.pnlRight.add(this.btnCancel);
    this.pnlRight.add(this.lblStart);
    this.pnlRight.add(this.txtStart);
    this.pnlRight.add(this.lblModifier);
    this.pnlRight.add(this.txtModifier);
    this.pnlRight.add(this.chkSkipEmptyLine);
    this.pnlRight.add(this.txaView);
    this.tpnMain.setFocusable(false);
    this.btnOk.setFocusable(false);
    this.btnCancel.setFocusable(false);
  }

  /**
   * 为各组件设置快捷键
   */
  private void setMnemonic() {
    this.chkSkipEmptyLine.setMnemonic('S');
  }

  /**
   * 填充所有标签页的字符
   */
  private void fillTabbedPane() {
    this.fillElements(IDENTIFIER_CHARS, "编号");
    this.fillElements(SIGN_CHARS, "符号");
  }

  /**
   * 填充一个标签页的字符
   * 
   * @param strElement 字符序列
   * @param strTitle 标签页标题
   */
  private void fillElements(String strElement, String strTitle) {
    if (Util.isTextEmpty(strElement) || Util.isTextEmpty(strTitle)) {
      return;
    }
    int elementCount = strElement.length();
    boolean isSpecial = false;
    JPanel pnlTemp = null;
    int gridTotal = 0;
    if (strElement.equals(IDENTIFIER_CHARS)) {
      isSpecial = true;
    }
    if (isSpecial) {
      pnlTemp = new JPanel(this.specialGridLayout); // 创建特殊格式的布局
      gridTotal = this.specialGridLayout.getRows() * this.specialGridLayout.getColumns();
    } else {
      pnlTemp = new JPanel(this.gridLayout); // 创建普通格式的布局
      gridTotal = this.gridLayout.getRows() * this.gridLayout.getColumns();
    }
    if (elementCount > gridTotal) { // 保证元素的个数不大于布局中预留的网格数，多出的元素将被舍弃
      elementCount = gridTotal;
    }
    int index = 0;
    for (; index < elementCount; index++) {
      char charElement = strElement.charAt(index);
      JLabel lblElement = this.createElement(String.valueOf(charElement), isSpecial);
      pnlTemp.add(lblElement);
    }
    for (; index < gridTotal; index++) {
      JLabel lblElement = this.createElement("", false);
      pnlTemp.add(lblElement);
    }
    this.tpnMain.add(pnlTemp, strTitle);
  }

  /**
   * 创建一个文本标签
   * 
   * @param strElement 显示的字符
   * @param isSpecial 是否为特殊格式
   * @return 新创建的文本标签
   */
  private JLabel createElement(String strElement, boolean isSpecial) {
    JLabel lblElement = null;
    if (isSpecial) {
      lblElement = new JLabel(SIGN_IDENTIFIER_NAMES[IDENTIFIER_CHARS.indexOf(strElement)]);
    } else {
      lblElement = new JLabel(strElement);
      lblElement.setFont(SIGN_VIEW_FONT);
    }
    if (!Util.isTextEmpty(strElement)) {
      lblElement.setHorizontalAlignment(SwingConstants.CENTER);
      lblElement.setFocusable(true); // 设置标签可以获得焦点
      lblElement.setOpaque(true); // 设置标签可以绘制背景
      lblElement.setBorder(this.etchedBorder);
      lblElement.setBackground(Color.WHITE);
      lblElement.addKeyListener(this.keyAdapter);
      lblElement.addFocusListener(this);
      lblElement.addMouseListener(this.mouseAdapter);
    }
    return lblElement;
  }

  /**
   * 使用或预览当前选中的符号或编号方式
   * 
   * @param isView 是否为预览模式，当为true时表示为预览当前模式，否则为使用当前模式
   */
  private void signIdentifier(boolean isView) {
    String[] arrText = null;
    boolean isSelected = false;
    if (isView) {
      arrText = SIGN_CHARS_VIEW.split("\n");
    } else {
      String selText = this.txaSource.getSelectedText();
      if (Util.isTextEmpty(selText)) {
        arrText = this.txaSource.getText().split("\n");
      } else {
        isSelected = true;
        arrText = Util.getCurrentLinesArray(this.txaSource, null);
      }
    }
    if (arrText == null || arrText.length <= 0) {
      return;
    }
    boolean isSpecial = false;
    if (this.tpnMain.getSelectedIndex() == 0) {
      isSpecial = true;
    }
    String strStart = this.txtStart.getText().trim();
    int start = 1;
    try {
      start = Integer.parseInt(strStart);
    } catch (NumberFormatException x) {
      // x.printStackTrace();
    }
    this.toConvertArray(arrText, isSpecial, start - 1);
    StringBuilder stbIndent = new StringBuilder();
    for (String str : arrText) {
      stbIndent.append(str + "\n");
    }
    if (isView) {
      this.txaView.setText(stbIndent.deleteCharAt(stbIndent.length() - 1).toString()); // 删除字符串末尾多余的换行符
    } else if (isSelected) {
      this.txaSource.replaceSelection(stbIndent.deleteCharAt(stbIndent.length() - 1).toString()); // 删除字符串末尾多余的换行符
    } else {
      this.txaSource.setText(stbIndent.deleteCharAt(stbIndent.length() - 1).toString()); // 删除字符串末尾多余的换行符
    }
  }

  /**
   * 保存符号或编号的修饰字符
   */
  private void saveModifier() {
    int index = this.tpnMain.getSelectedIndex();
    String strModifier = this.txtModifier.getText();
    if (index == 0) {
      this.strModifierSign = strModifier;
    } else if (index == 1) {
      this.strModifierIdentifier = strModifier;
    }
  }

  /**
   * 为指定的字符串数组添加当前选中的符号或编号
   * 
   * @param arrText 待处理的字符串数组
   * @param isSpecial 是否为特殊格式
   * @param start 起始编号
   */
  private void toConvertArray(String[] arrText, boolean isSpecial, int start) {
    int n = 0;
    int k = 0;
    boolean isSkipEmptyLine = this.chkSkipEmptyLine.isSelected();
    String strModifier = this.txtModifier.getText();
    if (isSpecial) {
      int index = 0;
      for (; index < SIGN_IDENTIFIER_NAMES.length; index++) {
        if (this.strSignIdentifier.equals(SIGN_IDENTIFIER_NAMES[index])) {
          break;
        }
      }
      for (; n < arrText.length; n++) {
        if (!isSkipEmptyLine || !Util.isTextEmpty(arrText[n])) {
          switch (index) {
          case 0: // 半角数字格式
            arrText[n] = (k + start + 1) + strModifier + arrText[n];
            break;
          case 1: // 全角数字格式
            arrText[n] = this.intToFullWidth(k + start + 1) + strModifier + arrText[n];
            break;
          case 2: // 简体汉字格式
            arrText[n] = this.intToChinese(k + start + 1, false) + strModifier + arrText[n];
            break;
          case 3: // 繁体汉字格式
            arrText[n] = this.intToChinese(k + start + 1, true) + strModifier + arrText[n];
            break;
          case 4: // 小写字母格式
            arrText[n] = this.intToLetter(k + start + 1, false) + strModifier + arrText[n];
            break;
          case 5: // 大写字母格式
            arrText[n] = this.intToLetter(k + start + 1, true) + strModifier + arrText[n];
            break;
          case 6: // 天干格式
            arrText[n] = this.intToTianGan(k + start) + strModifier + arrText[n];
            break;
          case 7: // 地支格式
            arrText[n] = this.intToDiZhi(k + start) + strModifier + arrText[n];
            break;
          case 8: // 干支格式
            arrText[n] = this.intToGanZhi(k + start) + strModifier + arrText[n];
            break;
          case 9: // 七曜格式
            arrText[n] = this.intToQiYao(k + start) + strModifier + arrText[n];
            break;
          }
          k++;
        }
      }
    } else {
      for (; n < arrText.length; n++) {
        if (!isSkipEmptyLine || !Util.isTextEmpty(arrText[n])) {
          arrText[n] = this.strSignIdentifier + strModifier + arrText[n];
        }
      }
    }
  }

  /**
   * 将给定的数字转换为全角数字格式
   * 
   * @param number 待转换的数字
   * @return 转换后的字符串
   */
  private String intToFullWidth(int number) {
    String strNumber = Integer.toString(number);
    char[] arrChar = strNumber.toCharArray();
    for (int i = 0; i < arrChar.length; i++) {
      char ch = arrChar[i];
      int index = HALF_WIDTH_NUMBERS.indexOf(ch);
      arrChar[i] = FULL_WIDTH_NUMBERS[index];
    }
    String result = new String(arrChar);
    return result;
  }

  /**
   * 将给定的数字转换为汉字格式
   * 
   * @param number 待转换的数字
   * @param isTraditional 是否转换为繁体汉字，为true表示转换为繁体，反之转换为简体
   * @return 转换后的字符串
   */
  private String intToChinese(int number, boolean isTraditional) {
    String str = "";
    StringBuilder sb = new StringBuilder(String.valueOf(number));
    sb = sb.reverse();
    String[] arrChineseNumbers = SIMPLIFIED_CHINESE_NUMBERS;
    String[] arrChineseUnits = SIMPLIFIED_CHINESE_UNITS;
    if (isTraditional) {
      arrChineseNumbers = TRADITIONAL_CHINESE_NUMBERS;
      arrChineseUnits = TRADITIONAL_CHINESE_UNITS;
    }
    int r = 0;
    int l = 0;
    for (int j = 0; j < sb.length(); j++) {
      r = Integer.valueOf(sb.substring(j, j + 1)); // 当前数字
      if (j != 0) {
        l = Integer.valueOf(sb.substring(j - 1, j)); // 上一个数字
      }
      if (j == 0) {
        if (r != 0 || sb.length() == 1) {
          str = arrChineseNumbers[r];
        }
        continue;
      }
      if (j == 1 || j == 2 || j == 3 || j == 5 || j == 6 || j == 7 || j == 9) {
        if (r != 0) {
          str = arrChineseNumbers[r] + arrChineseUnits[j] + str;
        } else if (l != 0) {
          str = arrChineseNumbers[r] + str;
        }
        continue;
      }
      if (j == 4 || j == 8) {
        str =  arrChineseUnits[j] + str;
        if ((l != 0 && r == 0) || r != 0) {
          str = arrChineseNumbers[r] + str;
        }
        continue;
      }
    }
    // 为了解决数值为：10~19时，会在开头多“一”的问题
    if (number >= 10 && number <= 19) {
      str = str.substring(1);
    }
    return str;
  }

  /**
   * 将给定的数字转换为天干
   * 
   * @param number 待转换的数字
   * @return 转换后的字符串
   */
  private String intToTianGan(int number) {
    int len = IDENTIFIER_TIANGAN.length();
    return String.valueOf(IDENTIFIER_TIANGAN.charAt(number % len));
  }

  /**
   * 将给定的数字转换为地支
   * 
   * @param number 待转换的数字
   * @return 转换后的字符串
   */
  private String intToDiZhi(int number) {
    int len = IDENTIFIER_DIZHI.length();
    return String.valueOf(IDENTIFIER_DIZHI.charAt(number % len));
  }

  /**
   * 将给定的数字转换为干支
   * 
   * @param number 待转换的数字
   * @return 转换后的字符串
   */
  private String intToGanZhi(int number) {
    int len1 = IDENTIFIER_TIANGAN.length();
    int len2 = IDENTIFIER_DIZHI.length();
    return String.valueOf(IDENTIFIER_TIANGAN.charAt(number % len1)) + String.valueOf(IDENTIFIER_DIZHI.charAt(number % len2));
  }

  /**
   * 将给定的数字转换为七曜
   * 
   * @param number 待转换的数字
   * @return 转换后的字符串
   */
  private String intToQiYao(int number) {
    int len = IDENTIFIER_QIYAO.length();
    return String.valueOf(IDENTIFIER_QIYAO.charAt(number % len));
  }

  /**
   * 将给定的数字转换为英文字母
   * 
   * @param number 待转换的数字
   * @param isUpperCase 是否转换为大写，为true表示转换为大写，反之转换为小写
   * @return 转换后的字符串
   */
  private String intToLetter(int number, boolean isUpperCase) {
    String strNumber = Integer.toString(number, LOWER_CASE_LETTERS.length()).toLowerCase();
    char[] arrChar = strNumber.toCharArray();
    for (int i = 0; i < arrChar.length; i++) {
      char ch = arrChar[i];
      int index = HALF_WIDTH_NUMBERS.indexOf(ch);
      if (index >= 0) {
        // 数字的最高位需要特殊处理
        if (i == 0) {
          arrChar[i] = LOWER_CASE_LETTERS.charAt(index - 1);
        } else {
          arrChar[i] = LOWER_CASE_LETTERS.charAt(index);
        }
      } else {
        index = LOWER_CASE_LETTERS.indexOf(ch);
        if (index >= 0) {
          arrChar[i] = LOWER_CASE_LETTERS.charAt(index + HALF_WIDTH_NUMBERS.length());
        }
      }
    }
    String result = new String(arrChar);
    if (isUpperCase) {
      result = result.toUpperCase();
    }
    return result;
  }

  /**
   * 添加和初始化事件监听器
   */
  private void addListeners() {
    this.tpnMain.addChangeListener(this);
    this.btnOk.addActionListener(this);
    this.btnCancel.addActionListener(this);
    this.txtStart.addKeyListener(this.keyAdapter);
    this.txtStart.getDocument().addDocumentListener(this);
    this.txtModifier.addKeyListener(this.keyAdapter);
    this.txtModifier.getDocument().addDocumentListener(this);
    this.chkSkipEmptyLine.addKeyListener(this.keyAdapter);
    this.mouseAdapter = new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        JLabel lblTemp = (JLabel) e.getSource();
        lblTemp.requestFocus(); // 当鼠标单击时，获得焦点
        if (e.getClickCount() == 2) {
          onEnter();
        }
      }
    };
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (this.btnOk.equals(source)) {
      this.onEnter();
    } else if (this.btnCancel.equals(source)) {
      this.onCancel();
    }
  }

  /**
   * 默认的"确定"操作方法
   */
  @Override
  public void onEnter() {
    this.signIdentifier(false); // 应用当前的列表符号或编号效果
    this.dispose();
  }

  /**
   * 默认的"取消"操作方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }

  /**
   * 当文本标签获得焦点时，将触发此事件
   */
  @Override
  public void focusGained(FocusEvent e) {
    JLabel lblTemp = (JLabel) e.getSource();
    lblTemp.setBackground(Color.PINK);
    this.strSignIdentifier = lblTemp.getText();
    this.signIdentifier(true); // 预览当前的列表符号或编号效果
  }

  /**
   * 当文本标签失去焦点时，将触发此事件
   */
  @Override
  public void focusLost(FocusEvent e) {
    JLabel lblTemp = (JLabel) e.getSource();
    lblTemp.setBackground(Color.WHITE);
  }

  /**
   * 当监听的组件状态变化时，将触发此事件
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    int index = this.tpnMain.getSelectedIndex();
    if (index == 0) {
      this.txtStart.setEnabled(true);
      this.txtStart.setFocusable(true);
      this.txtModifier.setText(this.strModifierSign);
    } else if (index == 1) {
      this.txtStart.setEnabled(false);
      this.txtStart.setFocusable(false);
      this.txtModifier.setText(this.strModifierIdentifier);
    }
    JPanel pnlTemp = (JPanel) this.tpnMain.getComponentAt(index);
    pnlTemp.getComponent(0).requestFocus();
  }

  private void execDocumentEvent(DocumentEvent e) {
    if (this.txtModifier.getDocument().equals(e.getDocument())) {
      this.saveModifier();
    }
    this.signIdentifier(true); // 预览当前的列表符号或编号效果
  }

  /**
   * 当文本控件插入文本时，将触发此事件
   */
  @Override
  public void insertUpdate(DocumentEvent e) {
    this.execDocumentEvent(e);
  }

  /**
   * 当文本控件删除文本时，将触发此事件
   */
  @Override
  public void removeUpdate(DocumentEvent e) {
    this.execDocumentEvent(e);
  }

  /**
   * 当文本控件修改文本时，将触发此事件
   */
  @Override
  public void changedUpdate(DocumentEvent e) {
    this.execDocumentEvent(e);
  }
}
