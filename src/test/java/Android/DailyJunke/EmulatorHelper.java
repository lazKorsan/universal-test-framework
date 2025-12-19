package Android.DailyJunke;

import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@Slf4j
public class EmulatorHelper {

    private static final String EMULATOR_PATH = System.getenv("ANDROID_HOME") + "/emulator/emulator";
    private static final long BOOT_TIMEOUT_SECONDS = 120;

    /**
     * 1. EMULATORU ÖNCEKİ OTURUMDAN BAŞLATMA (Hızlı)
     */
    public static boolean startEmulatorFromLastSession(String avdName) {
        return executeEmulatorCommand(EMULATOR_PATH + " -avd " + avdName,
                "Emulator started from last session");
    }

    /**
     * 2. UYGULAMA VERİLERİNİ SİLEREK BAŞLATMA (Cold boot, ana ekran)
     */
    public static boolean startEmulatorWithCleanAppData(String avdName) {
        return executeEmulatorCommand(EMULATOR_PATH + " -avd " + avdName + " -no-snapshot",
                "Emulator started with clean app data (cold boot)");
    }

    /**
     * 3. FABRİKA AYARLARINA DÖNDÜREREK BAŞLATMA
     */
    public static boolean startEmulatorWithFactoryReset(String avdName) {
        return executeEmulatorCommand(EMULATOR_PATH + " -avd " + avdName + " -wipe-data",
                "Emulator started with factory reset");
    }

    /**
     * 4. EN TEMİZ ŞEKİLDE BAŞLATMA (Hem fabrika hem snapshot temiz)
     */
    public static boolean startEmulatorCompletelyClean(String avdName) {
        return executeEmulatorCommand(EMULATOR_PATH + " -avd " + avdName + " -wipe-data -no-snapshot",
                "Emulator started completely clean");
    }

    /**
     * 5. ÖZEL SNAPSHOT'TAN BAŞLATMA (Hızlı ve temiz ana ekran)
     */
    public static boolean startEmulatorFromSnapshot(String avdName, String snapshotName) {
        return executeEmulatorCommand(EMULATOR_PATH + " -avd " + avdName + " -snapshot " + snapshotName,
                "Emulator started from snapshot: " + snapshotName);
    }

    /**
     * 6. SNAPSHOT OLUŞTURMA (Mevcut durumu kaydet)
     */
    public static boolean createSnapshot(String avdName, String snapshotName) {
        return executeEmulatorCommand(EMULATOR_PATH + " -avd " + avdName + " -snapshot save " + snapshotName,
                "Snapshot created: " + snapshotName);
    }

    /**
     * 7. EMULATOR'Ü KAPATMA
     */
    public static void killEmulator() {
        try {
            Runtime.getRuntime().exec("adb emu kill");
            log.info("Emulator killed via ADB");
        } catch (IOException e) {
            log.error("Error killing emulator: " + e.getMessage());
        }
    }

    /**
     * 8. TÜM EMULATOR'LERİ KAPATMA
     */
    public static void killAllEmulators() {
        try {
            Process process = Runtime.getRuntime().exec("adb devices");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("emulator")) {
                    String deviceId = line.split("\\s+")[0];
                    Runtime.getRuntime().exec("adb -s " + deviceId + " emu kill");
                    log.info("Killed emulator: " + deviceId);
                }
            }
        } catch (IOException e) {
            log.error("Error killing all emulators: " + e.getMessage());
        }
    }

    /**
     * 9. EMULATOR'ÜN BOOT OLUP OLMADIĞINI KONTROL ET
     */
    public static boolean waitForBootComplete(String deviceId) {
        try {
            for (int i = 0; i < BOOT_TIMEOUT_SECONDS; i++) {
                Process process = Runtime.getRuntime().exec("adb -s " + deviceId + " shell getprop sys.boot_completed");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String result = reader.readLine();

                if ("1".equals(result)) {
                    log.info("Emulator boot completed in " + i + " seconds");
                    return true;
                }

                TimeUnit.SECONDS.sleep(1);
            }
        } catch (Exception e) {
            log.error("Error checking boot status: " + e.getMessage());
        }
        return false;
    }

    /**
     * 10. ANA EKRANA DÖN (Home button)
     */
    public static void goToHomeScreen(String deviceId) {
        try {
            Runtime.getRuntime().exec("adb -s " + deviceId + " shell input keyevent KEYCODE_HOME");
            log.info("Navigated to home screen");
        } catch (IOException e) {
            log.error("Error going to home screen: " + e.getMessage());
        }
    }

    /**
     * 11. UYGULAMA VERİLERİNİ TEMİZLE (pm clear)
     */
    public static void clearAppData(String deviceId, String packageName) {
        try {
            Process process = Runtime.getRuntime().exec("adb -s " + deviceId + " shell pm clear " + packageName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("Success")) {
                    log.info("App data cleared for: " + packageName);
                }
            }
        } catch (IOException e) {
            log.error("Error clearing app data: " + e.getMessage());
        }
    }

    /**
     * 12. MEVCUT EMULATOR'LERİ LİSTELE
     */
    public static void listAvailableEmulators() {
        try {
            Process process = Runtime.getRuntime().exec(EMULATOR_PATH + " -list-avds");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            log.info("=== Available Emulators ===");
            while ((line = reader.readLine()) != null) {
                log.info("AVD: " + line);
            }
        } catch (IOException e) {
            log.error("Error listing emulators: " + e.getMessage());
        }
    }

    /**
     * 13. EMULATOR'Ü HEADLESS MODDA BAŞLAT (CI/CD için)
     */
    public static boolean startEmulatorHeadless(String avdName) {
        return executeEmulatorCommand(EMULATOR_PATH + " -avd " + avdName +
                        " -no-window -no-audio -no-boot-anim -gpu swiftshader",
                "Emulator started in headless mode");
    }

    /**
     * 14. EMULATOR'E DOSYA PUSH ETME
     */
    public static void pushFileToEmulator(String deviceId, String localPath, String devicePath) {
        try {
            Runtime.getRuntime().exec("adb -s " + deviceId + " push " + localPath + " " + devicePath);
            log.info("File pushed: " + localPath + " -> " + devicePath);
        } catch (IOException e) {
            log.error("Error pushing file: " + e.getMessage());
        }
    }

    /**
     * 15. EMULATOR'E DOSYA PULL ETME
     */
    public static void pullFileFromEmulator(String deviceId, String devicePath, String localPath) {
        try {
            Runtime.getRuntime().exec("adb -s " + deviceId + " pull " + devicePath + " " + localPath);
            log.info("File pulled: " + devicePath + " -> " + localPath);
        } catch (IOException e) {
            log.error("Error pulling file: " + e.getMessage());
        }
    }

    /**
     * ORTAK KOMUT ÇALIŞTIRMA METODU
     */
    private static boolean executeEmulatorCommand(String command, String logMessage) {
        try {
            log.info("Executing: " + command);
            Process process = Runtime.getRuntime().exec(command);

            // Hata akışını dinle (arka planda)
            new Thread(() -> {
                try {
                    BufferedReader errorReader = new BufferedReader(
                            new InputStreamReader(process.getErrorStream()));
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        log.error("Emulator Error: " + errorLine);
                    }
                } catch (IOException e) {
                    log.error("Error reading error stream: " + e.getMessage());
                }
            }).start();

            log.info(logMessage);
            return true;

        } catch (IOException e) {
            log.error("Error executing command: " + command + " - " + e.getMessage());
            return false;
        }
    }
}