package com.xxm.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author CodingXXM
 * @desc
 * @date 2019/10/26 15:19
 **/

@RestController
@Slf4j
public class IndexController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/test_sentinel")
    public void testSentinel() {
        int i = 1;
        while (true) {
            try {
                redisTemplate.opsForValue().set("testSentinel" + i, i + "");
                System.out.println("set: " + "testSentinel" + i + "-" + i + "");
                i++;
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                log.error("error", e);
            }
        }
    }

}
