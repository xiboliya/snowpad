package com.xiboliya.snowpad;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * "查找"和"替换"对话框
 * 
 * @author 冰原
 * 
 */
public class FindReplaceDialog extends BaseDialog implements ActionListener,
    CaretListener, ChangeListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JTabbedPane tpnMain = new JTabbedPane();
  private boolean isFindDown = true; // 向下查找
  private boolean isIgnoreCase = true; // 忽略大小写
  private boolean isWrap = false; // 循环查找
  private boolean isTransfer = false; // 转义扩展
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private String strFind = ""; // 查找的字符串
  // 查找
  private JPanel pnlFind = new JPanel();
  private JLabel lblFindTextF = new JLabel("查找内容：");
  private BaseTextField txtFindTextF = new BaseTextField();
  private JCheckBox chkNotIgnoreCaseF = new JCheckBox("区分大小写(C)", false);
  private JCheckBox chkIsWrapF = new JCheckBox("循环查找(W)", false);
  private JCheckBox chkTransferF = new JCheckBox("转义扩展(T)", false);
  private JButton btnHelpF = new JButton(Util.HELP_ICON);
  private JRadioButton radFindUpF = new JRadioButton("向上(U)", false);
  private JRadioButton radFindDownF = new JRadioButton("向下(D)", true);
  private JButton btnFindF = new JButton("查找(F)");
  private JButton btnCountF = new JButton("统计次数(T)");
  private JButton btnCancelF = new JButton("取消");
  private ButtonGroup bgpFindUpDownF = new ButtonGroup();
  private JPanel pnlFindUpDownF = new JPanel(new GridLayout(2, 1));
  // 替换
  private JPanel pnlReplace = new JPanel();
  private JLabel lblFindTextR = new JLabel("查找内容：");
  private JLabel lblReplaceTextR = new JLabel("替换为：");
  private BaseTextField txtFindTextR = new BaseTextField();
  private BaseTextField txtReplaceTextR = new BaseTextField();
  private JCheckBox chkNotIgnoreCaseR = new JCheckBox("区分大小写(C)", false);
  private JCheckBox chkIsWrapR = new JCheckBox("循环查找(W)", false);
  private JCheckBox chkTransferR = new JCheckBox("转义扩展(T)", false);
  private JButton btnHelpR = new JButton(Util.HELP_ICON);
  private JRadioButton radFindUpR = new JRadioButton("向上(U)", false);
  private JRadioButton radFindDownR = new JRadioButton("向下(D)", true);
  private JButton btnFindR = new JButton("查找(F)");
  private JButton btnReplaceR = new JButton("替换(R)");
  private JButton btnReplaceAllR = new JButton("全部替换(A)");
  private JButton btnCancelR = new JButton("取消");
  private ButtonGroup bgpFindUpDownR = new ButtonGroup();
  private JPanel pnlFindUpDownR = new JPanel(new GridLayout(2, 1));

  public FindReplaceDialog(JFrame owner, boolean modal, JTextArea txaSource,
      boolean visible) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.setTitle("查找");
    this.init();
    this.setMnemonic();
    this.addListeners();
    this.setSize(390, 205);
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
    this.chkNotIgnoreCaseF.setBounds(10, 50, 110, Util.VIEW_HEIGHT);
    this.pnlFind.add(this.chkNotIgnoreCaseF);
    this.chkIsWrapF.setBounds(10, 70, 110, Util.VIEW_HEIGHT);
    this.pnlFind.add(this.chkIsWrapF);
    this.chkTransferF.setBounds(10, 90, 95, Util.VIEW_HEIGHT);
    this.chkTransferF.setToolTipText("可使用\\n、\\t转义字符");
    this.pnlFind.add(this.chkTransferF);
    this.btnHelpF.setBounds(105, 90, 12, Util.VIEW_HEIGHT);
    this.pnlFind.add(this.btnHelpF);
    this.btnHelpF.setToolTipText("功能简介");
    this.btnHelpF.setContentAreaFilled(false);
    this.btnHelpF.setFocusable(false);
    this.pnlFindUpDownF.setBounds(145, 40, 95, 70);
    this.pnlFindUpDownF.setBorder(new TitledBorder("方向"));
    this.pnlFindUpDownF.add(this.radFindUpF);
    this.pnlFindUpDownF.add(this.radFindDownF);
    this.pnlFind.add(this.pnlFindUpDownF);
    this.btnFindF.setEnabled(false);
    this.btnFindF.setBounds(270, 10, 100, Util.BUTTON_HEIGHT);
    this.btnCountF.setEnabled(false);
    this.btnCountF.setBounds(270, 48, 100, Util.BUTTON_HEIGHT);
    this.btnCancelF.setBounds(270, 86, 100, Util.BUTTON_HEIGHT);
    this.pnlFind.add(this.btnFindF);
    this.pnlFind.add(this.btnCountF);
    this.pnlFind.add(this.btnCancelF);
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
    this.chkNotIgnoreCaseR.setBounds(10, 70, 110, Util.VIEW_HEIGHT);
    this.pnlReplace.add(this.chkNotIgnoreCaseR);
    this.chkIsWrapR.setBounds(10, 90, 110, Util.VIEW_HEIGHT);
    this.pnlReplace.add(this.chkIsWrapR);
    this.chkTransferR.setBounds(10, 110, 95, Util.VIEW_HEIGHT);
    this.chkTransferR.setToolTipText("可使用\\n、\\t转义字符");
    this.pnlReplace.add(this.chkTransferR);
    this.btnHelpR.setBounds(105, 110, 12, Util.VIEW_HEIGHT);
    this.pnlReplace.add(this.btnHelpR);
    this.btnHelpR.setToolTipText("功能简介");
    this.btnHelpR.setContentAreaFilled(false);
    this.btnHelpR.setFocusable(false);
    this.pnlFindUpDownR.setBounds(145, 60, 95, 70);
    this.pnlFindUpDownR.setBorder(new TitledBorder("方向"));
    this.pnlFindUpDownR.add(this.radFindUpR);
    this.pnlFindUpDownR.add(this.radFindDownR);
    this.pnlReplace.add(this.pnlFindUpDownR);
    this.btnFindR.setEnabled(false);
    this.btnReplaceR.setEnabled(false);
    this.btnReplaceAllR.setEnabled(false);
    this.btnFindR.setBounds(270, 10, 100, Util.BUTTON_HEIGHT);
    this.btnReplaceR.setBounds(270, 40, 100, Util.BUTTON_HEIGHT);
    this.btnReplaceAllR.setBounds(270, 70, 100, Util.BUTTON_HEIGHT);
    this.btnCancelR.setBounds(270, 100, 100, Util.BUTTON_HEIGHT);
    this.pnlReplace.add(this.btnFindR);
    this.pnlReplace.add(this.btnReplaceR);
    this.pnlReplace.add(this.btnReplaceAllR);
    this.pnlReplace.add(this.btnCancelR);
    this.bgpFindUpDownR.add(this.radFindDownR);
    this.bgpFindUpDownR.add(this.radFindUpR);
    // 主界面
    this.tpnMain.add(this.pnlFind, "查找");
    this.tpnMain.add(this.pnlReplace, "替换");
    this.pnlMain.add(this.tpnMain, BorderLayout.CENTER);
    this.setTabbedIndex(0);
    this.tpnMain.setFocusable(false);
  }

  /**
   * 设置选项卡的当前视图
   * 
   * @param index
   *          视图的索引号
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
    } else {
      this.txtFindTextR.selectAll();
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
   * @param strFind
   *          查找的字符串
   * @param isUpdate
   *          是否同步更新文本框的显示，更新为true，不更新为false
   */
  public void setFindText(String strFind, boolean isUpdate) {
    this.strFind = strFind;
    if (isUpdate) {
      this.txtFindTextF.setText(strFind);
      this.txtFindTextR.setText(strFind);
      if (this.getTabbedIndex() == 0) {
        this.txtFindTextF.selectAll();
      } else if (this.getTabbedIndex() == 1) {
        this.txtFindTextR.selectAll();
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
   * @param strReplace
   *          用于替换的字符串
   */
  public void setReplaceText(String strReplace) {
    this.txtReplaceTextR.setText(strReplace);
  }

  /**
   * 为各组件设置快捷键
   */
  private void setMnemonic() {
    // 查找
    this.chkNotIgnoreCaseF.setMnemonic('C');
    this.chkIsWrapF.setMnemonic('W');
    this.chkTransferF.setMnemonic('T');
    this.btnFindF.setMnemonic('F');
    this.btnCountF.setMnemonic('T');
    this.radFindUpF.setMnemonic('U');
    this.radFindDownF.setMnemonic('D');
    // 替换
    this.chkNotIgnoreCaseR.setMnemonic('C');
    this.chkIsWrapR.setMnemonic('W');
    this.chkTransferR.setMnemonic('T');
    this.btnFindR.setMnemonic('F');
    this.btnReplaceR.setMnemonic('R');
    this.btnReplaceAllR.setMnemonic('A');
    this.radFindUpR.setMnemonic('U');
    this.radFindDownR.setMnemonic('D');
  }

  /**
   * 为各组件添加监听器
   */
  private void addListeners() {
    this.tpnMain.addChangeListener(this);
    // 查找
    this.txtFindTextF.addCaretListener(this);
    this.btnFindF.addActionListener(this);
    this.btnCountF.addActionListener(this);
    this.btnCancelF.addActionListener(this);
    this.radFindDownF.addActionListener(this);
    this.radFindUpF.addActionListener(this);
    this.chkNotIgnoreCaseF.addActionListener(this);
    this.chkIsWrapF.addActionListener(this);
    this.chkTransferF.addActionListener(this);
    this.btnHelpF.addActionListener(this);
    this.txtFindTextF.addKeyListener(this.keyAdapter);
    this.chkNotIgnoreCaseF.addKeyListener(this.keyAdapter);
    this.chkIsWrapF.addKeyListener(this.keyAdapter);
    this.chkTransferF.addKeyListener(this.keyAdapter);
    this.radFindDownF.addKeyListener(this.keyAdapter);
    this.radFindUpF.addKeyListener(this.keyAdapter);
    this.btnCancelF.addKeyListener(this.buttonKeyAdapter);
    this.btnFindF.addKeyListener(this.buttonKeyAdapter);
    this.btnCountF.addKeyListener(this.buttonKeyAdapter);
    // 替换
    this.txtFindTextR.addCaretListener(this);
    this.btnFindR.addActionListener(this);
    this.btnReplaceR.addActionListener(this);
    this.btnReplaceAllR.addActionListener(this);
    this.btnCancelR.addActionListener(this);
    this.radFindDownR.addActionListener(this);
    this.radFindUpR.addActionListener(this);
    this.chkNotIgnoreCaseR.addActionListener(this);
    this.chkIsWrapR.addActionListener(this);
    this.chkTransferR.addActionListener(this);
    this.btnHelpR.addActionListener(this);
    this.txtFindTextR.addKeyListener(this.keyAdapter);
    this.txtReplaceTextR.addKeyListener(this.keyAdapter);
    this.chkNotIgnoreCaseR.addKeyListener(this.keyAdapter);
    this.chkIsWrapR.addKeyListener(this.keyAdapter);
    this.chkTransferR.addKeyListener(this.keyAdapter);
    this.radFindDownR.addKeyListener(this.keyAdapter);
    this.radFindUpR.addKeyListener(this.keyAdapter);
    this.btnCancelR.addKeyListener(this.buttonKeyAdapter);
    this.btnReplaceR.addKeyListener(this.buttonKeyAdapter);
    this.btnFindR.addKeyListener(this.buttonKeyAdapter);
    this.btnReplaceAllR.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 为各组件添加事件的处理方法
   */
  public void actionPerformed(ActionEvent e) {
    // 查找
    if (this.btnCancelF.equals(e.getSource())) {
      this.onCancel();
    } else if (this.btnFindF.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnCountF.equals(e.getSource())) {
      this.getTextCount();
    } else if (this.chkNotIgnoreCaseF.equals(e.getSource())) {
      boolean selected = this.chkNotIgnoreCaseF.isSelected();
      this.isIgnoreCase = !selected;
      this.chkNotIgnoreCaseR.setSelected(selected);
    } else if (this.chkIsWrapF.equals(e.getSource())) {
      boolean selected = this.chkIsWrapF.isSelected();
      this.isWrap = selected;
      this.chkIsWrapR.setSelected(selected);
    } else if (this.chkTransferF.equals(e.getSource())) {
      boolean selected = this.chkTransferF.isSelected();
      this.isTransfer = selected;
      this.chkTransferR.setSelected(selected);
    } else if (this.btnHelpF.equals(e.getSource())) {
      this.showHelp();
    } else if (this.radFindDownF.equals(e.getSource())) {
      this.isFindDown = true;
      this.radFindDownR.setSelected(true);
    } else if (this.radFindUpF.equals(e.getSource())) {
      this.isFindDown = false;
      this.radFindUpR.setSelected(true);
    }
    // 替换
    else if (this.btnCancelR.equals(e.getSource())) {
      this.onCancel();
    } else if (this.btnFindR.equals(e.getSource())) {
      this.findText(this.isFindDown);
    } else if (this.btnReplaceR.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnReplaceAllR.equals(e.getSource())) {
      this.replaceAllText();
    } else if (this.chkNotIgnoreCaseR.equals(e.getSource())) {
      boolean selected = this.chkNotIgnoreCaseR.isSelected();
      this.isIgnoreCase = !selected;
      this.chkNotIgnoreCaseF.setSelected(selected);
    } else if (this.chkIsWrapR.equals(e.getSource())) {
      boolean selected = this.chkIsWrapR.isSelected();
      this.isWrap = selected;
      this.chkIsWrapF.setSelected(selected);
    } else if (this.chkTransferR.equals(e.getSource())) {
      boolean selected = this.chkTransferR.isSelected();
      this.isTransfer = selected;
      this.chkTransferF.setSelected(selected);
    } else if (this.btnHelpR.equals(e.getSource())) {
      this.showHelp();
    } else if (this.radFindDownR.equals(e.getSource())) {
      this.isFindDown = true;
      this.radFindDownF.setSelected(true);
    } else if (this.radFindUpR.equals(e.getSource())) {
      this.isFindDown = false;
      this.radFindUpF.setSelected(true);
    }
  }

  /**
   * 默认的“取消”操作方法
   */
  public void onCancel() {
    this.dispose();
  }

  /**
   * 默认的“确定”操作方法
   */
  public void onEnter() {
    if (this.tpnMain.getSelectedIndex() == 0) {
      this.findText(this.isFindDown);
    } else {
      this.replaceText();
    }
  }

  /**
   * 弹出帮助窗口
   */
  private void showHelp() {
    JOptionPane.showMessageDialog(this,
        "开启此选项后，你可以将文本域中不方便输入搜索框的字符进行转义替换。\n比如，可以使用\\n代替换行，\\t代替Tab字符。",
        Util.SOFTWARE, JOptionPane.NO_OPTION);
  }

  /**
   * 查找字符串
   * 
   * @param isFindDown
   *          查找的方向，如果向下查找则为true，反之则为false
   * @return 查找结果，如果查找成功返回true，反之则为false
   */
  public boolean findText(boolean isFindDown) {
    if (this.strFind != null && !this.strFind.isEmpty()) {
      int index = Util.findText(this.strFind, this.txaSource, isFindDown,
          this.isIgnoreCase, this.isWrap, this.isTransfer);
      if (index >= 0) {
        if (this.isTransfer) {
          this.txaSource.select(index, index + this.strFind.length()
              - Util.transfer_count);
        } else {
          this.txaSource.select(index, index + this.strFind.length());
        }
        return true;
      } else {
        JOptionPane.showMessageDialog(this, "找不到\"" + this.strFind + "\"",
            Util.SOFTWARE, JOptionPane.NO_OPTION);
      }
    }
    return false;
  }

  /**
   * 替换字符串
   */
  private void replaceText() {
    boolean isEquals = false;
    String strSel = this.txaSource.getSelectedText();
    String strFindTemp = this.txtFindTextR.getText();
    String strReplaceTemp = this.txtReplaceTextR.getText();
    if (strSel != null) {
      if (this.isTransfer) {
        strFindTemp = Util.transfer(strFindTemp);
        strReplaceTemp = Util.transfer(strReplaceTemp);
      }
      if (this.isIgnoreCase) {
        if (strSel.equalsIgnoreCase(strFindTemp)) {
          isEquals = true;
        }
      } else {
        if (strSel.equals(strFindTemp)) {
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
   * 替换所有字符串
   */
  private void replaceAllText() {
    String strFindText = this.txtFindTextR.getText();
    String strReplaceText = this.txtReplaceTextR.getText();
    StringBuilder stbTextAll = new StringBuilder(this.txaSource.getText()); //
    StringBuilder stbTextAllTemp = new StringBuilder(stbTextAll); //
    if (this.isTransfer) {
      strFindText = Util.transfer(strFindText);
      strReplaceText = Util.transfer(strReplaceText);
    }
    StringBuilder stbFindTextTemp = new StringBuilder(strFindText);
    if (strFindText != null && strFindText.length() > 0) {
      int caretPos = 0; // 当前从哪个索引值开始搜索字符串
      int index = 0;
      int times = 0; // 循环次数
      int oldPos = this.txaSource.getCaretPosition(); // 替换之前文本域的插入点位置
      int newPos = oldPos; // 替换之后的插入点位置
      int offset = strFindText.length() - strReplaceText.length(); // 查找与替换的字符串长度的差值
      if (this.isIgnoreCase) {
        stbFindTextTemp = new StringBuilder(stbFindTextTemp.toString()
            .toLowerCase());
        stbTextAllTemp = new StringBuilder(stbTextAllTemp.toString()
            .toLowerCase());
      }
      while (caretPos >= 0) {
        index = stbTextAllTemp.indexOf(stbFindTextTemp.toString(), caretPos);
        if (index >= 0) {
          stbTextAll.replace(index, index + stbFindTextTemp.length(),
              strReplaceText);
          caretPos = index + strReplaceText.length();
          if (caretPos < oldPos) {
            newPos -= offset;
          }
          stbTextAllTemp = new StringBuilder(stbTextAll); //
          if (this.isIgnoreCase) {
            stbTextAllTemp = new StringBuilder(stbTextAllTemp.toString()
                .toLowerCase());
          }
        } else {
          break;
        }
        times++;
      }
      if (times > 0) {
        this.txaSource.setText(stbTextAll.toString());
        this.txaSource.setCaretPosition(Util.checkCaretPosition(this.txaSource,
            newPos));
        JOptionPane.showMessageDialog(this, "共替换 " + times + " 处。",
            Util.SOFTWARE, JOptionPane.NO_OPTION);
      } else {
        JOptionPane.showMessageDialog(this, "找不到\"" + this.strFind + "\"",
            Util.SOFTWARE, JOptionPane.NO_OPTION);
      }
    }
  }

  /**
   * "统计次数"的处理方法
   */
  private void getTextCount() {
    if (this.strFind == null || this.strFind.isEmpty()) {
      return;
    }
    String strText = this.txaSource.getText();
    String strFindTemp = this.strFind;
    if (this.isTransfer) {
      strFindTemp = Util.transfer(strFindTemp);
    }
    if (this.isIgnoreCase) {
      strFindTemp = strFindTemp.toLowerCase();
      strText = strText.toLowerCase();
    }
    int index = strText.indexOf(strFindTemp);
    int times = 0; // 字符串出现次数
    while (index >= 0) {
      index = strText.indexOf(strFindTemp, index + strFindTemp.length());
      times++;
    }
    JOptionPane.showMessageDialog(this, "共找到 " + times + " 处。", Util.SOFTWARE,
        JOptionPane.NO_OPTION);
  }

  /**
   * 当文本框的光标发生变化时，触发此事件
   */
  public void caretUpdate(CaretEvent e) {
    // 查找
    if (this.txtFindTextF.equals(e.getSource())) {
      this.strFind = this.txtFindTextF.getText();
      if (this.strFind.isEmpty()) {
        this.btnFindF.setEnabled(false);
        this.btnCountF.setEnabled(false);
      } else {
        this.btnFindF.setEnabled(true);
        this.btnCountF.setEnabled(true);
      }
    }
    // 替换
    else if (this.txtFindTextR.equals(e.getSource())) {
      this.strFind = this.txtFindTextR.getText();
      if (this.strFind.isEmpty()) {
        this.btnFindR.setEnabled(false);
        this.btnReplaceR.setEnabled(false);
        this.btnReplaceAllR.setEnabled(false);
      } else {
        this.btnFindR.setEnabled(true);
        this.btnReplaceR.setEnabled(true);
        this.btnReplaceAllR.setEnabled(true);
      }
    }
  }

  /**
   * 当选项卡改变当前视图时调用
   */
  public void stateChanged(ChangeEvent e) {
    if (this.tpnMain.getSelectedIndex() == 0) {
      this.setTitle(this.tpnMain.getTitleAt(0));
      this.txtFindTextF.setText(this.strFind);
      this.txtFindTextF.selectAll();
    } else if (this.tpnMain.getSelectedIndex() == 1) {
      this.setTitle(this.tpnMain.getTitleAt(1));
      this.txtFindTextR.setText(this.strFind);
      this.txtFindTextR.selectAll();
    }
  }
}
