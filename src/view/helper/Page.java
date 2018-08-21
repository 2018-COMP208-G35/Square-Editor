package view.helper;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.text.Font;

public class Page {
    private static String fontFamily;
    private static DoubleProperty PagePadding = new SimpleDoubleProperty(30.0D);
    private static DoubleProperty TitleFontSize = new SimpleDoubleProperty(40.0D);
    private static ObjectProperty<Font> titleFont = new SimpleObjectProperty(new Font("Times New Roman", 34.0D));
    private static DoubleProperty SectionFontSize = new SimpleDoubleProperty(30.0D);
    private static DoubleProperty SubsectionFontSize = new SimpleDoubleProperty(25.0D);
    private static DoubleProperty FontSize = new SimpleDoubleProperty(30.0D);
    private static DoubleProperty lineWidthLimit = new SimpleDoubleProperty(650.0D);
    private static DoubleProperty lineSpacing = new SimpleDoubleProperty(1.5D);

    private Page() {
    }

    public static double getPagePadding() {
        return PagePadding.get();
    }

    public static DoubleProperty pagePaddingProperty() {
        return PagePadding;
    }

    public Page(String fontFamily) {
        fontFamily = fontFamily;
    }

    public static String getFontFamily() {
        return fontFamily;
    }

    public static double getTitleFontSize() {
        return TitleFontSize.get();
    }

    public static DoubleProperty titleFontSizeProperty() {
        return TitleFontSize;
    }

    public static Font getTitleFont() {
        return (Font)titleFont.get();
    }

    public static ObjectProperty<Font> titleFontProperty() {
        return titleFont;
    }

    public static double getSectionFontSize() {
        return SectionFontSize.get();
    }

    public static DoubleProperty sectionFontSizeProperty() {
        return SectionFontSize;
    }

    public static double getSubsectionFontSize() {
        return SubsectionFontSize.get();
    }

    public static DoubleProperty subsectionFontSizeProperty() {
        return SubsectionFontSize;
    }

    public static double getFontSize() {
        return FontSize.get();
    }

    public static DoubleProperty fontSizeProperty() {
        return FontSize;
    }

    public static double getLineWidthLimit() {
        return lineWidthLimit.get();
    }

    public static DoubleProperty lineWidthLimitProperty() {
        return lineWidthLimit;
    }

    public static double getLineSpacing() {
        return lineSpacing.get();
    }

    public static DoubleProperty lineSpacingProperty() {
        return lineSpacing;
    }
}
