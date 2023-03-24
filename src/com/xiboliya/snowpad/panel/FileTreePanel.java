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

package com.xiboliya.snowpad.panel;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.xiboliya.snowpad.base.BaseComparator;
import com.xiboliya.snowpad.base.BaseTreeCellRenderer;
import com.xiboliya.snowpad.base.BaseTreeNode;
import com.xiboliya.snowpad.frame.SnowPadFrame;
import com.xiboliya.snowpad.util.Util;

/**
 * 自定义的文件树面板
 * 
 * @author 冰原
 * 
 */
public class FileTreePanel extends JPanel implements ActionListener, TreeExpansionListener, DragGestureListener {
  private static final long serialVersionUID = 1L;
  private static final ImageIcon CLOSE_ICON = new ImageIcon(ClassLoader.getSystemResource("res/close.png")); // 关闭图标
  private SnowPadFrame owner;
  private JLabel lblTitleText = new JLabel("文件树");
  private JButton btnTitleClose = new JButton();
  private JPanel pnlTitle = new JPanel();
  private BorderLayout layoutTitle = new BorderLayout();
  private BorderLayout layout = new BorderLayout();
  private BaseTreeNode treeNode = new BaseTreeNode("文件系统");
  private JTree treeMain = new JTree(treeNode);
  private KeyAdapter keyAdapter = null;
  private MouseAdapter mouseAdapter = null;
  private BaseComparator comparator = new BaseComparator();
  private DragSource dragSource = new DragSource();
  private DragGestureRecognizer dragGestureRecognizer = null;

  public FileTreePanel(SnowPadFrame owner) {
    this.owner = owner;
    this.init();
    this.addListeners();
  }

  /**
   * 初始化
   */
  private void init() {
    this.setLayout(this.layout);
    this.pnlTitle.setLayout(this.layoutTitle);
    this.pnlTitle.setBorder(new EmptyBorder(0, 5, 0, 0));
    this.btnTitleClose.setMargin(new Insets(0, 0, 0, 0));
    this.btnTitleClose.setIcon(CLOSE_ICON);
    this.pnlTitle.add(this.lblTitleText, BorderLayout.WEST);
    this.pnlTitle.add(this.btnTitleClose, BorderLayout.EAST);
    this.add(this.pnlTitle, BorderLayout.NORTH);
    this.add(new JScrollPane(this.treeMain), BorderLayout.CENTER);
    this.initFileTree();
    this.setVisible(true);
    this.dragGestureRecognizer = this.dragSource.createDefaultDragGestureRecognizer(this.treeMain, DnDConstants.ACTION_COPY_OR_MOVE, this);
  }

