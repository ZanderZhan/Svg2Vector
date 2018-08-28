package Action;

import UI.GenerateVectorDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class Svg2VectorAction extends AnAction {

    public Svg2VectorAction() {
        super("Hello");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
//        Svg2VectorHelper helper = new Svg2VectorHelper(e.getProject());
//        Path desktop = Paths.get("/Users/zander/Desktop");
//        helper.convertSvgToVector(desktop, desktop, true);
        GenerateVectorDialog.show(e.getProject());
    }
}
