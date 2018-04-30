package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import view.helper.Page;

public class Main extends Application {

    private static double MINIMUM_WIDOW_WIDTH = Page.getLineWidthLimit() + 4 * Page.getPagePadding();
    private static double MINIMUM_WIDOW_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        loadFont();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/editor.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/test.fxml"));

        // set minimum window size
        primaryStage.setMinWidth(MINIMUM_WIDOW_WIDTH);
        primaryStage.setMinHeight(MINIMUM_WIDOW_HEIGHT);

        primaryStage.setTitle("Square Editor");
        primaryStage.setScene(new Scene(root, MINIMUM_WIDOW_WIDTH, 800));

        primaryStage.show();
    }

    private void loadFont()
    {
        // load CMU
        Font.loadFont(getClass().getResource("/font/CMU/cmunrm.ttf").toExternalForm(), 17); // CMU Serif Roman
        Font.loadFont(getClass().getResource("/font/CMU/cmunti.ttf").toExternalForm(), 17); // CMU Serif Italic
        Font.loadFont(getClass().getResource("/font/CMU/cmunbx.ttf").toExternalForm(), 17); // CMU Serif Bold
        Font.loadFont(getClass().getResource("/font/CMU/cmunbi.ttf").toExternalForm(), 17); // CMU Serif BoldItalic
        Font.loadFont(getClass().getResource("/font/CMU/cmunui.ttf").toExternalForm(), 17); // CMU Serif BoldItalic
        // load Noto
        Font.loadFont(getClass().getResource("/font/noto-serif/NotoSerif-Bold.ttf").toExternalForm(), 17); // CMU Serif Roman
        Font.loadFont(getClass().getResource("/font/noto-serif/NotoSerif-BoldItalic.ttf").toExternalForm(), 17); // CMU Serif Italic
        Font.loadFont(getClass().getResource("/font/noto-serif/NotoSerif-Italic.ttf").toExternalForm(), 17); // CMU Serif Bold
        Font.loadFont(getClass().getResource("/font/noto-serif/NotoSerif-Regular.ttf").toExternalForm(), 17); // CMU Serif BoldItalic
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
