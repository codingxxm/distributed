package com.xxm;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author CodingXXM
 * @desc
 * @date 2019/10/10 21:09
 **/
@Slf4j
public class DataTest2 {

    private ZkClient client;

    private String conn = "192.168.1.24:2181";

    private String root = "/xxm";

    @Before
    public void init() {
        this.client = new ZkClient(conn, 30000);
        this.client.setZkSerializer(new MyZKSerializer());
    }

    @Test
    public void getData() {
        String data = this.client.readData("/xxm");
        log.info("data:{}", data);
    }

    @Test
    public void getData2() {
        Stat stat = new Stat();
        String o = this.client.readData("/xxm", stat);
        log.info("data:{}", o);
        log.info("info:{}", stat);
    }

    @Test
    public void createData() {
        String s = this.client.create("/xxm/seq", null, CreateMode.EPHEMERAL_SEQUENTIAL);
        log.info("create:{}", s);
    }

    @Test
    public void watcher() throws InterruptedException {
        List<String> childChanges = this.client.subscribeChildChanges(root, new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println("-----------------------------");
                System.out.println("s:" + s);
                list.stream().forEach(System.out::println);
                System.out.println("-----------------------------");
            }
        });
        System.out.println("-----------list----------");
        childChanges.stream().forEach(System.out::println);
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void watcher2() throws InterruptedException {
        this.client.subscribeDataChanges(root, new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("change data:" + s + o);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("data delete：" + s);
            }
        });
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }
}
