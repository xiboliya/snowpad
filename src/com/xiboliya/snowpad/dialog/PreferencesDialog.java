/**
 * Copyright (C) 2023 冰原
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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.common.CharEncoding;
import com.xiboliya.snowpad.common.LineSeparator;
import com.xiboliya.snowpad.frame.SnowPadFrame;
import com.xiboliya.snowpad.util.Util;

/**
 * "首选项"对话框
 * 
 * @author 冰原
 * 
 */
public class PreferencesDialog extends BaseDialog implements ActionListener, ItemListener, MouseListener {
  private static final long serialVersionUID = 1L;
  // 编码格式名称的数组
  private static final String[] ENCODING_NAMES = new String[] {
      CharEncoding.GB18030.getName(), CharEncoding.ANSI.getName(), CharEncoding.UTF8.getName(),
      CharEncoding.UTF8_NO_BOM.getName(), CharEncoding.ULE.getName(), CharEncoding.UBE.getName() };
  // 编码格式数据的数组
  private static final String[] ENCODING_VALUES = new String[] {
      CharEncoding.GB18030.getName(), CharEncoding.ANSI.getName(), CharEncoding.UTF8.getName(),
      CharEncoding.UTF8_NO_BOM.getName(), CharEncoding.ULE.getName(), CharEncoding.UBE.getName() };
  // 换行符格式名称的数组
  private static final String[] LINE_SEPARATOR_VALUES = new String[] {
      LineSeparator.DEFAULT.getValue(), LineSeparator.UNIX.getValue(), LineSeparator.MACINTOSH.getValue(), LineSeparator.WINDOWS.getValue() };
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JTabbedPane tpnMain = new JTabbedPane();
  private CharEncoding charEncoding = CharEncoding.GB18030; // 字符编码格式
  private LineSeparator lineSeparator = LineSeparator.DEFAULT; // 换行符格式
  private Color[] colorStyle = Util.COLOR_STYLE_DEFAULT; // 配色方案
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private EtchedBorder etchedBorder = new EtchedBorder();
  // 新建
  private JPanel pnlNew = new JPanel();
  private JLabel lblEncoding = new JLabel("默认字符编码：");
  private JComboBox<String> cmbEncoding = new JComboBox<String>(ENCODING_NAMES);
  private JLabel lblLineSeparator = new JLabel("默认换行符：");
  private JComboBox<String> cmbLineSeparator = new JComboBox<String>(LINE_SEPARATOR_VALUES);
  // 颜色
  private JPanel pnlColor = new JPanel();
  private JLabel lblColorFont = new JLabel("字体颜色：");
  private JLabel lblColorFontView = new JLabel();
  private JLabel lblColorBack = new JLabel("背景颜色：");
  private JLabel lblColorBackView = new JLabel();
  private JLabel lblColorCaret = new JLabel("光标颜色：");
  private JLabel lblColorCaretView = new JLabel();
  private JLabel lblColorSelFont = new JLabel("选区字体颜色：");
  private JLabel lblColorSelFontView = new JLabel();
  private JLabel lblColorSelBack = new JLabel("选区背景颜色：");
  private JLabel lblColorSelBackView = new JLabel();
  private JLabel lblColorBracketBack = new JLabel("匹配括号背景颜色：");
  private JLabel lblColorBracketBackView = new JLabel();
  private JLabel lblColorLineBack = new JLabel("当前行背景颜色：");
  private JLabel lblColorLineBackView = new JLabel();
  private JLabel lblColorWordBack = new JLabel("匹配文本背景颜色：");
  private JLabel lblColorWordBackView = new JLabel();
  // 主界面
  private JPanel pnlBottom = new JPanel();
  private BaseButton btnOk = new BaseButton("确定");
  private BaseButton btnCancel = new BaseButton("取消");

  public PreferencesDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.setTitle("首选项");
    this.init();
    this.initView();
    this.setMnemonic();
    this.addListeners();
    this.setSize(420, 280);
    this.setVisible(true);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   */
  @Override
  public void setVisible(boolean visible) {
    if (visible) {
      this.initView();
    }
    super.setVisible(visible);
  }

