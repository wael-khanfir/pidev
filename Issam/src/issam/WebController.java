/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package issam;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.web.WebEngine;

/**
 * FXML Controller class
 *
 * @author ISSAM
 */
public class WebController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private WebView viewweb;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        final WebEngine web = viewweb.getEngine();
        String urlweb = "https://www.youtube.com/" ;
        web.load(urlweb);
        
            
     }       

    @FXML
    private void youtube(ActionEvent event) {
        final WebEngine web = viewweb.getEngine();
        String urlweb = "https://www.youtube.com/" ;
        web.load(urlweb);
        
    }

    @FXML
    private void googlemeet(ActionEvent event) {
         final WebEngine web = viewweb.getEngine();
        String urlweb = "https://meet.google.com/" ;
        web.load(urlweb);
    }
    
    
}
