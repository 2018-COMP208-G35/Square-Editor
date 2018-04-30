package view.helper;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class FontMetricsHelper {
    private static FontMetricsHelper fontMetricsHelper = new FontMetricsHelper();
    private static Text metricsTester = new Text("A");

    private FontMetricsHelper()
    {
    }

    public static FontMetricsHelper getInstance()
    {
        return fontMetricsHelper;
    }

    public static double getFontHeight(String family, double size)
    {
        metricsTester.setFont(Font.font(family, size));
        return metricsTester.getBoundsInLocal().getHeight();
    }

    public static double getFontWidth(Character character, String family, double size)
    {
        metricsTester.setFont(Font.font(family, size));
        metricsTester.setText(String.valueOf(character));
        return metricsTester.getBoundsInLocal().getWidth();
    }

}
