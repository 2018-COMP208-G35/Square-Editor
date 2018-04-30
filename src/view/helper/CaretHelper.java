package view.helper;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import view.Caret;

public class CaretHelper {
    private static ToggleGroup caretGroup = new ToggleGroup();


    public static ToggleGroup getCaretGroup()
    {
        return caretGroup;
    }

    public static Caret getSelectedCaret()
    {
        return (Caret) caretGroup.getSelectedToggle().getUserData();

    }

    public static ReadOnlyObjectProperty<Toggle> getSelectedCaretProperty()
    {
        return caretGroup.selectedToggleProperty();
    }


}
