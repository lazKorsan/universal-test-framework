package Android.Utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RunTimeEmulatorStarter {

    private static Process emulatorProcess;

    public static void startEmulator(String avdName) {
        try {
            System.out.println("🚀 Emulator başlatılıyor: " + avdName);

            String[] command = {
                    "cmd", "/c",
                    "emulator -avd " + avdName
            };

            emulatorProcess = Runtime.getRuntime().exec(command);

            // Emulator loglarını oku (ÇOK ÖNEMLİ)
            new Thread(() -> {
                try (BufferedReader reader =
                             new BufferedReader(
                                     new InputStreamReader(emulatorProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[EMULATOR] " + line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            System.out.println("⏳ Emulator boot bekleniyor...");

        } catch (Exception e) {
            System.err.println("❌ Emulator başlatılamadı");
            e.printStackTrace();
        }
    }

    public static void stopEmulator() {
        try {
            System.out.println("🛑 Emulator kapatılıyor...");
            Runtime.getRuntime().exec("adb emu kill");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
