package weixin.lin.mcplugin;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

public class PluginMain extends PluginBase {
    private static PluginMain plugin;

   public static PluginMain getPlugin() {
        return plugin;
    }

    public static void setPlugin(PluginMain plugin) {
        PluginMain.plugin = plugin;
    }
    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        setPlugin(this);
        saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new EventListener(),this);
        this.getLogger().info(TextFormat.DARK_GREEN + "插件启动成功！");
    }

    @Override
    public void onDisable() {
        this.getLogger().info(TextFormat.DARK_GREEN + "插件关闭成功！");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
       sender.sendMessage(getConfig().getString("text","默认字符"));
      return true;
    }
}
