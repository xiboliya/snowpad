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

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

/**
 * "帮助主题"JFrame窗口
 * 
 * @author 冰原
 * 
 */
public class HelpFrame extends JFrame implements TreeSelectionListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private StringBuilder stbTitle = new StringBuilder(Util.HELP_TITLE); // 标题栏字符串
  private JSplitPane spnMain = new JSplitPane();
  private BaseTreeNode treeNode = new BaseTreeNode(Util.SOFTWARE);
  private JTree treeMain = new JTree(treeNode);
  private JTextArea txaMain = new JTextArea();

  public HelpFrame() {
    this.setTitle(this.stbTitle.toString());
    this.setSize(400, 400);
    this.setMinimumSize(new Dimension(300, 300)); // 设置主界面的最小尺寸
    this.setLocationRelativeTo(null); // 使窗口居中显示
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.init();
    this.addListeners();
    this.initTree();
    this.setIcon();
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.pnlMain.add(this.spnMain);
    this.spnMain.setLeftComponent(new JScrollPane(this.treeMain));
    this.spnMain.setRightComponent(new JScrollPane(this.txaMain));
    this.spnMain.setDividerLocation(120);
    this.spnMain.setResizeWeight(0.3);
    this.treeMain.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); // 设置JTree组件一次只能选择一个节点
    this.txaMain.setEditable(false);
    this.txaMain.setLineWrap(true);
    this.txaMain.setWrapStyleWord(true);
    this.txaMain.setTabSize(2);
  }

  /**
   * 初始化JTree组件
   */
  private void initTree() {
    BaseTreeNode treeNodeLevel1 = null;
    BaseTreeNode treeNodeLevel2 = null;
    BaseTreeNode treeNodeLevel3 = null;
    URL url = ClassLoader.getSystemResource("res/help.res");
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
      String line = reader.readLine();
      while (line != null) {
        if (line.trim().length() < 3) {
          line = reader.readLine();
          continue;
        }
        char type = line.charAt(0);
        String[] splitLine = line.substring(1).split(Util.PARAM_SPLIT, -1);
        switch (type) {
        case '0':
          this.treeNode.setContent(Util.transfer(splitLine[0]));
          break;
        case '1':
          treeNodeLevel1 = new BaseTreeNode(splitLine[0], Util.transfer(splitLine[1]));
          this.treeNode.add(treeNodeLevel1);
          break;
        case '2':
          if (treeNodeLevel1 != null) {
            treeNodeLevel1.add(treeNodeLevel2 = new BaseTreeNode(splitLine[0], Util.transfer(splitLine[1])));
          }
          break;
        case '3':
          if (treeNodeLevel2 != null) {
            treeNodeLevel2.add(treeNodeLevel3 = new BaseTreeNode(splitLine[0], Util.transfer(splitLine[1])));
          }
          break;
        case '4':
          if (treeNodeLevel3 != null) {
            treeNodeLevel3.add(new BaseTreeNode(splitLine[0], Util.transfer(splitLine[1])));
          }
          break;
        }
        line = reader.readLine();
      }
    this.treeMain.expandRow(0); // 展开第一级节点
    this.treeMain.setSelectionRow(0); // 选择首行节点
    } catch (Exception x) {
      // x.printStackTrace();
    } finally {
      try {
        reader.close();
      } catch (IOException x) {
        // x.printStackTrace();
      }
    }
  }

  /**
   * 设置自定义的窗口图标
   */
  private void setIcon() {
    try {
      this.setIconImage(Util.SW_ICON.getImage());
    } catch (Exception x) {
      // x.printStackTrace();
    }
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.treeMain.addTreeSelectionListener(this);
  }

  /**
   * 当监听的JTree组件选择发生变化时，将触发此事件
   */
  public void valueChanged(TreeSelectionEvent e) {
    BaseTreeNode treeNode = (BaseTreeNode) this.treeMain.getLastSelectedPathComponent();
    if (treeNode == null) {
      return;
    }
    this.stbTitle = new StringBuilder(Util.HELP_TITLE + " - " + treeNode.toString());
    this.setTitle(this.stbTitle.toString());
    this.txaMain.setText(treeNode.getContent());
  }

}
