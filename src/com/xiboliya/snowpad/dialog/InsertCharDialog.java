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
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseTextArea;
import com.xiboliya.snowpad.util.Util;

/**
 * "插入字符"对话框
 * 
 * @author 冰原
 * 
 */
public class InsertCharDialog extends BaseDialog implements ActionListener, FocusListener {
  private static final long serialVersionUID = 1L;
  private static final String INSERT_SPECIAL = "﹡＊♀♂㊣㈱卍卐℡⊕◎〓○●△▲▽▼◇◆□■☆★◢◣◤◥︳ˉ–—﹏﹋＿￣﹍﹉﹎﹊┄┆┅┇┈┊┉┋↑↓←→↖↗↙↘∥∣／＼∕﹨╳▂▃▄▅▆▇█▉▊▋▌▍▎▏▁▔▕┳┻┫┣┃━┏┓┗┛╋╱╲╮╭╯╰"; // 特殊符号
  private static final String INSERT_PUNCTUATION = "，、。．；：？﹖?！︰∶…‥′＇｀‵＂〃～~‖ˇ﹐﹑.﹒﹔﹕¨﹗（）︵︶｛｝︷︸〔〕︹︺【】︻︼〖〗［］《》︽︾〈〉︿﹀「」﹁﹂『』﹃﹄﹙﹚﹛﹜﹝﹞‘’“”〝〞ˋˊ§々"; // 标点符号
  private static final String INSERT_MATH = "≈≡≠＝≒≤≥≦≧＜＞≮≯±＋－×÷／∫∮∝∞∧∨∑∏∪∩∈∵∴∷⊥∥∠⌒⊙≌∽√﹢﹣﹤﹥﹦∟⊿π℅﹟＃#＆﹠&※№㏒㏑"; // 数学符号
  private static final String INSERT_UNIT = "°′″＄￥〒￠￡％℃℉﹩$﹪‰＠﹫㏕㎜㎝㎞㏎㎡㎎㎏㏄¤"; // 单位符号
  private static final String INSERT_DIGIT = "⒈⒉⒊⒋⒌⒍⒎⒏⒐⒑⒒⒓⒔⒕⒖⒗⒘⒙⒚⒛⑴⑵⑶⑷⑸⑹⑺⑻⑼⑽⑾⑿⒀⒁⒂⒃⒄⒅⒆⒇①②③④⑤⑥⑦⑧⑨⑩㈠㈡㈢㈣㈤㈥㈦㈧㈨㈩ⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫ"; // 数字符号
  private static final String INSERT_PINYIN = "āáǎàōóǒòēéěèīíǐìūúǔùǖǘǚǜüêɑńňǹɡㄅㄆㄇㄈㄉㄊㄋㄌㄍㄎㄏㄐㄑㄒㄓㄔㄕㄖㄗㄘㄙㄚㄛㄜㄝㄞㄟㄠㄡㄢㄣㄤㄥㄦㄧㄨㄩ"; // 拼音符号
  private static final int INSERT_MAX_ROW = 10; // 插入字符界面的最大行数
  private static final int INSERT_MAX_COLUMN = 10; // 插入字符界面的最大列数
  private static final int INSERT_MAX_ELEMENT = INSERT_MAX_ROW * INSERT_MAX_COLUMN; // 插入字符界面的最大元素数
  private static final Font INSERT_VIEW_FONT = new Font("宋体", Font.PLAIN, 80); // 插入字符界面中预览标签的字体
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JPanel pnlLeft = new JPanel(new BorderLayout());
  private JPanel pnlRight = new JPanel(null);
  private JTabbedPane tpnMain = new JTabbedPane();
  private GridLayout gridLayout = new GridLayout(INSERT_MAX_ROW, INSERT_MAX_COLUMN, 0, 0);
  private BaseButton btnOk = new BaseButton("插入");
  private BaseButton btnCancel = new BaseButton("关闭");
  private JLabel lblView = new JLabel();
  private EtchedBorder etchedBorder = new EtchedBorder();
  private KeyAdapter arrowKeyAdapter = new KeyAdapter() {
    public void keyPressed(KeyEvent e) {
      JLabel lblSource = (JLabel) e.getSource();
      switch (e.getKeyCode()) {
      case KeyEvent.VK_LEFT:
        moveFocus(lblSource, 0, -1);
        break;
      case KeyEvent.VK_RIGHT:
        moveFocus(lblSource, 0, 1);
        break;
      case KeyEvent.VK_UP:
        moveFocus(lblSource, -1, 0);
        break;
      case KeyEvent.VK_DOWN:
        moveFocus(lblSource, 1, 0);
        break;
      default:
        break;
      }
    }
  };
  private MouseAdapter mouseAdapter = null;

