package com.tool.lyrics.controller;

import com.tool.lyrics.model.DisplayConfig;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private RadioButton lAlignment;
    @FXML
    private RadioButton cAlignment;
    @FXML
    private RadioButton rAlignment;
    @FXML
    private ToggleGroup alignment;

    @FXML
    private Button configCheckBtn;
    @FXML
    private Button configCancelBtn;
    private LyricsController lyricsPlayer;

    public void initializeWithConfig(DisplayConfig originConfig) {
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontTypeComboBox.getItems().addAll(fontNames);
        fontTypeComboBox.setValue(originConfig.getFontStyle().getValue());
        textColorPicker.setValue(Color.web(originConfig.getTextColor().getValue()));
        bgColorPicker.setValue(Color.web(originConfig.getBgColor().getValue()));
        fontSizeSpinner.getValueFactory().setValue(originConfig.getFontSize().getValue());

        switch (originConfig.getTextAlignment().getValue()) {
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

    private TextAlignment getSelectedAlignment() {
        if (lAlignment.isSelected()) {
            return TextAlignment.LEFT;
        } else if (cAlignment.isSelected()) {
            return TextAlignment.CENTER;
        } else if (rAlignment.isSelected()) {
            return TextAlignment.RIGHT;
        }
        return TextAlignment.RIGHT;
    }

    @FXML
    public void setConfig(MouseEvent mouseEvent) {
        DisplayConfig config = lyricsPlayer.getAppConfig();
        config.getFontStyle().setValue(fontTypeComboBox.getValue());
        config.getBgColor().setValue(toRGBCode(bgColorPicker.getValue()));
        config.getTextColor().setValue(toRGBCode(textColorPicker.getValue()));
        config.getFontSize().setValue(fontSizeSpinner.getValue());
        config.getTextAlignment().setValue(getSelectedAlignment());

        closeStage(mouseEvent);
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
