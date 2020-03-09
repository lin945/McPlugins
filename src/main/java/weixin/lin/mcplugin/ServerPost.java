package weixin.lin.mcplugin;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        if (System.currentTimeMillis() >= (stt + ours * 1 * 60 * 1000)) {
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
        data.append("# 欢迎使用微信推送插件\n" +
                "##如果你觉得好用支持下作者！github：lin945\n" +
                "## 服务器信息：<br /> <br />");
        Date date = new Date();
        data.append("[========]<br />");
        data.append(">服务器时间：" + new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z").format(date));
        data.append("<br />>服务器Motd："+ PluginMain.getPlugin().getServer().getMotd());
        data.append("<br />服务器可用最大内存："+runt.maxMemory()/1048576+"M");
        data.append("\n>服务器使用内存："+runt.totalMemory()/1048576+"M");
        data.append("\n服务器空闲内存："+runt.freeMemory()/1048576+"M");
        data.append("\n>服务器可用核心："+runt.availableProcessors());
        data.append("\n服务器在线玩家：："+PluginMain.getPlugin().getServer().getOnlinePlayers().size());
        data.append("\n>服务器op："+PluginMain.getPlugin().getServer().getOps().getAll());
    }

    public void send(StringBuffer data) {
        try {
            PluginMain.getPlugin().getLogger().info("Server酱推送开始");
            PluginMain.getPlugin().getLogger().info(data.toString());
            URL url = new URL("https://sc.ftqq.com/" + PluginMain.getPlugin().getConfig().getString("key") + ".send?");
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
    }

}
