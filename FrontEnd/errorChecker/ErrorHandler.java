package FrontEnd.errorChecker;

import FrontEnd.nodes.Node;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;

import static FrontEnd.errorChecker.Context.errorList;

public class ErrorHandler {
    private boolean hasError = false;

    public ErrorHandler(Node root, File err) {
        root.checkError();
        if (!errorList.isEmpty()) {
            hasError = true;
            Collections.sort(errorList, Comparator.comparingInt(o -> o.errorLine));
            for (ErrorPair pair : errorList) {
                pair.printTo(err);
            }
        }
    }

    public boolean hasError() {
        return hasError;
    }
}
