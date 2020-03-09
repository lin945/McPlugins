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
        this.getLogger().info(TextFormat.DARK_GREEN + "微信推送插件启动成功！");
        this.getServer().getScheduler().scheduleRepeatingTask(new ServerPost(this,System.currentTimeMillis()),60*20);
    }

    @Override
    public void onDisable() {
        this.getLogger().info(TextFormat.DARK_GREEN + "插件关闭成功！");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (command.getName().toLowerCase().equals("serverpost")&&sender.isOp()) {
            if (args.length == 0) {
                sender.sendMessage("&b/serverpost set [key]\n&b/serverpost on \n&b/serverpost off\n/serverpost time [周期单位小时]");
            } else if (args.length == 1) {
                if (args[0].equals("on") | args[0].equals("off")) {
                    getConfig().set("status", args[0]);
                    saveConfig();
                    sender.sendMessage("设置成功！");
                } else {
                    sender.sendMessage("参数错误");
                }
            }else if (args.length == 2){
                if(args[0].equals("key")){
                    getConfig().set("key", args[1]);
                    saveConfig();
                    sender.sendMessage("key设置成功！");
                }else if(args[0].equals("time")&&Integer.parseInt(args[1])>=1&&Integer.parseInt(args[1])<=24){
                    getConfig().set("time", args[1]);
                    saveConfig();
                    sender.sendMessage("推送周期设置成功！");
                }else  if(args[0].equals("title")){
                    getConfig().set("title", args[1]);
                    saveConfig();
                    sender.sendMessage("推送标题设置成功！");
                }
            }
        }
        return true;
    }
}
