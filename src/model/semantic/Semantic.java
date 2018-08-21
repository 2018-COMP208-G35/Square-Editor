package model.semantic;

import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public enum Semantic {
    NORMAL(FontWeight.NORMAL, FontPosture.REGULAR),
    STRONG(FontWeight.BOLD, FontPosture.REGULAR),
    EMPHASIS(FontWeight.NORMAL, FontPosture.ITALIC),
    STRONG_EMPHASIS(FontWeight.BOLD, FontPosture.ITALIC);

    FontWeight fontWeight;
    FontPosture fontPosture;

    Semantic(FontWeight fontWeight, FontPosture fontPosture)
    {
        this.fontWeight = fontWeight;
        this.fontPosture = fontPosture;
    }

    public FontWeight getFontWeight()
    {
        return fontWeight;
    }

    public FontPosture getFontPosture()
    {
        return fontPosture;
    }
}
