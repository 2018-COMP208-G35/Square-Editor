package view.helper;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Painter {
    private static Painter painter = new Painter();

    ObjectProperty<Paint> titleColor = new SimpleObjectProperty<>(Color.BLACK);
    ObjectProperty<Paint> authorColor = new SimpleObjectProperty<>(Color.BLACK);
    ObjectProperty<Paint> paragraphColor = new SimpleObjectProperty<>(Color.BLACK);
    ObjectProperty<Paint> quoteColor = new SimpleObjectProperty<>(Color.BLACK);
    ObjectProperty<Paint> sectionColor = new SimpleObjectProperty<>(Color.BLACK);

    public static Painter getInstance()
    {
        return painter;
    }

    private Painter()
    {
    }

    public static Painter getPainter()
    {
        return painter;
    }

    public static void setPainter(Painter painter)
    {
        Painter.painter = painter;
    }

    public Paint getTitleColor()
    {
        return titleColor.get();
    }

    public ObjectProperty<Paint> titleColorProperty()
    {
        return titleColor;
    }

    public void setTitleColor(Paint titleColor)
    {
        this.titleColor.set(titleColor);
    }

    public Paint getAuthorColor()
    {
        return authorColor.get();
    }

    public ObjectProperty<Paint> authorColorProperty()
    {
        return authorColor;
    }

    public void setAuthorColor(Paint authorColor)
    {
        this.authorColor.set(authorColor);
    }

    public Paint getParagraphColor()
    {
        return paragraphColor.get();
    }

    public ObjectProperty<Paint> paragraphColorProperty()
    {
        return paragraphColor;
    }

    public void setParagraphColor(Paint paragraphColor)
    {
        this.paragraphColor.set(paragraphColor);
    }

    public Paint getQuoteColor()
    {
        return quoteColor.get();
    }

    public ObjectProperty<Paint> quoteColorProperty()
    {
        return quoteColor;
    }

    public void setQuoteColor(Paint quoteColor)
    {
        this.quoteColor.set(quoteColor);
    }

    public Paint getSectionColor()
    {
        return sectionColor.get();
    }

    public ObjectProperty<Paint> sectionColorProperty()
    {
        return sectionColor;
    }

    public void setSectionColor(Paint sectionColor)
    {
        this.sectionColor.set(sectionColor);
    }

    public String toCSS()
    {
        StringBuilder css = new StringBuilder();
        css.append(".title { color: " + ((Color) titleColor.getValue()).getRed()); // todo get css
        return null;
    }
}
