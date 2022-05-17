package Comphex;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Angel Ponce
 */
public class Comphex extends javax.swing.JFrame {

    DefaultMutableTreeNode rootProjects = new DefaultMutableTreeNode("Comphex");
    DefaultMutableTreeNode rootNavigator = new DefaultMutableTreeNode("Navigator");
    public final static ImageIcon FILE_IMG = new ImageIcon(Comphex.class.getResource("/Images/file.png"));
    public final static ImageIcon ERROR_IMG = new ImageIcon(Comphex.class.getResource("/Images/error.png"));
    public final static ImageIcon INFOR_IMG = new ImageIcon(Comphex.class.getResource("/Images/info.png"));
    public final static ImageIcon FILE_IMGX16 = new ImageIcon(Comphex.class.getResource("/Images/file-16.png"));
    public final static ImageIcon FOLDER_IMGX16 = new ImageIcon(Comphex.class.getResource("/Images/folder-16.png"));
    private final static File PROJECTS = new File("Projects");
    private static Project currentProject;
    public static final SimpleAttributeSet ERROR_SAS = new SimpleAttributeSet();
    public static final SimpleAttributeSet SUCCESS_SAS = new SimpleAttributeSet();
    public static final SimpleAttributeSet PK_SAS = new SimpleAttributeSet();
    public static final SimpleAttributeSet LABEL_SAS = new SimpleAttributeSet();
    public static final SimpleAttributeSet UNDERLINE_SAS = new SimpleAttributeSet();
    public static final SimpleAttributeSet COMMENT_SAS = new SimpleAttributeSet();
    public static final SimpleAttributeSet WARNING_SAS = new SimpleAttributeSet();
    public static Configuration configuration = new Configuration("com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme", 14);

    static {
        FlatAnimatedLafChange.duration = 750;
        StyleConstants.setBold(PK_SAS, true);
        StyleConstants.setForeground(PK_SAS, new Color(57, 173, 166));
        StyleConstants.setBold(LABEL_SAS, true);
        StyleConstants.setForeground(LABEL_SAS, Color.ORANGE);
        StyleConstants.setItalic(LABEL_SAS, true);
        StyleConstants.setForeground(ERROR_SAS, new Color(252, 53, 80));
        StyleConstants.setBold(ERROR_SAS, true);
        StyleConstants.setForeground(SUCCESS_SAS, new Color(58, 214, 79));
        StyleConstants.setBold(SUCCESS_SAS, true);
        StyleConstants.setUnderline(UNDERLINE_SAS, true);
        StyleConstants.setItalic(UNDERLINE_SAS, true);
        StyleConstants.setForeground(COMMENT_SAS, new Color(103, 165, 191));
        StyleConstants.setForeground(WARNING_SAS, Color.ORANGE);
    }

