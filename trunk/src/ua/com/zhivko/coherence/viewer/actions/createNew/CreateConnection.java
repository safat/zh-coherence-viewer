package ua.com.zhivko.coherence.viewer.actions.createNew;

import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardException;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardPage.WizardResultProducer;

/**
 *
 * @author Живко
 */
public class CreateConnection extends AbstractAction {

    public CreateConnection() {
        this.putValue(Action.NAME, "create new connection");
        this.putValue(Action.SMALL_ICON, new ImageIcon("icons/document-new.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Wizard wizard = WizardPage.createWizard(new WizardPage[]{
                    new ConnectionPage(),new ConfigurationPage(),  new VerificationPage()
                }, new WRP());
        WizardDisplayer.showWizard(wizard);
    }

    private class WRP implements WizardResultProducer{

        @Override
        public Object finish(Map wizardData) throws WizardException {
            System.err.println("s: " + wizardData);
            return "";
            
        }

        @Override
        public boolean cancel(Map settings) {
            return true;
        }

    }
}
