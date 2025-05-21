package org.wei.metabaseproxy.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;
import org.wei.metabaseproxy.model.DatabaseModel;
import org.wei.metabaseproxy.model.RunSqlParamsModel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月20日 09:58
 */
public class RunSqlParamsDialog extends DialogWrapper {
    private final Project project;
    private final RunSqlParamsModel runSqlParamsModel;
    private final List<DatabaseModel> databaseOptions;


    private HashMap<String,JTextField> paramsMap = new HashMap<>();
    private JComboBox<DatabaseModel> comboBox;

    public RunSqlParamsDialog(@Nullable Project project, RunSqlParamsModel runSqlParamsModel,
                              List<DatabaseModel> databaseOptions) {
        super(project);
        this.project = project;
        this.runSqlParamsModel = runSqlParamsModel;
        this.databaseOptions = databaseOptions;
        init();
        setTitle("执行SQL数据库及参数配置");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(new JLabel("查询的基础数据库"));
        comboBox = new JComboBox<>(databaseOptions.toArray(new DatabaseModel[0]));
        panel.add(comboBox);
        // comboBox 设置选中项目
        if (runSqlParamsModel.getSelectedDatabase() != null) {
            comboBox.setSelectedItem(runSqlParamsModel.getSelectedDatabase());
        }
        if (runSqlParamsModel.getUserVarMap() != null) {
            for (String key : runSqlParamsModel.getUserVarMap().keySet()) {
                JTextField textField = new JTextField();
                textField.setText(runSqlParamsModel.getUserVarMap().get(key));
                paramsMap.put(key, textField);
                panel.add(new JLabel("变量" + key + ":"));
                panel.add(textField);
            }
        }
        if(runSqlParamsModel.getUserInputVarList() != null){
            for (String key : runSqlParamsModel.getUserInputVarList()) {
                JTextField textField = new JTextField();
                paramsMap.put(key, textField);
                panel.add(new JLabel("变量" + key + ":"));
                panel.add(textField);
            }
        }
        return panel;
    }


    public RunSqlParamsModel getUserResetParams() {
        RunSqlParamsModel resetParamsModel = new RunSqlParamsModel();
        resetParamsModel.setSelectedDatabase((DatabaseModel) comboBox.getSelectedItem());
        HashMap<String, String> userVarMap = new HashMap<>();
        for (String key : paramsMap.keySet()) {
            userVarMap.put(key, paramsMap.get(key).getText());
        }
        resetParamsModel.setUserVarMap(userVarMap);
        return  resetParamsModel;
    }


}

