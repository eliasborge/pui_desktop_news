package application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import application.news.Article;
import application.news.User;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import serverConection.ConnectionManager;
import application.news.Article; 
import javafx.scene.web.WebView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class NewsReaderController {

    // FXML injected UI components

    @FXML private ImageView articleImageView;



    @FXML private Button articleEditButton;
    @FXML private Button articleDeleteButton;
    @FXML private Button articleNewButton;
    @FXML private Button loginButton;
    @FXML private Button loadArticleFromFileButton;
    @FXML private ListView<String> headlinesListView;
    @FXML private Label NewsforID;
    @FXML private WebView webView;
    @FXML private Button readMoreButton;

    

    // Model and user information
    private NewsReaderModel newsReaderModel = new NewsReaderModel();
    private User usr;
    private ObservableList<Article> allArticles;
    private Article selectedArticle;
    private Article article; 
    private ConnectionManager connectionManager;


    // Initialize the controller and fetch articles from the server
    @FXML
    void initialize() {
        newsReaderModel.retrieveData();
        allArticles = newsReaderModel.getArticles();
        System.out.println(allArticles);

        // Assuming you have a valid model and methods for data retrieval

        updateHeadlinesListView("ALL");
        refreshUserLabel();

        headlinesListView.getSelectionModel().selectFirst();

        // Set the selected article based on the initial selection
        System.out.println(headlinesListView.getSelectionModel().getSelectedIndex());
        setSelectedArticle(allArticles.get(headlinesListView.getSelectionModel().getSelectedIndex()));

        // Listen for changes in the selected item
        headlinesListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

            Article newArticle = allArticles.stream()
                .filter(article -> article.getTitle()
                    .equals(headlinesListView.getSelectionModel()
                        .getSelectedItem()))
                            .findFirst()
                                .get();
            setSelectedArticle(newArticle);


        });

        // Other initialization steps
        updateArticleButtons();  // Edit & Delete only possible if the article belongs to the logged user
        //getData();
        System.out.println(selectedArticle);

        // By default, show all categories



        // Label will display the current User's ID
        if (connectionManager != null) {
            String userID = connectionManager.getIdUser();
            NewsforID.setText("News Online for " + userID);
        }


    }

    private void refreshUserLabel(){
        if(usr != null){
            NewsforID.setText("News for " + usr.getLogin());
        }
        else{
            NewsforID.setText("News for Anonymus");
        }
    }



    // Set the current user and update the UI accordingly
    public void setUser(User user) {
        this.usr = user;
        updateLoginStatus();
        getData();
    }

    // Fetch data from the model and update the list view for category
    private void getData() {
        newsReaderModel.retrieveData();
        allArticles = newsReaderModel.getArticles();
        updateHeadlinesListView("ALL");
    }

    // Update the UI when an article is selected
    private void setSelectedArticle(Article article) {
        this.selectedArticle = article;
        System.out.println("The article is: " + article);
        if (article != null) {

            webView.getEngine().loadContent(article.getAbstractText());
            articleImageView.setImage(article.getImageData());
        }
        updateArticleButtons();
    }

    // Enable or disable article edit and delete buttons based on the selected article and user
    private void updateArticleButtons() {
        boolean isLoggedIn = usr != null;
        boolean isUsersArticle = isLoggedIn && selectedArticle != null && selectedArticle.getIdUser() == usr.getIdUser();
        articleEditButton.setDisable(!isUsersArticle);
        articleDeleteButton.setDisable(!isUsersArticle);
        // TODO: disable NewButton, if no user is logged in
        articleNewButton.setDisable(!isLoggedIn);
    }


    
    // Filter headlines based on the selected category
//    @FXML
//    private void filterHeadlines(ActionEvent event) {
//        MenuItem source = (MenuItem) event.getSource();
//        updateHeadlinesListView(source.getText());
//    }
    
    // each Category button has its own handler method
//    @FXML
//    private void handleECONOMY(ActionEvent event) {
//        updateHeadlinesListView("ECONOMY");
//    }
//
//    @FXML
//    private void handleINTERNATIONAL(ActionEvent event) {
//        updateHeadlinesListView("INTERNATIONAL");
//    }
//    
//    @FXML
//    private void handleNATIONAL(ActionEvent event) {
//        updateHeadlinesListView("NATIONAL");
//    }
//
//    @FXML
//    private void handleSPORTS(ActionEvent event) {
//        updateHeadlinesListView("SPORTS");
//    }
//    
//    @FXML
//    private void handleTECHNOLOGY(ActionEvent event) {
//        updateHeadlinesListView("TECHNOLOGY");
//    }

    // takes one item from the possible categories and checks if the currently selected article belongs to the selected category, from other group
