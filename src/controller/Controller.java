package controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.io.HTMLSaver;
import view.Caret;
import view.concrete.*;
import view.container.Article;
import view.container.Section;
import view.helper.CaretHelper;
import view.helper.Theme;

import javax.xml.transform.TransformerException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class Controller {

    // Tabs
    // Edit tab
    @FXML
    public MenuItem sectionItem;
    @FXML
    public MenuItem subSectionItem;
    @FXML
    public MenuItem subSubSectionItem;

    // toolbar
    @FXML
    public Label currentStructure;
    @FXML
    public Button intelligentSuggest;


    // edit pane
    @FXML
    public ScrollPane scrollContainer;
    @FXML
    public Button quoteBtn;
    @FXML
    public Button openFileBtn;
    @FXML
    public Button saveFileBtn;
    @FXML
    public Pane articleContainer;

    // style pane

    @FXML
    private ColorPicker titleColorPicker;
    @FXML
    private ColorPicker authoerColorPicker;
    @FXML
    private ColorPicker sectionsColorPicker;
    @FXML
    private ColorPicker paragraphColorPicker;
    @FXML
    private ColorPicker quoteColorPicker;
    @FXML
    private Label status;

    private final FileChooser fileChooser = new FileChooser();
    private Path currentFilePath;

    private Article editPane =
//            new Article(new Theme("Shree Devanagari 714", 50,
//                                                     "Shree Devanagari 714", 21,
//                                                     "Sathu", 30, 24, 22,
//                                                     "Shree Devanagari 714", 21,
//                                                     "Shree Devanagari 714", 22));
            new Article(new Theme("Noto Serif", 40,
                                  "Noto Serif", 21,
                                  "Noto Serif", 30, 20, 16,
                                  "Noto Serif", 16,
                                  "Noto Serif", 16));


    public void initialize()
    {
        articleContainer.getChildren().add(editPane);
//        editPane = new Article(new Theme("Georgia", 50,
//                                         "Verdana", 21,
//                                         "Baskerville", 30, 25, 23,
//                                         "CMU Serif", 21,
//                                         "Shree Devanagari 714", 22));
        setHandlers();

        Platform.runLater(new Runnable() {
            @Override
            public void run()
            {
                editPane.getChildren().get(0).requestFocus();
            }
        });
    }

    private void setHandlers()
    {


        // File Tab
        // Edit Tab

        quoteBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                editPane.createNewSectionAfterCaret(Article.Structure.QUOTE);
            }
        });

        sectionItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                editPane.createNewSectionAfterCaret(Article.Structure.SECTION_TITLE);
            }
        });

        subSectionItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                editPane.createNewSectionAfterCaret(Article.Structure.SUBSECTION_TITLE);
            }
        });

        subSubSectionItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                editPane.createNewSectionAfterCaret(Article.Structure.SUBSUBSECTION_TITLE);
            }
        });

        scrollContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                editPane.payAttention();
            }
        });

        CaretHelper.getSelectedCaretProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue)
            {
                Caret caret = CaretHelper.getSelectedCaret();
                if (caret != null)
                {
                    Section section = (Section) caret.getParent().getParent();

                    if (section instanceof MainTitle)
                    {
                        currentStructure.setText("Title");
                    }
                    else if (section instanceof Author)
                    {
                        currentStructure.setText("Author");
                    }

                    else if (section instanceof SectionTitle)
                    {
                        currentStructure.setText("Section");
                    }
                    else if (section instanceof SubSectionTitle)
                    {
                        currentStructure.setText("Subsection");
                    }
                    else if (section instanceof SubSubSectionTitle)
                    {
                        currentStructure.setText("Subsubsection");
                    }

                    else if (section instanceof Paragraph)
                    {
                        currentStructure.setText("Paragraph");
                    }
                    else if (section instanceof Quote)
                    {
                        currentStructure.setText("Quote");
                    }
                }
                else
                {
                    currentStructure.setText("Editing...");
                }


            }
        });


        intelligentSuggest.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                editPane.createMainTitle();
            }
        });

        editPane.getChildren().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> c)
            {
                c.next();
                if (!editPane.hasMainTitle())
                {
                    intelligentSuggest.setText("Title");
                    intelligentSuggest.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event)
                        {
                            editPane.createMainTitle();
                        }
                    });
                }
                else if (!editPane.hasAuthor())
                {
                    intelligentSuggest.setText("Author");
                    intelligentSuggest.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event)
                        {
                            editPane.createAuthor();
                        }
                    });
                }
                else if (!editPane.hasSection())
                {
                    intelligentSuggest.setText("Section");
                    intelligentSuggest.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event)
                        {
                            editPane.createNewSectionAfterCaret(Article.Structure.SECTION_TITLE);
                        }
                    });

                }
                else
                {

                    intelligentSuggest.setText("Paragraph");
                    intelligentSuggest.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event)
                        {
                            editPane.createNewSectionAfterCaret(Article.Structure.PARAGRAPH);
                        }
                    });
                }
            }
        });

    }


    public void setTitleColor(ActionEvent actionEvent)
    {
        editPane.paint(Article.Structure.MAIN_TITLE, titleColorPicker.getValue());
    }

    public void setAuthorColor(ActionEvent actionEvent)
    {
        editPane.paint(Article.Structure.AUTHOR, authoerColorPicker.getValue());
    }

    public void setSectionsColor(ActionEvent actionEvent)
    {
        editPane.paint(Article.Structure.SECTION_TITLE, sectionsColorPicker.getValue());
    }

    public void setParagraphColor(ActionEvent actionEvent)
    {
        editPane.paint(Article.Structure.PARAGRAPH, paragraphColorPicker.getValue());
    }

    public void setQuoteColor(ActionEvent actionEvent)
    {
        editPane.paint(Article.Structure.QUOTE, quoteColorPicker.getValue());
    }

    // File Tab Functions

    public void newFile(ActionEvent actionEvent)
    {
        // todo create new file
        // todo ask if user wants to save the current file
    }

    public void openFile(ActionEvent actionEvent)
    {
        // todo open file good good
        // before load in

        fileChooser.setTitle("Load File");
        File file = fileChooser.showOpenDialog(openFileBtn.getScene().getWindow());
        if (file != null)
        {
            if (file.getName().matches(".+\\.se"))
            {
                // todo parse file and load it

            }
            else
            {
                reportFailed("Fail to open the file " + file.getName() + " : Illegal File Name");
            }
        }
    }

    public void saveFile(ActionEvent actionEvent)
    {
        fileChooser.setTitle("Save Article");
        String title = editPane.getTitle();
        if (title == null)
        {
            title = "MyArticle";
        }
        fileChooser.setInitialFileName(title + ".se"); // todo should read title first
        File file = fileChooser.showSaveDialog(saveFileBtn.getScene().getWindow());
        if (file != null)
        {
            try (BufferedWriter out = new BufferedWriter(new FileWriter(file)))
            {
                out.write(editPane.toXML());
                reportSuccess("File Saved");
            }
            catch (IOException ex)
            {
                reportFailed("Fail to save file: " + ex.getMessage());
            }
        }
    }

    public void exportToHTML(ActionEvent actionEvent)
    {
        // todo export to html format

        fileChooser.setTitle("Export to HTML");
        String title = editPane.getTitle();
        if (title == null)
        {
            title = "MyArticle";
        }
        fileChooser.setInitialFileName(title + ".html");
        File file = fileChooser.showSaveDialog(saveFileBtn.getScene().getWindow());

        if (file != null)
        {
            try
            {
                HTMLSaver.getInstance().saveToHTML(editPane.toXML(), file);
            }
            catch (TransformerException e)
            {
                // todo set status bar text
                reportFailed("Fail to export: " + e.getMessage());
            }
            reportSuccess("Export to HTML successful");

        }
    }

    public void viewInBrowser(ActionEvent actionEvent)
    {
//        // todo view the article in the browser
        try
        {
            String html = HTMLSaver.getInstance().transformToHTML(editPane.toXML());
            WebView webView = new WebView();

            Callback<PopupFeatures, WebEngine> popupHandler = pFeatures -> {
                // Show a popup in a new window
                Stage stage = new Stage();
                stage.setTitle("Popup");
                WebView popupView = new WebView();
                VBox root = new VBox(popupView);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                return popupView.getEngine();
            };

            // Set the popup handler
            webView.getEngine().setCreatePopupHandler(popupHandler);
            webView.getEngine().loadContent(html);

            Stage view = new Stage();

            Pane root = new Pane();
            root.getChildren().add(webView);
            Scene scene = new Scene(root, 400, 500);

            view.setTitle("Hello World!");
            view.setScene(scene);
            view.show();
        }
        catch (TransformerException e)
        {
            reportFailed("Fail to transform file to HTML: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void reportFailed(String message)
    {
        status.setTextFill(Color.RED);
        status.setText(message);
    }

    public void reportSuccess(String message)
    {
        status.setTextFill(Color.GREEN);
        status.setText(message);
    }

}
