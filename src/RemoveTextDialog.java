package com.xiboliya.snowpad;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

/**
 * "切除"对话框
 * 
 * @author chen
 * 
 */
public class RemoveTextDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JRadioButton radRmLineStart = new JRadioButton("行首(S)", false);
  private JRadioButton radRmLineEnd = new JRadioButton("行尾(E)", true);
  private JPanel pnlRmLineStartEnd = new JPanel(new GridLayout(2, 1));
  private JLabel lblOffset = new JLabel("偏移量：");
  private BaseTextField txtOffset = new BaseTextField();
  private JButton btnOk = new JButton("确定");
  private JButton btnCancel = new JButton("取消");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private ButtonGroup bgpRmLineStartEnd = new ButtonGroup();

  public RemoveTextDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.init();
    this.addListeners();
    this.setSize(220, 150);
    this.setVisible(true);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   */
  public void setVisible(boolean visible) {
    if (visible) {
      this.txtOffset.setText("");
    }
    super.setVisible(visible);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("切除文本");
    this.pnlMain.setLayout(null);
    this.lblOffset.setBounds(20, 10, 60, Util.VIEW_HEIGHT);
    this.txtOffset.setBounds(80, 10, 100, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblOffset);
    this.pnlMain.add(this.txtOffset);
    this.pnlRmLineStartEnd.setBounds(10, 40, 85, 65);
    this.pnlRmLineStartEnd.setBorder(new TitledBorder("切除位置"));
    this.pnlRmLineStartEnd.add(this.radRmLineStart);
    this.pnlRmLineStartEnd.add(this.radRmLineEnd);
    this.pnlMain.add(this.pnlRmLineStartEnd);
    this.radRmLineStart.setMnemonic('S');
    this.radRmLineEnd.setMnemonic('E');
    this.bgpRmLineStartEnd.add(radRmLineStart);
    this.bgpRmLineStartEnd.add(radRmLineEnd);
    this.radRmLineStart.setSelected(true);
    this.btnOk.setBounds(110, 45, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(110, 80, 85, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.radRmLineStart.addKeyListener(this.keyAdapter);
    this.radRmLineEnd.addKeyListener(this.keyAdapter);
    this.txtOffset.addKeyListener(this.keyAdapter);
    this.btnOk.addActionListener(this);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 为各组件添加事件的处理方法
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    }
  }

  /**
   * 切除字符
   */
  private void removeText() {
    int rmSize = 0;
    try {
      rmSize = Integer.parseInt(this.txtOffset.getText().trim());
    } catch (NumberFormatException x) {
      x.printStackTrace();
      JOptionPane.showMessageDialog(this, "格式错误，请输入数字！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      this.txtOffset.requestFocus();
      this.txtOffset.selectAll();
      return;
    }
    if (rmSize <= 0) {
      JOptionPane.showMessageDialog(this, "数值必须大于0！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      this.txtOffset.requestFocus();
      this.txtOffset.selectAll();
    } else {
      this.toRemoveText(rmSize);
      this.dispose();
    }
  }

  /**
   * 从行首/行尾切除一定数目的字符
   * 
   * @param rmSize
   *          从行首/行尾切除字符的个数
   */
  private void toRemoveText(int rmSize) {
    CurrentLines currentLines = new CurrentLines(this.txaSource);
    String strContent = currentLines.getStrContent();
    int startIndex = currentLines.getStartIndex();
    int endIndex = currentLines.getEndIndex();
    String[] arrText = strContent.split("\n", -1); // 将当前选区的文本分行处理，包括末尾的多处空行
    boolean isRmLineStart = this.radRmLineStart.isSelected();
    StringBuilder stbText = new StringBuilder();
    for (int n = 0; n < arrText.length; n++) {
      int strLen = arrText[n].length();
      if (strLen <= rmSize) {
        arrText[n] = "";
      } else {
        if (isRmLineStart) {
          arrText[n] = arrText[n].substring(rmSize);
        } else {
          arrText[n] = arrText[n].substring(0, arrText[n].length() - rmSize);
        }
      }
      stbText.append(arrText[n] + "\n");
    }
    this.txaSource.replaceRange(stbText.deleteCharAt(stbText.length() - 1)
        .toString(), startIndex, endIndex);
    endIndex = startIndex + stbText.length() - 1; // 切除字符后，当前选区内末行的行尾偏移量
    if (this.txaSource.getText().length() == endIndex + 1) {
      endIndex++;
    }
    this.txaSource.select(startIndex, endIndex);
  }

  /**
   * 默认的"确定"操作方法
   */
  public void onEnter() {
    this.removeText();
  }

  /**
   * 默认的"取消"操作方法
   */
  public void onCancel() {
    this.dispose();
  }
}
