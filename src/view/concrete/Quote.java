package view.concrete;

import javafx.geometry.Pos;
import model.semantic.Semantic;
import view.container.TextSection;
import view.helper.Painter;

public class Quote extends TextSection {
    public Quote(String fontFamily, double size) {
        super(fontFamily, size);
        this.align(Pos.CENTER);
        this.getStyleClass().add("quote");
        this.currentSemantic = Semantic.EMPHASIS;
        this.fill.bind(Painter.getInstance().quoteColorProperty());
    }

    public String getContentInXML() {
        StringBuilder builder = new StringBuilder("<quote>");
        builder.append(super.toString());
        builder.append("</quote>");
        return builder.toString();
    }
}