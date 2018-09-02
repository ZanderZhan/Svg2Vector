package UI;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.common.base.Strings;
import org.intellij.images.fileTypes.impl.SvgFileType;
import utils.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Paths;

import Action.*;
import utils.Logger;

public class GenerateVectorDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField targetPathField;
    private JButton targetSelectBtn;
    private JTextField prefixField;
    private JTextField sourcePathField;
    private JButton sourceSelectBtn;

    private Project mProject;

    public GenerateVectorDialog(Project project) {

        mProject = project;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        init();
        setListener();

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        dispose();

        // add your code here
        Svg2VectorHelper helper = new Svg2VectorHelper(mProject);
        apply();
        helper.convertSvgToVector(getSource(), Paths.get(getTargetPath()), getPrefix());
    }

    private void apply() {
        PropertiesComponent.getInstance().setValue(Configuration.KEY_SOURCE_PATH, sourcePathField.getText());
        PropertiesComponent.getInstance().setValue(Configuration.KEY_TARGET_PATH, targetPathField.getText());
        PropertiesComponent.getInstance().setValue(Configuration.KEY_PREFIX, prefixField.getText());
    }

    private void init() {
        targetPathField.setText(getTargetPath());
        prefixField.setText(getPrefix());
        sourcePathField.setText(getSource());

        checkOkButtonStatus();
    }

    private void checkOkButtonStatus() {
        if (targetPathField.getText().isEmpty() || sourcePathField.getText().isEmpty()) {
            buttonOK.setEnabled(false);
            return;
        }
        buttonOK.setEnabled(true);
    }

    private String getTargetPath() {
        return PropertiesComponent.getInstance().getValue(Configuration.KEY_TARGET_PATH, Configuration.DEFAULT_TARGET_PATH);
    }

    private String getPrefix() {
        return PropertiesComponent.getInstance().getValue(Configuration.KEY_PREFIX, Configuration.DEFAULT_PREFIX);
    }

    private String getSource() {
        return PropertiesComponent.getInstance().getValue(Configuration.KEY_SOURCE_PATH, Configuration.DEFAULT_SOURCE_PATH);
    }

    private void setListener() {
        sourceSelectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooser(sourcePathField);
            }
        });

        targetSelectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
                VirtualFile virtualFile = FileChooser.chooseFile(descriptor, ProjectUtil.guessCurrentProject(targetPathField), null);
                if(virtualFile != null && virtualFile.isDirectory()) {
                    targetPathField.setText(virtualFile.getCanonicalPath());
                    checkOkButtonStatus();
                }


            }
        });
    }

    private void showFileChooser(JTextField textField) {

        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, true, false, false, false, true)
                .withFileFilter(file -> file.getFileType() == SvgFileType.INSTANCE);
        VirtualFile[] virtualFiles = FileChooser.chooseFiles(descriptor, ProjectUtil.guessCurrentProject(textField), null);

        String path = "";
        for (VirtualFile virtualFile: virtualFiles) {
            if(virtualFile == null) return ;
            if(virtualFile.getCanonicalPath() != null) {
                path += virtualFile.getCanonicalPath() + (virtualFiles[virtualFiles.length - 1].equals(virtualFile) ? "" : ";");
            }
        }
        if(Strings.isNotEmpty(path)){
            textField.setText(path);
            checkOkButtonStatus();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void show(Project project) {
        GenerateVectorDialog dialog = new GenerateVectorDialog(project);
        dialog.pack();
        dialog.setSize(500, 184);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
//        System.exit(0);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("target");
        panel3.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        targetPathField = new JTextField();
        panel3.add(targetPathField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        targetSelectBtn = new JButton();
        targetSelectBtn.setText("...");
        panel3.add(targetSelectBtn, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("prefix");
        panel3.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        prefixField = new JTextField();
        panel3.add(prefixField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("source");
        panel3.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sourcePathField = new JTextField();
        sourcePathField.setEditable(false);
        panel3.add(sourcePathField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        sourceSelectBtn = new JButton();
        sourceSelectBtn.setText("...");
        panel3.add(sourceSelectBtn, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
