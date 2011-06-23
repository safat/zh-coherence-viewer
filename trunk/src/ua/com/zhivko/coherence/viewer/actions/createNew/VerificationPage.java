package ua.com.zhivko.coherence.viewer.actions.createNew;

import java.awt.Component;
import java.util.Map;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardPanelNavResult;

/**
 *
 * @author Живко
 */
public class VerificationPage extends WizardPage{
    public VerificationPage() {
        super("create", "Verify connection");
    }

    protected String validateContents(Component component, Object event) {
        return null;
    }

    public static String getDescription() {
        return "Verify connection";
    }

    public WizardPanelNavResult allowNext(String stepName, Map settings, Wizard wizard) {
//            if (jCheckBox2.isSelected()) {
//                JOptionPane.showMessageDialog(this, "Checkbox2 was checked, so next is not allowed");
//                return WizardPanelNavResult.REMAIN_ON_PAGE;
//            }
        return WizardPanelNavResult.PROCEED;
    }
}
