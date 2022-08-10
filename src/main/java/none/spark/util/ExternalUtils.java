package none.spark.util;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

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
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, (ClipboardOwner) null);
    }
}
