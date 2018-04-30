package view.concrete;

import javafx.geometry.Pos;
import model.semantic.Semantic;
import view.container.TextSection;
import view.helper.Painter;

public class Quote extends TextSection {
    public Quote(String fontFamily, double size)
    {
        super(fontFamily, size);
        align(Pos.CENTER);
        getStyleClass().add("quote");
        currentSemantic = Semantic.EMPHASIS;
        fill.bind(Painter.getInstance().quoteColorProperty());
    }

    @Override
    public String getContentInXML()
    {
        StringBuilder builder = new StringBuilder("<quote>");
        builder.append(super.toString());
        builder.append("</quote>");
        return builder.toString();
    }
}
