package none.spark.util;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.InputStream;

public final class ExternalUtils {
    public static String getClipboardString() {
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                return (String) transferable.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException e) {
                return "";
            }
        }
        return "";
    }

    public static void setClipboardString(String p_setClipboardString_0_) {
        StringSelection stringselection = new StringSelection(p_setClipboardString_0_);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, null);
    }

    public static InputStream getResourceInputStream(String resourcePath) {
        return Class.class.getResourceAsStream(resourcePath);
    }
}
