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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseComparator;
import com.xiboliya.snowpad.base.BaseTreeCellRenderer;
import com.xiboliya.snowpad.base.BaseTreeNode;
import com.xiboliya.snowpad.dialog.RenameDialog;
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
  private static final ImageIcon REFRESH_ICON = new ImageIcon(ClassLoader.getSystemResource("res/refresh.png")); // 刷新图标
  private static final ImageIcon CLOSE_ICON = new ImageIcon(ClassLoader.getSystemResource("res/close.png")); // 关闭图标
  // 文件大小的单位换算比例
  private static final int UNIT_RATE = 1024;
  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
  private SnowPadFrame owner;
  private JLabel lblTitleText = new JLabel("文件树");
  private BaseButton btnRefresh = new BaseButton();
  private BaseButton btnClose = new BaseButton();
  private GridLayout layoutBtn = new GridLayout(1, 2);
  private JPanel pnlBtn = new JPanel();
  private JPanel pnlTitle = new JPanel();
  private BorderLayout layoutTitle = new BorderLayout();
  private BorderLayout layout = new BorderLayout();
  private BaseTreeNode treeNode = new BaseTreeNode("文件系统");
  private JTree treeMain = new JTree(this.treeNode);
  private KeyAdapter keyAdapter = null;
  private MouseAdapter mouseAdapter = null;
  private BaseComparator comparator = new BaseComparator();
  private DragSource dragSource = new DragSource();
  private DragGestureRecognizer dragGestureRecognizer = null;
  private JPopupMenu popMenuFile = new JPopupMenu();
  private JMenuItem itemPopFileOpen = new JMenuItem("打开文件(O)", 'O');
  private JMenuItem itemPopFileCopy = new JMenuItem("复制文件名(C)", 'C');
  private JMenuItem itemPopFileRename = new JMenuItem("重命名文件(R)", 'R');
  private JMenuItem itemPopFileInformation = new JMenuItem("查看文件属性(I)", 'I');
  private JMenuItem itemPopFileDelete = new JMenuItem("删除文件(D)", 'D');
  private RenameDialog renameDialog = null;
  private JPopupMenu popMenuDir = new JPopupMenu();
  private JMenuItem itemPopDirRefresh = new JMenuItem("刷新目录(F)", 'F');
  private JMenuItem itemPopDirCopy = new JMenuItem("复制目录名(C)", 'C');
  private JMenuItem itemPopDirRename = new JMenuItem("重命名目录(R)", 'R');
  private JMenuItem itemPopDirInformation = new JMenuItem("查看目录属性(I)", 'I');
  private JMenuItem itemPopDirNew = new JMenuItem("新建目录(N)", 'N');

  public FileTreePanel(SnowPadFrame owner) {
    this.owner = owner;
    this.init();
    this.addListeners();
  }

  /**
   * 初始化
   */
  private void init() {
    this.simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
    this.setLayout(this.layout);
    this.pnlBtn.setLayout(this.layoutBtn);
    this.btnRefresh.setIcon(REFRESH_ICON);
    this.btnRefresh.setFocusable(false);
    this.btnRefresh.setToolTipText("刷新");
    this.btnClose.setIcon(CLOSE_ICON);
    this.btnClose.setFocusable(false);
    this.btnClose.setToolTipText("关闭");
    this.pnlBtn.add(this.btnRefresh);
    this.pnlBtn.add(this.btnClose);
    this.pnlTitle.setLayout(this.layoutTitle);
    this.pnlTitle.setBorder(new EmptyBorder(0, 5, 0, 0));
    this.pnlTitle.add(this.lblTitleText, BorderLayout.WEST);
    this.pnlTitle.add(this.pnlBtn, BorderLayout.EAST);
    this.add(this.pnlTitle, BorderLayout.NORTH);
    this.add(new JScrollPane(this.treeMain), BorderLayout.CENTER);
    this.initFileTree();
    this.initPopMenu();
    this.setVisible(true);
    this.dragGestureRecognizer = this.dragSource.createDefaultDragGestureRecognizer(this.treeMain, DnDConstants.ACTION_COPY_OR_MOVE, this);
  }

  /**
   * 初始化文件树
   */
  private void initFileTree() {
    this.treeMain.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); // 设置JTree组件一次只能选择一个节点
    this.treeMain.setCellRenderer(new BaseTreeCellRenderer());
    this.refreshAll();
  }

  /**
   * 初始化快捷菜单
   */
  private void initPopMenu() {
    // 文件菜单
    this.popMenuFile.add(this.itemPopFileOpen);
    this.popMenuFile.add(this.itemPopFileCopy);
    this.popMenuFile.add(this.itemPopFileRename);
    this.popMenuFile.add(this.itemPopFileInformation);
    this.popMenuFile.add(this.itemPopFileDelete);
    Dimension popSizeFile = this.popMenuFile.getPreferredSize();
    popSizeFile.width += popSizeFile.width / 5; // 为了美观，适当加宽菜单的显示
    this.popMenuFile.setPopupSize(popSizeFile);
    // 目录菜单
    this.popMenuDir.add(this.itemPopDirRefresh);
    this.popMenuDir.add(this.itemPopDirCopy);
    this.popMenuDir.add(this.itemPopDirRename);
    this.popMenuDir.add(this.itemPopDirInformation);
    this.popMenuDir.add(this.itemPopDirNew);
    Dimension popSizeDir = this.popMenuDir.getPreferredSize();
    popSizeDir.width += popSizeDir.width / 5; // 为了美观，适当加宽菜单的显示
    this.popMenuDir.setPopupSize(popSizeDir);
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
    }
    if (file.isDirectory()) {
      // 如果当前节点已经添加过子节点，则不重复添加
      int childCount = treeNode.getChildCount();
      if (childCount > 0) {
        return;
      }
      File[] files = file.listFiles();
      if (files == null || files.length == 0) {
        return;
      }
      Arrays.sort(files, this.comparator);
      // 目录在前，文件在后
      ArrayList<File> fileList = new ArrayList<File>();
      for (File itemFile : files) {
        if (itemFile.isDirectory()) {
          treeNode.add(new BaseTreeNode(this.getViewName(itemFile), itemFile.getAbsolutePath()));
        } else {
          fileList.add(itemFile);
        }
      }
      for (File itemFile : fileList) {
        treeNode.add(new BaseTreeNode(this.getViewName(itemFile), itemFile.getAbsolutePath()));
      }
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
      public void keyPressed(KeyEvent e) {
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
        } else if (e.getButton() == MouseEvent.BUTTON3) { // 点击右键时，显示快捷菜单
          // 获取鼠标点击位置的树路径
          int row = treeMain.getRowForLocation(e.getX(), e.getY());
          // row为0时，表示根目录，跳过此目录
          if (row > 0) {
            // 选中右键点击的节点
            treeMain.setSelectionRow(row);
            File file = getCurrentFile();
            if (file != null) {
              if (file.isFile()) {
                popMenuFile.show(treeMain, e.getX(), e.getY());
              } else if (file.isDirectory()) {
                popMenuDir.show(treeMain, e.getX(), e.getY());
              }
            }
          }
        }
      }
    };
    this.btnRefresh.addActionListener(this);
    this.btnClose.addActionListener(this);
    this.treeMain.addTreeExpansionListener(this);
    this.treeMain.addKeyListener(this.keyAdapter);
    this.treeMain.addMouseListener(this.mouseAdapter);
    this.itemPopFileOpen.addActionListener(this);
    this.itemPopFileCopy.addActionListener(this);
    this.itemPopFileRename.addActionListener(this);
    this.itemPopFileInformation.addActionListener(this);
    this.itemPopFileDelete.addActionListener(this);
    this.itemPopDirRefresh.addActionListener(this);
    this.itemPopDirCopy.addActionListener(this);
    this.itemPopDirRename.addActionListener(this);
    this.itemPopDirInformation.addActionListener(this);
    this.itemPopDirNew.addActionListener(this);
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
   * 获取当前选中的节点
   * @return 当前选中的节点
   */
  private BaseTreeNode getCurrentNode() {
    TreePath treePath = this.treeMain.getSelectionPath();
    if (treePath == null) {
      return null;
    }
    return (BaseTreeNode)treePath.getLastPathComponent();
  }

  /**
   * 获取当前节点的父节点
   * @return 当前节点的父节点
   */
  private BaseTreeNode getParentNode() {
    BaseTreeNode node = this.getCurrentNode();
    if (node == null) {
      return null;
    }
    return (BaseTreeNode)node.getParent();
  }

  /**
   * 获取当前选中的文件
   * @return 当前选中的文件
   */
  private File getCurrentFile() {
    BaseTreeNode node = this.getCurrentNode();
    if (node == null) {
      return null;
    }
    return new File(node.getContent());
  }

  /**
   * 打开文件
   */
  private void openFile() {
    BaseTreeNode node = this.getCurrentNode();
    if (node == null) {
      return;
    }
    if (node.isLeaf()) {
      File file = new File(node.getContent());
      if (!file.isDirectory()) {
        owner.openFile(file);
      }
    }
  }

  /**
   * 复制文件名/目录名
   */
  private void copyName() {
    BaseTreeNode node = this.getCurrentNode();
    if (node == null) {
      return;
    }
    File file = new File(node.getContent());
    String name = file.getName();
    if (!Util.isTextEmpty(name)) {
      StringSelection ss = new StringSelection(name);
      Util.clipboard.setContents(ss, ss);
    }
  }

  /**
   * 重命名文件/目录
   */
  private void rename() {
    BaseTreeNode node = this.getCurrentNode();
    if (node == null) {
      return;
    }
    File file = new File(node.getContent());
    if (file.exists()) {
      this.openRenameDialog(file);
      this.refreshParentPath();
    }
  }

  /**
   * 打开重命名对话框
   * @param file 当前选中的文件
   */
  private void openRenameDialog(File file) {
    if (this.renameDialog == null) {
      this.renameDialog = new RenameDialog(this.owner, true, file);
    } else {
      this.renameDialog.setFile(file);
      this.renameDialog.setVisible(true);
    }
  }

  /**
   * 查看文件属性
   */
  private void showFileInformation() {
    BaseTreeNode node = this.getCurrentNode();
    if (node == null) {
      return;
    }
    File file = new File(node.getContent());
    if (!file.isFile()) {
      return;
    }
    StringBuilder stbFileInfo = new StringBuilder();
    stbFileInfo.append("文件路径：").append(file.getAbsolutePath()).append("\n");
    stbFileInfo.append("修改时间：").append(this.simpleDateFormat.format(file.lastModified())).append("\n");
    stbFileInfo.append("文件大小：").append(this.formatFileSize(file.length()));
    JOptionPane.showMessageDialog(this, Util.convertToMsg(stbFileInfo.toString()),
        Util.SOFTWARE, JOptionPane.PLAIN_MESSAGE);
  }

  /**
   * 格式化文件大小的显示
   * 
   * @param fileSize 文件字节数
   * @return 格式化后的文件大小的显示
   */
  private String formatFileSize(double fileSize) {
    if (fileSize < UNIT_RATE) {
      return this.formatNumber(fileSize) + " B(字节)";
    } else if (fileSize > UNIT_RATE * UNIT_RATE) {
      float size = (float)(fileSize / UNIT_RATE / UNIT_RATE);
      return this.formatNumber(size) + " MB(兆字节)";
    } else {
      float size = (float)(fileSize / UNIT_RATE);
      return this.formatNumber(size) + " KB(千字节)";
    }
  }

  /**
   * 格式化数字
   * 
   * @param number 原始数字
   * @return 格式化后的数字
   */
  private String formatNumber(double number) {
    StringBuilder stbFileSize = new StringBuilder(String.format("%.3f", number));
    while (stbFileSize.charAt(stbFileSize.length() - 1) == '0') {
      stbFileSize.deleteCharAt(stbFileSize.length() - 1);
    }
    if (stbFileSize.charAt(stbFileSize.length() - 1) == '.') {
      stbFileSize.deleteCharAt(stbFileSize.length() - 1);
    }
    return stbFileSize.toString();
  }

  /**
   * 刷新父目录
   */
  private void refreshParentPath() {
    BaseTreeNode node = this.getParentNode();
    this.refreshPath(node);
  }

  /**
   * 刷新当前目录
   */
  private void refreshCurrentPath() {
    BaseTreeNode node = this.getCurrentNode();
    this.refreshPath(node);
  }

  /**
   * 刷新指定目录
   * @param node 需要刷新的目录
   */
  private void refreshPath(BaseTreeNode node) {
    if (node == null) {
      return;
    }
    node.removeAllChildren();
    File parentFile = new File(node.getContent());
    if (!parentFile.exists() || !parentFile.isDirectory()) {
      this.treeMain.updateUI();
      return;
    }
    File[] files = parentFile.listFiles();
    if (files == null || files.length == 0) {
      this.treeMain.updateUI();
      return;
    }
    Arrays.sort(files, this.comparator);
    // 重新构建子节点（目录在前，文件在后）
    ArrayList<File> fileList = new ArrayList<File>();
    for (File itemFile : files) {
      if (itemFile.isDirectory()) {
        BaseTreeNode dirNode = new BaseTreeNode(this.getViewName(itemFile), itemFile.getAbsolutePath());
        node.add(dirNode);
        // 为子目录添加下一级节点
        this.addNextNode(dirNode, itemFile);
      } else {
        fileList.add(itemFile);
      }
    }
    for (File itemFile : fileList) {
      node.add(new BaseTreeNode(this.getViewName(itemFile), itemFile.getAbsolutePath()));
    }
    this.treeMain.updateUI();
  }

  /**
   * 删除文件
   */
  private void deleteFile() {
    BaseTreeNode node = this.getCurrentNode();
    if (node == null || !node.isLeaf()) {
      return;
    }
    File file = new File(node.getContent());
    if (!file.exists()) {
      return;
    }
    int result = JOptionPane.showConfirmDialog(this, Util.convertToMsg("此操作将删除磁盘文件：" + file + "\n是否继续？"),
          Util.SOFTWARE, JOptionPane.YES_NO_CANCEL_OPTION);
    if (result != JOptionPane.YES_OPTION) {
      return;
    }
    if (file.delete()) {
      this.refreshParentPath();
    } else {
      JOptionPane.showMessageDialog(this, Util.convertToMsg("文件：" + file + "删除失败！"),
          Util.SOFTWARE, JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * 查看目录属性
   */
  private void showDirInformation() {
    BaseTreeNode node = this.getCurrentNode();
    if (node == null) {
      return;
    }
    final File dir = new File(node.getContent());
    if (!dir.isDirectory()) {
      return;
    }
    final JLabel lblInfo = new JLabel();
    this.showDirInformation(dir, null, lblInfo);
    JOptionPane pane = new JOptionPane(lblInfo, JOptionPane.PLAIN_MESSAGE);
    final JDialog dialog = pane.createDialog(this, Util.SOFTWARE);
    // 非阻塞弹窗
    dialog.setModal(false);

    final SwingWorker<int[], Void> worker = new SwingWorker<int[], Void>() {

      /**
       * 子线程执行：耗时操作
       * @return 返回结果
       */
      @Override
      protected int[] doInBackground() throws Exception {
        int[] counts = new int[]{0, 0};
        getElementCount(dir, counts, this);
        return counts;
      }

      /**
       * 主线程执行：任务完成后更新UI
       */
      @Override
      protected void done() {
        // 弹窗已关闭或任务已取消，则不进行UI处理
        if (dialog == null || !dialog.isDisplayable() || !dialog.isVisible() || this.isCancelled()) {
          return;
        }
        try {
          // 获取子线程返回的结果，即doInBackground接口的返回值
          int[] counts = get();
          showDirInformation(dir, counts, lblInfo);
        } catch (Exception x) {
          // x.printStackTrace();
        }
      }
    };

    pane.addPropertyChangeListener(JOptionPane.VALUE_PROPERTY, new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent e) {
        // 监听弹窗属性变化
        if (!worker.isCancelled()) {
          worker.cancel(true);
        }
      }
    });
    dialog.setVisible(true);

    // 启动后台任务
    worker.execute();
  }

  private void showDirInformation(File dir, int[] counts, JLabel lblInfo) {
    StringBuilder stbFileInfo = new StringBuilder();
    stbFileInfo.append("目录路径：").append(dir.getAbsolutePath()).append("\n");
    stbFileInfo.append("修改时间：").append(this.simpleDateFormat.format(dir.lastModified())).append("\n");
    if (counts == null) {
      stbFileInfo.append("子目录数：").append("统计中...").append("\n");
      stbFileInfo.append("子文件数：").append("统计中...");
    } else {
      stbFileInfo.append("子目录数：").append(counts[0]).append("\n");
      stbFileInfo.append("子文件数：").append(counts[1]);
    }
    String text = "<html>" + Util.convertToMsg(stbFileInfo.toString()).replace("\n", "<br>") + "</html>";
    lblInfo.setText(text);
  }

  private void getElementCount(File dir, int[] counts, SwingWorker<int[], Void> worker) {
    if (worker.isCancelled()) {
      return;
    }
    File[] files = dir.listFiles();
    if (files == null) {
      return;
    }
    for (File file : files) {
      if (worker.isCancelled()) {
        return;
      }
      if (file.isDirectory()) {
        counts[0] ++;
        this.getElementCount(file, counts, worker);
      } else if (file.isFile()) {
        counts[1] ++;
      }
    }
  }

  /**
   * 新建目录
   */
  private void createNewDir() {
    BaseTreeNode node = this.getCurrentNode();
    if (node == null) {
      return;
    }
    for (int i = 1; ; i++) {
      File file = new File(node.getContent() + "/新建目录" + i);
      if (!file.exists()) {
        boolean success = file.mkdirs();
        if (!success) {
          JOptionPane.showMessageDialog(this, Util.convertToMsg("新建目录失败！"),
              Util.SOFTWARE, JOptionPane.ERROR_MESSAGE);
        } else {
          this.refreshCurrentPath();
        }
        break;
      }
    }

  }

  /**
   * 刷新文件树
   */
  private void refreshAll() {
    this.treeNode.removeAllChildren();
    File[] roots = File.listRoots();
    if (roots == null || roots.length == 0) {
      this.treeMain.updateUI();
      return;
    }
    Arrays.sort(roots, this.comparator);
    for (File root : roots) {
      BaseTreeNode rootNode = new BaseTreeNode(this.getViewName(root), root.getAbsolutePath());
      this.treeNode.add(rootNode);
      this.addNextNode(rootNode, root);
    }
    this.treeMain.expandRow(0); // 展开第一级节点
    this.treeMain.setSelectionRow(0); // 选择首行节点
    this.treeMain.updateUI();
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
    Object source = e.getSource();
    if (this.btnRefresh.equals(source)) {
      this.refreshAll();
    } else if (this.btnClose.equals(source)) {
      this.close();
    } else if (this.itemPopFileOpen.equals(source)) {
      this.openFile();
    } else if (this.itemPopFileCopy.equals(source)) {
      this.copyName();
    } else if (this.itemPopFileRename.equals(source)) {
      this.rename();
    } else if (this.itemPopFileInformation.equals(source)) {
      this.showFileInformation();
    } else if (this.itemPopFileDelete.equals(source)) {
      this.deleteFile();
    } else if (this.itemPopDirRefresh.equals(source)) {
      this.refreshCurrentPath();
    } else if (this.itemPopDirCopy.equals(source)) {
      this.copyName();
    } else if (this.itemPopDirRename.equals(source)) {
      this.rename();
    } else if (this.itemPopDirInformation.equals(source)) {
      this.showDirInformation();
    } else if (this.itemPopDirNew.equals(source)) {
      this.createNewDir();
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
        if (file.isDirectory()) {
          this.addNextNode(childNode, file);
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
