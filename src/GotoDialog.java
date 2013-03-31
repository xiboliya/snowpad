package com.xiboliya.snowpad;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

/**
 * "转到"对话框
 * 
 * @author chen
 * 
 */
public class GotoDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JTabbedPane tpnMain = new JTabbedPane();
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  // 行号
  private JPanel pnlLine = new JPanel();
  private JLabel lblCurLine = new JLabel();
  private JLabel lblEndLine = new JLabel();
  private JLabel lblGotoLine = new JLabel("转到行号：");
  private BaseTextField txtGotoLine = new BaseTextField();
  private JButton btnGotoLine = new JButton("确定");
  private JButton btnCancelLine = new JButton("取消");
  // 偏移量
  private JPanel pnlOffset = new JPanel();
  private JLabel lblCurOffset = new JLabel();
  private JLabel lblEndOffset = new JLabel();
  private JLabel lblGotoOffset = new JLabel("转到偏移量：");
  private BaseTextField txtGotoOffset = new BaseTextField();
  private JButton btnGotoOffset = new JButton("确定");
  private JButton btnCancelOffset = new JButton("取消");

  public GotoDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.setTitle("转到");
    this.init();
    this.updateView();
    this.addListeners();
    this.setSize(240, 190);
    this.setVisible(true);
  }

  private void init() {
    // 行号
    this.pnlLine.setLayout(null);
    this.lblCurLine.setBounds(15, 10, 220, Util.VIEW_HEIGHT);
    this.lblEndLine.setBounds(15, 35, 220, Util.VIEW_HEIGHT);
    this.pnlLine.add(this.lblCurLine);
    this.pnlLine.add(this.lblEndLine);
    this.lblGotoLine.setBounds(15, 60, 80, Util.VIEW_HEIGHT);
    this.txtGotoLine.setBounds(95, 58, 100, Util.INPUT_HEIGHT);
    this.pnlLine.add(this.lblGotoLine);
    this.pnlLine.add(this.txtGotoLine);
    this.btnGotoLine.setBounds(20, 90, 85, 23);
    this.btnCancelLine.setBounds(120, 90, 85, 23);
    this.pnlLine.add(this.btnGotoLine);
    this.pnlLine.add(this.btnCancelLine);
    // 偏移量
    this.pnlOffset.setLayout(null);
    this.lblCurOffset.setBounds(15, 10, 220, Util.VIEW_HEIGHT);
    this.lblEndOffset.setBounds(15, 35, 220, Util.VIEW_HEIGHT);
    this.pnlOffset.add(this.lblCurOffset);
    this.pnlOffset.add(this.lblEndOffset);
    this.lblGotoOffset.setBounds(15, 60, 80, Util.VIEW_HEIGHT);
    this.txtGotoOffset.setBounds(95, 58, 100, Util.INPUT_HEIGHT);
    this.pnlOffset.add(this.lblGotoOffset);
    this.pnlOffset.add(this.txtGotoOffset);
    this.btnGotoOffset.setBounds(20, 90, 85, Util.BUTTON_HEIGHT);
    this.btnCancelOffset.setBounds(120, 90, 85, Util.BUTTON_HEIGHT);
    this.pnlOffset.add(this.btnGotoOffset);
    this.pnlOffset.add(this.btnCancelOffset);

    this.tpnMain.add(this.pnlLine, "行号");
    this.tpnMain.add(this.pnlOffset, "偏移量");
    this.pnlMain.add(this.tpnMain, BorderLayout.CENTER);
    this.setTabbedIndex(0);
    this.tpnMain.setFocusable(false);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   */
  public void setVisible(boolean visible) {
    if (visible) {
      this.updateView();
    }
    super.setVisible(visible);
  }

  /**
   * 设置选项卡的当前视图
   * 
   * @param index
   *          视图的索引号
   */
  private void setTabbedIndex(int index) {
    this.tpnMain.setSelectedIndex(index);
  }

  /**
   * 获取选项卡当前视图的索引号
   * 
   * @return 当前视图的索引号
   */
  private int getTabbedIndex() {
    return this.tpnMain.getSelectedIndex();
  }

  /**
   * 更新当前行号和结尾行号的显示
   */
  private void updateView() {
    CurrentLine currentLine = new CurrentLine(this.txaSource);
    int lineNum = currentLine.getLineNum() + 1;
    this.lblCurLine.setText("当前行号：" + lineNum);
    this.lblEndLine.setText("结尾行号：" + this.txaSource.getLineCount());
    int currentIndex = currentLine.getCurrentIndex();
    this.lblCurOffset.setText("当前偏移量：" + currentIndex);
    this.lblEndOffset.setText("结尾偏移量：" + this.txaSource.getText().length());
  }

  private void addListeners() {
    this.btnGotoLine.addActionListener(this);
    this.btnCancelLine.addActionListener(this);
    this.txtGotoLine.addKeyListener(this.keyAdapter);
    this.btnCancelLine.addKeyListener(this.buttonKeyAdapter);
    this.btnGotoLine.addKeyListener(this.buttonKeyAdapter);
    this.btnGotoOffset.addActionListener(this);
    this.btnCancelOffset.addActionListener(this);
    this.txtGotoOffset.addKeyListener(this.keyAdapter);
    this.btnCancelOffset.addKeyListener(this.buttonKeyAdapter);
    this.btnGotoOffset.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * "取消"按钮的处理方法
   */
  private void cancelGoto() {
    this.dispose();
    this.txtGotoLine.setText("");
    this.txtGotoOffset.setText("");
  }

  /**
   * 转到指定行号
   */
  private void gotoLine() {
    int total = this.txaSource.getLineCount(); // 文本域总行数
    String str = this.txtGotoLine.getText().trim();
    if (str.isEmpty()) {
      JOptionPane.showMessageDialog(this, "行号不能为空！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      this.txtGotoLine.requestFocus();
      return;
    }
    int target = 1; // 指定的行号
    try {
      target = Integer.parseInt(str);
    } catch (NumberFormatException x) {
      x.printStackTrace();
      JOptionPane.showMessageDialog(this, "格式错误，请输入数值！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      this.txtGotoLine.requestFocus();
      this.txtGotoLine.selectAll();
      return;
    }
    if (target <= 0) {
      JOptionPane.showMessageDialog(this, "行号必须大于0！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    } else if (target > total) {
      JOptionPane.showMessageDialog(this, "行号超出范围！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    } else {
      try {
        // 获取指定行起始处的偏移量，指定行号的取值范围：x>=0 && x<文本域总行数
        int offset = this.txaSource.getLineStartOffset(target - 1);
        this.txaSource.setCaretPosition(offset);
      } catch (BadLocationException x) {
        x.printStackTrace();
      }
      this.cancelGoto();
    }
    this.txtGotoLine.requestFocus();
    this.txtGotoLine.selectAll();
  }

  /**
   * 转到指定偏移量
   */
  private void gotoOffset() {
    int total = this.txaSource.getText().length(); // 文本域总偏移量
    String str = this.txtGotoOffset.getText().trim();
    if (str.isEmpty()) {
      JOptionPane.showMessageDialog(this, "偏移量不能为空！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      this.txtGotoOffset.requestFocus();
      return;
    }
    int target = 0; // 指定的偏移量
    try {
      target = Integer.parseInt(str);
    } catch (NumberFormatException x) {
      x.printStackTrace();
      JOptionPane.showMessageDialog(this, "格式错误，请输入数值！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      this.txtGotoOffset.requestFocus();
      this.txtGotoOffset.selectAll();
      return;
    }
    if (target < 0) {
      JOptionPane.showMessageDialog(this, "偏移量必须大于等于0！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    } else if (target > total) {
      JOptionPane.showMessageDialog(this, "偏移量超出范围！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    } else {
      this.txaSource.setCaretPosition(target);
      this.cancelGoto();
    }
    this.txtGotoOffset.requestFocus();
    this.txtGotoOffset.selectAll();
  }

  public void actionPerformed(ActionEvent e) {
    if (this.btnCancelLine.equals(e.getSource())
        || this.btnCancelOffset.equals(e.getSource())) {
      this.onCancel();
    } else if (this.btnGotoLine.equals(e.getSource())
        || this.btnGotoOffset.equals(e.getSource())) {
      this.onEnter();
    }
  }

  /**
   * 默认的"确定"操作方法
   */
  public void onEnter() {
    if (this.getTabbedIndex() == 0) {
      this.gotoLine();
    } else {
      this.gotoOffset();
    }
  }

  /**
   * 默认的"取消"操作方法
   */
  public void onCancel() {
    this.cancelGoto();
  }
}
