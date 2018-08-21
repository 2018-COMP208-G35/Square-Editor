package view;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;
import view.helper.CaretHelper;

public class Caret extends Line {
    private static double CARET_WIDTH = 1.0D;
    private FadeTransition fadeTransition = new FadeTransition(Duration.millis(500.0D), this);
    private Toggle caretToggle = new ToggleButton();

    public Caret(double size) {
        super(0.0D, 0.0D, 0.0D, size);
        this.caretToggle.setToggleGroup(CaretHelper.getInstance().getCaretGroup());
        this.addListener();
        this.setStroke(Color.TRANSPARENT);
        this.setStrokeWidth(CARET_WIDTH);
        this.setStrokeType(StrokeType.CENTERED);
        this.fadeTransition.setFromValue(1.0D);
        this.fadeTransition.setToValue(0.0D);
        this.fadeTransition.setCycleCount(-1);
        this.fadeTransition.setAutoReverse(true);
    }

    private void addListener() {
        this.caretToggle.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    Caret.this.hide();
                }

            }
        });
    }

    public void setHeight(double height) {
        this.setEndY(height);
    }

    public void show() {
        this.caretToggle.setUserData(this);
        this.caretToggle.setSelected(true);
        this.setStroke(Color.BLACK);
        this.fadeTransition.play();
    }

    public void hide() {
        this.fadeTransition.stop();
        this.setStroke(Color.TRANSPARENT);
        this.caretToggle.setSelected(false);
    }

    public boolean isOn() {
        return this.caretToggle.isSelected();
    }

    public static double getCaretWidth() {
        return CARET_WIDTH;
    }
}
