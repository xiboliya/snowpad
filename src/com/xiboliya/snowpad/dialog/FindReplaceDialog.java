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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseRadioButton;
import com.xiboliya.snowpad.base.BaseTextArea;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.common.HighlightColorStyle;
import com.xiboliya.snowpad.common.SearchBean;
import com.xiboliya.snowpad.common.SearchResult;
import com.xiboliya.snowpad.common.SearchStyle;
import com.xiboliya.snowpad.frame.SnowPadFrame;
import com.xiboliya.snowpad.manager.ListenerManager;
import com.xiboliya.snowpad.panel.SearchResultPanel;
import com.xiboliya.snowpad.util.Util;
import com.xiboliya.snowpad.window.TipsWindow;
import com.xiboliya.snowpad.window.TipsWindow.Background;

/**
 * "查找"和"替换"对话框
 * 
 * @author 冰原
 * 
 */
public class FindReplaceDialog extends BaseDialog implements ActionListener,
    DocumentListener, ChangeListener, WindowFocusListener {
  private static final long serialVersionUID = 1L;
  private SearchResultPanel searchResultPanel; // 查找结果面板
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JTabbedPane tpnMain = new JTabbedPane();
  private boolean isFindDown = true; // 向下查找
  private boolean isMatchCase = true; // 区分大小写
  private boolean isMatchWholeWord = false; // 全词匹配
  private boolean isWrap = true; // 循环查找
  private SearchStyle searchStyle = SearchStyle.DEFAULT; // 搜索模式
  private boolean addBookmark = false; // 标记时添加书签
  private boolean clearBookmark = false; // 标记时清除已有书签
  private boolean clearMark = false; // 标记时清除已有标记
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private String strFind = ""; // 查找的字符串
  private Matcher matcher = null; // 通过解释Pattern对指定文本执行匹配操作的引擎
  // 查找
  private JPanel pnlFind = new JPanel();
  private JLabel lblFindTextF = new JLabel("查找内容：");
  private BaseTextField txtFindTextF = new BaseTextField();
  private JCheckBox chkMatchCaseF = new JCheckBox("区分大小写(C)", true);
  private JCheckBox chkMatchWholeWordF = new JCheckBox("全词匹配(O)", false);
  private JCheckBox chkIsWrapF = new JCheckBox("循环查找(W)", false);
  private BaseRadioButton radDefaultF = new BaseRadioButton("普通(E)", true);
  private BaseRadioButton radTransferF = new BaseRadioButton("转义扩展(T)", false);
  private BaseRadioButton radPatternF = new BaseRadioButton("正则表达式(P)", false);
  private BaseRadioButton radFindUpF = new BaseRadioButton("向上(U)", false);
  private BaseRadioButton radFindDownF = new BaseRadioButton("向下(D)", true);
  private BaseButton btnFindF = new BaseButton("查找(F)");
  private BaseButton btnCountAllF = new BaseButton("全部统计(N)");
  private BaseButton btnCountSelF = new BaseButton("选区内统计(S)");
  private BaseButton btnSearchInFileF = new BaseButton("文件中查找(I)");
  private BaseButton btnCopyResultLinesF = new BaseButton("复制结果行(L)");
  private BaseButton btnCutResultLinesF = new BaseButton("剪切结果行(X)");
  private BaseButton btnDelResultLinesF = new BaseButton("删除结果行(M)");
  private BaseButton btnCancelF = new BaseButton("取消");
  private ButtonGroup bgpSearchStyleF = new ButtonGroup();
  private ButtonGroup bgpFindUpDownF = new ButtonGroup();
  private JPanel pnlSearchStyleF = new JPanel(new GridLayout(3, 1));
  private JPanel pnlFindUpDownF = new JPanel(new GridLayout(2, 1));
  // 替换
  private JPanel pnlReplace = new JPanel();
  private JLabel lblFindTextR = new JLabel("查找内容：");
  private JLabel lblReplaceTextR = new JLabel("替换为：");
  private BaseTextField txtFindTextR = new BaseTextField();
  private BaseTextField txtReplaceTextR = new BaseTextField();
  private JCheckBox chkMatchCaseR = new JCheckBox("区分大小写(C)", true);
  private JCheckBox chkMatchWholeWordR = new JCheckBox("全词匹配(O)", false);
  private JCheckBox chkIsWrapR = new JCheckBox("循环查找(W)", false);
  private BaseRadioButton radDefaultR = new BaseRadioButton("普通(E)", true);
  private BaseRadioButton radTransferR = new BaseRadioButton("转义扩展(T)", false);
  private BaseRadioButton radPatternR = new BaseRadioButton("正则表达式(P)", false);
  private BaseRadioButton radFindUpR = new BaseRadioButton("向上(U)", false);
  private BaseRadioButton radFindDownR = new BaseRadioButton("向下(D)", true);
  private BaseButton btnFindR = new BaseButton("查找(F)");
  private BaseButton btnReplaceR = new BaseButton("替换(R)");
  private BaseButton btnReplaceAllR = new BaseButton("全部替换(A)");
  private BaseButton btnReplaceSelR = new BaseButton("选区内替换(S)");
  private BaseButton btnCancelR = new BaseButton("取消");
  private ButtonGroup bgpSearchStyleR = new ButtonGroup();
  private ButtonGroup bgpFindUpDownR = new ButtonGroup();
  private JPanel pnlSearchStyleR = new JPanel(new GridLayout(3, 1));
  private JPanel pnlFindUpDownR = new JPanel(new GridLayout(2, 1));
  // 标记
  private JPanel pnlMark = new JPanel();
  private JLabel lblFindTextM = new JLabel("查找内容：");
  private BaseTextField txtFindTextM = new BaseTextField();
  private JCheckBox chkAddBookmark = new JCheckBox("添加书签(R)", false);
  private JCheckBox chkClearBookmark = new JCheckBox("清除已有书签(L)", false);
  private JCheckBox chkClearMark = new JCheckBox("清除已有标记(A)", false);
  private JCheckBox chkMatchCaseM = new JCheckBox("区分大小写(C)", true);
  private JCheckBox chkMatchWholeWordM = new JCheckBox("全词匹配(O)", false);
  private BaseRadioButton radDefaultM = new BaseRadioButton("普通(E)", true);
  private BaseRadioButton radTransferM = new BaseRadioButton("转义扩展(T)", false);
  private BaseRadioButton radPatternM = new BaseRadioButton("正则表达式(P)", false);
  private BaseButton btnMark = new BaseButton("标记(M)");
  private BaseButton btnMarkSel = new BaseButton("选区内标记(S)");
  private BaseButton btnClearMark = new BaseButton("清除标记(K)");
  private BaseButton btnClearBookmark = new BaseButton("清除书签(B)");
  private BaseButton btnCancelM = new BaseButton("取消");
  private ButtonGroup bgpSearchStyleM = new ButtonGroup();
  private JPanel pnlSearchStyleM = new JPanel(new GridLayout(3, 1));

  public FindReplaceDialog(JFrame owner, boolean modal, BaseTextArea txaSource, SearchResultPanel searchResultPanel, boolean visible) {
    super(owner, modal, txaSource);
    this.searchResultPanel = searchResultPanel;
    this.setTitle("查找");
    this.init();
    this.initView();
    this.setMnemonic();
    this.addListeners();
    this.setSize(400, 350);
    this.setVisible(visible);
  }

  /**
   * 界面初始化
   */
  private void init() {
    // 查找
    this.pnlFind.setLayout(null);
    this.lblFindTextF.setBounds(10, 10, 70, Util.VIEW_HEIGHT);
    this.txtFindTextF.setBounds(80, 9, 180, Util.INPUT_HEIGHT);
    this.pnlFind.add(this.lblFindTextF);
    this.pnlFind.add(this.txtFindTextF);
    this.chkMatchCaseF.setBounds(10, 50, 110, Util.VIEW_HEIGHT);
    this.pnlFind.add(this.chkMatchCaseF);
    this.chkMatchWholeWordF.setBounds(10, 70, 110, Util.VIEW_HEIGHT);
    this.pnlFind.add(this.chkMatchWholeWordF);
    this.chkIsWrapF.setBounds(10, 90, 110, Util.VIEW_HEIGHT);
    this.pnlFind.add(this.chkIsWrapF);
    this.pnlFindUpDownF.setBounds(10, 120, 95, 70);
    this.pnlFindUpDownF.setBorder(new TitledBorder("方向"));
    this.pnlFindUpDownF.add(this.radFindUpF);
    this.pnlFindUpDownF.add(this.radFindDownF);
    this.pnlFind.add(this.pnlFindUpDownF);
    this.pnlSearchStyleF.setBounds(130, 100, 130, 90);
    this.pnlSearchStyleF.setBorder(new TitledBorder("模式"));
    this.pnlSearchStyleF.add(this.radDefaultF);
    this.pnlSearchStyleF.add(this.radTransferF);
    this.pnlSearchStyleF.add(this.radPatternF);
    this.radTransferF.setToolTipText("可使用\\n、\\t转义字符");
    this.pnlFind.add(this.pnlSearchStyleF);
    this.btnFindF.setEnabled(false);
    this.btnCountAllF.setEnabled(false);
    this.btnCountSelF.setEnabled(false);
    this.btnSearchInFileF.setEnabled(false);
    this.btnCopyResultLinesF.setEnabled(false);
    this.btnCutResultLinesF.setEnabled(false);
    this.btnDelResultLinesF.setEnabled(false);
    this.btnFindF.setBounds(270, 10, 110, Util.BUTTON_HEIGHT);
    this.btnCountAllF.setBounds(270, 45, 110, Util.BUTTON_HEIGHT);
    this.btnCountSelF.setBounds(270, 80, 110, Util.BUTTON_HEIGHT);
    this.btnSearchInFileF.setBounds(270, 115, 110, Util.BUTTON_HEIGHT);
    this.btnCopyResultLinesF.setBounds(270, 150, 110, Util.BUTTON_HEIGHT);
    this.btnCutResultLinesF.setBounds(270, 185, 110, Util.BUTTON_HEIGHT);
    this.btnDelResultLinesF.setBounds(270, 220, 110, Util.BUTTON_HEIGHT);
    this.btnCancelF.setBounds(270, 255, 110, Util.BUTTON_HEIGHT);
    this.pnlFind.add(this.btnFindF);
    this.pnlFind.add(this.btnCountAllF);
    this.pnlFind.add(this.btnCountSelF);
    this.pnlFind.add(this.btnSearchInFileF);
    this.pnlFind.add(this.btnCopyResultLinesF);
    this.pnlFind.add(this.btnCutResultLinesF);
    this.pnlFind.add(this.btnDelResultLinesF);
    this.pnlFind.add(this.btnCancelF);
    this.bgpSearchStyleF.add(this.radDefaultF);
    this.bgpSearchStyleF.add(this.radTransferF);
    this.bgpSearchStyleF.add(this.radPatternF);
    this.bgpFindUpDownF.add(this.radFindDownF);
    this.bgpFindUpDownF.add(this.radFindUpF);
    // 替换
    this.pnlReplace.setLayout(null);
    this.lblFindTextR.setBounds(10, 10, 70, Util.VIEW_HEIGHT);
    this.txtFindTextR.setBounds(80, 9, 180, Util.INPUT_HEIGHT);
    this.pnlReplace.add(this.lblFindTextR);
    this.pnlReplace.add(this.txtFindTextR);
    this.lblReplaceTextR.setBounds(10, 38, 70, Util.VIEW_HEIGHT);
    this.txtReplaceTextR.setBounds(80, 37, 180, Util.INPUT_HEIGHT);
    this.pnlReplace.add(this.lblReplaceTextR);
    this.pnlReplace.add(this.txtReplaceTextR);
    this.chkMatchCaseR.setBounds(10, 70, 110, Util.VIEW_HEIGHT);
    this.pnlReplace.add(this.chkMatchCaseR);
    this.chkMatchWholeWordR.setBounds(10, 90, 110, Util.VIEW_HEIGHT);
    this.pnlReplace.add(this.chkMatchWholeWordR);
    this.chkIsWrapR.setBounds(10, 110, 110, Util.VIEW_HEIGHT);
    this.pnlReplace.add(this.chkIsWrapR);
    this.pnlFindUpDownR.setBounds(10, 135, 95, 70);
    this.pnlFindUpDownR.setBorder(new TitledBorder("方向"));
    this.pnlFindUpDownR.add(this.radFindUpR);
    this.pnlFindUpDownR.add(this.radFindDownR);
    this.pnlReplace.add(this.pnlFindUpDownR);
    this.pnlSearchStyleR.setBounds(130, 115, 130, 90);
    this.pnlSearchStyleR.setBorder(new TitledBorder("模式"));
    this.pnlSearchStyleR.add(this.radDefaultR);
    this.pnlSearchStyleR.add(this.radTransferR);
    this.pnlSearchStyleR.add(this.radPatternR);
    this.radTransferR.setToolTipText("可使用\\n、\\t转义字符");
    this.pnlReplace.add(this.pnlSearchStyleR);
    this.btnFindR.setEnabled(false);
    this.btnReplaceR.setEnabled(false);
    this.btnReplaceAllR.setEnabled(false);
    this.btnReplaceSelR.setEnabled(false);
    this.btnFindR.setBounds(270, 10, 110, Util.BUTTON_HEIGHT);
    this.btnReplaceR.setBounds(270, 45, 110, Util.BUTTON_HEIGHT);
    this.btnReplaceAllR.setBounds(270, 80, 110, Util.BUTTON_HEIGHT);
    this.btnReplaceSelR.setBounds(270, 115, 110, Util.BUTTON_HEIGHT);
    this.btnCancelR.setBounds(270, 150, 110, Util.BUTTON_HEIGHT);
    this.pnlReplace.add(this.btnFindR);
    this.pnlReplace.add(this.btnReplaceR);
    this.pnlReplace.add(this.btnReplaceAllR);
    this.pnlReplace.add(this.btnReplaceSelR);
    this.pnlReplace.add(this.btnCancelR);
    this.bgpSearchStyleR.add(this.radDefaultR);
    this.bgpSearchStyleR.add(this.radTransferR);
    this.bgpSearchStyleR.add(this.radPatternR);
    this.bgpFindUpDownR.add(this.radFindDownR);
    this.bgpFindUpDownR.add(this.radFindUpR);
    // 标记
    this.pnlMark.setLayout(null);
    this.lblFindTextM.setBounds(10, 10, 70, Util.VIEW_HEIGHT);
    this.txtFindTextM.setBounds(80, 9, 180, Util.INPUT_HEIGHT);
    this.pnlMark.add(this.lblFindTextM);
    this.pnlMark.add(this.txtFindTextM);
    this.chkAddBookmark.setBounds(10, 50, 110, Util.VIEW_HEIGHT);
    this.pnlMark.add(this.chkAddBookmark);
    this.chkClearBookmark.setBounds(10, 70, 130, Util.VIEW_HEIGHT);
    this.pnlMark.add(this.chkClearBookmark);
    this.chkClearMark.setBounds(10, 90, 130, Util.VIEW_HEIGHT);
    this.pnlMark.add(this.chkClearMark);
    this.chkMatchCaseM.setBounds(10, 110, 110, Util.VIEW_HEIGHT);
    this.pnlMark.add(this.chkMatchCaseM);
    this.chkMatchWholeWordM.setBounds(10, 130, 110, Util.VIEW_HEIGHT);
    this.pnlMark.add(this.chkMatchWholeWordM);
    this.pnlSearchStyleM.setBounds(10, 160, 130, 90);
    this.pnlSearchStyleM.setBorder(new TitledBorder("模式"));
    this.pnlSearchStyleM.add(this.radDefaultM);
    this.pnlSearchStyleM.add(this.radTransferM);
    this.pnlSearchStyleM.add(this.radPatternM);
    this.radTransferM.setToolTipText("可使用\\n、\\t转义字符");
    this.pnlMark.add(this.pnlSearchStyleM);
    this.btnMark.setEnabled(false);
    this.btnMarkSel.setEnabled(false);
    this.btnMark.setBounds(270, 10, 110, Util.BUTTON_HEIGHT);
    this.btnMarkSel.setBounds(270, 45, 110, Util.BUTTON_HEIGHT);
    this.btnClearMark.setBounds(270, 80, 110, Util.BUTTON_HEIGHT);
    this.btnClearBookmark.setBounds(270, 115, 110, Util.BUTTON_HEIGHT);
    this.btnCancelM.setBounds(270, 150, 110, Util.BUTTON_HEIGHT);
    this.pnlMark.add(this.btnMark);
    this.pnlMark.add(this.btnMarkSel);
    this.pnlMark.add(this.btnClearMark);
    this.pnlMark.add(this.btnClearBookmark);
    this.pnlMark.add(this.btnCancelM);
    this.bgpSearchStyleM.add(this.radDefaultM);
    this.bgpSearchStyleM.add(this.radTransferM);
    this.bgpSearchStyleM.add(this.radPatternM);
    // 主界面
    this.tpnMain.add(this.pnlFind, "查找");
    this.tpnMain.add(this.pnlReplace, "替换");
    this.tpnMain.add(this.pnlMark, "标记");
    this.pnlMain.add(this.tpnMain, BorderLayout.CENTER);
    this.setTabbedIndex(0);
    this.tpnMain.setFocusable(false);
  }

  /**
   * 初始化各选项的状态
   */
  private void initView() {
    this.isMatchCase = Util.setting.matchCase;
    this.isMatchWholeWord = Util.setting.matchWholeWord;
    this.isWrap = Util.setting.isWrap;
    this.isFindDown = Util.setting.findDown;
    this.searchStyle = Util.setting.searchStyle;
    this.addBookmark = Util.setting.addBookmark;
    this.clearBookmark = Util.setting.clearBookmark;
    this.clearMark = Util.setting.clearMark;
    this.chkMatchCaseF.setSelected(this.isMatchCase);
    this.chkMatchCaseR.setSelected(this.isMatchCase);
    this.chkMatchCaseM.setSelected(this.isMatchCase);
    this.chkIsWrapF.setSelected(this.isWrap);
    this.chkIsWrapR.setSelected(this.isWrap);
    this.chkAddBookmark.setSelected(this.addBookmark);
    this.chkClearBookmark.setSelected(this.clearBookmark);
    this.chkClearMark.setSelected(this.clearMark);
    if (this.isFindDown) {
      this.radFindDownF.setSelected(true);
      this.radFindDownR.setSelected(true);
    } else {
      this.radFindUpF.setSelected(true);
      this.radFindUpR.setSelected(true);
    }
    switch (this.searchStyle) {
    case DEFAULT:
      this.radDefaultF.setSelected(true);
      this.radDefaultR.setSelected(true);
      this.radDefaultM.setSelected(true);
      break;
    case TRANSFER:
      this.radTransferF.setSelected(true);
      this.radTransferR.setSelected(true);
      this.radTransferM.setSelected(true);
      break;
    case PATTERN:
      this.radPatternF.setSelected(true);
      this.radPatternR.setSelected(true);
      this.radPatternM.setSelected(true);
      break;
    }
    this.refreshMatchWholeWord();
  }

  /**
   * 刷新全词匹配复选框显示
   */
  private void refreshMatchWholeWord() {
    // 当查找模式为正则表达式时，全词匹配功能不可用
    if (this.searchStyle == SearchStyle.PATTERN) {
      this.isMatchWholeWord = false;
      this.chkMatchWholeWordF.setEnabled(false);
      this.chkMatchWholeWordR.setEnabled(false);
      this.chkMatchWholeWordM.setEnabled(false);
      this.chkMatchWholeWordF.setSelected(false);
      this.chkMatchWholeWordR.setSelected(false);
      this.chkMatchWholeWordM.setSelected(false);
    } else {
      this.isMatchWholeWord = Util.setting.matchWholeWord;
      this.chkMatchWholeWordF.setEnabled(true);
      this.chkMatchWholeWordR.setEnabled(true);
      this.chkMatchWholeWordM.setEnabled(true);
      this.chkMatchWholeWordF.setSelected(this.isMatchWholeWord);
      this.chkMatchWholeWordR.setSelected(this.isMatchWholeWord);
      this.chkMatchWholeWordM.setSelected(this.isMatchWholeWord);
    }
  }

  /**
   * 设置选项卡的当前视图
   * 
   * @param index 视图的索引号
   */
  public void setTabbedIndex(int index) {
    this.tpnMain.setSelectedIndex(index);
  }

  /**
   * 获取选项卡当前视图的索引号
   * 
   * @return 当前视图的索引号
   */
  public int getTabbedIndex() {
    return this.tpnMain.getSelectedIndex();
  }

  /**
   * 选中“查找内容”中的文本
   */
  public void setFindTextSelect() {
    int tabbedIndex = this.getTabbedIndex();
    if (tabbedIndex == 0) {
      this.txtFindTextF.selectAll();
    } else if (tabbedIndex == 1) {
      this.txtFindTextR.selectAll();
    } else {
      this.txtFindTextM.selectAll();
    }
  }

  /**
   * 获取查找的字符串
   * 
   * @return 查找的字符串
   */
  public String getFindText() {
    return this.strFind;
  }

  /**
   * 设置查找的字符串
   * 
   * @param strFind 查找的字符串
   * @param isUpdate 是否同步更新文本框的显示，更新为true，不更新为false
   */
  public void setFindText(String strFind, boolean isUpdate) {
    this.strFind = strFind;
    if (isUpdate) {
      this.txtFindTextF.setText(strFind);
      this.txtFindTextR.setText(strFind);
      this.txtFindTextM.setText(strFind);
      if (this.getTabbedIndex() == 0) {
        this.txtFindTextF.selectAll();
      } else if (this.getTabbedIndex() == 1) {
        this.txtFindTextR.selectAll();
      } else if (this.getTabbedIndex() == 2) {
        this.txtFindTextM.selectAll();
      }
    }
  }

  /**
   * 获取用于替换的字符串
   * 
   * @return 用于替换的字符串
   */
  public String getReplaceText() {
    return this.txtReplaceTextR.getText();
  }

  /**
   * 设置用于替换的字符串
   * 
   * @param strReplace 用于替换的字符串
   */
  public void setReplaceText(String strReplace) {
    this.txtReplaceTextR.setText(strReplace);
  }

  /**
   * 为各组件设置快捷键
   */
  private void setMnemonic() {
    // 查找
    this.chkMatchCaseF.setMnemonic('C');
    this.chkMatchWholeWordF.setMnemonic('O');
    this.chkIsWrapF.setMnemonic('W');
    this.radDefaultF.setMnemonic('E');
    this.radTransferF.setMnemonic('T');
    this.radPatternF.setMnemonic('P');
    this.btnFindF.setMnemonic('F');
    this.btnCountAllF.setMnemonic('N');
    this.btnCountSelF.setMnemonic('S');
    this.btnSearchInFileF.setMnemonic('I');
    this.btnCopyResultLinesF.setMnemonic('L');
    this.btnCutResultLinesF.setMnemonic('X');
    this.btnDelResultLinesF.setMnemonic('M');
    this.radFindUpF.setMnemonic('U');
    this.radFindDownF.setMnemonic('D');
    // 替换
    this.chkMatchCaseR.setMnemonic('C');
    this.chkMatchWholeWordR.setMnemonic('O');
    this.chkIsWrapR.setMnemonic('W');
    this.radDefaultR.setMnemonic('E');
    this.radTransferR.setMnemonic('T');
    this.radPatternR.setMnemonic('P');
    this.btnFindR.setMnemonic('F');
    this.btnReplaceR.setMnemonic('R');
    this.btnReplaceAllR.setMnemonic('A');
    this.btnReplaceSelR.setMnemonic('S');
    this.radFindUpR.setMnemonic('U');
    this.radFindDownR.setMnemonic('D');
    // 标记
    this.chkAddBookmark.setMnemonic('R');
    this.chkClearBookmark.setMnemonic('L');
    this.chkClearMark.setMnemonic('A');
    this.chkMatchCaseM.setMnemonic('C');
    this.chkMatchWholeWordM.setMnemonic('O');
    this.radDefaultM.setMnemonic('E');
    this.radTransferM.setMnemonic('T');
    this.radPatternM.setMnemonic('P');
    this.btnMark.setMnemonic('M');
    this.btnMarkSel.setMnemonic('S');
    this.btnClearMark.setMnemonic('K');
    this.btnClearBookmark.setMnemonic('B');
  }

  /**
   * 为各组件添加监听器
   */
  private void addListeners() {
    this.tpnMain.addChangeListener(this);
    // 查找
    this.txtFindTextF.getDocument().addDocumentListener(this);
    this.btnFindF.addActionListener(this);
    this.btnCountAllF.addActionListener(this);
    this.btnCountSelF.addActionListener(this);
    this.btnSearchInFileF.addActionListener(this);
    this.btnCopyResultLinesF.addActionListener(this);
    this.btnCutResultLinesF.addActionListener(this);
    this.btnDelResultLinesF.addActionListener(this);
    this.btnCancelF.addActionListener(this);
    this.radFindDownF.addActionListener(this);
    this.radFindUpF.addActionListener(this);
    this.chkMatchCaseF.addActionListener(this);
    this.chkMatchWholeWordF.addActionListener(this);
    this.chkIsWrapF.addActionListener(this);
    this.radDefaultF.addActionListener(this);
    this.radTransferF.addActionListener(this);
    this.radPatternF.addActionListener(this);
    this.txtFindTextF.addKeyListener(this.keyAdapter);
    this.chkMatchCaseF.addKeyListener(this.keyAdapter);
    this.chkMatchWholeWordF.addKeyListener(this.keyAdapter);
    this.chkIsWrapF.addKeyListener(this.keyAdapter);
    this.radDefaultF.addKeyListener(this.keyAdapter);
    this.radTransferF.addKeyListener(this.keyAdapter);
    this.radPatternF.addKeyListener(this.keyAdapter);
    this.radFindDownF.addKeyListener(this.keyAdapter);
    this.radFindUpF.addKeyListener(this.keyAdapter);
    this.btnCancelF.addKeyListener(this.buttonKeyAdapter);
    this.btnFindF.addKeyListener(this.buttonKeyAdapter);
    this.btnCountAllF.addKeyListener(this.buttonKeyAdapter);
    this.btnCountSelF.addKeyListener(this.buttonKeyAdapter);
    this.btnSearchInFileF.addKeyListener(this.buttonKeyAdapter);
    this.btnCopyResultLinesF.addKeyListener(this.buttonKeyAdapter);
    this.btnCutResultLinesF.addKeyListener(this.buttonKeyAdapter);
    this.btnDelResultLinesF.addKeyListener(this.buttonKeyAdapter);
    // 替换
    this.txtFindTextR.getDocument().addDocumentListener(this);
    this.btnFindR.addActionListener(this);
    this.btnReplaceR.addActionListener(this);
    this.btnReplaceAllR.addActionListener(this);
    this.btnReplaceSelR.addActionListener(this);
    this.btnCancelR.addActionListener(this);
    this.radFindDownR.addActionListener(this);
    this.radFindUpR.addActionListener(this);
    this.chkMatchCaseR.addActionListener(this);
    this.chkMatchWholeWordR.addActionListener(this);
    this.chkIsWrapR.addActionListener(this);
    this.radDefaultR.addActionListener(this);
    this.radTransferR.addActionListener(this);
    this.radPatternR.addActionListener(this);
    this.txtFindTextR.addKeyListener(this.keyAdapter);
    this.txtReplaceTextR.addKeyListener(this.keyAdapter);
    this.chkMatchCaseR.addKeyListener(this.keyAdapter);
    this.chkMatchWholeWordR.addKeyListener(this.keyAdapter);
    this.chkIsWrapR.addKeyListener(this.keyAdapter);
    this.radDefaultR.addKeyListener(this.keyAdapter);
    this.radTransferR.addKeyListener(this.keyAdapter);
    this.radPatternR.addKeyListener(this.keyAdapter);
    this.radFindDownR.addKeyListener(this.keyAdapter);
    this.radFindUpR.addKeyListener(this.keyAdapter);
    this.btnCancelR.addKeyListener(this.buttonKeyAdapter);
    this.btnReplaceR.addKeyListener(this.buttonKeyAdapter);
    this.btnFindR.addKeyListener(this.buttonKeyAdapter);
    this.btnReplaceAllR.addKeyListener(this.buttonKeyAdapter);
    this.btnReplaceSelR.addKeyListener(this.buttonKeyAdapter);
    // 标记
    this.txtFindTextM.getDocument().addDocumentListener(this);
    this.btnMark.addActionListener(this);
    this.btnMarkSel.addActionListener(this);
    this.btnClearMark.addActionListener(this);
    this.btnClearBookmark.addActionListener(this);
    this.btnCancelM.addActionListener(this);
    this.chkAddBookmark.addActionListener(this);
    this.chkClearBookmark.addActionListener(this);
    this.chkClearMark.addActionListener(this);
    this.chkMatchCaseM.addActionListener(this);
    this.chkMatchWholeWordM.addActionListener(this);
    this.radDefaultM.addActionListener(this);
    this.radTransferM.addActionListener(this);
    this.radPatternM.addActionListener(this);
    this.txtFindTextM.addKeyListener(this.keyAdapter);
    this.chkAddBookmark.addKeyListener(this.keyAdapter);
    this.chkClearBookmark.addKeyListener(this.keyAdapter);
    this.chkClearMark.addKeyListener(this.keyAdapter);
    this.chkMatchCaseM.addKeyListener(this.keyAdapter);
    this.chkMatchWholeWordM.addKeyListener(this.keyAdapter);
    this.radDefaultM.addKeyListener(this.keyAdapter);
    this.radTransferM.addKeyListener(this.keyAdapter);
    this.radPatternM.addKeyListener(this.keyAdapter);
    this.btnMark.addKeyListener(this.buttonKeyAdapter);
    this.btnMarkSel.addKeyListener(this.buttonKeyAdapter);
    this.btnClearMark.addKeyListener(this.buttonKeyAdapter);
    this.btnClearBookmark.addKeyListener(this.buttonKeyAdapter);
    this.btnCancelM.addKeyListener(this.buttonKeyAdapter);
    // 为窗口添加焦点监听器
    this.addWindowFocusListener(this);
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (this.btnCancelF.equals(source)) { // 查找
      this.onCancel();
    } else if (this.btnFindF.equals(source)) {
      this.onEnter();
    } else if (this.btnCountAllF.equals(source)) {
      this.getTextCountAll();
    } else if (this.btnCountSelF.equals(source)) {
      this.getTextCountSel();
    } else if (this.btnSearchInFileF.equals(source)) {
      this.searchInFile();
    } else if (this.btnCopyResultLinesF.equals(source)) {
      this.copyResultLines();
    } else if (this.btnCutResultLinesF.equals(source)) {
      this.cutResultLines();
    } else if (this.btnDelResultLinesF.equals(source)) {
      this.delResultLines();
    } else if (this.chkMatchCaseF.equals(source)) {
      Util.setting.matchCase = this.isMatchCase = this.chkMatchCaseF.isSelected();
      this.chkMatchCaseR.setSelected(this.isMatchCase);
      this.chkMatchCaseM.setSelected(this.isMatchCase);
    } else if (this.chkMatchWholeWordF.equals(source)) {
      Util.setting.matchWholeWord = this.isMatchWholeWord = this.chkMatchWholeWordF.isSelected();
      this.chkMatchWholeWordR.setSelected(this.isMatchWholeWord);
      this.chkMatchWholeWordM.setSelected(this.isMatchWholeWord);
    } else if (this.chkIsWrapF.equals(source)) {
      boolean selected = this.chkIsWrapF.isSelected();
      Util.setting.isWrap = this.isWrap = selected;
      this.chkIsWrapR.setSelected(selected);
    } else if (this.radDefaultF.equals(source)) {
      this.radDefaultR.setSelected(true);
      this.radDefaultM.setSelected(true);
      Util.setting.searchStyle = this.searchStyle = SearchStyle.DEFAULT;
      this.refreshMatchWholeWord();
    } else if (this.radTransferF.equals(source)) {
      this.radTransferR.setSelected(true);
      this.radTransferM.setSelected(true);
      Util.setting.searchStyle = this.searchStyle = SearchStyle.TRANSFER;
      this.refreshMatchWholeWord();
    } else if (this.radPatternF.equals(source)) {
      this.radPatternR.setSelected(true);
      this.radPatternM.setSelected(true);
      Util.setting.searchStyle = this.searchStyle = SearchStyle.PATTERN;
      this.refreshMatchWholeWord();
    } else if (this.radFindDownF.equals(source)) {
      Util.setting.findDown = this.isFindDown = true;
      this.radFindDownR.setSelected(true);
    } else if (this.radFindUpF.equals(source)) {
      Util.setting.findDown = this.isFindDown = false;
      this.radFindUpR.setSelected(true);
    } else if (this.btnCancelR.equals(source)) { // 替换
      this.onCancel();
    } else if (this.btnFindR.equals(source)) {
      this.findText(this.isFindDown);
    } else if (this.btnReplaceR.equals(source)) {
      this.onEnter();
    } else if (this.btnReplaceAllR.equals(source)) {
      this.replaceAllText();
    } else if (this.btnReplaceSelR.equals(source)) {
      this.replaceSelText();
    } else if (this.chkMatchCaseR.equals(source)) {
      Util.setting.matchCase = this.isMatchCase = this.chkMatchCaseR.isSelected();
      this.chkMatchCaseF.setSelected(this.isMatchCase);
      this.chkMatchCaseM.setSelected(this.isMatchCase);
    } else if (this.chkMatchWholeWordR.equals(source)) {
      Util.setting.matchWholeWord = this.isMatchWholeWord = this.chkMatchWholeWordR.isSelected();
      this.chkMatchWholeWordF.setSelected(this.isMatchWholeWord);
      this.chkMatchWholeWordM.setSelected(this.isMatchWholeWord);
    } else if (this.chkIsWrapR.equals(source)) {
      boolean selected = this.chkIsWrapR.isSelected();
      Util.setting.isWrap = this.isWrap = selected;
      this.chkIsWrapF.setSelected(selected);
    } else if (this.radDefaultR.equals(source)) {
      this.radDefaultF.setSelected(true);
      Util.setting.searchStyle = this.searchStyle = SearchStyle.DEFAULT;
      this.refreshMatchWholeWord();
    } else if (this.radTransferR.equals(source)) {
      this.radTransferF.setSelected(true);
      Util.setting.searchStyle = this.searchStyle = SearchStyle.TRANSFER;
      this.refreshMatchWholeWord();
    } else if (this.radPatternR.equals(source)) {
      this.radPatternF.setSelected(true);
      Util.setting.searchStyle = this.searchStyle = SearchStyle.PATTERN;
      this.refreshMatchWholeWord();
    } else if (this.radFindDownR.equals(source)) {
      Util.setting.findDown = this.isFindDown = true;
      this.radFindDownF.setSelected(true);
    } else if (this.radFindUpR.equals(source)) {
      Util.setting.findDown = this.isFindDown = false;
      this.radFindUpF.setSelected(true);
    } else if (this.btnCancelM.equals(source)) { // 标记
      this.onCancel();
    } else if (this.chkMatchCaseM.equals(source)) {
      Util.setting.matchCase = this.isMatchCase = this.chkMatchCaseM.isSelected();
      this.chkMatchCaseF.setSelected(this.isMatchCase);
      this.chkMatchCaseR.setSelected(this.isMatchCase);
    } else if (this.chkMatchWholeWordM.equals(source)) {
      Util.setting.matchWholeWord = this.isMatchWholeWord = this.chkMatchWholeWordM.isSelected();
      this.chkMatchWholeWordF.setSelected(this.isMatchWholeWord);
      this.chkMatchWholeWordR.setSelected(this.isMatchWholeWord);
    } else if (this.radDefaultM.equals(source)) {
      this.radDefaultF.setSelected(true);
      this.radDefaultR.setSelected(true);
      Util.setting.searchStyle = this.searchStyle = SearchStyle.DEFAULT;
      this.refreshMatchWholeWord();
    } else if (this.radTransferM.equals(source)) {
      this.radTransferF.setSelected(true);
      this.radTransferR.setSelected(true);
      Util.setting.searchStyle = this.searchStyle = SearchStyle.TRANSFER;
      this.refreshMatchWholeWord();
    } else if (this.radPatternM.equals(source)) {
      this.radPatternF.setSelected(true);
      this.radPatternR.setSelected(true);
      Util.setting.searchStyle = this.searchStyle = SearchStyle.PATTERN;
      this.refreshMatchWholeWord();
    } else if (this.chkAddBookmark.equals(source)) {
      Util.setting.addBookmark = this.addBookmark = this.chkAddBookmark.isSelected();
    } else if (this.chkClearBookmark.equals(source)) {
      Util.setting.clearBookmark = this.clearBookmark = this.chkClearBookmark.isSelected();
    } else if (this.chkClearMark.equals(source)) {
      Util.setting.clearMark = this.clearMark = this.chkClearMark.isSelected();
    } else if (this.btnMark.equals(source)) {
      this.mark();
    } else if (this.btnMarkSel.equals(source)) {
      this.markSel();
    } else if (this.btnClearMark.equals(source)) {
      this.clearMark();
    } else if (this.btnClearBookmark.equals(source)) {
      this.clearBookmark();
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
    if (this.tpnMain.getSelectedIndex() == 0) {
      this.findText(this.isFindDown);
    } else {
      this.replaceText();
    }
  }

  /**
   * 查找字符串
   * 
   * @param isFindDown 查找的方向，如果向下查找则为true，反之则为false
   * @return 查找结果，如果查找成功返回true，反之则为false
   */
  public boolean findText(boolean isFindDown) {
    if (!Util.isTextEmpty(this.strFind)) {
      int index = Util.findText(this.strFind, this.txaSource, -1, isFindDown,
          this.isMatchCase, this.isMatchWholeWord, this.isWrap, this.searchStyle);
      if (index >= 0) {
        if (this.searchStyle == SearchStyle.PATTERN) {
          this.txaSource.select(index, index + Util.matcher_length);
        } else if (this.searchStyle == SearchStyle.TRANSFER) {
          this.txaSource.select(index, index + this.strFind.length() - Util.transfer_count);
        } else {
          this.txaSource.select(index, index + this.strFind.length());
        }
        return true;
      } else if (index == Util.PATTERN_SYNTAX_ERROR_INDEX) {
        this.showMessageDialog("正则表达式语法错误，请修改！");
      } else {
        this.showMessageDialog("找不到\"" + this.strFind + "\"");
      }
    }
    return false;
  }

  /**
   * 弹出提示框，针对当前窗口不显示的情况下，做兼容处理
   * 
   * @param message 提示框显示文本
   */
  private void showMessageDialog(String message) {
    if (Util.isTextEmpty(message)) {
      return;
    }
    if (this.isVisible()) {
      JOptionPane.showMessageDialog(this, message, Util.SOFTWARE, JOptionPane.NO_OPTION);
    } else {
      // 第一个参数不能是this.txaSource，当this.txaSource显示滚动条的时候，就会导致提示框位置位于屏幕底部
      JOptionPane.showMessageDialog(this.getOwner(), message, Util.SOFTWARE, JOptionPane.NO_OPTION);
    }
  }

  /**
   * 单次替换字符串
   */
  private void replaceText() {
    boolean isEquals = false;
    String strSel = this.txaSource.getSelectedText();
    String strFindTemp = this.txtFindTextR.getText();
    String strReplaceTemp = this.txtReplaceTextR.getText();
    if (strSel != null) {
      if (this.searchStyle == SearchStyle.PATTERN) {
        strReplaceTemp = Util.transfer(strReplaceTemp);
      } else if (this.searchStyle == SearchStyle.TRANSFER) {
        strFindTemp = Util.transfer(strFindTemp);
        strReplaceTemp = Util.transfer(strReplaceTemp);
      }
      if (!this.isMatchCase) {
        if (this.searchStyle == SearchStyle.PATTERN) {
          if (strSel.matches("(?i)" + strFindTemp)) { // 正则表达式中，可用(?i)打开不区分大小写的属性
            isEquals = true;
          }
        } else if (strSel.equalsIgnoreCase(strFindTemp)) {
          isEquals = true;
        }
      } else {
        if (this.searchStyle == SearchStyle.PATTERN) {
          if (strSel.matches(strFindTemp)) {
            isEquals = true;
          }
        } else if (strSel.equals(strFindTemp)) {
          isEquals = true;
        }
      }
    }
    if (isEquals) {
      this.txaSource.replaceSelection(strReplaceTemp);
    }
    this.findText(this.isFindDown);
  }

  /**
   * 多次替换字符串
   * 
   * @param isSel 是否针对选区操作，true表示选区操作，false表示全文操作
   */
  private void replaceText(boolean isSel) {
    String str = this.txaSource.getText();
    int selStart = this.txaSource.getSelectionStart();
    if (isSel) {
      str = this.txaSource.getSelectedText();
    }
    String strFindText = this.txtFindTextR.getText();
    String strReplaceText = this.txtReplaceTextR.getText();
    StringBuilder stbTextAll = new StringBuilder(str);
    StringBuilder stbTextAllTemp = new StringBuilder(stbTextAll);
    if (this.searchStyle == SearchStyle.PATTERN) {
      strReplaceText = Util.transfer(strReplaceText);
    } else if (this.searchStyle == SearchStyle.TRANSFER) {
      strFindText = Util.transfer(strFindText);
      strReplaceText = Util.transfer(strReplaceText);
    }
    StringBuilder stbFindTextTemp = new StringBuilder(strFindText);
    if (!Util.isTextEmpty(strFindText)) {
      int caretPos = 0; // 当前从哪个索引值开始搜索字符串
      int times = 0; // 循环次数
      int oldPos = this.txaSource.getCaretPosition(); // 替换之前文本域的插入点位置
      int newPos = oldPos; // 替换之后的插入点位置
      int offset = strFindText.length() - strReplaceText.length(); // 查找与替换的字符串长度的差值
      if (!this.isMatchCase) {
        if (this.searchStyle == SearchStyle.PATTERN) {
          stbFindTextTemp = new StringBuilder("(?i)" + stbFindTextTemp); // 正则表达式中，可用(?i)打开不区分大小写的属性
        } else {
          stbFindTextTemp = new StringBuilder(stbFindTextTemp.toString().toLowerCase());
        }
        stbTextAllTemp = new StringBuilder(stbTextAllTemp.toString().toLowerCase());
      }
      if (this.searchStyle == SearchStyle.PATTERN) {
        try {
          this.matcher = Pattern.compile(stbFindTextTemp.toString()).matcher(stbTextAll);
        } catch (PatternSyntaxException x) {
          TipsWindow.show(this, "正则表达式语法错误，请修改！");
          return;
        }
        int length = 0;
        while (this.matcher.find()) {
          if (this.matcher.end() < oldPos) {
            length = this.matcher.end() - this.matcher.start();
            offset = length - strReplaceText.length();
            newPos -= offset;
          }
          times++;
        }
        if (times > 0) {
          stbTextAll = new StringBuilder(this.matcher.replaceAll(strReplaceText));
        }
      } else {
        int findLength = stbFindTextTemp.length();
        int replaceLength = strReplaceText.length();
        int indexTemp = 0;
        for (int index = 0; caretPos >= 0; times++) {
          index = stbTextAllTemp.indexOf(stbFindTextTemp.toString(), caretPos);
          if (index >= 0) {
            indexTemp = index - (findLength - replaceLength) * times;
            stbTextAll.replace(indexTemp, indexTemp + findLength, strReplaceText);
            caretPos = index + findLength;
            if (caretPos < oldPos) {
              newPos -= offset;
            }
          } else {
            break;
          }
        }
      }
      if (times > 0) {
        if (isSel) {
          this.txaSource.replaceSelection(stbTextAll.toString());
          this.txaSource.select(selStart, selStart + stbTextAll.length());
        } else {
          this.txaSource.setText(stbTextAll.toString());
          this.txaSource.setCaretPosition(Util.checkCaretPosition(this.txaSource, newPos));
        }
        TipsWindow.show(this, "共替换 " + times + " 处。", Background.GREEN);
      } else {
        JOptionPane.showMessageDialog(this, "找不到\"" + this.strFind + "\"", Util.SOFTWARE, JOptionPane.NO_OPTION);
      }
    }
  }

  /**
   * 替换所有字符串
   */
  private void replaceAllText() {
    this.replaceText(false);
  }

  /**
   * 替换选区字符串
   */
  private void replaceSelText() {
    this.replaceText(true);
  }

  /**
   * "全部统计"的处理方法
   */
  private void getTextCountAll() {
    int times = this.getTextCount(this.txaSource.getText());
    this.showTextCountResult(times);
  }

  /**
   * "选区内统计"的处理方法
   */
  private void getTextCountSel() {
    int times = this.getTextCount(this.txaSource.getSelectedText());
    this.showTextCountResult(times);
  }

  /**
   * 提示统计到的字符串次数
   * 
   * @param times 字符串次数
   */
  private void showTextCountResult(int times) {
    if (times > 0) {
      TipsWindow.show(this, "共找到 " + times + " 处。", TipsWindow.Background.GREEN);
    } else if(times == 0) {
      TipsWindow.show(this, "共找到 " + times + " 处。", TipsWindow.Background.PINK);
    }
  }

  /**
   * 在给定的文本中统计字符串次数
   * 
   * @param strText 给定的文本
   * @return 字符串出现次数
   */
  private int getTextCount(String strText) {
    int times = 0; // 字符串出现次数
    if (Util.isTextEmpty(this.strFind) || Util.isTextEmpty(strText)) {
      return times;
    }
    String strFindTemp = this.strFind;
    if (this.searchStyle == SearchStyle.TRANSFER) {
      strFindTemp = Util.transfer(strFindTemp);
    }
    if (!this.isMatchCase) {
      if (this.searchStyle == SearchStyle.PATTERN) {
        strFindTemp = "(?i)" + strFindTemp; // 正则表达式中，可用(?i)打开不区分大小写的属性
      } else {
        strFindTemp = strFindTemp.toLowerCase();
      }
      strText = strText.toLowerCase();
    }
    if (this.searchStyle == SearchStyle.PATTERN) {
      try {
        this.matcher = Pattern.compile(strFindTemp).matcher(strText);
      } catch (PatternSyntaxException x) {
        TipsWindow.show(this, "正则表达式语法错误，请修改！");
        return -1;
      }
      while (this.matcher.find()) {
        times++;
      }
    } else {
      int index = strText.indexOf(strFindTemp);
      while (index >= 0) {
        if (this.isMatchWholeWord) {
          if (Util.isMatchWholeWord(strText, index, strFindTemp.length())) {
            times++;
          }
        } else {
          times++;
        }
        index = strText.indexOf(strFindTemp, index + strFindTemp.length());
      }
    }
    return times;
  }

  /**
   * “文件中查找”的处理方法
   */
  private void searchInFile() {
    LinkedList<SearchBean> listIndex = new LinkedList<SearchBean>();
    int index = 0;
    if (!Util.isTextEmpty(this.strFind)) {
      while (index >= 0) {
        index = Util.findText(this.strFind, this.txaSource, index,
            true, this.isMatchCase, this.isMatchWholeWord, false, this.searchStyle);
        if (index >= 0) {
          SearchBean searchBean = new SearchBean();
          searchBean.setStart(index);
          if (this.searchStyle == SearchStyle.PATTERN) {
            index = index + Util.matcher_length;
          } else if (this.searchStyle == SearchStyle.TRANSFER) {
            index = index + this.strFind.length() - Util.transfer_count;
          } else {
            index = index + this.strFind.length();
          }
          searchBean.setEnd(index);
          listIndex.add(searchBean);
        }
      }
    }
    if (listIndex.isEmpty()) {
      if (index == Util.PATTERN_SYNTAX_ERROR_INDEX) {
        TipsWindow.show(this, "正则表达式语法错误，请修改！");
      } else {
        JOptionPane.showMessageDialog(this, "找不到\"" + this.strFind + "\"", Util.SOFTWARE, JOptionPane.NO_OPTION);
      }
    } else {
      SearchResult searchResult = new SearchResult(this.txaSource, this.strFind, listIndex);
      this.searchResultPanel.refreshResult(searchResult);
      ((SnowPadFrame) this.getOwner()).viewSearchResult(true);
      this.dispose();
    }
  }

  /**
   * “复制结果行”的处理方法
   */
  private void copyResultLines() {
    LinkedList<SearchBean> listIndex = new LinkedList<SearchBean>();
    int index = 0;
    if (!Util.isTextEmpty(this.strFind)) {
      while (index >= 0) {
        index = Util.findText(this.strFind, this.txaSource, index,
            true, this.isMatchCase, this.isMatchWholeWord, false, this.searchStyle);
        if (index >= 0) {
          SearchBean searchBean = new SearchBean();
          searchBean.setStart(index);
          if (this.searchStyle == SearchStyle.PATTERN) {
            index = index + Util.matcher_length;
          } else if (this.searchStyle == SearchStyle.TRANSFER) {
            index = index + this.strFind.length() - Util.transfer_count;
          } else {
            index = index + this.strFind.length();
          }
          searchBean.setEnd(index);
          listIndex.add(searchBean);
        }
      }
    }
    if (listIndex.isEmpty()) {
      if (index == Util.PATTERN_SYNTAX_ERROR_INDEX) {
        TipsWindow.show(this, "正则表达式语法错误，请修改！");
      } else {
        JOptionPane.showMessageDialog(this, "找不到\"" + this.strFind + "\"", Util.SOFTWARE, JOptionPane.NO_OPTION);
      }
    } else {
      String strSource = this.txaSource.getText();
      StringBuilder stbResult = new StringBuilder(); // 结果字符串
      int lineNumUsed = -1; // 最近一次使用的行号
      int times = 0; // 复制的行数
      try {
        for (SearchBean searchBean : listIndex) {
          int lineNumStart = this.txaSource.getLineOfOffset(searchBean.getStart());
          if (lineNumUsed >= lineNumStart) {
            continue;
          }
          int lineNumEnd = this.txaSource.getLineOfOffset(searchBean.getEnd());
          lineNumUsed = lineNumEnd;
          StringBuilder stbLine = new StringBuilder();
          for (int i = lineNumStart; i <= lineNumEnd; i++) {
            stbLine.append(strSource.substring(this.txaSource.getLineStartOffset(i), this.txaSource.getLineEndOffset(i)));
            if (stbLine.charAt(stbLine.length() - 1) != '\n') {
              stbLine.append('\n');
            }
            times++;
          }
          stbResult.append(stbLine);
        }
        ListenerManager.getInstance().postClipboardEvent(stbResult.toString());
        TipsWindow.show(this, "共复制 " + times + " 行。", Background.GREEN);
      } catch (Exception x) {
         x.printStackTrace();
      }
    }
  }

  /**
   * “剪切结果行”的处理方法
   */
  private void cutResultLines() {
    LinkedList<SearchBean> listIndex = new LinkedList<SearchBean>();
    int index = 0;
    if (!Util.isTextEmpty(this.strFind)) {
      while (index >= 0) {
        index = Util.findText(this.strFind, this.txaSource, index,
            true, this.isMatchCase, this.isMatchWholeWord, false, this.searchStyle);
        if (index >= 0) {
          SearchBean searchBean = new SearchBean();
          searchBean.setStart(index);
          if (this.searchStyle == SearchStyle.PATTERN) {
            index = index + Util.matcher_length;
          } else if (this.searchStyle == SearchStyle.TRANSFER) {
            index = index + this.strFind.length() - Util.transfer_count;
          } else {
            index = index + this.strFind.length();
          }
          searchBean.setEnd(index);
          listIndex.add(searchBean);
        }
      }
    }
    if (listIndex.isEmpty()) {
      if (index == Util.PATTERN_SYNTAX_ERROR_INDEX) {
        TipsWindow.show(this, "正则表达式语法错误，请修改！");
      } else {
        JOptionPane.showMessageDialog(this, "找不到\"" + this.strFind + "\"", Util.SOFTWARE, JOptionPane.NO_OPTION);
      }
    } else {
      String strSource = this.txaSource.getText();
      StringBuilder stbResult = new StringBuilder(); // 结果字符串
      int lineNumUsed = -1; // 最近一次使用的行号
      int times = 0; // 剪切的行数
      // 行号集合
      LinkedList<Integer> listLineNum = new LinkedList<Integer>();
      try {
        for (SearchBean searchBean : listIndex) {
          int lineNumStart = this.txaSource.getLineOfOffset(searchBean.getStart());
          if (lineNumUsed >= lineNumStart) {
            continue;
          }
          int lineNumEnd = this.txaSource.getLineOfOffset(searchBean.getEnd());
          lineNumUsed = lineNumEnd;
          StringBuilder stbLine = new StringBuilder();
          for (int i = lineNumStart; i <= lineNumEnd; i++) {
            stbLine.append(strSource.substring(this.txaSource.getLineStartOffset(i), this.txaSource.getLineEndOffset(i)));
            if (stbLine.charAt(stbLine.length() - 1) != '\n') {
              stbLine.append('\n');
            }
            times++;
            if (!listLineNum.contains(i)) {
              listLineNum.add(i);
            }
          }
          stbResult.append(stbLine);
        }
        ListenerManager.getInstance().postClipboardEvent(stbResult.toString());
        Collections.sort(listLineNum);
        stbResult = new StringBuilder(strSource);
        int size = listLineNum.size();
        for (int i = size - 1; i >= 0; i--) {
          stbResult.delete(this.txaSource.getLineStartOffset(listLineNum.get(i)), this.txaSource.getLineEndOffset(listLineNum.get(i)));
        }
        this.txaSource.setText(stbResult.toString());
        TipsWindow.show(this, "共剪切 " + times + " 行。", Background.GREEN);
      } catch (Exception x) {
         x.printStackTrace();
      }
    }
  }

  /**
   * “删除结果行”的处理方法
   */
  private void delResultLines() {
    LinkedList<SearchBean> listIndex = new LinkedList<SearchBean>();
    int index = 0;
    if (!Util.isTextEmpty(this.strFind)) {
      while (index >= 0) {
        index = Util.findText(this.strFind, this.txaSource, index,
            true, this.isMatchCase, this.isMatchWholeWord, false, this.searchStyle);
        if (index >= 0) {
          SearchBean searchBean = new SearchBean();
          searchBean.setStart(index);
          if (this.searchStyle == SearchStyle.PATTERN) {
            index = index + Util.matcher_length;
          } else if (this.searchStyle == SearchStyle.TRANSFER) {
            index = index + this.strFind.length() - Util.transfer_count;
          } else {
            index = index + this.strFind.length();
          }
          searchBean.setEnd(index);
          listIndex.add(searchBean);
        }
      }
    }
    if (listIndex.isEmpty()) {
      if (index == Util.PATTERN_SYNTAX_ERROR_INDEX) {
        TipsWindow.show(this, "正则表达式语法错误，请修改！");
      } else {
        JOptionPane.showMessageDialog(this, "找不到\"" + this.strFind + "\"", Util.SOFTWARE, JOptionPane.NO_OPTION);
      }
    } else {
      int lineNumUsed = -1; // 最近一次使用的行号
      int times = 0; // 删除的行数
      // 行号集合
      LinkedList<Integer> listLineNum = new LinkedList<Integer>();
      try {
        for (SearchBean searchBean : listIndex) {
          int lineNumStart = this.txaSource.getLineOfOffset(searchBean.getStart());
          if (lineNumUsed >= lineNumStart) {
            continue;
          }
          int lineNumEnd = this.txaSource.getLineOfOffset(searchBean.getEnd());
          lineNumUsed = lineNumEnd;
          StringBuilder stbLine = new StringBuilder();
          for (int i = lineNumStart; i <= lineNumEnd; i++) {
            times++;
            if (!listLineNum.contains(i)) {
              listLineNum.add(i);
            }
          }
        }
        Collections.sort(listLineNum);
        StringBuilder stbResult = new StringBuilder(this.txaSource.getText());
        int size = listLineNum.size();
        for (int i = size - 1; i >= 0; i--) {
          stbResult.delete(this.txaSource.getLineStartOffset(listLineNum.get(i)), this.txaSource.getLineEndOffset(listLineNum.get(i)));
        }
        this.txaSource.setText(stbResult.toString());
        TipsWindow.show(this, "共删除 " + times + " 行。", Background.GREEN);
      } catch (Exception x) {
         x.printStackTrace();
      }
    }
  }

  /**
   * “标记”的处理方法
   */
  private void mark() {
    if (this.clearMark) {
      this.clearMark();
    }
    if (this.clearBookmark) {
      this.clearBookmark();
    }
    LinkedList<SearchBean> listIndex = new LinkedList<SearchBean>();
    int index = 0;
    if (!Util.isTextEmpty(this.strFind)) {
      while (index >= 0) {
        index = Util.findText(this.strFind, this.txaSource, index,
            true, this.isMatchCase, this.isMatchWholeWord, false, this.searchStyle);
        if (index >= 0) {
          SearchBean searchBean = new SearchBean();
          searchBean.setStart(index);
          if (this.searchStyle == SearchStyle.PATTERN) {
            index = index + Util.matcher_length;
          } else if (this.searchStyle == SearchStyle.TRANSFER) {
            index = index + this.strFind.length() - Util.transfer_count;
          } else {
            index = index + this.strFind.length();
          }
          searchBean.setEnd(index);
          listIndex.add(searchBean);
        }
      }
    }
    if (listIndex.isEmpty()) {
      if (index == Util.PATTERN_SYNTAX_ERROR_INDEX) {
        TipsWindow.show(this, "正则表达式语法错误，请修改！");
      } else {
        JOptionPane.showMessageDialog(this, "找不到\"" + this.strFind + "\"", Util.SOFTWARE, JOptionPane.NO_OPTION);
      }
    } else {
      for (SearchBean searchBean : listIndex) {
        Util.addHighlight(this.txaSource, searchBean.getStart(), searchBean.getEnd(), this.txaSource.getMarkBackColor(), HighlightColorStyle.STYLE_MARK.getIndex());
      }
      if (this.addBookmark) {
        try {
          for (SearchBean searchBean : listIndex) {
            int lineNum = this.txaSource.getLineOfOffset(searchBean.getStart());
            this.txaSource.addBookmark(lineNum);
          }
          ((SnowPadFrame)this.getOwner()).repaintLineNumberView();
        } catch (Exception x) {
          // x.printStackTrace();
        }
      }
    }
  }

  /**
   * “选区内标记”的处理方法
   */
  private void markSel() {
    if (this.clearMark) {
      this.clearMark();
    }
    if (this.clearBookmark) {
      this.clearBookmark();
    }
    LinkedList<SearchBean> listIndex = new LinkedList<SearchBean>();
    String str = this.txaSource.getSelectedText();
    int selStart = this.txaSource.getSelectionStart();
    String strFindText = this.txtFindTextM.getText();
    StringBuilder stbTextAll = new StringBuilder(str);
    if (this.searchStyle == SearchStyle.TRANSFER) {
      strFindText = Util.transfer(strFindText);
    }
    StringBuilder stbFindText = new StringBuilder(strFindText);
    if (!this.isMatchCase) {
      if (this.searchStyle == SearchStyle.PATTERN) {
        stbFindText = new StringBuilder("(?i)" + stbFindText); // 正则表达式中，可用(?i)打开不区分大小写的属性
      } else {
        stbFindText = new StringBuilder(stbFindText.toString().toLowerCase());
      }
      stbTextAll = new StringBuilder(stbTextAll.toString().toLowerCase());
    }
    if (this.searchStyle == SearchStyle.PATTERN) {
      try {
        this.matcher = Pattern.compile(stbFindText.toString()).matcher(stbTextAll);
      } catch (PatternSyntaxException x) {
        TipsWindow.show(this, "正则表达式语法错误，请修改！");
        return;
      }
      while (this.matcher.find()) {
        SearchBean searchBean = new SearchBean();
        int start = selStart + this.matcher.start();
        int end = selStart + this.matcher.end();
        searchBean.setStart(start);
        searchBean.setEnd(end);
        listIndex.add(searchBean);
      }
    } else {
      int findLength = stbFindText.length();
      int caretPos = 0; // 当前从哪个索引值开始搜索字符串
      for (int index = 0; caretPos >= 0;) {
        index = stbTextAll.indexOf(stbFindText.toString(), caretPos);
        if (index >= 0) {
          // 全词匹配判断
          if (this.isMatchWholeWord && !Util.isMatchWholeWord(stbTextAll.toString(), index, findLength)) {
            caretPos = index + findLength;
            continue;
          }
          SearchBean searchBean = new SearchBean();
          int start = selStart + index;
          int end = start + findLength;
          searchBean.setStart(start);
          searchBean.setEnd(end);
          listIndex.add(searchBean);
          caretPos = index + findLength;
        } else {
          break;
        }
      }
    }
    if (listIndex.isEmpty()) {
      JOptionPane.showMessageDialog(this, "找不到\"" + this.strFind + "\"", Util.SOFTWARE, JOptionPane.NO_OPTION);
    } else {
      for (SearchBean searchBean : listIndex) {
        Util.addHighlight(this.txaSource, searchBean.getStart(), searchBean.getEnd(), this.txaSource.getMarkBackColor(), HighlightColorStyle.STYLE_MARK.getIndex());
      }
      if (this.addBookmark) {
        try {
          for (SearchBean searchBean : listIndex) {
            int lineNum = this.txaSource.getLineOfOffset(searchBean.getStart());
            this.txaSource.addBookmark(lineNum);
          }
          ((SnowPadFrame)this.getOwner()).repaintLineNumberView();
        } catch (Exception x) {
          // x.printStackTrace();
        }
      }
    }
  }

  /**
   * “清除标记”的处理方法
   */
  private void clearMark() {
    Util.rmHighlight(this.txaSource, HighlightColorStyle.STYLE_MARK.getIndex());
  }

  /**
   * “清除书签”的处理方法
   */
  private void clearBookmark() {
    this.txaSource.clearBookmarks();
    ((SnowPadFrame)this.getOwner()).repaintLineNumberView();
  }

  /**
   * 设置选区操作按钮的状态
   */
  private void setBtnSelEnabled() {
    String strSel = this.txaSource.getSelectedText();
    boolean isSelEmpty = Util.isTextEmpty(strSel);
    if (this.tpnMain.getSelectedIndex() == 0) {
      if (isSelEmpty || Util.isTextEmpty(this.txtFindTextF.getText())) {
        this.btnCountSelF.setEnabled(false);
      } else {
        this.btnCountSelF.setEnabled(true);
      }
    } else if (this.tpnMain.getSelectedIndex() == 1) {
      if (isSelEmpty || Util.isTextEmpty(this.txtFindTextR.getText())) {
        this.btnReplaceSelR.setEnabled(false);
      } else {
        this.btnReplaceSelR.setEnabled(true);
      }
    } else if (this.tpnMain.getSelectedIndex() == 2) {
      if (isSelEmpty || Util.isTextEmpty(this.txtFindTextM.getText())) {
        this.btnMarkSel.setEnabled(false);
      } else {
        this.btnMarkSel.setEnabled(true);
      }
    }
  }

  private void execDocumentEvent(DocumentEvent e) {
    Object source = e.getDocument();
    if (this.txtFindTextF.getDocument().equals(source)) { // 查找
      this.strFind = this.txtFindTextF.getText();
      if (Util.isTextEmpty(this.strFind)) {
        this.btnFindF.setEnabled(false);
        this.btnCountAllF.setEnabled(false);
        this.btnSearchInFileF.setEnabled(false);
        this.btnCopyResultLinesF.setEnabled(false);
        this.btnCutResultLinesF.setEnabled(false);
        this.btnDelResultLinesF.setEnabled(false);
      } else {
        this.btnFindF.setEnabled(true);
        this.btnCountAllF.setEnabled(true);
        this.btnSearchInFileF.setEnabled(true);
        this.btnCopyResultLinesF.setEnabled(true);
        this.btnCutResultLinesF.setEnabled(true);
        this.btnDelResultLinesF.setEnabled(true);
      }
    } else if (this.txtFindTextR.getDocument().equals(source)) { // 替换
      this.strFind = this.txtFindTextR.getText();
      if (Util.isTextEmpty(this.strFind)) {
        this.btnFindR.setEnabled(false);
        this.btnReplaceR.setEnabled(false);
        this.btnReplaceAllR.setEnabled(false);
      } else {
        this.btnFindR.setEnabled(true);
        this.btnReplaceR.setEnabled(true);
        this.btnReplaceAllR.setEnabled(true);
      }
    } else if (this.txtFindTextM.getDocument().equals(source)) { // 标记
      this.strFind = this.txtFindTextM.getText();
      if (Util.isTextEmpty(this.strFind)) {
        this.btnMark.setEnabled(false);
        this.btnMarkSel.setEnabled(false);
      } else {
        this.btnMark.setEnabled(true);
        this.btnMarkSel.setEnabled(true);
      }
    }
    this.setBtnSelEnabled();
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

  /**
   * 当选项卡改变当前视图时调用
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    if (this.tpnMain.getSelectedIndex() == 0) {
      this.setTitle(this.tpnMain.getTitleAt(0));
      this.txtFindTextF.setText(this.strFind);
      this.txtFindTextF.selectAll();
    } else if (this.tpnMain.getSelectedIndex() == 1) {
      this.setTitle(this.tpnMain.getTitleAt(1));
      this.txtFindTextR.setText(this.strFind);
      this.txtFindTextR.selectAll();
    } else if (this.tpnMain.getSelectedIndex() == 2) {
      this.setTitle(this.tpnMain.getTitleAt(2));
      this.txtFindTextM.setText(this.strFind);
      this.txtFindTextM.selectAll();
    }
    this.setBtnSelEnabled();
  }

  /**
   * 当窗口获得焦点时，将触发此事件
   */
  @Override
  public void windowGainedFocus(WindowEvent e) {
    this.setBtnSelEnabled();
  }

  /**
   * 当窗口失去焦点时，将触发此事件
   */
  @Override
  public void windowLostFocus(WindowEvent e) {
  }
}
