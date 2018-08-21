package view.helper;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Painter {
    private static Painter painter = new Painter();
    ObjectProperty<Paint> titleColor;
    ObjectProperty<Paint> authorColor;
    ObjectProperty<Paint> paragraphColor;
    ObjectProperty<Paint> quoteColor;
    ObjectProperty<Paint> sectionColor;

    public static Painter getInstance() {
        return painter;
    }

    private Painter() {
        this.titleColor = new SimpleObjectProperty(Color.BLACK);
        this.authorColor = new SimpleObjectProperty(Color.BLACK);
        this.paragraphColor = new SimpleObjectProperty(Color.BLACK);
        this.quoteColor = new SimpleObjectProperty(Color.BLACK);
        this.sectionColor = new SimpleObjectProperty(Color.BLACK);
    }

    public static Painter getPainter() {
        return painter;
    }

    public static void setPainter(Painter painter) {
        painter = painter;
    }

    public Paint getTitleColor() {
        return (Paint)this.titleColor.get();
    }

    public ObjectProperty<Paint> titleColorProperty() {
        return this.titleColor;
    }

    public void setTitleColor(Paint titleColor) {
        this.titleColor.set(titleColor);
    }

    public Paint getAuthorColor() {
        return (Paint)this.authorColor.get();
    }

    public ObjectProperty<Paint> authorColorProperty() {
        return this.authorColor;
    }

    public void setAuthorColor(Paint authorColor) {
        this.authorColor.set(authorColor);
    }

    public Paint getParagraphColor() {
        return (Paint)this.paragraphColor.get();
    }

    public ObjectProperty<Paint> paragraphColorProperty() {
        return this.paragraphColor;
    }

    public void setParagraphColor(Paint paragraphColor) {
        this.paragraphColor.set(paragraphColor);
    }

    public Paint getQuoteColor() {
        return (Paint)this.quoteColor.get();
    }

    public ObjectProperty<Paint> quoteColorProperty() {
        return this.quoteColor;
    }

    public void setQuoteColor(Paint quoteColor) {
        this.quoteColor.set(quoteColor);
    }

    public Paint getSectionColor() {
        return (Paint)this.sectionColor.get();
    }

    public ObjectProperty<Paint> sectionColorProperty() {
        return this.sectionColor;
    }

    public void setSectionColor(Paint sectionColor) {
        this.sectionColor.set(sectionColor);
    }

    public String toCSS() {
        StringBuilder css = new StringBuilder();
        css.append(".title { color: " + ((Color)this.titleColor.getValue()).getRed());
        return null;
    }
}
