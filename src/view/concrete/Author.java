package view.concrete;

import javafx.geometry.Pos;
import model.semantic.Semantic;
import view.container.TextSection;
import view.helper.Painter;

public class Author extends TextSection {
    public Author(String fontFamily, double size) {
        super(fontFamily, size);
        this.align(Pos.CENTER);
        this.getStyleClass().add("author");
        this.setCurrentSemantic(Semantic.NORMAL);
        this.fill.bind(Painter.getInstance().authorColorProperty());
    }

    public String getContentInXML() {
        StringBuilder builder = new StringBuilder("<author>");
        builder.append(super.toString());
        builder.append("</author>");
        return builder.toString();
    }
}
