package com.xxm.cli;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.util.Arrays;
import java.util.List;

/**
 * @author CodingXXM
 * @desc
 * @date 2019/10/20 2:30
 **/
public class JedisSingleTest {

    static JedisPool jedisPool = null;

    @Before
    public void init() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMinIdle(5);
        jedisPool = new JedisPool(jedisPoolConfig, "192.168.1.11", 6379, 3000, null);
    }

    @Test
    public void cmdTest() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            System.out.println(jedis.set("test2", "cmdTest"));
            System.out.println(jedis.get("test2"));
        }finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    @Test
    public void piplineTest() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Pipeline pipeline = jedis.pipelined();
            pipeline.incr("pipTestIncr");
            pipeline.set("pipTestStr", "pipVal");

            for (int i = 0; i < 10; i++) {
                pipeline.set("xxm"+i, "val"+i);
            }

            List<Object> objects = pipeline.syncAndReturnAll();
            System.out.println(objects);

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Test
    public void luaTest() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String scripts = " redis.call('set', KEYS[1], ARGV[1]) " +
                    " local count = redis.call('get', KEYS[1]) " +
                    " local t1 = tonumber(count) " +
                    " local t2 = tonumber(count) * 5 " +
                    " redis.call('set', KEYS[2], t1+ARGV[2]) " +
                    " redis.call('set', KEYS[3], t2) " +
                    " return 0";
            Object eval = jedis.eval(scripts, Arrays.asList("luaTest1", "luaTest2", "luaTest3"), Arrays.asList("10", "15"));
            System.out.println(eval);

        }finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

}
