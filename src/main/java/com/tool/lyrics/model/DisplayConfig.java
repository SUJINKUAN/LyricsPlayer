package com.tool.lyrics.model;

import javafx.beans.property.*;
import javafx.scene.text.TextAlignment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisplayConfig {
    private StringProperty fontStyle = new SimpleStringProperty();
    private IntegerProperty fontSize = new SimpleIntegerProperty();
    private StringProperty textColor = new SimpleStringProperty();
    private StringProperty bgColor = new SimpleStringProperty();
    private ObjectProperty<TextAlignment> textAlignment = new SimpleObjectProperty<>();


}
