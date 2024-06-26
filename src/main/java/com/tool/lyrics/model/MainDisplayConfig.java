package com.tool.lyrics.model;

import javafx.beans.property.*;
import javafx.scene.text.TextAlignment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MainDisplayConfig {
    private StringProperty fontStyle = new SimpleStringProperty();
    private IntegerProperty fontSize = new SimpleIntegerProperty();
    private IntegerProperty levitateFontSize = new SimpleIntegerProperty();
    private StringProperty textColor = new SimpleStringProperty();
    private StringProperty bgColor = new SimpleStringProperty();
    private ObjectProperty<TextAlignment> textAlignment = new SimpleObjectProperty<>();

    public static MainDisplayConfig instance;


    public static MainDisplayConfig getInstance() {
        if (instance == null) {
            instance = new MainDisplayConfig();
        }
        return instance;
    }
}
