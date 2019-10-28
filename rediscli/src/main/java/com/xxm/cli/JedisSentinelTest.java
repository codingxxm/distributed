package com.xxm.cli;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * @author CodingXXM
 * @desc 哨兵模式
 * @date 2019/10/24 21:34
 **/

@Slf4j
public class JedisSentinelTest {


    static JedisPoolConfig poolConfig = null;
    static JedisSentinelPool sentinelPool = null;

    @Before
    public void init(){
        poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(5);

        String masterName = "mymaster";
        HashSet<String> sentinels = new HashSet<>();
        sentinels.add(new HostAndPort("192.168.1.11", 26379).toString());
        sentinels.add(new HostAndPort("192.168.1.11", 26380).toString());
        sentinels.add(new HostAndPort("192.168.1.11", 26381).toString());

        sentinelPool = new JedisSentinelPool(masterName, sentinels, poolConfig, 3000, null);

    }

    @Test
    public void test() {
        Jedis jedis = null;
        try {
            jedis = sentinelPool.getResource();
            System.out.println(jedis.set("sentinelTest", "sentinelVal"));
            System.out.println(jedis.get("sentinelTest"));
        } finally {
            if (jedis!=null) {
                jedis.close();
            }
        }
    }

    @Test
    public void test2() {
        Jedis jedis = null;
        try {
            jedis = sentinelPool.getResource();
            int i = 0;
            while (true) {
                System.out.println(jedis.set("sentinelTest" + i, "sentinelVal" + i));
                System.out.println(jedis.get("sentinelTest" + i));
                ++i;
                TimeUnit.SECONDS.sleep(2);
            }
        } catch (InterruptedException e) {
            log.error("error:", e);
        } finally {
            if (jedis!=null) {
                jedis.close();
            }
        }
    }



}
