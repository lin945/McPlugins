package weixin.lin.mcplugin;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import cn.nukkit.Nukkit;
import cn.nukkit.Player;
import cn.nukkit.scheduler.PluginTask;

public class ServerPost extends PluginTask<PluginMain> {
    private long stt;
    private int ours;
    private StringBuffer data;

    public ServerPost(PluginMain owner, long stt) {
        super(owner);
        this.stt = stt;
        ours = PluginMain.getPlugin().getConfig().getInt("time", 5);
    }

    @Override
    public void onRun(int i) {
        if (System.currentTimeMillis() >= (stt + ours * 60 * 60 * 1000)) {
            stt = System.currentTimeMillis();
            ours = PluginMain.getPlugin().getConfig().getInt("time", 5);
            if (PluginMain.getPlugin().getConfig().getString("status").equals("on")) {
                getData();
                send(data);
                
            }
        }
    }

    void getData() {
        Runtime runt= Runtime.getRuntime();
        data = new StringBuffer();
        data.append("text=" + PluginMain.getPlugin().getConfig().getString("title"));
        data.append("&desp=");
        data.append("## 欢迎使用微信推送插件\n" +
                "##如果你觉得好用支持下作者！[github](https://github.com/lin945/McPlugins)：lin945\n" +
                "#### 服务器信息：\n");
        Date date = new Date();
       long between=System.currentTimeMillis()-Nukkit.START_TIME;
        long day = between / (24 * 60 * 60 * 1000);
        long hour = (between / (60 * 60 * 1000) - day * 24);
        long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        data.append("* 服务器时间：" + new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z").format(date)+"\n");
       data.append("* 服务器运行时间："+day+"天"+hour+"小时"+min+"分"+s+"秒\n");
        data.append("* 服务器Motd："+ PluginMain.getPlugin().getServer().getMotd()+"\n");
        data.append("* 服务器可用最大内存："+runt.maxMemory()/1048576+"M\n");
        data.append("* 服务器使用内存："+(runt.totalMemory()/1048576-runt.freeMemory()/1048576)+"M"+"\n");
        data.append("* 服务器空闲内存："+runt.freeMemory()/1048576+"M"+"\n");
        data.append("* 服务器可用核心："+runt.availableProcessors()+"\n");
        data.append("* 服务器在线玩家：");
          Map<UUID,Player> p= PluginMain.getPlugin().getServer().getOnlinePlayers();
        for (UUID playeruid:p.keySet()) {
            data.append("* "+p.get(playeruid).getName()+"\n");
        }
        data.append("\n");
        data.append("* 服务器op："+PluginMain.getPlugin().getServer().getOps().getAll()+"\n");
        data.append("* 服务器tps："+PluginMain.getPlugin().getServer().getTicksPerSecondAverage());
    }

    public int send(StringBuffer data) {
        try {
            PluginMain.getPlugin().getLogger().info("Server酱推送开始");
            String key=PluginMain.getPlugin().getConfig().getString("key","erro");
            if(key.equals("keys")|key.equals("erro")|key.isEmpty()){
                PluginMain.getPlugin().getLogger().alert("Server酱key设置错误请检查配置");
               return -1;
            }
            URL url = new URL("https://sc.ftqq.com/" +  key+ ".send?");
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();//创建实例连接指定URL上的内容
                connection.setRequestMethod("POST");
                // 设置连接主机服务器超时时间：15000毫秒
                connection.setConnectTimeout(15000);
                // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                OutputStream os = connection.getOutputStream();
                // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
                os.write(data.toString().getBytes());
                InputStream is = connection.getInputStream();//获取内容的字节流
                if (connection.getResponseCode() == 200) {
                    PluginMain.getPlugin().getLogger().info("Server酱推送成功");
                }
                os.close();
                is.close();//关闭流

            } catch (MalformedURLException e) {//当try代码块有异常时转到catch代码块
                PluginMain.getPlugin().getLogger().alert("Server酱推送出错" + e.toString());
            } catch (IOException e) {//当try代码块有异常时转到catch代码块
                PluginMain.getPlugin().getLogger().alert("Server酱推送出错" + e.toString());
            }


        } catch (Exception e) {
            PluginMain.getPlugin().getLogger().alert("Server酱推送出错" + e.toString());
        }
        return 1;
    }

}
