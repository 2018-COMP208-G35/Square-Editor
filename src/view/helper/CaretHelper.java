package view.helper;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import view.Caret;


public class CaretHelper
{
    private static CaretHelper caretHelper = new CaretHelper();
    private static ToggleGroup caretGroup = new ToggleGroup();

    private CaretHelper()
    {
    }

    public static CaretHelper getInstance()
    {
        return caretHelper;
    }

    public ToggleGroup getCaretGroup()
    {
        return caretGroup;
    }

    public Caret getSelectedCaret()
    {
        return (Caret) caretGroup.getSelectedToggle().getUserData();
    }

    public ReadOnlyObjectProperty<Toggle> getSelectedCaretProperty()
    {
        return caretGroup.selectedToggleProperty();
    }
}