  /**
   * 初始化文件树
   */
  private void initFileTree() {
    this.treeMain.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); // 设置JTree组件一次只能选择一个节点
    this.treeMain.setCellRenderer(new BaseTreeCellRenderer());
    File[] roots = File.listRoots();
    if (roots == null || roots.length == 0) {
      return;
    }
    Arrays.sort(roots, this.comparator);
    for (File root : roots) {
      this.addNextNode(treeNode, root);
    }
    this.treeMain.expandRow(0); // 展开第一级节点
    this.treeMain.setSelectionRow(0); // 选择首行节点
  }

  /**
   * 将文件或目录添加到树的特定节点中
   * 
   * @param treeNode 树节点
   * @param file 文件
   */
  private void addNextNode(BaseTreeNode treeNode, File file) {
    if (file == null) {
      return;
    } else if (file.isDirectory()) {
      BaseTreeNode dirNode = new BaseTreeNode(this.getViewName(file), file.getAbsolutePath());
      treeNode.add(dirNode);
      File[] files = file.listFiles();
      if (files == null || files.length == 0) {
        return;
      }
      Arrays.sort(files, this.comparator);
      for (File itemFile : files) {
        int level = dirNode.getLevel();
        if (level < 2) {
          this.addNextNode(dirNode, itemFile);
        }
      }
    } else {
      treeNode.add(new BaseTreeNode(this.getViewName(file), file.getAbsolutePath()));
    }
  }

  /**
   * 获取文件在树中的显示名称
   * 
   * @param file 文件
   * @return 文件在树中的显示名称
   */
  private String getViewName(File file) {
    String fileName = file.getName();
    if (Util.isTextEmpty(fileName)) {
      fileName = String.valueOf(file);
    }
    return fileName;
  }

  /**
   * 添加各组件的事件监听器
   */
  private void addListeners() {
    this.keyAdapter = new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          // 按回车键，打开文件、展开目录、折叠目录
          operate(true);
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
          // 按ESC键，关闭面板
          close();
        }
      }
    };
    this.mouseAdapter = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          // 双击鼠标，打开文件
          operate(false);
        }
      }
    };
    this.btnTitleClose.addActionListener(this);
    this.treeMain.addTreeExpansionListener(this);
    this.treeMain.addKeyListener(this.keyAdapter);
    this.treeMain.addMouseListener(this.mouseAdapter);
  }

  /**
   * 触发按键和鼠标点击事件后，操作文件或树
   * 
   * @param isOperateTree 是否操作树：展开目录、折叠目录
   */
  private void operate(boolean isOperateTree) {
    TreePath treePath = this.treeMain.getSelectionPath();
    if (treePath == null) {
      return;
    }
    BaseTreeNode node = (BaseTreeNode)treePath.getLastPathComponent();
    if (node.isLeaf()) {
      // 打开文件
      File file = new File(node.getContent());
      if (!file.isDirectory()) {
        owner.openFile(file);
      }
    } else if (isOperateTree) {
      if (this.treeMain.isExpanded(treePath)) {
        // 折叠目录
        this.treeMain.collapsePath(treePath);
      } else {
        // 展开目录
        this.treeMain.expandPath(treePath);
      }
    }
  }

  /**
   * 关闭面板
   */
  private void close() {
    this.owner.viewFileTree(false);
  }

  /**
   * 发生操作时调用
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (this.btnTitleClose.equals(e.getSource())) {
      this.close();
    }
  }

  /**
   * 每当树中的一个项被展开时调用
   */
  @Override
  public void treeExpanded(TreeExpansionEvent e) {
    TreePath treePath = e.getPath();
    BaseTreeNode node = (BaseTreeNode)treePath.getLastPathComponent();
    // 将叶节点目录的下一级文件或目录添加到树中
    if (!node.isLeaf()) {
      int childCount = node.getChildCount();
      if (childCount <= 0) {
        return;
      }
      for (int i = 0; i < childCount; i++) {
        BaseTreeNode childNode = (BaseTreeNode)node.getChildAt(i);
        File file = new File(childNode.getContent());
        if (!file.isDirectory()) {
          continue;
        }
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
          continue;
        }
        Arrays.sort(files, this.comparator);
        for (File itemFile : files) {
          childNode.add(new BaseTreeNode(this.getViewName(itemFile), itemFile.getAbsolutePath()));
        }
      }
    }
  }

  /**
   * 每当树中的一个项被折叠时调用
   */
  @Override
  public void treeCollapsed(TreeExpansionEvent e) {
  }

  /**
   * 已经检测到与平台有关的拖动启动动作时调用
   */
  @Override
  public void dragGestureRecognized(DragGestureEvent e) {
    TreePath treePath = this.treeMain.getSelectionPath();
    if (treePath == null) {
      return;
    }
    BaseTreeNode node = (BaseTreeNode)treePath.getLastPathComponent();
    if (node.isLeaf()) {
      // 打开文件
      final File file = new File(node.getContent());
      if (!file.isDirectory()) {
        Transferable transferable = new Transferable() {
          @Override
          public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.javaFileListFlavor};
          }

          @Override
          public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.javaFileListFlavor.equals(flavor);
          }

          @Override
          public Object getTransferData(DataFlavor flavor) {
            return Arrays.asList(file);
          }
        };
        this.dragSource.startDrag(e, DragSource.DefaultCopyDrop, transferable, null);
      }
    }
  }
}
