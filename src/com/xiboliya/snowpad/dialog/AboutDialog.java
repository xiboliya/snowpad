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
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.util.Util;

/**
 * "关于"对话框
 * 
 * @author 冰原
 * 
 */
public class AboutDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private GridLayout layout = null;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JPanel pnlCenter = new JPanel();
  private JPanel pnlEast = new JPanel();
  private JPanel pnlWest = new JPanel();
  private JPanel pnlSouth = new JPanel();
  private JPanel pnlNorth = new JPanel();
  private JLabel lblWest = new JLabel(" ");
  private JLabel lblNorth = new JLabel();
  private JLabel lblEast = new JLabel(" ");
  private BaseButton btnOk = new BaseButton(" 确定 ");
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private LinkedList<JLabel> labelList = new LinkedList<JLabel>(); // 存放显示标签的链表

  public AboutDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.init();
    this.addListeners();
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("关于");
    this.btnOk.setMargin(new Insets(3, 15, 3, 15));
    this.pnlWest.add(this.lblWest);
    this.pnlNorth.add(this.lblNorth);
    this.pnlEast.add(this.lblEast);
    this.pnlSouth.add(this.btnOk);
    this.pnlMain.add(this.pnlWest, BorderLayout.WEST);
    this.pnlMain.add(this.pnlNorth, BorderLayout.NORTH);
    this.pnlMain.add(this.pnlEast, BorderLayout.EAST);
    this.pnlMain.add(this.pnlSouth, BorderLayout.SOUTH);
    this.initLabelList();
    this.layout = new GridLayout(this.labelList.size(), 1);
    this.pnlCenter.setLayout(this.layout);
    this.pnlMain.add(this.pnlCenter, BorderLayout.CENTER);
    this.lblNorth.setIcon(Util.SW_ICON);
  }

  /**
   * 初始化链表
   */
  private void initLabelList() {
    String strGithubCode = "https://github.com/xiboliya/snowpad";
    String strCodeCloud = "https://gitee.com/xiboliya/snowpad";
    String strGitCode = "https://gitcode.net/chenzhengfeng/snowpad";
    String[] arrStrLabel = new String[] {
        "软件名称：" + Util.SOFTWARE,
        "软件版本：" + Util.VERSION,
        "软件作者：冰原",
        "作者QQ：155222113",
        "作者邮箱：chenzhengfeng@163.com",
        "<html>GitHub：<a href='" + strGithubCode + "'>" + strGithubCode + "</a></html>",
        "<html>Gitee：<a href='" + strCodeCloud + "'>" + strCodeCloud + "</a></html>",
        "<html>GitCode：<a href='" + strGitCode + "'>" + strGitCode + "</a></html>",
        "软件版权：遵循GNU GPL第三版开源许可协议的相关条款" };
    int length = arrStrLabel.length;
    for (int i = 0; i < length; i++) {
      this.appendLabelList(arrStrLabel[i]);
    }
    this.addLinkByIndex(5, strGithubCode);
    this.addLinkByIndex(6, strCodeCloud);
    this.addLinkByIndex(7, strGitCode);
  }

  /**
   * 追加一个空的显示标签
   * 
   * @param strLabel 标签显示文本
   */
  private void appendLabelList(String strLabel) {
    JLabel lblTemp = new JLabel(strLabel);
    this.labelList.add(lblTemp);
    this.pnlCenter.add(lblTemp);
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.btnOk.addActionListener(this);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 为指定下标的标签设置链接
   * 
   * @param index 标签的下标
   * @param strLink 链接字符串
   */
  private void addLinkByIndex(int index, final String strLink) {
    if (index < 0 || index >= this.labelList.size() || Util.isTextEmpty(strLink)) {
      return;
    }
    JLabel lblTemp = this.labelList.get(index);
    lblTemp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    lblTemp.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        try {
          if (Util.OS_NAME.indexOf("Windows") >= 0) {
            // Windows系统，将调用系统命令，打开网页
            Runtime.getRuntime().exec("cmd /c start " + strLink);
          } else if (Util.OS_NAME.indexOf("Mac") >= 0) {
            // MacOS系统，将调用系统命令，打开网页
            Runtime.getRuntime().exec("open " + strLink);
          } else { // 如为Unix/Linux系统，则试图使用特定浏览器打开
            openLinkByBrowser(0, strLink);
          }
        } catch (Exception x) {
          // 如果操作系统不支持上述命令，将抛出异常
          // x.printStackTrace();
        }
      }
    });
  }

  /**
   * 使用特定的浏览器打开链接
   * 
   * @param index 浏览器数组的索引
   * @param strLink 链接字符串
   */
  private void openLinkByBrowser(int index, final String strLink) {
    String[] arrBrowser = new String[] { "chrome", "firefox", "opera" };
    if (index < 0 || index >= arrBrowser.length) {
      return;
    } else {
      try {
        Runtime.getRuntime().exec(arrBrowser[index] + " " + strLink);
      } catch (Exception x) {
        // 如果未能正常打开链接，则递归调用本方法，试图使用上述数组中的下一个浏览器打开
        this.openLinkByBrowser(++index, strLink);
        // x.printStackTrace();
      }
    }
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    }
  }

  /**
   * 默认的"确定"操作方法
   */
  @Override
  public void onEnter() {
    this.dispose();
  }

  /**
   * 默认的"取消"操作方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }
}
