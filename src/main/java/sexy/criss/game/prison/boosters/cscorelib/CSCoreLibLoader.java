package sexy.criss.game.prison.boosters.cscorelib;

import org.bukkit.plugin.*;
import org.json.simple.*;
import java.net.*;
import java.io.*;

public class CSCoreLibLoader
{
    Plugin plugin;
    URL url;
    URL download;
    File file;
    
    public CSCoreLibLoader(final Plugin plugin) {
        this.plugin = plugin;
        try {
            this.url = new URL("https://servermods.cursecdn.com/files/956/561/CS-CoreLib_v1.5.15.jar");
        }
        catch (MalformedURLException ex) {}
    }
    
    public boolean load() {
        if (this.plugin.getServer().getPluginManager().isPluginEnabled("CS-CoreLib")) {
            return true;
        }
        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, (Runnable)new Runnable() {
            @Override
            public void run() {
                System.err.println(" ");
                System.err.println("#################### - \u0424\u0410\u0422\u0410\u041b\u042c\u041d\u0410\u042f \u041e\u0428\u0418\u0411\u041a\u0410 - ####################");
                System.err.println(" ");
                System.err.println(CSCoreLibLoader.this.plugin.getName() + " \u043d\u0435 \u043c\u043e\u0436\u0435\u0442 \u0431\u044b\u0442\u044c \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d!");
                System.err.println("\u041f\u043e\u0445\u043e\u0436\u0435, \u0447\u0442\u043e \u0432\u044b \u043d\u0435 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u0438\u043b\u0438 CS-CoreLib.");
                System.err.println("\u041d\u043e \u043c\u043e\u0436\u0435\u0442\u0435 \u043d\u0435 \u0432\u043e\u043b\u043d\u043e\u0432\u0430\u0442\u044c\u0441\u044f, \u043c\u044b \u0441\u0430\u043c\u0438");
                System.err.println("\u0441\u043a\u0430\u0447\u0430\u0435\u043c \u0438 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u0438\u043c \u0435\u0433\u043e.");
                System.err.println("\u0414\u043e \u0434\u043e \u0442\u043e\u0433\u043e \u0432\u0440\u0435\u043c\u0435\u043d\u0438, " + CSCoreLibLoader.this.plugin.getName() + " \u0431\u0443\u0434\u0435\u0442 \u043e\u0442\u043a\u043b\u044e\u0447\u0435\u043d");
                System.err.println("\u043f\u043e\u0441\u043b\u0435 \u0442\u043e\u0433\u043e, \u043a\u0430\u043a \u043f\u0440\u043e\u0446\u0435\u0441\u0441 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0438 CS-CoreLib \u0437\u0430\u0432\u0435\u0440\u0448\u0438\u0442\u0441\u044f,");
                System.out.println("\u0432\u0430\u043c \u0431\u0443\u0434\u0435\u0442 \u043f\u0440\u0435\u0434\u043b\u043e\u0436\u0435\u043d\u043e \u043f\u0435\u0440\u0435\u0437\u0430\u043f\u0443\u0441\u0442\u0438\u0442\u044c \u0441\u0435\u0440\u0432\u0435\u0440.");
                System.err.println("- Shinigami_03");
                System.err.println(" ");
                System.err.println("#################### - \u0424\u0410\u0422\u0410\u041b\u042c\u041d\u0410\u042f \u041e\u0428\u0418\u0411\u041a\u0410 - ####################");
                System.err.println(" ");
                if (CSCoreLibLoader.this.connect()) {
                    CSCoreLibLoader.this.install();
                }
            }
        }, 0L);
        return false;
    }
    
    private boolean connect() {
        try {
            final URLConnection connection = this.url.openConnection();
            connection.setConnectTimeout(5000);
            connection.addRequestProperty("User-Agent", "CS-CoreLib Loader (by mrCookieSlime)");
            connection.setDoOutput(true);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final JSONArray array = (JSONArray)JSONValue.parse(reader.readLine());
            this.download = new URL((String)((JSONObject)array.get(array.size() - 1)).get((Object)"downloadUrl"));
            this.file = new File("plugins/" + (String)((JSONObject)array.get(array.size() - 1)).get((Object)"name") + ".jar");
            return true;
        }
        catch (IOException e) {
            System.err.println(" ");
            System.err.println("#################### - \u0424\u0410\u0422\u0410\u041b\u042c\u041d\u0410\u042f \u041e\u0428\u0418\u0411\u041a\u0410 - ####################");
            System.err.println(" ");
            System.err.println("\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0438\u0442\u044c\u0441¤ \u043a BukkitDev, \u043c\u043e\u0436\u0435\u0442 \u043e\u043d \u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d?");
            System.err.println(" ");
            System.err.println("#################### - \u0424\u0410\u0422\u0410\u041b\u042c\u041d\u0410\u042f \u041e\u0428\u0418\u0411\u041a\u0410 - ####################");
            System.err.println(" ");
            return false;
        }
    }
    
    private void install() {
        BufferedInputStream input = null;
        FileOutputStream output = null;
        try {
            input = new BufferedInputStream(this.download.openStream());
            output = new FileOutputStream(this.file);
            final byte[] data = new byte[1024];
            int read;
            while ((read = input.read(data, 0, 1024)) != -1) {
                output.write(data, 0, read);
            }
        }
        catch (Exception ex) {
            System.err.println(" ");
            System.err.println("#################### - \u0424\u0410\u0422\u0410\u041b\u042c\u041d\u0410\u042f \u041e\u0428\u0418\u0411\u041a\u0410 - ####################");
            System.err.println(" ");
            System.err.println("\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0441\u043a\u0430\u0447\u0430\u0442\u044c CS-CoreLib");
            System.err.println(" ");
            System.err.println("#################### - \u0424\u0410\u0422\u0410\u041b\u042c\u041d\u0410\u042f \u041e\u0428\u0418\u0411\u041a\u0410 - ####################");
            System.err.println(" ");
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
                System.err.println(" ");
                System.err.println("#################### - \u0424\u0410\u0422\u0410\u041b\u042c\u041d\u0410\u042f \u041e\u0428\u0418\u0411\u041a\u0410 - ####################");
                System.err.println(" ");
                System.err.println("\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u0435 \u0441\u0435\u0440\u0432\u0435\u0440, \u0447\u0442\u043e\u0431\u044b \u0437\u0430\u0432\u0435\u0440\u0448\u0438\u0442\u044c \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0443");
                System.err.println("    " + this.plugin.getName() + " \u0438 CS-CoreLib");
                System.err.println(" ");
                System.err.println("#################### - \u0424\u0410\u0422\u0410\u041b\u042c\u041d\u0410\u042f \u041e\u0428\u0418\u0411\u041a\u0410 - ####################");
                System.err.println(" ");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
                System.err.println(" ");
                System.err.println("#################### - \u0424\u0410\u0422\u0410\u041b\u042c\u041d\u0410\u042f \u041e\u0428\u0418\u0411\u041a\u0410 - ####################");
                System.err.println(" ");
                System.err.println("\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u0435 \u0441\u0435\u0440\u0432\u0435\u0440, \u0447\u0442\u043e\u0431\u044b \u0437\u0430\u0432\u0435\u0440\u0448\u0438\u0442\u044c \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0443");
                System.err.println("    " + this.plugin.getName() + " \u0438 CS-CoreLib");
                System.err.println(" ");
                System.err.println("#################### - \u0424\u0410\u0422\u0410\u041b\u042c\u041d\u0410\u042f \u041e\u0428\u0418\u0411\u041a\u0410 - ####################");
                System.err.println(" ");
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
}