    public Comphex() {
        Configuration c = Configuration.load();
        if (c != null) {
            configuration = c;
        }
        changeTheme(configuration.getTheme());
        initComponents();
        //Load projects
        if (PROJECTS.exists()) {
            for (File p : PROJECTS.listFiles()) {
                if (p.isDirectory()) {
                    Project project = new Project(p.getName());
                    if (project.projectExist()) {
                        DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(project);
                        rootProjects.add(projectNode);
                    }
                }
            }
        }
        //Load themes
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String t : Theme.NAMES) {
            listModel.addElement(t);
        }
        themeList.setModel(listModel);
        //Add event to some theme clicked
        themeList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    changeTheme(Theme.THEMES[themeList.getSelectedIndex()]);
                    configuration.setTheme(Theme.THEMES[themeList.getSelectedIndex()]);
                }
            }
        });
        //Add click file listener to the tree files
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                int nodeRow = tree.getRowForLocation(me.getX(), me.getY());
                if (nodeRow != -1) {
                    if (me.getClickCount() == 2) {
                        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                        if (selectedNode.isLeaf()) {
                            if (selectedNode.getUserObject() instanceof Project) {
                                openProject((Project) selectedNode.getUserObject());
                            }
                        }
                    }
                }
            }
        });
        //Add click file listener to the navigator files
        navigator.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                int nodeRow = navigator.getRowForLocation(me.getX(), me.getY());
                if (nodeRow != -1) {
                    if (me.getClickCount() == 2) {
                        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) navigator.getLastSelectedPathComponent();
                        if (selectedNode.isLeaf()) {
                            if (selectedNode.getUserObject() instanceof File) {
                                viewFile((File) selectedNode.getUserObject());
                            }
                        }
                    }
                }
            }
        });
        tree.expandRow(tree.getRowCount() - 1);
        fileViewerDialog.setLocationRelativeTo(null);
        themeBuilderDialog.setLocationRelativeTo(null);
        fontSizeDialog.setLocationRelativeTo(null);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        code.setText("\n");
        super.setLocationRelativeTo(null);
        super.setTitle("Comphex");
        fontSize.setValue(configuration.getFontSize());
        fontSizeIndicator.setText("size : " + fontSize.getValue() + "px");
        code.setFont(new Font("Ebrima", Font.PLAIN, fontSize.getValue()));
        console.setFont(new Font("Ebrima", Font.PLAIN, fontSize.getValue()));
        fileViewer.setFont(new Font("Ebrima", Font.PLAIN, fontSize.getValue()));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        fileViewerDialog = new javax.swing.JDialog(this);
        fileViewerContainer = new javax.swing.JPanel();
        fileName = new javax.swing.JLabel();
        scroolForFileViewer = new javax.swing.JScrollPane();
        fileViewer = new javax.swing.JTextPane();
        fileChooser = new javax.swing.JFileChooser();
        themeBuilderDialog = new javax.swing.JDialog(this);
        themeBuilderContainer = new javax.swing.JPanel();
        titleTheme = new javax.swing.JLabel();
        scrollForThemeList = new javax.swing.JScrollPane();
        themeList = new javax.swing.JList<>();
        fontSizeDialog = new javax.swing.JDialog(this);
        fontSizeContainer = new javax.swing.JPanel();
        fontSizeTitle = new javax.swing.JLabel();
        fontSize = new javax.swing.JSlider();
        fontSizeIndicator = new javax.swing.JLabel();
        container = new javax.swing.JPanel();
        toolBar = new javax.swing.JToolBar();
        newFile = new javax.swing.JButton();
        s1 = new javax.swing.JToolBar.Separator();
        openFile = new javax.swing.JButton();
        s2 = new javax.swing.JToolBar.Separator();
        saveFile = new javax.swing.JButton();
        s3 = new javax.swing.JToolBar.Separator();
        deleteFile = new javax.swing.JButton();
        s4 = new javax.swing.JToolBar.Separator();
        exportFile = new javax.swing.JButton();
        s5 = new javax.swing.JToolBar.Separator();
        runFile = new javax.swing.JButton();
        split = new javax.swing.JSplitPane();
        leftContainer = new javax.swing.JPanel();
        scrollForTree = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree(rootProjects);
        scrollForNavigator = new javax.swing.JScrollPane();
        navigator = new javax.swing.JTree(rootNavigator);
        rightContainer = new javax.swing.JPanel();
        codeContainer = new javax.swing.JPanel();
        scrollForCode = new javax.swing.JScrollPane();
        code = new javax.swing.JTextPane();
        consoleContainer = new javax.swing.JPanel();
        toolConsole = new javax.swing.JToolBar();
        closeConsole = new javax.swing.JButton();
        scrollForConsole = new javax.swing.JScrollPane();
        console = new javax.swing.JTextPane();
        menuBar = new javax.swing.JMenuBar();
        fileOption = new javax.swing.JMenu();
        newFileOption = new javax.swing.JMenuItem();
        openFileOption = new javax.swing.JMenuItem();
        saveFileOption = new javax.swing.JMenuItem();
        exportFileOption = new javax.swing.JMenuItem();
        deleteFileOption = new javax.swing.JMenuItem();
        editOption = new javax.swing.JMenu();
        themeOption = new javax.swing.JMenuItem();
        fontSizeOption = new javax.swing.JMenuItem();
        windowOption = new javax.swing.JMenu();
        showConsoleOption = new javax.swing.JCheckBoxMenuItem();
        showNavigatorOption = new javax.swing.JCheckBoxMenuItem();

        fileViewerDialog.setModal(true);
        fileViewerDialog.setSize(new java.awt.Dimension(500, 500));

        fileViewerContainer.setLayout(new java.awt.BorderLayout());

        fileName.setFont(new java.awt.Font("Ebrima", 1, 18)); // NOI18N
        fileName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fileName.setText("Ttitle");
        fileViewerContainer.add(fileName, java.awt.BorderLayout.PAGE_START);

        fileViewer.setFont(new java.awt.Font("Ebrima", 0, 14)); // NOI18N
        scroolForFileViewer.setViewportView(fileViewer);

        fileViewerContainer.add(scroolForFileViewer, java.awt.BorderLayout.CENTER);

        fileViewerDialog.getContentPane().add(fileViewerContainer, java.awt.BorderLayout.CENTER);

        themeBuilderDialog.setModal(true);
        themeBuilderDialog.setSize(new java.awt.Dimension(500, 500));

        themeBuilderContainer.setLayout(new java.awt.BorderLayout());

        titleTheme.setFont(new java.awt.Font("Ebrima", 1, 18)); // NOI18N
        titleTheme.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleTheme.setText("Theme Builder");
        themeBuilderContainer.add(titleTheme, java.awt.BorderLayout.PAGE_START);

        themeList.setFont(new java.awt.Font("Ebrima", 1, 16)); // NOI18N
        themeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollForThemeList.setViewportView(themeList);

        themeBuilderContainer.add(scrollForThemeList, java.awt.BorderLayout.CENTER);

        themeBuilderDialog.getContentPane().add(themeBuilderContainer, java.awt.BorderLayout.CENTER);

        fontSizeDialog.setModal(true);
        fontSizeDialog.setSize(new java.awt.Dimension(500, 150));

        fontSizeContainer.setLayout(new java.awt.BorderLayout());

        fontSizeTitle.setFont(new java.awt.Font("Ebrima", 1, 18)); // NOI18N
        fontSizeTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fontSizeTitle.setText("Font Size");
        fontSizeContainer.add(fontSizeTitle, java.awt.BorderLayout.PAGE_START);

        fontSize.setMajorTickSpacing(10);
        fontSize.setMinimum(10);
        fontSize.setMinorTickSpacing(1);
        fontSize.setPaintLabels(true);
        fontSize.setPaintTicks(true);
        fontSize.setValue(14);
        fontSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fontSizeStateChanged(evt);
            }
        });
        fontSizeContainer.add(fontSize, java.awt.BorderLayout.CENTER);

        fontSizeIndicator.setFont(new java.awt.Font("Ebrima", 1, 18)); // NOI18N
        fontSizeIndicator.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fontSizeIndicator.setText("size : 14px");
        fontSizeContainer.add(fontSizeIndicator, java.awt.BorderLayout.PAGE_END);

        fontSizeDialog.getContentPane().add(fontSizeContainer, java.awt.BorderLayout.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(720, 500));
        setPreferredSize(new java.awt.Dimension(900, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        container.setLayout(new java.awt.BorderLayout());

        toolBar.setRollover(true);
        toolBar.setToolTipText("Tools");

        newFile.setFont(new java.awt.Font("Ebrima", 1, 12)); // NOI18N
        newFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/add-file.png"))); // NOI18N
        newFile.setText("New file");
        newFile.setToolTipText("New file");
        newFile.setFocusable(false);
        newFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newFile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        newFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newFileActionPerformed(evt);
            }
        });
        toolBar.add(newFile);
        toolBar.add(s1);

        openFile.setFont(new java.awt.Font("Ebrima", 1, 12)); // NOI18N
        openFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/open-file.png"))); // NOI18N
        openFile.setText("Open file");
        openFile.setToolTipText("Open file");
        openFile.setFocusable(false);
        openFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openFile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        openFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFileActionPerformed(evt);
            }
        });
        toolBar.add(openFile);
        toolBar.add(s2);

        saveFile.setFont(new java.awt.Font("Ebrima", 1, 12)); // NOI18N
        saveFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/save-file.png"))); // NOI18N
        saveFile.setText("Save file");
        saveFile.setToolTipText("Save file");
        saveFile.setEnabled(false);
        saveFile.setFocusable(false);
        saveFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveFile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        saveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFileActionPerformed(evt);
            }
        });
        toolBar.add(saveFile);
        toolBar.add(s3);

        deleteFile.setFont(new java.awt.Font("Ebrima", 1, 12)); // NOI18N
        deleteFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delete-file.png"))); // NOI18N
        deleteFile.setText("Delete file");
        deleteFile.setFocusable(false);
        deleteFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteFile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        deleteFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteFileActionPerformed(evt);
            }
        });
        toolBar.add(deleteFile);
        toolBar.add(s4);

        exportFile.setFont(new java.awt.Font("Ebrima", 1, 12)); // NOI18N
        exportFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/export-file.png"))); // NOI18N
        exportFile.setText("Export file");
        exportFile.setFocusable(false);
        exportFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        exportFile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        exportFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportFileActionPerformed(evt);
            }
        });
        toolBar.add(exportFile);
        toolBar.add(s5);

        runFile.setFont(new java.awt.Font("Ebrima", 1, 12)); // NOI18N
        runFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/run-file.png"))); // NOI18N
        runFile.setText("Run file");
        runFile.setToolTipText("Run fil");
        runFile.setEnabled(false);
        runFile.setFocusable(false);
        runFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        runFile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        runFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runFileActionPerformed(evt);
            }
        });
        toolBar.add(runFile);

        container.add(toolBar, java.awt.BorderLayout.PAGE_START);

        split.setDividerLocation(200);

        leftContainer.setLayout(new java.awt.GridLayout(2, 1));

        tree.setFont(new java.awt.Font("Ebrima", 0, 14)); // NOI18N
        scrollForTree.setViewportView(tree);

        leftContainer.add(scrollForTree);

        navigator.setFont(new java.awt.Font("Ebrima", 0, 14)); // NOI18N
        scrollForNavigator.setViewportView(navigator);

        leftContainer.add(scrollForNavigator);

        split.setLeftComponent(leftContainer);

        rightContainer.setLayout(new java.awt.GridBagLayout());

        codeContainer.setLayout(new java.awt.BorderLayout());

        code.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Project", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Ebrima", 1, 12))); // NOI18N
        code.setFont(new java.awt.Font("Ebrima", 0, 14)); // NOI18N
        code.setMargin(new java.awt.Insets(5, 5, 5, 5));
        code.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codeKeyReleased(evt);
            }
        });
        scrollForCode.setViewportView(code);

        codeContainer.add(scrollForCode, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.62;
        rightContainer.add(codeContainer, gridBagConstraints);

        consoleContainer.setLayout(new java.awt.BorderLayout());

        toolConsole.setRollover(true);

        closeConsole.setFont(new java.awt.Font("Ebrima", 1, 12)); // NOI18N
        closeConsole.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/close.png"))); // NOI18N
        closeConsole.setText("Output");
        closeConsole.setToolTipText("Close");
        closeConsole.setFocusable(false);
        closeConsole.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        closeConsole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeConsoleActionPerformed(evt);
            }
        });
        toolConsole.add(closeConsole);

        consoleContainer.add(toolConsole, java.awt.BorderLayout.PAGE_START);

        console.setEditable(false);
        console.setFont(new java.awt.Font("Ebrima", 0, 14)); // NOI18N
        scrollForConsole.setViewportView(console);

        consoleContainer.add(scrollForConsole, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.38;
        rightContainer.add(consoleContainer, gridBagConstraints);

        split.setRightComponent(rightContainer);

        container.add(split, java.awt.BorderLayout.CENTER);

        getContentPane().add(container, java.awt.BorderLayout.CENTER);

        fileOption.setText("File");
        fileOption.setFont(new java.awt.Font("Ebrima", 1, 14)); // NOI18N

        newFileOption.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newFileOption.setFont(new java.awt.Font("Ebrima", 1, 12)); // NOI18N
        newFileOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/add-file-16.png"))); // NOI18N
        newFileOption.setText("New file");
        newFileOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newFileOptionActionPerformed(evt);
            }
        });
        fileOption.add(newFileOption);

        openFileOption.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openFileOption.setFont(new java.awt.Font("Ebrima", 1, 12)); // NOI18N
        openFileOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/open-file-16.png"))); // NOI18N
        openFileOption.setText("Open file");
        openFileOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFileOptionActionPerformed(evt);
            }
        });
        fileOption.add(openFileOption);

        saveFileOption.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveFileOption.setFont(new java.awt.Font("Ebrima", 1, 12)); // NOI18N
        saveFileOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/save-file-16.png"))); // NOI18N
        saveFileOption.setText("Save file");
        saveFileOption.setEnabled(false);
        saveFileOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFileOptionActionPerformed(evt);
            }
        });
        fileOption.add(saveFileOption);

        exportFileOption.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        exportFileOption.setFont(new java.awt.Font("Ebrima", 1, 12)); // NOI18N
        exportFileOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/export-file-16.png"))); // NOI18N
        exportFileOption.setText("Export file");
        exportFileOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportFileOptionActionPerformed(evt);
            }
        });
        fileOption.add(exportFileOption);

        deleteFileOption.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, java.awt.event.InputEvent.CTRL_MASK));
        deleteFileOption.setFont(new java.awt.Font("Ebrima", 1, 12)); // NOI18N
        deleteFileOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delete-file-16.png"))); // NOI18N
        deleteFileOption.setText("Delete file");
        deleteFileOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteFileOptionActionPerformed(evt);
            }
        });
        fileOption.add(deleteFileOption);

        menuBar.add(fileOption);

        editOption.setText("Edit");
        editOption.setFont(new java.awt.Font("Ebrima", 1, 14)); // NOI18N

        themeOption.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        themeOption.setFont(new java.awt.Font("Ebrima", 1, 12)); // NOI18N
        themeOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/open-file-16.png"))); // NOI18N
        themeOption.setText("Theme Builder");
        themeOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                themeOptionActionPerformed(evt);
            }
        });
        editOption.add(themeOption);

        fontSizeOption.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        fontSizeOption.setFont(new java.awt.Font("Ebrima", 1, 12)); // NOI18N
        fontSizeOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/font-size.png"))); // NOI18N
        fontSizeOption.setText("Font size");
        fontSizeOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontSizeOptionActionPerformed(evt);
            }
        });
        editOption.add(fontSizeOption);

        menuBar.add(editOption);

        windowOption.setText("Window");
        windowOption.setFont(new java.awt.Font("Ebrima", 1, 14)); // NOI18N

        showConsoleOption.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        showConsoleOption.setFont(new java.awt.Font("Ebrima", 1, 12)); // NOI18N
        showConsoleOption.setSelected(true);
        showConsoleOption.setText("Show console");
        showConsoleOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/console.png"))); // NOI18N
        showConsoleOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showConsoleOptionActionPerformed(evt);
            }
        });
        windowOption.add(showConsoleOption);

        showNavigatorOption.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        showNavigatorOption.setFont(new java.awt.Font("Ebrima", 1, 12)); // NOI18N
        showNavigatorOption.setSelected(true);
        showNavigatorOption.setText("Show navigator");
        showNavigatorOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/navigator.png"))); // NOI18N
        showNavigatorOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showNavigatorOptionActionPerformed(evt);
            }
        });
        windowOption.add(showNavigatorOption);

        menuBar.add(windowOption);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newFileActionPerformed
        newFile();
    }//GEN-LAST:event_newFileActionPerformed

    private void newFileOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newFileOptionActionPerformed
        newFile();
    }//GEN-LAST:event_newFileOptionActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (isModify()) {
            int response = yesNoOption("Project \"" + currentProject + "\" is modify. Save?");
            switch (response) {
                case JOptionPane.YES_OPTION:
                    currentProject.save(new ArrayList(Arrays.asList(code.getText().split("\n"))));
                    break;
                case JOptionPane.NO_OPTION:
                    break;
                default:
                    break;
            }
        }
        Configuration.config(configuration);
    }//GEN-LAST:event_formWindowClosing

    private void saveFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFileActionPerformed
        if (currentProject != null) {
            if (currentProject.save(new ArrayList(Arrays.asList(code.getText().split("\n"))))) {
                saveFile.setEnabled(false);
                saveFileOption.setEnabled(false);
            } else {
                saveFile.setEnabled(true);
                saveFileOption.setEnabled(true);
            }
        }
    }//GEN-LAST:event_saveFileActionPerformed

    private void saveFileOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFileOptionActionPerformed
        if (currentProject != null) {
            if (currentProject.save(new ArrayList(Arrays.asList(code.getText().split("\n"))))) {
                saveFile.setEnabled(false);
                saveFileOption.setEnabled(false);
            } else {
                saveFile.setEnabled(true);
                saveFileOption.setEnabled(true);
            }
        }
    }//GEN-LAST:event_saveFileOptionActionPerformed

    private void runFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runFileActionPerformed
        compile();
    }//GEN-LAST:event_runFileActionPerformed

    private void openFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFileActionPerformed
        openNewProject();
    }//GEN-LAST:event_openFileActionPerformed

    private void openFileOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFileOptionActionPerformed
        openNewProject();
    }//GEN-LAST:event_openFileOptionActionPerformed

    private void deleteFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteFileActionPerformed
        deleteFile();
    }//GEN-LAST:event_deleteFileActionPerformed

    private void deleteFileOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteFileOptionActionPerformed
        deleteFile();
    }//GEN-LAST:event_deleteFileOptionActionPerformed

    private void closeConsoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeConsoleActionPerformed
        consoleContainer.setVisible(false);
        showConsoleOption.setState(false);
    }//GEN-LAST:event_closeConsoleActionPerformed

    private void codeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codeKeyReleased
        if (isModify()) {
            saveFile.setEnabled(true);
            saveFileOption.setEnabled(true);
        } else {
            saveFile.setEnabled(false);
            saveFileOption.setEnabled(false);
        }
        if (code.getText().length() >= 1) {
            if (!String.valueOf(code.getText().charAt(code.getText().length() - 1)).matches("\n")) {
                int caret = code.getCaretPosition();
                addToCode("\n", null);
                code.setCaretPosition(caret);
            }
        }
        paintRK(code.getCaretPosition());
    }//GEN-LAST:event_codeKeyReleased

    private void exportFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportFileActionPerformed
        exportFile();
    }//GEN-LAST:event_exportFileActionPerformed

    private void exportFileOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportFileOptionActionPerformed
        exportFile();
    }//GEN-LAST:event_exportFileOptionActionPerformed

    private void showConsoleOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showConsoleOptionActionPerformed
        consoleContainer.setVisible(showConsoleOption.getState());
    }//GEN-LAST:event_showConsoleOptionActionPerformed

    private void showNavigatorOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showNavigatorOptionActionPerformed
        scrollForNavigator.setVisible(showNavigatorOption.getState());
    }//GEN-LAST:event_showNavigatorOptionActionPerformed

    private void themeOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themeOptionActionPerformed
        themeBuilderDialog.setVisible(true);
    }//GEN-LAST:event_themeOptionActionPerformed

    private void fontSizeOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fontSizeOptionActionPerformed
        fontSizeDialog.setVisible(true);
    }//GEN-LAST:event_fontSizeOptionActionPerformed

    private void fontSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fontSizeStateChanged
        fontSizeIndicator.setText("size : " + fontSize.getValue() + "px");
        configuration.setFontSize(fontSize.getValue());
        code.setFont(new Font("Ebrima", Font.PLAIN, fontSize.getValue()));
        console.setFont(new Font("Ebrima", Font.PLAIN, fontSize.getValue()));
        fileViewer.setFont(new Font("Ebrima", Font.PLAIN, fontSize.getValue()));
    }//GEN-LAST:event_fontSizeStateChanged

    private void compile() {
        console.setText("");
        consoleContainer.setVisible(true);
        showConsoleOption.setState(true);
        if (currentProject != null) {
            if (currentProject.save(new ArrayList(Arrays.asList(code.getText().split("\n"))))) {
                saveFile.setEnabled(false);
                saveFileOption.setEnabled(false);
                Pattern.compile(currentProject, code.getText(), (c, a) -> addToConsole(c, a));
            } else {
                addToConsole("Error, we don't find project to save it", ERROR_SAS);
            }
        } else {
            error("No project selected");
        }
    }

    private void changeTheme(String theme) {
        try {
            FlatAnimatedLafChange.showSnapshot();
            UIManager.setLookAndFeel(theme);
            FlatLaf.updateUI();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.err.println(ex);
        }
    }

    private void newFile() {
        String projectName = input("Project name: ", "Create new file");
        if (projectName != null) {
            if (projectName.matches(Pattern.PROJECT_NAME)) {
                //The name is correct, can create a comphex project
                Project project = new Project(projectName);
                //Verify if the project exist
                if (project.projectExist()) {
                    message("Project create successfully");
                    ((DefaultTreeModel) tree.getModel()).insertNodeInto(new DefaultMutableTreeNode(project), rootProjects, tree.getRowCount() - 1);
                    ((DefaultTreeModel) tree.getModel()).reload();
                } else {
                    error("Something went wrong");
                }
            } else {
                error("Incorrect name");
            }
        }
    }

    private void openProject(Project project) {
        //View changes at current project
        if (isModify()) {
            //No changes saved yet
            int response = yesNoOption("Project \"" + currentProject + "\" is modified. Save?");
            if (response == JOptionPane.YES_OPTION) {
                if (currentProject.save(new ArrayList(Arrays.asList(code.getText().split("\n"))))) {
                    message("Saved successfully!");
                    code.setBorder(BorderFactory.createTitledBorder(project.getProjectName()));
                    code.setText(project.readCode() + "\n");
                    paintRK();
                    console.setText("");
                    currentProject = project;
                    rootNavigator.removeAllChildren();
                    rootNavigator.add(new DefaultMutableTreeNode(project.getCode()));
                    rootNavigator.add(new DefaultMutableTreeNode(project.getHex()));
                    ((DefaultTreeModel) navigator.getModel()).reload();
                    runFile.setEnabled(true);
                    saveFile.setEnabled(false);
                    saveFileOption.setEnabled(false);
                } else {
                    error("Something went wrong, verify if the project \"" + currentProject + "\" exist");
                }
            } else if (response == JOptionPane.NO_OPTION) {
                code.setBorder(BorderFactory.createTitledBorder(project.getProjectName()));
                code.setText(project.readCode() + "\n");
                paintRK();
                console.setText("");
                currentProject = project;
                rootNavigator.removeAllChildren();
                rootNavigator.add(new DefaultMutableTreeNode(project.getCode()));
                rootNavigator.add(new DefaultMutableTreeNode(project.getHex()));
                ((DefaultTreeModel) navigator.getModel()).reload();
                runFile.setEnabled(true);
                saveFile.setEnabled(false);
                saveFileOption.setEnabled(false);
            }
        } else {
            code.setBorder(BorderFactory.createTitledBorder(project.getProjectName()));
            code.setText(project.readCode() + "\n");
            console.setText("");
            paintRK();
            currentProject = project;
            rootNavigator.removeAllChildren();
            rootNavigator.add(new DefaultMutableTreeNode(project.getCode()));
            rootNavigator.add(new DefaultMutableTreeNode(project.getHex()));
            ((DefaultTreeModel) navigator.getModel()).reload();
            runFile.setEnabled(true);
            saveFile.setEnabled(false);
            saveFileOption.setEnabled(false);
        }
    }

    private void viewFile(File file) {
        FileReader fr;
        BufferedReader br;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line;
            String content = "";
            while ((line = br.readLine()) != null) {
                content += line + "\n";
            }
            fileViewer.setText(content);
            fileName.setText(file.getName());
            fileViewerDialog.setVisible(true);
            fr.close();
            br.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void openNewProject() {
        int response = fileChooser.showOpenDialog(this);
        if (response == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile != null) {
                if (selectedFile.getName().matches("\\w+\\.code")) {
                    String projectName = selectedFile.getName().replace(".code", "");
                    Project newProject = new Project(projectName);
                    if (newProject.projectExist()) {
                        File projectCode = newProject.getCode();
                        newProject.setCode(selectedFile);
                        String importCode = newProject.readCode();
                        newProject.setCode(projectCode);
                        newProject.save(new ArrayList(Arrays.asList(importCode.split("\n"))));
                        rootProjects.add(new DefaultMutableTreeNode(newProject));
                        ((DefaultTreeModel) tree.getModel()).reload();
                        message("Project opened successfully");
                    } else {
                        error("Something went wrong");
                    }
                } else {
                    error("Please, select a \".code\" file");
                }
            } else {
                error("No selected file");
            }
        }
    }

    private void deleteFile() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (selectedNode != null) {
            int response = yesNoOption("Are you sure to delete \"" + selectedNode.getUserObject() + "\" ?"
                    + "\nThis operation will erase all information about this project");
            if (response == JOptionPane.YES_OPTION) {
                if (selectedNode.getUserObject() instanceof Project) {
                    if (((Project) selectedNode.getUserObject()).exists()) {
                        for (File child : ((Project) selectedNode.getUserObject()).listFiles()) {
                            child.delete();
                        }
                        if (((Project) selectedNode.getUserObject()).delete()) {
                            if ((Project) selectedNode.getUserObject() == currentProject) {
                                code.setBorder(BorderFactory.createTitledBorder("Project"));
                                code.setText("");
                                console.setText("");
                                currentProject = null;
                                rootNavigator.removeAllChildren();
                                ((DefaultTreeModel) navigator.getModel()).reload();
                                runFile.setEnabled(false);
                                saveFile.setEnabled(false);
                                saveFileOption.setEnabled(false);
                            }
                            rootProjects.remove(selectedNode);
                            ((DefaultTreeModel) tree.getModel()).reload();
                            message("Project delete successfully!");
                        } else {
                            error("Something went wrong");
                        }
                    } else {
                        error("Project not found!");
                    }
                }
            }
        } else {
            error("Please select a project to delete");
        }
    }

    private void exportFile() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (selectedNode != null) {
            if (selectedNode.getUserObject() instanceof Project) {
                Project selectedProject = (Project) selectedNode.getUserObject();
                int response = fileChooser.showSaveDialog(this);
                if (response == JFileChooser.APPROVE_OPTION) {
                    File createdFile = fileChooser.getSelectedFile();
                    if (createdFile.getName().matches(Pattern.PROJECT_NAME + "\\.code")) {
                        File currentCodeFile = selectedProject.getCode();
                        String currentCode = selectedProject.readCode();
                        selectedProject.setCode(createdFile);
                        if (selectedProject.save(new ArrayList<>(Arrays.asList(currentCode.split("\n"))))) {
                            message("Project exported successfully!");
                        } else {
                            error("Can't export \"" + selectedProject.getProjectName() + "\" project");
                        }
                        selectedProject.setCode(currentCodeFile);
                    } else {
                        error("Please, selecte a \".code\" file to export");
                    }
                }
            }
        } else {
            error("Please selecte a project to export");
        }
    }

    private boolean isModify() {
        if (currentProject != null) {
            String currentCode = code.getText().trim();
            String projectCode = currentProject.readCode().trim();
            return !projectCode.equals(currentCode);
        }
        return false;
    }

    private void error(String error) {
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE, ERROR_IMG);
    }

    private void message(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE, INFOR_IMG);
    }

    private int yesNoOption(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, INFOR_IMG);
    }

    private String input(String message, String inputRequest) {
        return (String) JOptionPane.showInputDialog(this, message, inputRequest, JOptionPane.INFORMATION_MESSAGE, FILE_IMG, null, null);
    }

    private void addToConsole(String value, SimpleAttributeSet sas) {
        try {
            console.getStyledDocument().insertString(console.getStyledDocument().getLength(), value + "\n", sas);
        } catch (BadLocationException e) {
            System.err.println(e);
        }
    }

    private void addToCode(String value, SimpleAttributeSet sas) {
        try {
            code.getStyledDocument().insertString(code.getCaretPosition(), value, sas);
        } catch (BadLocationException e) {
            System.err.println(e);
        }
    }

    private void paintRK(int caretPosition) {
        String currentCode = code.getText().toLowerCase();
        code.setText("");
        String element = "";
        for (int i = 0; i < currentCode.length(); i++) {
            String character = String.valueOf(currentCode.charAt(i));
            if (character.matches("\n")) {
                if (isReservedKey(element)) {
                    addToCode(element.toUpperCase() + character, PK_SAS);
                } else if (isLabel(element + character)) {
                    addToCode(element + character, LABEL_SAS);
                } else {
                    int j = 0;
                    for (j = i - 1; j > 0; j--) {
                        if (String.valueOf(currentCode.charAt(j)).matches("(\n|;)")) {
                            break;
                        }
                    }
                    j = j >= 0 ? j : 0;
                    if (isLabelUsed(currentCode.substring(j, i))) {
                        addToCode(element + character, LABEL_SAS);
                    } else {
                        addToCode(element + character, null);
                    }
                }
                element = "";
            } else if (character.matches("\\s+")) {
                if (isReservedKey(element)) {
                    addToCode(element.toUpperCase() + character, PK_SAS);
                } else if (isLabel(element + character)) {
                    addToCode(element + character, LABEL_SAS);
                } else {
                    int j = 0;
                    for (j = i - 1; j > 0; j--) {
                        if (String.valueOf(currentCode.charAt(j)).matches("(\n|;)")) {
                            break;
                        }
                    }
                    j = j >= 0 ? j : 0;
                    if (isLabelUsed(currentCode.substring(j, i))) {
                        addToCode(element + character, LABEL_SAS);
                    } else {
                        addToCode(element + character, null);
                    }
                }
                element = "";
            } else if (character.matches("\\W+") && !character.matches(";")) {
                if (isReservedKey(element)) {
                    addToCode(element.toUpperCase(), PK_SAS);
                    addToCode(character, null);
                } else if (isLabel(element + character)) {
                    addToCode(element + character, LABEL_SAS);
                } else {
                    int j = 0;
                    for (j = i - 1; j > 0; j--) {
                        if (String.valueOf(currentCode.charAt(j)).matches("(\n|;)")) {
                            break;
                        }
                    }
                    j = j >= 0 ? j : 0;
                    if (isLabelUsed(currentCode.substring(j, i))) {
                        addToCode(element + character, LABEL_SAS);
                    } else {
                        addToCode(element + character, null);
                    }
                }
                element = "";
            } else if (character.matches(";")) {
                if (isReservedKey(element)) {
                    addToCode(element.toUpperCase(), PK_SAS);
                    String comment = "";
                    while (!character.matches("\n") && i < currentCode.length() - 1) {
                        comment += character;
                        i++;
                        character = String.valueOf(currentCode.charAt(i));
                    }
                    addToCode(comment + "\n", COMMENT_SAS);
                    element = "";
                } else {
                    addToCode(element, null);
                    String comment = "";
                    while (!character.matches("\n") && i < currentCode.length() - 1) {
                        comment += character;
                        i++;
                        character = String.valueOf(currentCode.charAt(i));
                    }
                    addToCode(comment + "\n", COMMENT_SAS);
                    element = "";
                }
            } else {
                element += character;
            }
        }
        if (!element.isEmpty()) {
            addToCode(element, null);
        }
        code.setCaretPosition(caretPosition);
    }

    private void paintRK() {
        String currentCode = code.getText().toLowerCase();
        code.setText("");
        String element = "";
        for (int i = 0; i < currentCode.length(); i++) {
            String character = String.valueOf(currentCode.charAt(i));
            if (character.matches("\n")) {
                if (isReservedKey(element)) {
                    addToCode(element.toUpperCase() + character, PK_SAS);
                } else if (isLabel(element + character)) {
                    addToCode(element + character, LABEL_SAS);
                } else {
                    int j = 0;
                    for (j = i - 1; j > 0; j--) {
                        if (String.valueOf(currentCode.charAt(j)).matches("(\n|;)")) {
                            break;
                        }
                    }
                    j = j >= 0 ? j : 0;
                    if (isLabelUsed(currentCode.substring(j, i))) {
                        addToCode(element + character, LABEL_SAS);
                    } else {
                        addToCode(element + character, null);
                    }
                }
                element = "";
            } else if (character.matches("\\s+")) {
                if (isReservedKey(element)) {
                    addToCode(element.toUpperCase() + character, PK_SAS);
                } else if (isLabel(element + character)) {
                    addToCode(element + character, LABEL_SAS);
                } else {
                    int j = 0;
                    for (j = i - 1; j > 0; j--) {
                        if (String.valueOf(currentCode.charAt(j)).matches("(\n|;)")) {
                            break;
                        }
                    }
                    j = j >= 0 ? j : 0;
                    if (isLabelUsed(currentCode.substring(j, i))) {
                        addToCode(element + character, LABEL_SAS);
                    } else {
                        addToCode(element + character, null);
                    }
                }
                element = "";
            } else if (character.matches("\\W+") && !character.matches(";")) {
                if (isReservedKey(element)) {
                    addToCode(element.toUpperCase(), PK_SAS);
                    addToCode(character, null);
                } else if (isLabel(element + character)) {
                    addToCode(element + character, LABEL_SAS);
                } else {
                    int j = 0;
                    for (j = i - 1; j > 0; j--) {
                        if (String.valueOf(currentCode.charAt(j)).matches("(\n|;)")) {
                            break;
                        }
                    }
                    j = j >= 0 ? j : 0;
                    if (isLabelUsed(currentCode.substring(j, i))) {
                        addToCode(element + character, LABEL_SAS);
                    } else {
                        addToCode(element + character, null);
                    }
                }
                element = "";
            } else if (character.matches(";")) {
                if (isReservedKey(element)) {
                    addToCode(element.toUpperCase(), PK_SAS);
                    String comment = "";
                    while (!character.matches("\n") && i < currentCode.length() - 1) {
                        comment += character;
                        i++;
                        character = String.valueOf(currentCode.charAt(i));
                    }
                    addToCode(comment + "\n", COMMENT_SAS);
                    element = "";
                } else {
                    addToCode(element, null);
                    String comment = "";
                    while (!character.matches("\n") && i < currentCode.length() - 1) {
                        comment += character;
                        i++;
                        character = String.valueOf(currentCode.charAt(i));
                    }
                    addToCode(comment + "\n", COMMENT_SAS);
                    element = "";
                }
            } else {
                element += character;
            }
        }
        if (!element.isEmpty()) {
            addToCode(element, null);
        }
    }

    private boolean isReservedKey(String value) {
        for (String pattern : Pattern.RESERVED_KEYS) {
            if (value.toLowerCase().trim().matches(pattern)) {
                return true;
            }
        }
        return false;
    }

    private boolean isLabel(String value) {
        return value.toLowerCase().trim().matches("([a-z]|_|\\d)?[a-z]\\w*:");
    }

    private boolean isLabelUsed(String value) {
        return value.toLowerCase().trim().matches("goto\\s+([a-z]|_|\\d)?[a-z]\\w*");
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.put("Tree.leafIcon", FILE_IMGX16);
            UIManager.put("Tree.openIcon", FOLDER_IMGX16);
            UIManager.put("Tree.closedIcon", FOLDER_IMGX16);
        } catch (Exception e) {
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Comphex().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeConsole;
    private javax.swing.JTextPane code;
    private javax.swing.JPanel codeContainer;
    private javax.swing.JTextPane console;
    private javax.swing.JPanel consoleContainer;
    private javax.swing.JPanel container;
    private javax.swing.JButton deleteFile;
    private javax.swing.JMenuItem deleteFileOption;
    private javax.swing.JMenu editOption;
    private javax.swing.JButton exportFile;
    private javax.swing.JMenuItem exportFileOption;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JLabel fileName;
    private javax.swing.JMenu fileOption;
    private javax.swing.JTextPane fileViewer;
    private javax.swing.JPanel fileViewerContainer;
    private javax.swing.JDialog fileViewerDialog;
    private javax.swing.JSlider fontSize;
    private javax.swing.JPanel fontSizeContainer;
    private javax.swing.JDialog fontSizeDialog;
    private javax.swing.JLabel fontSizeIndicator;
    private javax.swing.JMenuItem fontSizeOption;
    private javax.swing.JLabel fontSizeTitle;
    private javax.swing.JPanel leftContainer;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTree navigator;
    private javax.swing.JButton newFile;
    private javax.swing.JMenuItem newFileOption;
    private javax.swing.JButton openFile;
    private javax.swing.JMenuItem openFileOption;
    private javax.swing.JPanel rightContainer;
    private javax.swing.JButton runFile;
    private javax.swing.JToolBar.Separator s1;
    private javax.swing.JToolBar.Separator s2;
    private javax.swing.JToolBar.Separator s3;
    private javax.swing.JToolBar.Separator s4;
    private javax.swing.JToolBar.Separator s5;
    private javax.swing.JButton saveFile;
    private javax.swing.JMenuItem saveFileOption;
    private javax.swing.JScrollPane scrollForCode;
    private javax.swing.JScrollPane scrollForConsole;
    private javax.swing.JScrollPane scrollForNavigator;
    private javax.swing.JScrollPane scrollForThemeList;
    private javax.swing.JScrollPane scrollForTree;
    private javax.swing.JScrollPane scroolForFileViewer;
    private javax.swing.JCheckBoxMenuItem showConsoleOption;
    private javax.swing.JCheckBoxMenuItem showNavigatorOption;
    private javax.swing.JSplitPane split;
    private javax.swing.JPanel themeBuilderContainer;
    private javax.swing.JDialog themeBuilderDialog;
    private javax.swing.JList<String> themeList;
    private javax.swing.JMenuItem themeOption;
    private javax.swing.JLabel titleTheme;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JToolBar toolConsole;
    private javax.swing.JTree tree;
    private javax.swing.JMenu windowOption;
    // End of variables declaration//GEN-END:variables
}