  /**
   * 界面初始化
   */
  private void init() {
    this.pnlMain.setLayout(null);
    // 新建
    this.pnlNew.setLayout(null);
    this.lblEncoding.setBounds(10, 10, 100, Util.VIEW_HEIGHT);
    this.cmbEncoding.setBounds(110, 10, 150, Util.INPUT_HEIGHT);
    this.lblLineSeparator.setBounds(10, 50, 100, Util.VIEW_HEIGHT);
    this.cmbLineSeparator.setBounds(110, 50, 150, Util.INPUT_HEIGHT);
    this.pnlNew.add(this.lblEncoding);
    this.pnlNew.add(this.cmbEncoding);
    this.pnlNew.add(this.lblLineSeparator);
    this.pnlNew.add(this.cmbLineSeparator);
    // 颜色
    this.pnlColor.setLayout(null);
    this.lblColorFont.setBounds(10, 10, 100, Util.VIEW_HEIGHT);
    this.lblColorFontView.setBounds(110, 10, Util.VIEW_HEIGHT, Util.VIEW_HEIGHT);
    this.lblColorFontView.setBorder(this.etchedBorder);
    this.lblColorFontView.setOpaque(true);
    this.lblColorBack.setBounds(10, 40, 100, Util.VIEW_HEIGHT);
    this.lblColorBackView.setBounds(110, 40, Util.VIEW_HEIGHT, Util.VIEW_HEIGHT);
    this.lblColorBackView.setBorder(this.etchedBorder);
    this.lblColorBackView.setOpaque(true);
    this.lblColorCaret.setBounds(10, 70, 100, Util.VIEW_HEIGHT);
    this.lblColorCaretView.setBounds(110, 70, Util.VIEW_HEIGHT, Util.VIEW_HEIGHT);
    this.lblColorCaretView.setBorder(this.etchedBorder);
    this.lblColorCaretView.setOpaque(true);
    this.lblColorSelFont.setBounds(10, 100, 100, Util.VIEW_HEIGHT);
    this.lblColorSelFontView.setBounds(110, 100, Util.VIEW_HEIGHT, Util.VIEW_HEIGHT);
    this.lblColorSelFontView.setBorder(this.etchedBorder);
    this.lblColorSelFontView.setOpaque(true);
    this.lblColorSelBack.setBounds(10, 130, 100, Util.VIEW_HEIGHT);
    this.lblColorSelBackView.setBounds(110, 130, Util.VIEW_HEIGHT, Util.VIEW_HEIGHT);
    this.lblColorSelBackView.setBorder(this.etchedBorder);
    this.lblColorSelBackView.setOpaque(true);
    this.lblColorBracketBack.setBounds(200, 10, 120, Util.VIEW_HEIGHT);
    this.lblColorBracketBackView.setBounds(320, 10, Util.VIEW_HEIGHT, Util.VIEW_HEIGHT);
    this.lblColorBracketBackView.setBorder(this.etchedBorder);
    this.lblColorBracketBackView.setOpaque(true);
    this.lblColorLineBack.setBounds(200, 40, 120, Util.VIEW_HEIGHT);
    this.lblColorLineBackView.setBounds(320, 40, Util.VIEW_HEIGHT, Util.VIEW_HEIGHT);
    this.lblColorLineBackView.setBorder(this.etchedBorder);
    this.lblColorLineBackView.setOpaque(true);
    this.lblColorWordBack.setBounds(200, 70, 120, Util.VIEW_HEIGHT);
    this.lblColorWordBackView.setBounds(320, 70, Util.VIEW_HEIGHT, Util.VIEW_HEIGHT);
    this.lblColorWordBackView.setBorder(this.etchedBorder);
    this.lblColorWordBackView.setOpaque(true);
    this.pnlColor.add(this.lblColorFont);
    this.pnlColor.add(this.lblColorFontView);
    this.pnlColor.add(this.lblColorBack);
    this.pnlColor.add(this.lblColorBackView);
    this.pnlColor.add(this.lblColorCaret);
    this.pnlColor.add(this.lblColorCaretView);
    this.pnlColor.add(this.lblColorSelFont);
    this.pnlColor.add(this.lblColorSelFontView);
    this.pnlColor.add(this.lblColorSelBack);
    this.pnlColor.add(this.lblColorSelBackView);
    this.pnlColor.add(this.lblColorBracketBack);
    this.pnlColor.add(this.lblColorBracketBackView);
    this.pnlColor.add(this.lblColorLineBack);
    this.pnlColor.add(this.lblColorLineBackView);
    this.pnlColor.add(this.lblColorWordBack);
    this.pnlColor.add(this.lblColorWordBackView);
    // 主界面
    this.tpnMain.setBounds(0, 0, 420, 210);
    this.tpnMain.add(this.pnlNew, "新建");
    this.tpnMain.add(this.pnlColor, "颜色");
    this.pnlMain.add(this.tpnMain);
    this.tpnMain.setSelectedIndex(0);
    this.tpnMain.setFocusable(false);

    this.pnlBottom.setLayout(null);
    this.pnlBottom.setBounds(0, 210, 420, 70);
    this.btnOk.setBounds(90, 10, 90, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(240, 10, 90, Util.BUTTON_HEIGHT);
    this.pnlBottom.add(this.btnOk);
    this.pnlBottom.add(this.btnCancel);
    this.pnlMain.add(this.pnlBottom);
  }

  /**
   * 初始化各选项的状态
   */
  private void initView() {
    // 新建
    this.charEncoding = Util.setting.defaultCharEncoding;
    this.cmbEncoding.setSelectedItem(this.charEncoding.getName());
    this.lineSeparator = Util.setting.defaultLineSeparator;
    this.cmbLineSeparator.setSelectedItem(this.lineSeparator.getValue());
    // 颜色
    this.colorStyle = Util.setting.colorStyle.clone();
    this.lblColorFontView.setBackground(this.colorStyle[0]);
    this.lblColorFontView.repaint(); // 重绘标签，以解决在修改透明度颜色后，绘制标签背景错乱的问题
    this.lblColorBackView.setBackground(this.colorStyle[1]);
    this.lblColorBackView.repaint(); // 重绘标签，以解决在修改透明度颜色后，绘制标签背景错乱的问题
    this.lblColorCaretView.setBackground(this.colorStyle[2]);
    this.lblColorCaretView.repaint(); // 重绘标签，以解决在修改透明度颜色后，绘制标签背景错乱的问题
    this.lblColorSelFontView.setBackground(this.colorStyle[3]);
    this.lblColorSelFontView.repaint(); // 重绘标签，以解决在修改透明度颜色后，绘制标签背景错乱的问题
    this.lblColorSelBackView.setBackground(this.colorStyle[4]);
    this.lblColorSelBackView.repaint(); // 重绘标签，以解决在修改透明度颜色后，绘制标签背景错乱的问题
    this.lblColorBracketBackView.setBackground(this.colorStyle[5]);
    this.lblColorBracketBackView.repaint(); // 重绘标签，以解决在修改透明度颜色后，绘制标签背景错乱的问题
    this.lblColorLineBackView.setBackground(this.colorStyle[6]);
    this.lblColorLineBackView.repaint(); // 重绘标签，以解决在修改透明度颜色后，绘制标签背景错乱的问题
    this.lblColorWordBackView.setBackground(this.colorStyle[7]);
    this.lblColorWordBackView.repaint(); // 重绘标签，以解决在修改透明度颜色后，绘制标签背景错乱的问题
  }

  /**
   * 为各组件设置快捷键
   */
  private void setMnemonic() {
  }

  /**
   * 为各组件添加监听器
   */
  private void addListeners() {
    // 新建
    this.cmbEncoding.addKeyListener(this.keyAdapter);
    this.cmbEncoding.addItemListener(this);
    this.cmbLineSeparator.addKeyListener(this.keyAdapter);
    this.cmbLineSeparator.addItemListener(this);
    // 颜色
    this.lblColorFontView.addMouseListener(this);
    this.lblColorBackView.addMouseListener(this);
    this.lblColorCaretView.addMouseListener(this);
    this.lblColorSelFontView.addMouseListener(this);
    this.lblColorSelBackView.addMouseListener(this);
    this.lblColorBracketBackView.addMouseListener(this);
    this.lblColorLineBackView.addMouseListener(this);
    this.lblColorWordBackView.addMouseListener(this);
    // 主界面
    this.btnOk.addActionListener(this);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 设置字符编码格式
   */
  private void updateEncoding() {
    int index = this.cmbEncoding.getSelectedIndex();
    switch (index) {
      case 0:
        this.charEncoding = CharEncoding.GB18030;
        break;
      case 1:
        this.charEncoding = CharEncoding.ANSI;
        break;
      case 2:
        this.charEncoding = CharEncoding.UTF8;
        break;
      case 3:
        this.charEncoding = CharEncoding.UTF8_NO_BOM;
        break;
      case 4:
        this.charEncoding = CharEncoding.ULE;
        break;
      case 5:
        this.charEncoding = CharEncoding.UBE;
        break;
    }
  }

  /**
   * 设置换行符格式
   */
  private void updateLineSeparator() {
    int index = this.cmbLineSeparator.getSelectedIndex();
    switch (index) {
      case 0:
        this.lineSeparator = LineSeparator.DEFAULT;
        break;
      case 1:
        this.lineSeparator = LineSeparator.UNIX;
        break;
      case 2:
        this.lineSeparator = LineSeparator.MACINTOSH;
        break;
      case 3:
        this.lineSeparator = LineSeparator.WINDOWS;
        break;
    }
  }

  /**
   * "字体颜色"的处理方法
   */
  private void setFontColor() {
    Color color = JColorChooser.showDialog(this, "字体颜色", this.colorStyle[0]);
    if (color == null) {
      return;
    }
    this.lblColorFontView.setBackground(color);
    this.lblColorFontView.repaint(); // 重绘标签，以解决在修改透明度颜色后，绘制标签背景错乱的问题
    this.colorStyle[0] = color;
  }

  /**
   * "背景颜色"的处理方法
   */
  private void setBackColor() {
    Color color = JColorChooser.showDialog(this, "背景颜色", this.colorStyle[1]);
    if (color == null) {
      return;
    }
    this.lblColorBackView.setBackground(color);
    this.lblColorBackView.repaint(); // 重绘标签，以解决在修改透明度颜色后，绘制标签背景错乱的问题
    this.colorStyle[1] = color;
  }

  /**
   * "光标颜色"的处理方法
   */
  private void setCaretColor() {
    Color color = JColorChooser.showDialog(this, "光标颜色", this.colorStyle[2]);
    if (color == null) {
      return;
    }
    this.lblColorCaretView.setBackground(color);
    this.lblColorCaretView.repaint(); // 重绘标签，以解决在修改透明度颜色后，绘制标签背景错乱的问题
    this.colorStyle[2] = color;
  }

  /**
   * "选区字体颜色"的处理方法
   */
  private void setSelFontColor() {
    Color color = JColorChooser.showDialog(this, "选区字体颜色", this.colorStyle[3]);
    if (color == null) {
      return;
    }
    this.lblColorSelFontView.setBackground(color);
    this.lblColorSelFontView.repaint(); // 重绘标签，以解决在修改透明度颜色后，绘制标签背景错乱的问题
    this.colorStyle[3] = color;
  }

  /**
   * "选区背景颜色"的处理方法
   */
  private void setSelBackColor() {
    Color color = JColorChooser.showDialog(this, "选区背景颜色", this.colorStyle[4]);
    if (color == null) {
      return;
    }
    this.lblColorSelBackView.setBackground(color);
    this.lblColorSelBackView.repaint(); // 重绘标签，以解决在修改透明度颜色后，绘制标签背景错乱的问题
    this.colorStyle[4] = color;
  }

  /**
   * "匹配括号背景颜色"的处理方法
   */
  private void setBracketBackColor() {
    Color color = JColorChooser.showDialog(this, "匹配括号背景颜色", this.colorStyle[5]);
    if (color == null) {
      return;
    }
    this.lblColorBracketBackView.setBackground(color);
    this.lblColorBracketBackView.repaint(); // 重绘标签，以解决在修改透明度颜色后，绘制标签背景错乱的问题
    this.colorStyle[5] = color;
  }

  /**
   * "当前行背景颜色"的处理方法
   */
  private void setLineBackColor() {
    Color color = JColorChooser.showDialog(this, "当前行背景颜色", this.colorStyle[6]);
    if (color == null) {
      return;
    }
    this.lblColorLineBackView.setBackground(color);
    this.lblColorLineBackView.repaint(); // 重绘标签，以解决在修改透明度颜色后，绘制标签背景错乱的问题
    this.colorStyle[6] = color;
  }

  /**
   * "匹配文本背景颜色"的处理方法
   */
  private void setWordBackColor() {
    Color color = JColorChooser.showDialog(this, "匹配文本背景颜色", this.colorStyle[7]);
    if (color == null) {
      return;
    }
    this.lblColorWordBackView.setBackground(color);
    this.lblColorWordBackView.repaint(); // 重绘标签，以解决在修改透明度颜色后，绘制标签背景错乱的问题
    this.colorStyle[7] = color;
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    }
  }

  /**
   * 默认的“取消”操作方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }

  /**
   * 默认的“确定”操作方法
   */
  @Override
  public void onEnter() {
    Util.setting.defaultCharEncoding = this.charEncoding;
    Util.setting.defaultLineSeparator = this.lineSeparator;
    Util.setting.colorStyle = this.colorStyle;
    ((SnowPadFrame) this.getOwner()).refreshTextAreaColorStyle();
    this.dispose();
  }

  /**
   * 当用户已选定或取消选定某项时，触发此事件
   */
  @Override
  public void itemStateChanged(ItemEvent e) {
    Object source = e.getSource();
    if (this.cmbEncoding.equals(source)) {
      this.updateEncoding();
    } else if (this.cmbLineSeparator.equals(source)) {
      this.updateLineSeparator();
    }
  }

  /**
   * 当鼠标点击（按下并释放）时，触发此事件
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    Object source = e.getSource();
    if (this.lblColorFontView.equals(source)) {
      this.setFontColor();
    } else if (this.lblColorBackView.equals(source)) {
      this.setBackColor();
    } else if (this.lblColorCaretView.equals(source)) {
      this.setCaretColor();
    } else if (this.lblColorSelFontView.equals(source)) {
      this.setSelFontColor();
    } else if (this.lblColorSelBackView.equals(source)) {
      this.setSelBackColor();
    } else if (this.lblColorBracketBackView.equals(source)) {
      this.setBracketBackColor();
    } else if (this.lblColorLineBackView.equals(source)) {
      this.setLineBackColor();
    } else if (this.lblColorWordBackView.equals(source)) {
      this.setWordBackColor();
    }
  }

  /**
   * 当鼠标进入到组件上时，触发此事件
   */
  @Override
  public void mouseEntered(MouseEvent e) {
    
  }

  /**
   * 当鼠标离开组件时，触发此事件
   */
  @Override
  public void mouseExited(MouseEvent e) {
    
  }

  /**
   * 当鼠标按下时，触发此事件
   */
  @Override
  public void mousePressed(MouseEvent e) {
    
  }

  /**
   * 当鼠标释放时，触发此事件
   */
  @Override
  public void mouseReleased(MouseEvent e) {
    
  }
}
