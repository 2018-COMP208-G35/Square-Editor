package view;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
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

    private static double CARET_WIDTH = 1;
    private FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), this);
    private Toggle caretToggle = new ToggleButton();

    public Caret(double size)
    {
        super(0, 0, 0, size);
        caretToggle.setToggleGroup(CaretHelper.getCaretGroup());
        addListener();

        this.setStroke(Color.TRANSPARENT);
        this.setStrokeWidth(CARET_WIDTH);
        this.setStrokeType(StrokeType.CENTERED);

        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(Timeline.INDEFINITE);
        fadeTransition.setAutoReverse(true);
    }

    private void addListener()
    {
        caretToggle.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                if (newValue == false)
                {
                    hide();
                }
            }
        });
    }

    /**
     * Set the length of the caret
     */
    public void setHeight(double height)
    {
        this.setEndY(height);
    }

    /**
     * Display the caret
     */
    public void show()
    {
        // todo set stroke to black
        this.setStroke(Color.BLACK);
        fadeTransition.play();
        caretToggle.setSelected(true);
        caretToggle.setUserData(this); // todo do it here? or at content box?
    }

    /**
     * Hide the caret
     */
    public void hide()
    {
        fadeTransition.stop();
        // set stroke to transparent
        this.setStroke(Color.TRANSPARENT);
        caretToggle.setSelected(false);
    }

    public boolean isOn()
    {
        return caretToggle.isSelected();
    }

    public static double getCaretWidth()
    {
        return CARET_WIDTH;
    }
}
