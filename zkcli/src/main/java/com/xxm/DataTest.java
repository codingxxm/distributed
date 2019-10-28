package com.xxm;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author CodingXXM
 * @desc dataTest
 * @date 2019/10/7 13:08
 **/
@Slf4j
public class DataTest {

    ZooKeeper zooKeeper;

    @Before
    public void init() throws IOException {
        String conn = "192.168.56.101:2181";
        zooKeeper = new ZooKeeper(conn, 30000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                log.info("watcher path: {}", watchedEvent.getPath());
                log.info("watchedEvent", watchedEvent);
            }
        });
    }

    @Test
    public void getData() throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData("/xxm", false, null);
        log.info("data: {}", new String(data));
        List<String> children = zooKeeper.getChildren("/xxm", false);
        children.stream().forEach(e -> log.info("children: {}", e));
    }

    //添加监听，使用默认监听，一次有效
    @Test
    public void getData2() throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData("/xxm", true, null);
        log.info("add watcher: {}", new String(data));
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    //添加监听，使用自定义监听，在监听回调内继续添加监听(类似递归)
    @Test
    public void getData3() throws KeeperException, InterruptedException {
        Stat stat = new Stat();
        byte[] data = zooKeeper.getData("/xxm", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    //继续添加下一次监听
                    zooKeeper.getData(watchedEvent.getPath(), this, null);
                } catch (KeeperException | InterruptedException e) {
                    log.error("error", e);
                }
                log.info("watcher:{}", watchedEvent.getPath());
            }
        }, stat);
        log.info("stat:{}", stat);
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void getChildren() throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren("/xxm", false);
        children.stream().forEach(System.out::println);
    }

    @Test
    public void getData4() throws InterruptedException {
        zooKeeper.getData("/xxm", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
                //rc为异常时的返回码
                log.info("stat: {}", stat);
            }
        }, "");
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void getChildren2() throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren("/xxm", event -> {
            //子节点增加或者删除的时候触发监听回调，回调获取的是父节点的path
            log.info("watcher:{}", event.getPath());
            try {
                //子节点需要再次获取
                List<String> children1 = zooKeeper.getChildren(event.getPath(), false);
                children1.stream().forEach(System.out::println);
            } catch (KeeperException | InterruptedException e) {
                log.error("error", e);
            }
        });
        children.stream().forEach(System.out::println);
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void createData() throws KeeperException, InterruptedException {
        //设置多权限，有一个符合即可
        List<ACL> list = new ArrayList<>();
        ACL acl = new ACL(ZooDefs.Perms.ALL, new Id("world", "anyone"));
        ACL acl2 = new ACL(ZooDefs.Perms.ALL, new Id("ip", "192.168.1.26"));
        ACL acl3 = new ACL(ZooDefs.Perms.ALL, new Id("ip", "127.0.0.1"));
        list.add(acl);
        list.add(acl2);
        list.add(acl3);
        String s = zooKeeper.create("/xxm/seq", "tmpdata".getBytes(), list, CreateMode.PERSISTENT);
        log.info("create:{}", s);
    }
}
