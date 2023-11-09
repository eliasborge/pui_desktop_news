package application;


import application.news.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import serverConection.ConnectionManager;

import java.io.IOException;


public class LoginController {
//TODO Add all attribute and methods as needed 
	private LoginModel loginModel = new LoginModel();

	// DEV_TEAM_03
	// 123703@3
	
	private User loggedUsr = null;

	@FXML
	private Button buttonLogin;
	@FXML
	private Button buttonBack;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private Alert alert;


	@FXML
	public void initialize(){
		this.alert = new Alert(Alert.AlertType.ERROR);
	}

	public LoginController (){
	
		//Uncomment next sentence to use data from server instead dummy data
		//loginModel.setDummyData(false);
	}

	public void login() {
		String username = this.username.getText();
		String password = this.password.getText();


		if( verifyLogin()){


			this.loggedUsr = loginModel.validateUser(username,password);
			if(this.loggedUsr != null){

				try {
					Stage stage = (Stage) buttonLogin.getScene().getWindow();
					FXMLLoader loader = new FXMLLoader(getClass().getResource(AppScenes.READER.getFxmlFile()));
					Pane root = loader.load();
					NewsReaderController controller = loader.getController();
					controller.setUsr(loggedUsr);

					Scene scene = new Scene(root);
					stage.setScene(scene);

				} catch (IOException e) {
					e.printStackTrace();
					// Handle the exception if there's an issue loading the READER page.
				}

			}
		}

	}

	public void goBack() throws IOException {
		try {
			Stage stage = (Stage) buttonLogin.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource(AppScenes.READER.getFxmlFile()));
			Pane root = loader.load();

			Scene scene = new Scene(root);
			stage.setScene(scene);

		} catch (IOException e) {
			e.printStackTrace();
			// Handle the exception if there's an issue loading the READER page.
		}
	}

	private boolean verifyLogin(){
		if(this.username.getText().isEmpty()){
			alert.setAlertType(Alert.AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("You have to input a username");
			alert.show();
			return false;
		}
		else if(this.password.getText().isEmpty()){
			alert.setAlertType(Alert.AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("You have to input a password");
			alert.show();
			return false;
		}
		return true;
	}
	
	User getLoggedUsr() {
		return loggedUsr;
		
	}
		
	void setConnectionManager (ConnectionManager connection) {
		this.loginModel.setConnectionManager(connection);
	}
}