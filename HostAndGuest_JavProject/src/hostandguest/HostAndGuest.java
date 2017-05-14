package hostandguest;

import com.hostandguest.entities.Booking;
import com.hostandguest.services.ReviewService;
import com.hostandguest.entities.Review;
import com.hostandguest.entities.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author The
 */
public class HostAndGuest extends Application {

    @Override
    public void start(Stage stage) throws Exception {
       Parent root = FXMLLoader.load(getClass().getResource("/com/hostandguest/views/login.fxml"));
       Scene scene = new Scene(root);
       stage.setScene(scene);
       stage.setTitle("Host and Guest - Login ");
       stage.setResizable(false);
       stage.sizeToScene();
       stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
