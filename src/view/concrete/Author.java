package view.concrete;

import javafx.geometry.Pos;
import model.semantic.Semantic;
import view.container.TextSection;
import view.helper.Painter;

public class Author extends TextSection {

    public Author(String fontFamily, double size)
    {
        super(fontFamily, size);
        this.align(Pos.CENTER);
        getStyleClass().add("author");
        setCurrentSemantic(Semantic.NORMAL);
        fill.bind(Painter.getInstance().authorColorProperty());
    }

    @Override
    public String getContentInXML()
    {
        StringBuilder builder = new StringBuilder("<author>");
        builder.append(super.toString());
        builder.append("</author>");
        return builder.toString();
    }
}
