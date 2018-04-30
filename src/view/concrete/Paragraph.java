package view.concrete;

import javafx.geometry.Pos;
import model.semantic.Semantic;
import view.container.TextSection;
import view.helper.Painter;

public class Paragraph extends TextSection {
    public Paragraph(String fontFamily, double size)
    {
        super(fontFamily, size);
        this.align(Pos.TOP_LEFT);
        getStyleClass().add("paragraph");
        setCurrentSemantic(Semantic.NORMAL);
        fill.bind(Painter.getInstance().paragraphColorProperty());
    }

    @Override
    public String getContentInXML()
    {
        StringBuilder builder = new StringBuilder("<paragraph>");
        builder.append(super.getContentInXML());
        builder.append("</paragraph>");
        return builder.toString();
    }
}
