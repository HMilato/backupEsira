/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.service;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zul.*;
/**
 *
 * @author Ussimane
 */
public class Macro extends HtmlMacroComponent {
     private Label myLabel;

    @Override
    public void afterCompose() {
        super.afterCompose(); // DON'T forget this
        Button myButton = (Button) getFellow("myButton");
        myLabel = (Label) getFellow("myLabel");

        myButton.addEventListener(Events.ON_CLICK, new EventListener() {
            public void onEvent(Event event) throws Exception {
                myLabel.setValue("Hooray! 'myButton' was clicked.");
            }
        });
    }
}