//    @FXML
//    public void handleCategorySelection(ActionEvent event) {
//    	MenuItem selectedCategory = (MenuItem) event.getTarget();
//    	categoryMenu.setText(categoryText);
//    }
    
    //  This method will be triggered when a category is selected.
    @FXML
    private void handleCategorySelection(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();
        String selectedCategory = source.getText();
        updateHeadlinesListView(selectedCategory);
    }

  
    
    // Update the headlines list view with filtered articles based on the category
//    private void updateHeadlinesListView(String category) {
//        ObservableList<Article> filteredArticles = "All".equalsIgnoreCase(category) ? allArticles : allArticles.filtered(article -> category.equalsIgnoreCase(article.getCategory()));
//        articlesListView.setItems(filteredArticles);
//    }
    
    private void updateHeadlinesListView(String category) {
        ObservableList<String> filteredHeadlines = FXCollections.observableArrayList();
        // If "ALL" is selected, add all article titles to filteredHeadlines.
        if ("ALL".equalsIgnoreCase(category)) {
            allArticles.forEach(article -> filteredHeadlines.add(article.getTitle()));
        } else {
            // Otherwise, filter allArticles based on the selected category and add their titles to filteredHeadlines.
        	allArticles.stream()
                .filter(article -> category.equalsIgnoreCase(article.getCategory()))
                .forEach(article -> filteredHeadlines.add(article.getTitle()));
        }

        // set the filteredHeadlines as the items of headlinesListView
        headlinesListView.setItems(filteredHeadlines);
    }

    // Set the connection manager for the model
    void setConnectionManager(ConnectionManager connection) {
        newsReaderModel.setConnectionManager(connection);
        getData();
    }

    // Update the UI based on the user's login status
    private void updateLoginStatus() {
        boolean isLoggedIn = (usr != null);
        articleNewButton.setDisable(!isLoggedIn);
        refreshUserLabel();
        if (selectedArticle != null) {
            updateArticleButtons();
        }
    }

  
    // Actions, when buttons are clicked in the order "Load article from file", "Login", "New", "Edit", "Delete", "Exit" -------------------------------------

    // Load article from file TODO
    @FXML
    private void handleLoadArticle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            // Handle the file (e.g., read and load the article)
            // You can read the file and process it as needed
            try {
                String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                // Process the content as needed
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception
            }
        }
    }
    
    // Login Button
    @FXML
    private void handleLoginButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, maybe show an error message
        }
    }
    
    // This method will be triggered when the "New" button is clicked.
    @FXML
    private void handleNewArticle(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ArticleEdit.fxml"));
            Stage stage = (Stage) articleNewButton.getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, maybe show an error message
        }
    }
    
    // Handle the editing of an existing article 
    @FXML
    private void handleEditArticle(ActionEvent event) {
        try {
            // Assuming you have a method getSelectedArticle() that returns the currently selected article
            Article selectedArticle = allArticles.stream()
                .filter(article -> article.getTitle().equals(headlinesListView.getSelectionModel().getSelectedItem()))
                .findFirst().get();
            if (selectedArticle == null) {
                JOptionPane.showMessageDialog(null, "No article was selected"); // If no article was selected
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ArticleEdit.fxml"));
            Stage stage = (Stage) articleEditButton.getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(loader.load());

            // Get the controller and pass the article to the ArticleEditController
            ArticleEditController controller = loader.getController();
            controller.setArticle(selectedArticle); // Pass the selected article to the edit controller

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, maybe show an error message
        }

    }

        
    // Handle the deletion of an article
    @FXML
    public void handleDeleteArticle(ActionEvent event) {
        if (selectedArticle != null) {
            newsReaderModel.deleteArticle(selectedArticle);
            headlinesListView.getItems().remove(selectedArticle);
            // Clear the selection and update the UI
            headlinesListView.getSelectionModel().clearSelection();
            selectedArticle = null;
            updateArticleButtons();
        }
    }

    // Exit Button
    @FXML
    private void handleExitButton(ActionEvent event) {
        // Get the stage of the current scene
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }

    
    // If "ReadMoreButton" is clicked, switch to the next Scene
    @FXML
    private void handleReadMore(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ArticleDetails.fxml"));
            Stage stage = (Stage) readMoreButton.getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(loader.load());
            
         // Get the controller and pass the article to the ArticleDetailsController
            ArticleDetailsController controller = loader.getController();
            controller.setArticle(selectedArticle);

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, maybe show an error message
        }
    }
    // Save an article to a file
    public void saveArticleToFile(Article article, String fileName) {
        // The JsonArticle class is assumed to have a method to convert an article to JSON
        // JsonObject jsonArticle = JsonArticle.articleToJson(article);
        // TODO: still todo
    }
}