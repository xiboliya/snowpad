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

import java.awt.Color;
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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.common.CharEncoding;
import com.xiboliya.snowpad.common.ColorStyle;
import com.xiboliya.snowpad.common.LineSeparator;
import com.xiboliya.snowpad.frame.SnowPadFrame;
import com.xiboliya.snowpad.util.Util;
import com.xiboliya.snowpad.view.ColorView;

/**
 * "首选项"对话框
 * 
 * @author 冰原
 * 
 */
public class PreferencesDialog extends BaseDialog implements ActionListener, ItemListener, MouseListener, ListSelectionListener {
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
  // 配色方案中各颜色名称的数组
  private static final String[] COLOR_NAMES = new String[] {
      "字体颜色", "背景颜色", "光标颜色", "选区字体颜色", "选区背景颜色",
      "匹配括号背景颜色", "当前行背景颜色", "匹配文本背景颜色", "标记文本背景颜色", "书签颜色",
      "行号栏字体颜色", "行号栏背景颜色" };
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JTabbedPane tpnMain = new JTabbedPane();
  private CharEncoding charEncoding = CharEncoding.GB18030; // 字符编码格式
  private LineSeparator lineSeparator = LineSeparator.DEFAULT; // 换行符格式
  private ColorStyle colorStyle = null; // 配色方案
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  // 新建
  private JPanel pnlNew = new JPanel();
  private JLabel lblEncoding = new JLabel("默认字符编码：");
  private JComboBox<String> cmbEncoding = new JComboBox<String>(ENCODING_NAMES);
  private JLabel lblLineSeparator = new JLabel("默认换行符：");
  private JComboBox<String> cmbLineSeparator = new JComboBox<String>(LINE_SEPARATOR_VALUES);
  // 颜色
  private JPanel pnlColor = new JPanel();
  private JLabel lblColorStyle = new JLabel("样式：");
  private JList<String> listColor = new JList<String>(COLOR_NAMES);
  private JScrollPane srpColor = new JScrollPane(this.listColor);
  private JLabel lblColor = new JLabel("颜色：");
  private ColorView colorView = new ColorView();
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
    this.lblColorStyle.setBounds(10, 10, 100, Util.VIEW_HEIGHT);
    this.srpColor.setBounds(10, 35, 140, 120);
    this.lblColor.setBounds(230, 55, 50, 50);
    this.colorView.setBounds(280, 55, 50, 50);
    this.pnlColor.add(this.lblColorStyle);
    this.pnlColor.add(this.srpColor);
    this.pnlColor.add(this.lblColor);
    this.pnlColor.add(this.colorView);
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
    this.listColor.setSelectedIndex(0);
    this.listColor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.refreshColorView();
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
    this.listColor.addListSelectionListener(this);
    this.listColor.addKeyListener(this.keyAdapter);
    this.colorView.addMouseListener(this);
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
   * 获取当前颜色
   */
  private Color getCurrentColor() {
    int index = this.listColor.getSelectedIndex();
    Color color = null;
    switch (index) {
      case 0:
        color = this.colorStyle.fontColor;
        break;
      case 1:
        color = this.colorStyle.backColor;
        break;
      case 2:
        color = this.colorStyle.caretColor;
        break;
      case 3:
        color = this.colorStyle.selFontColor;
        break;
      case 4:
        color = this.colorStyle.selBackColor;
        break;
      case 5:
        color = this.colorStyle.bracketBackColor;
        break;
      case 6:
        color = this.colorStyle.lineBackColor;
        break;
      case 7:
        color = this.colorStyle.wordBackColor;
        break;
      case 8:
        color = this.colorStyle.markBackColor;
        break;
      case 9:
        color = this.colorStyle.bookmarkColor;
        break;
      case 10:
        color = this.colorStyle.lineNumberViewFontColor;
        break;
      case 11:
        color = this.colorStyle.lineNumberViewBackColor;
        break;
    }
    return color;
  }

  /**
   * 缓存当前颜色
   */
  private void saveCurrentColor(Color color) {
    int index = this.listColor.getSelectedIndex();
    switch (index) {
      case 0:
        this.colorStyle.fontColor = color;
        break;
      case 1:
        this.colorStyle.backColor = color;
        break;
      case 2:
        this.colorStyle.caretColor = color;
        break;
      case 3:
        this.colorStyle.selFontColor = color;
        break;
      case 4:
        this.colorStyle.selBackColor = color;
        break;
      case 5:
        this.colorStyle.bracketBackColor = color;
        break;
      case 6:
        this.colorStyle.lineBackColor = color;
        break;
      case 7:
        this.colorStyle.wordBackColor = color;
        break;
      case 8:
        this.colorStyle.markBackColor = color;
        break;
      case 9:
        this.colorStyle.bookmarkColor = color;
        break;
      case 10:
        this.colorStyle.lineNumberViewFontColor = color;
        break;
      case 11:
        this.colorStyle.lineNumberViewBackColor = color;
        break;
    }
  }

  /**
   * 刷新当前颜色
   */
  private void refreshColorView() {
    Color color = this.getCurrentColor();
    this.colorView.setColor(color);
    // 滚动到列表中当前选择的项
    this.listColor.ensureIndexIsVisible(this.listColor.getSelectedIndex());
  }

  /**
   * 设置当前颜色
   */
  private void setCurrentColor() {
    String title = this.listColor.getSelectedValue();
    Color color = JColorChooser.showDialog(this, title, this.getCurrentColor());
    if (color == null) {
      return;
    }
    this.colorView.setColor(color);
    this.saveCurrentColor(color);
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
    if (this.colorView.equals(source)) {
      this.setCurrentColor();
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

  /**
   * 当列表框改变选择时，触发此事件
   */
  @Override
  public void valueChanged(ListSelectionEvent e) {
    Object source = e.getSource();
    if (this.listColor.equals(source)) {
      this.refreshColorView();
    }
  }
}