  /**
   * 构造方法
   * 
   * @param owner 用于显示该对话框的父组件
   * @param modal 是否为模式对话框
   * @param txaSource 针对操作的文本域
   */
  public InsertCharDialog(JFrame owner, boolean modal, BaseTextArea txaSource) {
    super(owner, modal, txaSource);
    this.init();
    this.addListeners();
    this.setSize(340, 275);
    this.fillTabbedPane();
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("插入字符");
    this.pnlMain.setLayout(null);
    this.pnlLeft.setBounds(0, 0, 230, 245);
    this.pnlRight.setBounds(230, 0, 110, 240);
    this.pnlMain.add(this.pnlLeft);
    this.pnlMain.add(this.pnlRight);
    this.pnlLeft.add(this.tpnMain, BorderLayout.CENTER);
    this.btnOk.setBounds(10, 30, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(10, 70, 85, Util.BUTTON_HEIGHT);
    this.lblView.setBounds(6, 125, 96, 96);
    this.lblView.setBorder(this.etchedBorder);
    this.lblView.setHorizontalAlignment(SwingConstants.CENTER);
    this.lblView.setOpaque(true);
    this.pnlRight.add(this.btnOk);
    this.pnlRight.add(this.btnCancel);
    this.pnlRight.add(this.lblView);
    this.tpnMain.setFocusable(false);
    this.lblView.setFont(INSERT_VIEW_FONT);
    this.btnOk.setFocusable(false);
    this.btnCancel.setFocusable(false);
  }

  /**
   * 填充所有标签页的字符
   */
  private void fillTabbedPane() {
    this.fillElements(INSERT_SPECIAL, "特殊符号");
    this.fillElements(INSERT_PUNCTUATION, "标点符号");
    this.fillElements(INSERT_MATH, "数学符号");
    this.fillElements(INSERT_UNIT, "单位符号");
    this.fillElements(INSERT_DIGIT, "数字符号");
    this.fillElements(INSERT_PINYIN, "拼音符号");
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
    JPanel pnlTemp = new JPanel(this.gridLayout);
    int elementCount = strElement.length();
    if (elementCount > INSERT_MAX_ELEMENT) {
      elementCount = INSERT_MAX_ELEMENT;
    }
    int index = 0;
    for (; index < elementCount; index++) {
      char charElement = strElement.charAt(index);
      JLabel lblElement = this.createElement(String.valueOf(charElement));
      pnlTemp.add(lblElement);
    }
    for (; index < INSERT_MAX_ELEMENT; index++) {
      JLabel lblElement = this.createElement("");
      pnlTemp.add(lblElement);
    }
    this.tpnMain.add(pnlTemp, strTitle);
  }

  /**
   * 创建一个文本标签
   * 
   * @param strElement 显示的字符
   * @return 新创建的文本标签
   */
  private JLabel createElement(String strElement) {
    JLabel lblElement = new JLabel(strElement);
    lblElement.setFocusable(false); // 空白填充标签不允许获得焦点，避免焦点落在看不见的格子上
    if (!Util.isTextEmpty(strElement)) {
      lblElement.setHorizontalAlignment(SwingConstants.CENTER);
      lblElement.setFocusable(true); // 设置标签可以获得焦点
      lblElement.setOpaque(true); // 设置标签可以绘制背景
      lblElement.setBorder(this.etchedBorder);
      lblElement.setBackground(Color.WHITE);
      lblElement.addKeyListener(this.keyAdapter);
      lblElement.addKeyListener(this.arrowKeyAdapter); // 支持用方向键在标签之间切换
      lblElement.addFocusListener(this);
      lblElement.addMouseListener(this.mouseAdapter);
    }
    return lblElement;
  }

  /**
   * 预览当前选中的字符
   * 
   * @param strView 当前选中文本标签的字符
   */
  private void setView(String strView) {
    this.lblView.setText(strView);
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.btnOk.addActionListener(this);
    this.btnCancel.addActionListener(this);
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
  public void onEnter() {
    this.txaSource.replaceSelection(this.lblView.getText());
  }

  /**
   * 默认的"取消"操作方法
   */
  public void onCancel() {
    this.dispose();
  }

  /**
   * 当文本标签获得焦点时，将触发此事件
   */
  public void focusGained(FocusEvent e) {
    JLabel lblTemp = (JLabel) e.getSource();
    lblTemp.setBackground(Color.PINK);
    this.setView(lblTemp.getText());
  }

  /**
   * 当文本标签失去焦点时，将触发此事件
   */
  public void focusLost(FocusEvent e) {
    JLabel lblTemp = (JLabel) e.getSource();
    lblTemp.setBackground(Color.WHITE);
  }

  /**
   * 沿指定方向把焦点移动到相邻的标签上，不可获得焦点的空标签会被跳过
   * 
   * @param lblSource 当前获得焦点的标签
   * @param rowStep 行方向的步长，-1为向上，1为向下，0为不移动
   * @param columnStep 列方向的步长，-1为向左，1为向右，0为不移动
   */
  private void moveFocus(JLabel lblSource, int rowStep, int columnStep) {
    Container pnlParent = lblSource.getParent();
    if (pnlParent == null || !(pnlParent.getLayout() instanceof GridLayout)) {
      return;
    }
    int columnCount = ((GridLayout) pnlParent.getLayout()).getColumns();
    int componentCount = pnlParent.getComponentCount();
    int index = -1;
    for (int i = 0; i < componentCount; i++) {
      if (pnlParent.getComponent(i) == lblSource) {
        index = i;
        break;
      }
    }
    if (index < 0 || columnCount <= 0) {
      return;
    }
    int row = index / columnCount;
    int column = index % columnCount;
    while (true) {
      row += rowStep;
      column += columnStep;
      if (row < 0 || column < 0 || column >= columnCount) {
        return;
      }
      int target = row * columnCount + column;
      if (target >= componentCount) {
        return;
      }
      if (pnlParent.getComponent(target) instanceof JLabel) {
        JLabel lblTarget = (JLabel) pnlParent.getComponent(target);
        // 只有真正显示字符的格子才是有效的目标，空白填充格子一律跳过
        if (lblTarget.isFocusable() && !Util.isTextEmpty(lblTarget.getText())) {
          lblTarget.requestFocus();
          return;
        }
      }
    }
  }
}
