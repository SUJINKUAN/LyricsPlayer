package com.tool.lyrics.controller;

import com.tool.lyrics.model.LevitateDisplayConfig;
import com.tool.lyrics.model.MainDisplayConfig;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class DisplayConfigController {
    @FXML
    public ComboBox<String> fontTypeComboBox;

    @FXML
    private Label fontSizeLabel;
    @FXML
    private Label textColorLabel;
    @FXML
    private Label bgColorLabel;
    @FXML
    private Label alignmentLabel;

    @FXML
    private ColorPicker textColorPicker;
    @FXML
    private ColorPicker bgColorPicker;
    @FXML
    private Spinner<Integer> fontSizeSpinner;
    @FXML
    private Spinner<Integer> levitateFontSizeSpinner;
    @FXML
    private RadioButton lAlignment;
    @FXML
    private RadioButton cAlignment;
    @FXML
    private RadioButton rAlignment;
    @FXML
    private ToggleGroup alignment;

    public ColorPicker levitateTextColorPicker;
    public ColorPicker levitateBgColorPicker;
    public RadioButton levitateLAlignment;
    public RadioButton levitateCAlignment;
    public RadioButton levitateRAlignment;
    public ComboBox<String> levitateFontTypeComboBox;
    public ToggleGroup levitateAlignment;

    @FXML
    private Button configCheckBtn;
    @FXML
    private Button configCancelBtn;
    private MainDisplayConfig mainDisplayConfig = MainDisplayConfig.getInstance();
    private LevitateDisplayConfig levitateDisplayConfig = LevitateDisplayConfig.getInstance();

    public void initializeConfig() {
        initMainDisplayConfig();
        initLevitateDisplayConfig();
    }

    private void initMainDisplayConfig() {
        fontTypeComboBox.getItems().addAll(getFontNames());
        fontTypeComboBox.setValue(mainDisplayConfig.getFontStyle().getValue());
        textColorPicker.setValue(Color.web(mainDisplayConfig.getTextColor().getValue()));
        bgColorPicker.setValue(Color.web(mainDisplayConfig.getBgColor().getValue()));
        fontSizeSpinner.getValueFactory().setValue(mainDisplayConfig.getFontSize().getValue());
        switch (mainDisplayConfig.getTextAlignment().getValue()) {
            case LEFT:
                lAlignment.setSelected(true);
                break;
            case CENTER:
                cAlignment.setSelected(true);
                break;
            case RIGHT:
                rAlignment.setSelected(true);
                break;
        }
    }

    private void initLevitateDisplayConfig() {
        levitateFontTypeComboBox.getItems().addAll(getFontNames());
        levitateFontTypeComboBox.setValue(levitateDisplayConfig.getFontStyle().getValue());
        levitateTextColorPicker.setValue(Color.web(levitateDisplayConfig.getTextColor().getValue()));
        levitateBgColorPicker.setValue(Color.web(levitateDisplayConfig.getBgColor().getValue()));
        levitateFontSizeSpinner.getValueFactory().setValue(levitateDisplayConfig.getFontSize().getValue());
        switch (levitateDisplayConfig.getTextAlignment().getValue()) {
            case LEFT:
                levitateLAlignment.setSelected(true);
                break;
            case CENTER:
                levitateCAlignment.setSelected(true);
                break;
            case RIGHT:
                levitateRAlignment.setSelected(true);
                break;
        }
    }

    private static String[] getFontNames() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    }

    private TextAlignment getMainSelectedAlignment() {
        if (lAlignment.isSelected()) {
            return TextAlignment.LEFT;
        } else if (cAlignment.isSelected()) {
            return TextAlignment.CENTER;
        } else if (rAlignment.isSelected()) {
            return TextAlignment.RIGHT;
        }
        return TextAlignment.RIGHT;
    }

    private TextAlignment getLevitateSelectedAlignment() {
        if (levitateLAlignment.isSelected()) {
            return TextAlignment.LEFT;
        } else if (levitateCAlignment.isSelected()) {
            return TextAlignment.CENTER;
        } else if (levitateRAlignment.isSelected()) {
            return TextAlignment.RIGHT;
        }
        return TextAlignment.RIGHT;
    }

    @FXML
    public void setConfig() {
        MainDisplayConfig mainConfig = MainDisplayConfig.getInstance();
        mainConfig.getFontStyle().setValue(fontTypeComboBox.getValue());
        mainConfig.getBgColor().setValue(toRGBCode(bgColorPicker.getValue()));
        mainConfig.getTextColor().setValue(toRGBCode(textColorPicker.getValue()));
        mainConfig.getFontSize().setValue(fontSizeSpinner.getValue());
        mainConfig.getTextAlignment().setValue(getMainSelectedAlignment());

        LevitateDisplayConfig levitateConfig = LevitateDisplayConfig.getInstance();
        levitateConfig.getFontStyle().setValue(levitateFontTypeComboBox.getValue());
        levitateConfig.getBgColor().setValue(toRGBCode(levitateBgColorPicker.getValue()));
        levitateConfig.getTextColor().setValue(toRGBCode(levitateTextColorPicker.getValue()));
        levitateConfig.getFontSize().setValue(levitateFontSizeSpinner.getValue());
        levitateConfig.getTextAlignment().setValue(getLevitateSelectedAlignment());
    }


    private void closeStage(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void cancelConfig(MouseEvent mouseEvent) {
        closeStage(mouseEvent);
    }

    private String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
